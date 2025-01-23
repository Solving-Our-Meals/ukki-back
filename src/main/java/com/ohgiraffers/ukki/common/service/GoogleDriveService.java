package com.ohgiraffers.ukki.common.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Service
public class GoogleDriveService {
    private final Drive driveService;
    
    public GoogleDriveService(Drive driveService) {
        this.driveService = driveService;
    }
    
    public String uploadFile(MultipartFile file, String folderName, String customFileName) {
        try {
            // 파일 확장자 추출
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // 최종 파일명 생성 (예: inquiry1.jpg)
            String finalFileName = customFileName + extension;
            
            File fileMetadata = new File();
            fileMetadata.setName(finalFileName);
            fileMetadata.setParents(Collections.singletonList(folderName));
            
            InputStreamContent mediaContent = 
                new InputStreamContent(file.getContentType(), file.getInputStream());
            
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
                
            return uploadedFile.getId();
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 파일 URL 가져오기
    public String getFileUrl(String fileId) {
        return String.format("https://drive.google.com/uc?id=%s", fileId);
    }

    // 파일 삭제
    public void deleteFile(String fileId) {
        try {
            driveService.files().delete(fileId).execute();
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        }
    }
} 