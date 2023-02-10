package org.zerock.mreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable { // 실제 파일과 관련된 모든 정보를 갖는다.

    private String fileName;
    private String uuid;
    private String folderPath;

    public String getImageURL(){ // 전체 업로드 필요한 경우 대비해 해당 메서드 제공
        try{
            return URLEncoder.encode(folderPath+"/"+uuid+"_"+fileName, "UTF-8");
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    // 브라우저에서 섬네일 처리
    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(folderPath + "/s_"+uuid+"_"+fileName,"UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

}
