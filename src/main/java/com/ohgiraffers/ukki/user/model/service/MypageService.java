package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.user.model.dao.MypageMapper;
import com.ohgiraffers.ukki.user.model.dto.MypageDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageInquiryDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReservationDTO;
import com.ohgiraffers.ukki.user.model.dto.MypageReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    public MypageService(JwtService jwtService, MypageMapper mypageMapper) {
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

}
