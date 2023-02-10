package org.zerock.mreview.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mreview.dto.UploadResultDTO;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
    // 파일 업로드는 Ajax 방식으로 처리(업로드 결과에 대한 별도 화면 작성 불필요)
    // 업로드 결과는 JSON 형태로 제공

    @Value("${org.zerock.upload.path}") // application.yml 변수
    private String uploadPath;


    /*파일 업로드*/
    @PostMapping("/uploadAjax")
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        for(MultipartFile uploadFile : uploadFiles){

            // 이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image")==false){
                log.warn("this file is not image type");
                // 이미지 아닐경우 403 Forbidden 변환
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            // 브라우저 별 파일명 상이(IE - 전체 경로, chrome - 파일명)
            String originalName = uploadFile.getOriginalFilename();
            String fileName = originalName.substring(originalName.lastIndexOf("\\")+1);

            log.info("filename : "+fileName);

            // 날짜별 폴더 생성
            String folderPath = makeFolder();

            // UUID
            String uuid = UUID.randomUUID().toString();

            // 저장할 파일 이름 중간에 "_" 이용해 구분
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);

            try{
                // 원본 파일 저장
                uploadFile.transferTo(savePath); // 실제 이미지 저장(transferTo -> uploadFile의 파일 저장 메서드)

                // 섬네일 생성
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator + "s_" +uuid + "_" + fileName;
                // 섬네일 파일 이름은 중간에 s_로 시작하도록
                File thumbnailFile = new File(thumbnailSaveName);
                // 섬네일 생성
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));

            }catch (IOException e){
                e.printStackTrace();
            }
        } // end for
        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }
    // 이미지 저장할 폴더 생성
    private String  makeFolder(){
        String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        String folderPath = str.replace("/", File.separator);

        // make folder -----
        File uploadPathFolder = new File(uploadPath, folderPath);

        if(uploadPathFolder.exists() == false){
            uploadPathFolder.mkdirs();
        }
        return folderPath;
    }

    /*업로드 이미지 출력하기*/
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName){ // URL 인코딩된 파일명을 파라미터로 받아 해당 파일을 byte[]로 만들어 브라우저 전송
        ResponseEntity<byte[]> result = null;

        try{
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("fileName : "+srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file : "+file);

            HttpHeaders header = new HttpHeaders();

            // MIME 타입 처리 - 확장자에 따라 브라우저에 전송하는 타입 변경
            header.add("Content-Type", Files.probeContentType(file.toPath()));
            // 파일 데이터 처리
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);

        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /*업로드 파일 삭제*/
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFiles(String fileName){
        String srcFileName = null;

        try{
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            File thumbnail = new File(file.getParent(), "s_"+file.getName());

            result = thumbnail.delete();

            return new ResponseEntity<>(result, HttpStatus.OK);

        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
