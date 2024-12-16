package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
    }

    public boolean signupId(String userId) {
        return signupMapper.signupId(userId) == 0;
    }

    public boolean signupPwd(String password) {
        // 비밀번호가 영문, 숫자, 특수문자 조합으로 8~20자 사이인지 확인하는 정규 표현식
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,20}$";
        return password.matches(passwordRegex);
    }

    // 이메일 인증 코드 전송
    public boolean sendVerificationCode(String email) {
        // 인증 코드 생성
        String verificationCode = generateVerificationCode();

        // 이메일 전송 (메일 서버 설정 필요)
        sendEmail(email, verificationCode);

        // Redis에 이메일과 인증 코드 저장, TTL 설정
        redisTemplate.opsForValue().set(email, verificationCode, 5, TimeUnit.MINUTES); // 인증 코드는 5분 유효

        return true; // 이메일 전송 성공
    }

    // 이메일 인증 코드 확인
    public boolean verifyCode(String email, String verificationCode) {
        // Redis에서 저장된 인증 코드 가져오기
        String storedCode = redisTemplate.opsForValue().get(email);

        // 인증 코드가 존재하고 일치하는지 확인
        if (storedCode != null && storedCode.equals(verificationCode)) {
            // 인증 완료 후 Redis에서 해당 인증 코드 삭제
            redisTemplate.delete(email);
            return true; // 인증 성공
        }

        return false; // 인증 실패
    }

    // 인증 코드 생성 (6자리 숫자)
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    // 이메일 보내기 메서드 (이메일 서버 설정 필요)
    private void sendEmail(String email, String verificationCode) {
        String from = "your-email@example.com"; // 보내는 이메일
        String host = "smtp.example.com"; // SMTP 서버
        String username = "your-username"; // 이메일 계정
        String password = "your-password"; // 이메일 계정 비밀번호

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("이메일 인증 코드");
            message.setText("인증 코드: " + verificationCode);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
