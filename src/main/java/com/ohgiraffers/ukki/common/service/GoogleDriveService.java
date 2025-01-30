package com.ohgiraffers.ukki.common.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            InputStreamContent mediaContent = new InputStreamContent(file.getContentType(), file.getInputStream());

            // 파일 업로드
            File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            System.out.println("파일 업로드 성공, 파일 ID: " + uploadedFile.getId());

            // 파일을 공개로 설정 (링크로 누구나 접근 가능하도록)
            setFilePermissionToPublic(uploadedFile.getId());
            // 권한 설정 완료 로그
            System.out.println("파일 권한 설정 완료: " + uploadedFile.getId());

            String fileUrl = getFileUrl(uploadedFile.getId());
            System.out.println("파일 URL: " + fileUrl);

            return uploadedFile.getId();
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
        }
    }

    // 파일을 공개로 설정하는 메서드
    private void setFilePermissionToPublic(String fileId) {
        try {
            Permission permission = new Permission();
            permission.setType("anyone");  // 누구나
            permission.setRole("reader");  // 읽기 권한

            // 파일 권한 설정
            driveService.permissions().create(fileId, permission).execute();
        } catch (Exception e) {
            throw new RuntimeException("파일 공개 설정 실패: " + e.getMessage());
        }
    }

    // 파일 URL 가져오기
    public String getFileUrl(String fileId) {
        System.out.println(fileId);
        return String.format("https://drive.google.com/uc?id=%s", fileId);
    }

    // 파일 삭제
    public void deleteFile(String fileId) {
        if (fileId == null || fileId.isEmpty()) {
            return; // 파일 ID가 없으면 그냥 리턴
        }

        try {
            driveService.files().delete(fileId).execute();
        } catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                // 파일이 없는 경우 무시하고 진행
                return;
            }
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 실패: " + e.getMessage());
        }
    }

    public byte[] downloadFile(String fileId) throws IOException {
        // 파일 다운로드
        File file = driveService.files().get(fileId).execute();

        // 파일 스트림으로 다운로드
        InputStream inputStream = driveService.files().get(fileId).executeMediaAsInputStream();

        // byte[]로 변환
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }

        return outputStream.toByteArray();  // 파일의 byte 배열 반환
    }
}
