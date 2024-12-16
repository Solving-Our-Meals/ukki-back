package com.ohgiraffers.ukki.inquiry.model.dto;

public class InquiryCategoryDTO {
    private int categoryNo;
    private String categoryName;

    public InquiryCategoryDTO(){}

    public InquiryCategoryDTO(int categoryNo, String categoryName) {
        this.categoryNo = categoryNo;
        this.categoryName = categoryName;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "InquiryCategoryDTO{" +
                "categoryNo=" + categoryNo +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
