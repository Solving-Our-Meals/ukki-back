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
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public boolean updateInquiry(MypageInquiryDTO inquiryToUpdate, MultipartFile file, String userId) {
        try {
            if (file != null && !file.isEmpty()) {
                String filePath = saveFile(file, userId);

                inquiryToUpdate.setFile(filePath);
            }

            int updatedRows = mypageMapper.updateInquiry(inquiryToUpdate);

            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static final String FILE_UPLOAD_DIR = "/path/to/your/upload/directory";  // 파일 경로

    public String saveFile(MultipartFile file, String userId) throws IOException {
        Path uploadPath = Paths.get(FILE_UPLOAD_DIR, userId);
        if (!uploadPath.toFile().exists()) {
            uploadPath.toFile().mkdirs();
        }

        Path filePath = uploadPath.resolve(file.getOriginalFilename());
        file.transferTo(filePath.toFile());

        return filePath.toString();
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

    private final String profileImageDirectory = "/path/to/upload/directory"; // 업로드 디렉토리 경로

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

            // 4. 매퍼를 통해 사용자 정보 업데이트
            int updatedRows = mypageMapper.updateUserInfo(updateUserInfoDTO);

            return updatedRows > 0;  // 성공적으로 업데이트되었으면 true 반환
        } catch (IOException e) {
            e.printStackTrace();
            return false;  // 파일 저장 실패 시 false 반환
        }
    }

/*    public boolean deleteUser(String userId) {
    }*/
}
