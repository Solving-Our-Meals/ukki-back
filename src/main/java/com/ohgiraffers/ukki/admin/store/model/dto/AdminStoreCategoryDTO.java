package com.ohgiraffers.ukki.admin.store.model.dto;

public class AdminStoreCategoryDTO {

    private int categoryNo;
    private String categoryName;

    public AdminStoreCategoryDTO(){}

    public AdminStoreCategoryDTO(int categoryNo, String categoryName) {
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
        return "CategoryDTO{" +
                "categoryNo=" + categoryNo +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
