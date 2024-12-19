package com.ohgiraffers.ukki.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

import java.time.LocalDate;

public class InquiryListDTO {
    private String inquiryTitle;
    private LocalDate inquiryDate;
    private String state;

    public InquiryListDTO(){}

    public InquiryListDTO(String inquiryTitle, LocalDate inquiryDate, String state) {
        this.inquiryTitle = inquiryTitle;
        this.inquiryDate = inquiryDate;
        this.state = state;
    }

    public String getInquiryTitle() {
        return inquiryTitle;
    }

    public void setInquiryTitle(String inquiryTitle) {
        this.inquiryTitle = inquiryTitle;
    }

    public LocalDate getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(LocalDate inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "InquiryListDTO{" +
                "inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryDate=" + inquiryDate +
                ", state='" + state + '\'' +
                '}';
    }
}
