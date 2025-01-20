package com.ohgiraffers.ukki.store.model.dto;

public class ReportReviewDTO {

    private long reportNo;
    private String reportTitle;
    private String reportContent;
    private String reportDate;
    private long reviewNo;
    private String answerTitle;
    private String answerContent;
    private String answerDate;
    private String status;

    public ReportReviewDTO(){}

    public ReportReviewDTO(long reportNo, String reportTitle, String reportContent, String reportDate, long reviewNo, String answerTitle, String answerContent, String answerDate, String status) {
        this.reportNo = reportNo;
        this.reportTitle = reportTitle;
        this.reportContent = reportContent;
        this.reportDate = reportDate;
        this.reviewNo = reviewNo;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.status = status;
    }

    public long getReportNo() {
        return reportNo;
    }

    public void setReportNo(long reportNo) {
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

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public long getReviewNo() {
        return reviewNo;
    }

    public void setReviewNo(long reviewNo) {
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

    public String getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(String answerDate) {
        this.answerDate = answerDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ReportReviewDTO{" +
                "reportNo=" + reportNo +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportContent='" + reportContent + '\'' +
                ", reportDate='" + reportDate + '\'' +
                ", reviewNo=" + reviewNo +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate='" + answerDate + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
