package com.ohgiraffers.ukki.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class InquiryDTO {
    private int inquiryNo;
    private String inquiryTitle;
    private String inquiryContent;
    private String inquiryDate;
    private int categoryNo;
    private int userNo;
    private String answerTitle;
    private String answerContent;
    private String answerDate;
    private InquiryState state;
    private String file;

    public InquiryDTO(){};

    public InquiryDTO(int inquiryNo, String inquiryTitle, String inquiryContent, LocalDate inquiryDate, int categoryNo, int userNo, String answerTitle, String answerContent, LocalDate answerDate, InquiryState state, String file) {
        this.inquiryNo = inquiryNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryContent = inquiryContent;
        this.inquiryDate = inquiryDate.toString();
        this.categoryNo = categoryNo;
        this.userNo = userNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate.toString();
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

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
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

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
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
