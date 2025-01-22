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
import org.springframework.transaction.annotation.Transactional;
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
//    private static final String FILE_UPLOAD_DIR = "\\\\192.168.0.138\\ukki_nas\\inquiry";
    private static final String FILE_UPLOAD_DIR = "C:\\Temp\\inquiry";

    public boolean updateInquiry(MypageInquiryDTO inquiryToUpdate, MultipartFile file, String userId) {
        try {
            // 1. 기존 파일 삭제
            String existingFileName = inquiryToUpdate.getFile();  // 기존 파일명 가져오기
            if (existingFileName != null && !existingFileName.isEmpty()) {
                String existingFilePath = getInquiryFilePath(existingFileName);  // Inquiry 경로로 수정
                System.out.println("기존 파일 경로: " + existingFilePath);  // 경로 출력하여 확인

                File existingFile = new File(existingFilePath);

                if (existingFile.exists()) {
                    boolean deleted = existingFile.delete();  // 파일 삭제
                    if (deleted) {
                        System.out.println("기존 파일 삭제 성공: " + existingFilePath);
                    } else {
                        System.out.println("기존 파일 삭제 실패: " + existingFilePath);
                        System.out.println("파일 삭제 실패. 파일의 삭제 권한 또는 경로를 확인하세요.");
                    }
                } else {
                    System.out.println("기존 파일이 존재하지 않습니다: " + existingFilePath);
                }
            }

            // 2. 새 파일 저장
            if (file != null && !file.isEmpty()) {
                String newFileName = file.getOriginalFilename();  // 파일명 그대로 사용
                String newFilePath = saveInquiryFile(file, newFileName);  // Inquiry 저장
                inquiryToUpdate.setFile(newFileName);  // DB에는 파일명만 저장
            }

            // 3. DB 업데이트
            int updatedRows = mypageMapper.updateInquiry(inquiryToUpdate);
            return updatedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 인쿼리용 파일 경로 생성
    private String getInquiryFilePath(String fileName) {
        // inquiry 파일 저장 디렉토리와 파일명을 결합하여 전체 경로를 반환
        return "C:\\Temp\\inquiry\\" + fileName;
    }

    // 인쿼리용 파일을 저장하는 메소드
    private String saveInquiryFile(MultipartFile file, String fileName) throws IOException {
        // 로컬 경로 설정 (인쿼리 폴더로 저장)
        String networkPath = "C:\\Temp\\inquiry\\";  // inquiry 폴더로 설정
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

    private String saveFile(MultipartFile file, String userId) throws IOException {
        // 파일 이름 가져오기
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("파일 이름이 잘못되었습니다.");
        }
        System.out.println("업로드된 파일 이름: " + fileName);

        // 로컬 경로 설정 (필요한 디렉터리를 사용자 폴더로 설정)
        String networkPath = FILE_UPLOAD_DIR + File.separator;
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

        MypageInquiryDTO inquiry = mypageMapper.findInquiryById(inquiryNo);

        if (inquiry != null && inquiry.getFile() != null && !inquiry.getFile().isEmpty()) {
            boolean fileDeleted = deleteFile(inquiry.getFile());
            if (!fileDeleted) {
                return false;
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

    private static final String PROFILE_IMAGE_DIR = "C:\\Temp\\profile";
//    private final String PROFILE_IMAGE_DIR = "\\\\192.168.0.138\\ukki_nas"; // 업로드 디렉토리 경로
public boolean updateProfileImage(String userId, MultipartFile profileImage) {
    try {
        String existingFilePath = getExistingFilePath(userId);

        // 기존 프로필 이미지가 있으면 삭제
        if (existingFilePath != null && !existingFilePath.isEmpty()) {
            File existingFile = new File(existingFilePath);
            if (existingFile.exists()) {
                boolean deleted = existingFile.delete();
                if (deleted) {
                    System.out.println("기존 파일 삭제 성공: " + existingFilePath);
                } else {
                    System.out.println("기존 파일 삭제 실패: " + existingFilePath);
                }
            }
        }

        // 새 프로필 이미지가 존재하면 저장
        if (profileImage != null && !profileImage.isEmpty()) {
            String newFilePath = saveFileProfile(profileImage, userId);  // saveFile 대신 saveFileProfile 사용

            // DB에 새 경로를 업데이트 (경로만 저장)
            MypageProfileImageDTO profileImageDTO = new MypageProfileImageDTO();
            profileImageDTO.setUserId(userId);
            profileImageDTO.setFile(newFilePath);

            // DB 업데이트
            int updatedRows = mypageMapper.updateUserProfileImage(profileImageDTO);
            return updatedRows > 0;
        }

        return false;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
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

    private String saveFileProfile(MultipartFile file, String userId) throws IOException {
        // 파일 이름 가져오기 (프로필 이미지 이름을 userId를 포함한 형태로 저장)
        String fileName = userId + "_profile_image.jpg";  // 프로필 이미지 파일명은 고정
        if (fileName == null || fileName.isEmpty()) {
            throw new IOException("파일 이름이 잘못되었습니다.");
        }
        System.out.println("업로드된 파일 이름: " + fileName);

        // 저장할 경로 설정
        String networkPath = PROFILE_IMAGE_DIR + File.separator;
        System.out.println("파일 저장 경로: " + networkPath);

        // 디렉토리 생성
        File directory = new File(networkPath);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (dirCreated) {
                System.out.println("디렉토리 생성 성공: " + networkPath);
            } else {
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
            throw new IOException("파일 저장 오류", e);
        }
    }

/*    @Transactional // 여러 데이터베이스 연산을 하나의 트랜잭션으로 처리
    public boolean deleteUser(String userId) {
        try {
            String profileImagePath = getExistingFilePath(userId);
            if (profileImagePath != null) {
                boolean isFileDeleted = deleteFile(profileImagePath);
                if (!isFileDeleted) {
                    return false;
                }
            }

            int result = mypageMapper.deleteUserById(userId);
            if (result == 0) {
                return false;
            }

            return true;
        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return false;
        }
    }*/

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

            // 프로필 이미지 삭제
            String profileImagePath = getExistingFilePath(userId);
            if (profileImagePath != null && !profileImagePath.isEmpty()) {
                boolean isFileDeleted = deleteFile(profileImagePath);
                if (!isFileDeleted) {
                    throw new RuntimeException("프로필 이미지 제거 실패");
                } else {
                    System.out.println("프로필 사진 없음");
                }
            } else {
                System.out.println("프로필 이미지가 존재하지 않습니다. 건너뜁니다.");
            }

            // 리뷰 이미지 삭제
            List<String> reviewImages = mypageMapper.getReviewImagesByUserId(userNo);
            if (reviewImages != null && !reviewImages.isEmpty()) {
                for (String reviewImagePath : reviewImages) {
                    boolean isFileDeleted = deleteFile(reviewImagePath);
                    if (!isFileDeleted) {
                        System.out.println("Failed to delete review image: " + reviewImagePath);
                        continue;
                    }
                }
            } else {
                System.out.println("리뷰 이미지가 존재하지 않습니다. 건너뜁니다.");
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
}
