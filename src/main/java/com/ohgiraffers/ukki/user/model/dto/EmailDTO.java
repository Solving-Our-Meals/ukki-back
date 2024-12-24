package com.ohgiraffers.ukki.user.model.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailDTO {
    private String email;
    private String code;

    public EmailDTO() {}

    public EmailDTO(String email, String code) {
        this.email = email;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isValidEmail() {
        String emailConfirm = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailConfirm);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.matches();
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "email='" + email + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
