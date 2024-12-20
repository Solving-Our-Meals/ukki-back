package com.ohgiraffers.ukki.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

import java.time.LocalDate;

public class InquiryListDTO {
    private String division;
    private int inquiryNo;
    private String inquiryTitle;
    private LocalDate inquiryDate;
    private String state;

    public InquiryListDTO(){}

    public InquiryListDTO(String division, int inquiryNo, String inquiryTitle, LocalDate inquiryDate, String state) {
        this.division = division;
        this.inquiryNo = inquiryNo;
        this.inquiryTitle = inquiryTitle;
        this.inquiryDate = inquiryDate;
        this.state = state;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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
                "division='" + division + '\'' +
                ", inquiryNo=" + inquiryNo +
                ", inquiryTitle='" + inquiryTitle + '\'' +
                ", inquiryDate=" + inquiryDate +
                ", state='" + state + '\'' +
                '}';
    }
}
