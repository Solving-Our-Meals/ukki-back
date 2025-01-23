package com.ohgiraffers.ukki.user.model.dto;

public class MypageDeleteAccount {
    private Long userNo;
    private String email;
    private int noshowCount;

    public MypageDeleteAccount() {}

    public MypageDeleteAccount(Long userNo, String email, int noshowCount) {
        this.userNo = userNo;
        this.email = email;
        this.noshowCount = noshowCount;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNoshowCount() {
        return noshowCount;
    }

    public void setNoshowCount(int noshowCount) {
        this.noshowCount = noshowCount;
    }

    @Override
    public String toString() {
        return "MypageDeleteAccount{" +
                "userNo=" + userNo +
                ", email='" + email + '\'' +
                ", noshowCount=" + noshowCount +
                '}';
    }
}
