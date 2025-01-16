package com.ohgiraffers.ukki.admin.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

public class AnswerDTO {

    private int inquiryNo;
    private String answerTitle;
    private String answerContent;
    private String answerDate;
    private InquiryState state;

    public AnswerDTO(){}

    public AnswerDTO(int inquiryNo, String answerTitle, String answerContent, String answerDate, InquiryState state) {
        this.inquiryNo = inquiryNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.state = state;
    }

    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
        this.inquiryNo = inquiryNo;
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

    @Override
    public String toString() {
        return "AnswerDTO{" +
                "inquiryNo=" + inquiryNo +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate='" + answerDate + '\'' +
                ", state=" + state +
                '}';
    }
}
