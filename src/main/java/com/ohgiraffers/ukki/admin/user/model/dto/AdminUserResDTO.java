package com.ohgiraffers.ukki.admin.user.model.dto;

public class AdminUserResDTO {
    private int userNo;
    private int totalRes;
    private int totalRandomRes;

    public AdminUserResDTO() {}

    public AdminUserResDTO(int userNo, int totalRes, int totalRandomRes) {
        this.userNo = userNo;
        this.totalRes = totalRes;
        this.totalRandomRes = totalRandomRes;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public int getTotalRes() {
        return totalRes;
    }

    public void setTotalRes(int totalRes) {
        this.totalRes = totalRes;
    }

    public int getTotalRandomRes() {
        return totalRandomRes;
    }

    public void setTotalRandomRes(int totalRandomRes) {
        this.totalRandomRes = totalRandomRes;
    }

    @Override
    public String toString() {
        return "AdminUserResDTO{" +
                "userNo=" + userNo +
                ", totalRes=" + totalRes +
                ", totalRandomRes=" + totalRandomRes +
                '}';
    }
}
