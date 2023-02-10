package org.zerock.mreview.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {
    // 파일 업로드는 Ajax 방식으로 처리(업로드 결과에 대한 별도 화면 작성 불필요)
    // 업로드 결과는 JSON 형태로 제공

    @Value("${org.zerock.upload.path}") // application.yml 변수
    private String uploadPath;


    // 파일 업로드
    @PostMapping("/uploadAjax")
    public void uploadFile(MultipartFile[] uploadFiles){

        for(MultipartFile uploadFile : uploadFiles){

            // 이미지 파일만 업로드 가능
            if(uploadFile.getContentType().startsWith("image")==false){
                log.warn("this file is not image type");
                return;
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
                uploadFile.transferTo(savePath); // transferTo -> uploadFile의 파일 저장 메서드
            }catch (IOException e){
                e.printStackTrace();
            }

        } // end for
    }

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
}
