package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.auth.model.service.JwtService;
import com.ohgiraffers.ukki.common.InquiryState;
import com.ohgiraffers.ukki.common.service.GoogleDriveService;
import com.ohgiraffers.ukki.user.model.dao.MypageMapper;
import com.ohgiraffers.ukki.user.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MypageService {

    private static final String PROFILE_IMAGE_FOLDER_ID = "1gxTNO0bbEGV-VeLlzMV76N1jGI03neUM";
    private static final String Inquiry_FOLDER_ID = "1Bzigy3LlWfu5wAj7vB5Xdp_QapW76eQG";


    private final JwtService jwtService;
    private final MypageMapper mypageMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public MypageService(JwtService jwtService, MypageMapper mypageMapper, BCryptPasswordEncoder passwordEncoder, GoogleDriveService googleDriveService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.mypageMapper = mypageMapper;
        this.googleDriveService = googleDriveService;
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

    public List<MypageReservationDTO> getUserReservationFromTokenWithSearch(String jwtToken, String userId, String search) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        // search 파라미터가 있을 경우 findUserReservationByUserIdWithSearch 호출
        List<MypageReservationDTO> reservations;
        if (search != null && !search.isEmpty()) {
            reservations = mypageMapper.findUserReservationByUserIdWithSearch(userId, search); // selectList()를 사용합니다.
        } else {
            reservations = mypageMapper.findUserReservationByUserId(userId); // 기본 쿼리
        }

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
            // DB에서 저장된 파일 ID 가져오기
            String existingFileId = inquiryToUpdate.getFile();

            // 기존 파일이 있으면 Google Drive에서 삭제
            if (existingFileId != null && !existingFileId.isEmpty()) {
                try {
                    System.out.println(existingFileId);
                    // 구글 드라이브에서 파일 삭제
                    googleDriveService.deleteFile(existingFileId);
                    System.out.println("기존 파일 삭제 성공, 파일 ID: " + existingFileId);
                } catch (Exception e) {
                    System.out.println("기존 파일 삭제 실패: " + e.getMessage());
                    // 예외 발생 시에도 계속 진행하려면 여기서 true를 반환하도록 설정
                }
            } else {
                System.out.println("기존 파일이 드라이브에 존재하지 않거나 파일 ID가 잘못되었습니다.");
            }

            if (file != null && !file.isEmpty()) {
                // 폴더 이름을 폴더 ID로 변경 (Inquiry 폴더 ID 사용)
                String folderId = Inquiry_FOLDER_ID;  // Inquiry 폴더 ID

                // 파일 업로드 및 파일 ID 반환
                String fileId = googleDriveService.uploadFile(file, folderId, userId + "_inquiry_files");

                // 파일 URL을 DB에 저장 (구글 드라이브 URL 형식으로)
                String fileUrl = googleDriveService.getFileUrl(fileId);
                inquiryToUpdate.setFile(fileId);
            }

            // DB 업데이트
            int updatedRows = mypageMapper.updateInquiry(inquiryToUpdate);
            return updatedRows > 0;  // 업데이트된 행이 있으면 true 반환
        } catch (Exception e) {
            e.printStackTrace();
            return false;  // 예외 발생 시 false 반환
        }
    }


    public boolean deleteInquiry(int inquiryNo) {
        MypageInquiryDTO inquiry = mypageMapper.findInquiryById(inquiryNo);

        if (inquiry != null) {
            String fileId = inquiry.getFile();

            if (fileId != null && !fileId.isEmpty()) {
                try {
                    googleDriveService.deleteFile(fileId);
                    System.out.println("구글 드라이브 파일 삭제 성공, 파일 ID: " + fileId);
                } catch (Exception e) {
                    System.out.println("파일 삭제 중 오류 발생: " + e.getMessage());
                    return false;
                }
            }
        }

        int result = mypageMapper.deleteInquiryById(inquiryNo);

        return result > 0;
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

            // 1.
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

    public MypageReviewDTO getUserReviewDetailFromToken(String jwtToken, String userId, Long reviewNo) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        MypageReviewDTO reviewDetail = mypageMapper.findUserReviewDetailByReviewNo(reviewNo);

        if (reviewDetail == null) {
            throw new IllegalArgumentException("리뷰를 찾을 수 없습니다.");
        }

        return reviewDetail;
    }

    public boolean updateProfileImage(String userId, MultipartFile profileImage) {
        try {
            // 기존 프로필 이미지 경로 가져오기 (Google Drive 파일 ID)
            String existingFileId = getExistingFileId(userId);

            // 기존 파일이 있으면 삭제
            if (existingFileId != null && !existingFileId.isEmpty()) {
                try {
                    googleDriveService.deleteFile(existingFileId); // Google Drive에서 파일 삭제
                    System.out.println("구글 드라이브 파일에 있던 기존 파일 제거했습니다: " + existingFileId);
                } catch (Exception e) {
                    System.out.println("파일 삭제 실패: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("파일 제거 실패: 파일 ID가 없습니다.");
            }

            // 새 프로필 이미지가 존재하면 Google Drive에 저장
            if (profileImage != null && !profileImage.isEmpty()) {
                // 파일을 Google Drive에 업로드하고 파일 ID 반환
                String fileId = googleDriveService.uploadFile(profileImage, PROFILE_IMAGE_FOLDER_ID, userId + "_profile_image");

                // 업로드한 파일의 ID를 DB에 저장 (파일 URL 대신 fileId만 저장)
                MypageProfileImageDTO profileImageDTO = new MypageProfileImageDTO();
                profileImageDTO.setUserId(userId);
                profileImageDTO.setFile(fileId);  // 파일 ID만 저장

                // DB 업데이트
                int updatedRows = mypageMapper.updateUserProfileImage(profileImageDTO);
                return updatedRows > 0;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getExistingFileId(String userId) {
        // DB에서 파일 ID만 가져옵니다.
        String fileId = mypageMapper.findProfileImagePathByUserId(userId);

        // fileId가 존재하면 바로 반환
        return (fileId != null && !fileId.isEmpty()) ? fileId : null;
    }


    // 프로필은 메소드로 경로 가져옴
    private String getExistingFilePath(String userId) {
        String existingFilePath = mypageMapper.findProfileImagePathByUserId(userId);

        // 만약 프로필 이미지 경로가 없으면 기본 경로를 리턴하거나 null을 반환
        if (existingFilePath == null || existingFilePath.isEmpty()) {
            return null;
        }

        return existingFilePath;
    }


    public String saveFileProfile(MultipartFile file, String userId) throws IOException {
        try {
            // 파일 이름 생성 (userId를 포함한 프로필 이미지 이름)
            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null || originalFileName.isEmpty()) {
                throw new IOException("파일 이름이 잘못되었습니다.");
            }

            // 파일 확장자 추출
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = userId + "_profile_image" + fileExtension;  // 확장자 포함하여 파일명 생성
            System.out.println("업로드된 파일 이름: " + fileName);

            // Google Drive에 파일 업로드하기
            // 폴더 ID를 전달해야 함
            String fileId = googleDriveService.uploadFile(file, PROFILE_IMAGE_FOLDER_ID, fileName);

            // 업로드된 파일의 Google Drive URL 반환
            String fileUrl = googleDriveService.getFileUrl(fileId);
            System.out.println("파일 업로드 완료: " + fileUrl);
            return fileUrl;  // Google Drive URL 반환
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("파일 저장 오류", e);
        }
    }



    @Transactional
    public boolean deleteUser(String userId) {
        try {
            Long userNo = mypageMapper.getUserNoById(userId);
            if (userNo == null) {
                throw new RuntimeException("User not found for userId: " + userId);
            }

            MypageDeleteAccount mypageDeleteAccount = mypageMapper.getUserNoByIdForNoshow(userId);
            if (mypageDeleteAccount == null) {
                throw new RuntimeException("User not found for userId: " + userId);
            }

            if (mypageDeleteAccount.getNoshowCount() >= 1) {
                String email = mypageDeleteAccount.getEmail();
                int noshow = mypageDeleteAccount.getNoshowCount();
                if (email != null) {
                    int result = mypageMapper.insertEmailIntoNoshow(email, noshow);
                    if (result == 0) {
                        throw new RuntimeException("Failed to insert email into TBL_NOSHOW");
                    }
                } else {
                    throw new RuntimeException("Email not found for userId: " + userId);
                }
            }

            // 프로필 이미지 삭제 (구글 드라이브에서 삭제)
            String profileImageFileId = getExistingFileId(userId);
            if (profileImageFileId != null && !profileImageFileId.isEmpty()) {
                try {
                    googleDriveService.deleteFile(profileImageFileId);  // 구글 드라이브에서 파일 삭제
                    System.out.println("프로필 이미지 삭제 성공, 파일 ID: " + profileImageFileId);
                } catch (Exception e) {
                    throw new RuntimeException("프로필 이미지 삭제 실패: " + e.getMessage());
                }
            } else {
                System.out.println("프로필 이미지가 존재하지 않거나 파일 ID가 잘못되었습니다.");
            }

            // 리뷰 이미지 삭제 (구글 드라이브에서 삭제)
            List<String> reviewImageFileIds = mypageMapper.getReviewImagesByUserId(userNo);
            if (reviewImageFileIds != null && !reviewImageFileIds.isEmpty()) {
                for (String reviewImageFileId : reviewImageFileIds) {
                    try {
                        googleDriveService.deleteFile(reviewImageFileId);
                        System.out.println("리뷰 이미지 삭제 성공, 파일 ID: " + reviewImageFileId);
                    } catch (Exception e) {
                        System.out.println("리뷰 이미지 삭제 실패: " + reviewImageFileId);
                        continue;
                    }
                }
            } else {
                System.out.println("리뷰 이미지가 존재하지 않거나 파일 ID가 잘못되었습니다.");
            }


            // 리뷰 삭제
            int reviewDeleteResult = mypageMapper.deleteReviewsByUserId(userNo);
            if (reviewDeleteResult < 1) {
                System.out.println("삭제할 리뷰가 없거나 삭제에 실패했습니다. 건너뜁니다.");
            } else {
                System.out.println("리뷰 삭제 성공");
            }

            // 유저 활동 삭제
            int userAct = mypageMapper.deleteUserAct(userNo);
            if (userAct < 1) {
                System.out.println("삭제할 유저 활동이 없거나 삭제에 실패했습니다. 건너뜁니다.");
            } else {
                System.out.println("유저 활동 삭제 성공");
            }


            // 사용자 삭제
            int result = mypageMapper.deleteUserById(userId);
            if (result == 0) {
                throw new RuntimeException("Failed to delete user");
            }

            return true;
        } catch (Exception e) {
            // 예외 로그 출력
            e.printStackTrace();
            throw new RuntimeException("Error deleting user: " + e.getMessage()); // 클라이언트로 에러 메시지 전달
        }
    }

    public boolean deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath); // 파일 경로
            if (Files.exists(path)) {
                Files.delete(path);
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public MypageReservationDetailDTO getReservationDetail(int resNo, String userId, String jwtToken) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("토큰이 일치하지 않음 -> DTO나 토큰 정보 확인");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        MypageReservationDetailDTO reservationDetail = mypageMapper.findReservationDetailByResNo(resNo);

        if (reservationDetail == null) {
            throw new IllegalArgumentException("예약을 찾을 수 없습니다.");
        }

        return reservationDetail;
    }

    public List<MypageReviewDTO> getUserReviewFromTokenWithSearch(String jwtToken, String userId, String search) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        return mypageMapper.findUserReviewByUserIdWithSearch(userId, search);
    }

    public List<MypageInquiryDTO> getUserInquiryFromTokenWithSerach(String jwtToken, String userId, String search) {
        if (!jwtService.validateToken(jwtToken)) {
            throw new IllegalArgumentException("엑토 일치하지 않음 -> DTO나 토큰 정보 확인바람");
        }

        Map<String, Object> userInfo = jwtService.getUserInfoFromToken(jwtToken);
        String extractedUserId = (String) userInfo.get("userId");

        if (!extractedUserId.equals(userId)) {
            throw new IllegalArgumentException("사용자 정보가 일치하지 않음");
        }

        return mypageMapper.findUserInquiryByUserIdWithSearch(userId, search);
    }

    private static final String QR_FILE_PATH = "\\\\192.168.0.138\\ukki_nas\\qr\\";

    public boolean deleteReservation(Long resNo) {
        try {
            MypageReservationQRDTO reservationQR = mypageMapper.findReservationQRById(resNo);
            System.out.println(reservationQR);

            if (reservationQR != null && reservationQR.getQr() != null && !reservationQR.getQr().isEmpty()) {
                String qrFilePath = QR_FILE_PATH + reservationQR.getQr() + ".png";

                File file = new File(qrFilePath);

                if (file.exists()) {
                    System.out.println("파일 있어요.");
                    file.delete();
                } else {
                    System.out.println("파일 없어요.");
                }
            }

            int deletedRows = mypageMapper.deleteReservation(resNo);
            if (deletedRows > 0) {
                Long userNo = reservationQR.getUserNo();
                int updatedCount = mypageMapper.countReservation(userNo);

                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
