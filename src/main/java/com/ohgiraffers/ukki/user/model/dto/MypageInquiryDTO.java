package com.ohgiraffers.ukki.user.model.dto;

import com.ohgiraffers.ukki.common.InquiryState;

import java.io.File;
import java.util.Date;

public class MypageInquiryDTO {
    private int userNo;
    private String userId;
    private int inquiryNo;
    private InquiryState inquiryState;
    private String title;
    private String text;
    private int categoryNo;
    private String category;
    private Date inquiryDate;
    private String answerTitle;
    private String answerContent;
    private Date answerDate;
    private String file;
    private String search;

    public MypageInquiryDTO() {}

    public MypageInquiryDTO(int userNo, String userId, int inquiryNo, InquiryState inquiryState, String title, String text, int categoryNo, String category, Date inquiryDate, String answerTitle, String answerContent, Date answerDate, String file, String search) {
        this.userNo = userNo;
        this.userId = userId;
        this.inquiryNo = inquiryNo;
        this.inquiryState = inquiryState;
        this.title = title;
        this.text = text;
        this.categoryNo = categoryNo;
        this.category = category;
        this.inquiryDate = inquiryDate;
        this.answerTitle = answerTitle;
        this.answerContent = answerContent;
        this.answerDate = answerDate;
        this.file = file;
        this.search = search;
    }

    public int getUserNo() {
        return userNo;
    }

    public void setUserNo(int userNo) {
        this.userNo = userNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getInquiryNo() {
        return inquiryNo;
    }

    public void setInquiryNo(int inquiryNo) {
        this.inquiryNo = inquiryNo;
    }

    public InquiryState getInquiryState() {
        return inquiryState;
    }

    public void setInquiryState(InquiryState inquiryState) {
        this.inquiryState = inquiryState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCategoryNo() {
        return categoryNo;
    }

    public void setCategoryNo(int categoryNo) {
        this.categoryNo = categoryNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(Date inquiryDate) {
        this.inquiryDate = inquiryDate;
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

    public Date getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(Date answerDate) {
        this.answerDate = answerDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    @Override
    public String toString() {
        return "MypageInquiryDTO{" +
                "userNo=" + userNo +
                ", userId='" + userId + '\'' +
                ", inquiryNo=" + inquiryNo +
                ", inquiryState=" + inquiryState +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", categoryNo=" + categoryNo +
                ", category='" + category + '\'' +
                ", inquiryDate=" + inquiryDate +
                ", answerTitle='" + answerTitle + '\'' +
                ", answerContent='" + answerContent + '\'' +
                ", answerDate=" + answerDate +
                ", file='" + file + '\'' +
                ", search='" + search + '\'' +
                '}';
    }
}
