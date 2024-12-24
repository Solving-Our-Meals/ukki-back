package com.ohgiraffers.ukki.report.model.dto;

import java.time.LocalDate;

public class ReportListDTO {
    private String division;
    private int reportNo;
    private String reportTitle;
    private LocalDate reportDate;
    private String state;

    public ReportListDTO(){}

    public ReportListDTO(String division, int reportNo, String reportTitle, LocalDate reportDate, String state) {
        this.division = division;
        this.reportNo = reportNo;
        this.reportTitle = reportTitle;
        this.reportDate = reportDate;
        this.state = state;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
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

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "ReportListDTO{" +
                "division='" + division + '\'' +
                ", reportNo=" + reportNo +
                ", reportTitle='" + reportTitle + '\'' +
                ", reportDate=" + reportDate +
                ", state='" + state + '\'' +
                '}';
    }
}
