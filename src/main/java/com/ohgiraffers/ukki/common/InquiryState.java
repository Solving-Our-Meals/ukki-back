package com.ohgiraffers.ukki.common;

public enum InquiryState {
    CHECK("확인완료"),
    PROCESSING("처리중"),
    COMPLETE("처리완료");

    private String inquiryState;

    InquiryState() {}

    InquiryState(String inquiryState) {
        this.inquiryState=inquiryState;
    }

    public String getInquiryState() {
        return inquiryState;
    }
}
