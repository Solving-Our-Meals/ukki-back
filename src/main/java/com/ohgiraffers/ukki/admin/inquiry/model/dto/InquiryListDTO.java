package com.ohgiraffers.ukki.admin.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

public class InquiryListDTO {

    private int inqNo;
    private String categoryName;
    private String inqDate;
    private String inqTitle;
    private String inqContent;
    private InquiryState state;
    private boolean isInquiry;

    public InquiryListDTO(){}

    public InquiryListDTO(int inqNo, String categoryName, String inqDate, String inqTitle, String inqContent, InquiryState state, boolean isInquiry) {
        this.inqNo = inqNo;
        this.categoryName = categoryName;
        this.inqDate = inqDate;
        this.inqTitle = inqTitle;
        this.inqContent = inqContent;
        this.state = state;
        this.isInquiry = isInquiry;
    }

    public int getInqNo() {
        return inqNo;
    }

    public void setInqNo(int inqNo) {
        this.inqNo = inqNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getInqDate() {
        return inqDate;
    }

    public void setInqDate(String inqDate) {
        this.inqDate = inqDate;
    }

    public String getInqTitle() {
        return inqTitle;
    }

    public void setInqTitle(String inqTitle) {
        this.inqTitle = inqTitle;
    }

    public String getInqContent() {
        return inqContent;
    }

    public void setInqContent(String inqContent) {
        this.inqContent = inqContent;
    }

    public InquiryState getState() {
        return state;
    }

    public void setState(InquiryState state) {
        this.state = state;
    }

    public boolean isInquiry() {
        return isInquiry;
    }

    public void setInquiry(boolean inquiry) {
        isInquiry = inquiry;
    }

    @Override
    public String toString() {
        return "InquiryListDTO{" +
                "inqNo=" + inqNo +
                ", categoryName='" + categoryName + '\'' +
                ", inqDate='" + inqDate + '\'' +
                ", inqTitle='" + inqTitle + '\'' +
                ", inqContent='" + inqContent + '\'' +
                ", state=" + state +
                ", isInquiry=" + isInquiry +
                '}';
    }
}
