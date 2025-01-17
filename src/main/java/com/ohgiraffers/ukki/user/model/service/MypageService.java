package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dao.MypageMapper;
import com.ohgiraffers.ukki.user.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MypageService {

    private final JwtService jwtService;
    private final MypageMapper mypageMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public MypageService(JwtService jwtService, MypageMapper mypageMapper, BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mypageMapper = mypageMapper;
    }

    public MypageDTO getUserInfoFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        MypageDTO mypageDTO = mypageMapper.findUserInfoByUserId(userId);

        if (mypageDTO == null) {
            throw new IllegalArgumentException("사용자 찾을 수 없음");
        }
        return mypageDTO;
    }

    public List<MypageReservationDTO> getUserReservationFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }


        List<MypageReservationDTO> reservations = mypageMapper.findUserReservationByUserId(userId);

        if (reservations == null || reservations.isEmpty()) {
            return new ArrayList<>();
        }

        return reservations;
    }

    public List<MypageReviewDTO> getUserReviewFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        // 리뷰 목록을 조회
        List<MypageReviewDTO> reviews = mypageMapper.findUserReviewByUserId(userId);


        if (reviews == null || reviews.isEmpty()) {
            return new ArrayList<>();
        }

        return reviews;
    }


    public boolean deleteReview(int reviewNo) {
        int result = mypageMapper.deleteReviewById(reviewNo);

        return result > 0;
    }

    public List<MypageInquiryDTO> getUserInquiryFromToken(String jwtToken, String userId) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }


        List<MypageInquiryDTO> inquiry = mypageMapper.findUserInquiryByUserId(userId);

        if (inquiry == null || inquiry.isEmpty()) {
            return new ArrayList<>();
        }

        return inquiry;
    }

    public boolean updateInquiryStatus(int inquiryNo, InquiryState inquiryState) {
        int result = mypageMapper.updateInquiryStatus(inquiryNo, inquiryState);
        return result > 0;
    }
    private static final String FILE_UPLOAD_DIR = "\\\\192.168.0.138\\ukki_nas\\inquiry";  // 네트워크 공유 경로

    public boolean updateInquiry(MypageInquiryDTO inquiryToUpdate, MultipartFile file, String userId) {
        try {
            // 파일이 존재하는 경우에만 파일 경로 설정
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file, userId);  // 서비스에서만 호출
                inquiryToUpdate.setFile(filePath); // 파일 경로 저장
            }

            // 문의 업데이트
            int updatedRows = mypageMapper.updateInquiry(inquiryToUpdate);
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String saveFile(MultipartFile file, String userId) throws IOException {


        // 파일 이름 가져오기
        String fileName = file.getOriginalFilename();
        System.out.println("업로드된 파일 이름: " + fileName);

        // 네트워크 경로와 사용자 ID를 합쳐서 폴더 경로 설정
        String networkPath = FILE_UPLOAD_DIR + File.separator + userId;
        System.out.println("파일 저장 경로: " + networkPath);

        // 디렉토리 생성
        File directory = new File(networkPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("디렉토리 생성 실패: " + networkPath);
            }
        }

        // 파일 저장 경로 설정
        File targetFile = new File(directory, fileName);
        System.out.println("파일을 저장하는 경로: " + targetFile.getAbsolutePath());

        try {
            file.transferTo(targetFile);  // 파일 저장
            System.out.println("파일 저장 완료: " + targetFile.getAbsolutePath());
            return targetFile.getAbsolutePath();  // 저장된 파일 경로 리턴
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("파일 저장 오류", e);  // 파일 저장 실패 시 예외 처리
        }
    }


    public boolean deleteInquiry(int inquiryNo) {
        int result = mypageMapper.deleteInquiryById(inquiryNo);

        return result > 0;
    }

    private String getFilePathByFileId(String fileId, String userId) {
        return "/path/to/files/" + userId + "/" + fileId;
    }

    public Resource loadFile(String fileId, String userId) {
        String filePath = getFilePathByFileId(fileId, userId);

        if (filePath == null) {
            return null;
        }

        Path path = Paths.get(filePath);
        Resource resource = new FileSystemResource(path);

        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    public boolean verifyPassword(String userId, String password) {
        String storedPassword = mypageMapper.findPasswordByUserId(userId);

        if (storedPassword == null) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        return passwordEncoder.matches(password, storedPassword);
    }

    public MypageInfoDTO getUserInfo(String userId) {
        MypageInfoDTO userInfo = mypageMapper.findUserProfileInfo(userId);

        if (userInfo == null) {
            return null;
        }

        return userInfo;
    }

    private final String profileImageDirectory = "\\\\192.168.0.138\\ukki_nas"; // 업로드 디렉토리 경로

    public boolean updateUserInfo(String userId, String userName, String userPass, MultipartFile profileImage) {
        try {
            MypageUpdateUserInfoDTO updateUserInfoDTO = new MypageUpdateUserInfoDTO();
            updateUserInfoDTO.setUserId(userId);

            // 1. 프로필 이미지 처리
            if (profileImage != null && !profileImage.isEmpty()) {
                // 파일을 저장할 경로 생성
                String filename = userId + "_" + profileImage.getOriginalFilename();
                File targetFile = new File(profileImageDirectory, filename);
                profileImage.transferTo(targetFile); // 파일 저장

                // 파일 경로를 DTO에 설정
                updateUserInfoDTO.setFile(filename);
            }

            // 2. 닉네임 업데이트
            if (userName != null && !userName.isEmpty()) {
                updateUserInfoDTO.setUserName(userName);
            }

            // 3. 비밀번호 업데이트
            if (userPass != null && !userPass.isEmpty()) {
                String encodedPassword = passwordEncoder.encode(userPass);  // 비밀번호를 암호화
                updateUserInfoDTO.setUserPass(encodedPassword);
            }

            int updatedRows = mypageMapper.updateUserInfo(updateUserInfoDTO);

            return updatedRows > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

/*    public boolean deleteUser(String userId) {
    }*/
}
