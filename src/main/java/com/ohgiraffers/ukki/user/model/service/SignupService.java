package com.ohgiraffers.ukki.user.model.service;

import com.ohgiraffers.ukki.user.model.dao.SignupMapper;
import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupMapper signupMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(SignupMapper signupMapper) {
        this.signupMapper = signupMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean signupId(SignupUserDTO signupUserDTO) {
        int count = signupMapper.signupId(signupUserDTO.getUserId());
        return count == 0;
    }

    public String validatePassword(String password) {
        if (password.length() < 8) {
            return "ⓘ 비밀번호는 최소 8자 이상이어야 합니다.";
        }

        if (!password.matches(".*[0-9].*")) {
            return "ⓘ 비밀번호에는 최소 하나의 숫자가 포함되어야 합니다.";
        }

        if (!password.matches(".*[!@#$%^&*()_+\\-\\=\\[\\]{};':\"\\\\|,.<>\\/?]+.*")) {
            return "ⓘ 비밀번호에는 최소 하나의 특수문자가 포함되어야 합니다.";
        }

        return "ⓘ 비밀번호가 유효합니다.";
    }

    public boolean signupNickname(SignupUserDTO signupUserDTO) {
        int count = signupMapper.signupNickname(signupUserDTO.getUserName());
        return count == 0;
    }

    public boolean realSignup(SignupUserDTO signupUserDTO) {
        try {
            String hashedPassword = passwordEncoder.encode(signupUserDTO.getUserPass());
            signupUserDTO.setUserPass(hashedPassword);

            int noshowCount = signupMapper.getNoshowCountByEmail(signupUserDTO.getEmail()); // DTO에서 이메일 가져와서 그 이메일의 NOSHOW 횟수를 가져오라
//            System.out.println(noshowCount); 여기까지 반환 잘됨
            if (noshowCount > 0) {
                signupUserDTO.setNoshow(noshowCount); // 만약 반환된 값이 0보다 크면 값을 DTO의 noshow 횟수로 설정혀라
                signupMapper.signup(signupUserDTO); // 회원가입 시키기
                // 이제 업데이트는 쿼리문이 아닌 여기서 해준 상태라고 볼 수 있고
                // 값을 가져왔으니 기존 노쇼에선 제거해줄것
                signupMapper.removeEmailFromNoshow(signupUserDTO.getEmail()); // DTO에서 이메일 가져와서 그 이메일 삭제혀라

            } else {
                signupUserDTO.setNoshow(0);
                signupMapper.signup(signupUserDTO);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}