package com.ohgiraffers.ukki.notice.model.dto;

public class UserNoticeDTO {

    private long noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private String date;
    private long categoryNo;

    public UserNoticeDTO(){}

    public UserNoticeDTO(long noticeNo, String noticeTitle, String noticeContent, String date, long categoryNo) {
        this.noticeNo = noticeNo;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.date = date;
        this.categoryNo = categoryNo;
    }

    public long getNoticeNo() {
        return noticeNo;
    }

    public void setNoticeNo(long noticeNo) {
        this.noticeNo = noticeNo;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(long categoryNo) {
        this.categoryNo = categoryNo;
    }

    @Override
    public String toString() {
        return "UserNoticeDTO{" +
                "noticeNo=" + noticeNo +
                ", noticeTitle='" + noticeTitle + '\'' +
                ", noticeContent='" + noticeContent + '\'' +
                ", date='" + date + '\'' +
                ", categoryNo=" + categoryNo +
                '}';
    }
}
