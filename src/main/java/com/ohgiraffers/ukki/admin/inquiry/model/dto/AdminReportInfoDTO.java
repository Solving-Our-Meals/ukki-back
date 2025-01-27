package com.ohgiraffers.ukki.admin.inquiry.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

import java.time.LocalDate;

public class AdminReportInfoDTO {
    private int reportNo;
    private String reportTitle;
    private String reportContent;
    private LocalDate reportDate;
    private int reviewNo;
    private String answerTitle;
    private String answerContent;
    private LocalDate answerDate;
    private InquiryState state;

    public AdminReportInfoDTO(){}

    public AdminReportInfoDTO(int reportNo, String reportTitle, String reportContent, LocalDate reportDate, int reviewNo, String answerTitle, String answerContent, LocalDate answerDate, InquiryState state) {
        this.reportNo = reportNo;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.reportDate = reportDate;
        this.reviewNo = reviewNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.state = state;
    }

    public int getReportNo() {
        return reportNo;
    }

    public void setReportNo(int reportNo) {
        this.reportNo = reportNo;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public int getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(int reviewNo) {
        this.reviewNo = reviewNo;
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

    @Override
    public String toString() {
        return "ReportDTO{" +
                "reportNo=" + reportNo +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportContent='" + reportContent + '\'' +
                ", reportDate=" + reportDate +
                ", reviewNo=" + reviewNo +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate=" + answerDate +
                ", state=" + state +
                '}';
    }
}
