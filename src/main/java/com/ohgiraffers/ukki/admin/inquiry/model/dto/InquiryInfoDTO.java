package com.ohgiraffers.ukki.admin.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

import java.time.LocalDate;

public class InquiryInfoDTO {
    private int inquiryNo;
    private String inquiryTitle;
    private String inquiryContent;
    private LocalDate inquiryDate;
    private int categoryNo;
    private int userNo;
    private String answerTitle;
    private String answerContent;
    private LocalDate answerDate;
    private InquiryState state;
    private String file;

    public InquiryInfoDTO(){};

    public InquiryInfoDTO(int inquiryNo, String inquiryTitle, String inquiryContent, LocalDate inquiryDate, int categoryNo, int userNo, String answerTitle, String answerContent, LocalDate answerDate, InquiryState state, String file) {
        this.inquiryNo = inquiryNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryDate = inquiryDate;
        this.categoryNo = categoryNo;
        this.userNo = userNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.state = state;
        this.file = file;
    }


    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
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

    public LocalDate getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(LocalDate inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
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

    public LocalDate getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDate answerDate) {
        this.answerDate = answerDate;
    }

    public InquiryState getState() {
        return state;
    }

    public void setState(InquiryState state) {
        this.state = state;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "InquiryDTO{" +
                "inquiryNo=" + inquiryNo +
                ", inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryContent='" + inquiryContent + '\'' +
                ", inquiryDate=" + inquiryDate +
                ", categoryNo=" + categoryNo +
                ", userNo=" + userNo +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate=" + answerDate +
                ", state=" + state +
                ", file='" + file + '\'' +
                '}';
    }
}
