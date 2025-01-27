package com.ohgiraffers.ukki.store.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class StoreInquiryDTO {

    private long inquiryNo;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryDate;
    private long reviewNo;
    private int categoryNo;
    private long userNo;
    private String answerTitle;
    private String answerContent;
    private String answerDate;
    private String state;
    private String file;
    private boolean isInquiry;

    public StoreInquiryDTO(){}

    public StoreInquiryDTO(long inquiryNo, String inquiryTitle, String inquiryContent, String inquiryDate, long reviewNo, int categoryNo, long userNo, String answerTitle, String answerContent, String answerDate, String state, String file, boolean isInquiry) {
        this.inquiryNo = inquiryNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryDate = inquiryDate;
        this.reviewNo = reviewNo;
        this.categoryNo = categoryNo;
        this.userNo = userNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.state = state;
        this.file = file;
        this.isInquiry = isInquiry;
    }

    public long getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(long inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public String getInquiryTitle() {
        return inquiryTitle;
    }

    public void setInquiryTitle(String inquiryTitle) {
        this.inquiryTitle = inquiryTitle;
    }

    public String getInquiryContent() {
        return inquiryContent;
    }

    public void setInquiryContent(String inquiryContent) {
        this.inquiryContent = inquiryContent;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public long getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(long reviewNo) {
        this.reviewNo = reviewNo;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public long getUserNo() {
        return userNo;
    }

    public void setUserNo(long userNo) {
        this.userNo = userNo;
    }

    public String getAnswerTitle() {
        return answerTitle;
    }

    public void setAnswerTitle(String answerTitle) {
        this.answerTitle = answerTitle;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public boolean isInquiry() {
        return isInquiry;
    }

    public void setInquiry(boolean inquiry) {
        isInquiry = inquiry;
    }

    @Override
    public String toString() {
        return "InquiryDTO{" +
                "inquiryNo=" + inquiryNo +
                ", inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryContent='" + inquiryContent + '\'' +
                ", inquiryDate='" + inquiryDate + '\'' +
                ", reviewNo=" + reviewNo +
                ", categoryNo=" + categoryNo +
                ", userNo=" + userNo +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate='" + answerDate + '\'' +
                ", state='" + state + '\'' +
                ", file='" + file + '\'' +
                ", isInquiry=" + isInquiry +
                '}';
    }
}
