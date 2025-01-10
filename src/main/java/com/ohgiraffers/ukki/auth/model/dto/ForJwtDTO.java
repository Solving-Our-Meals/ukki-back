package com.ohgiraffers.ukki.auth.model.dto;

public class ForJwtDTO {
    private int userNo;
    private String userRole;

    ForJwtDTO () {}

    public ForJwtDTO(int userNo, String userRole) {
        this.userNo = userNo;
        this.userRole = userRole;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "ForJwtDTO{" +
                "userNo=" + userNo +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}
