package com.ohgiraffers.ukki.store.model.dto;

import java.util.ArrayList;
import java.util.List;

public class BannerDTO {

    private long storeNo;
    private String banner1;
    private String banner2;
    private String banner3;
    private String banner4;
    private String banner5;

    public BannerDTO(long storeNo, String banner1, String banner2, String banner3, String banner4, String banner5) {
        this.storeNo = storeNo;
        this.banner1 = banner1;
        this.banner2 = banner2;
        this.banner3 = banner3;
        this.banner4 = banner4;
        this.banner5 = banner5;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getBanner1() {
        return banner1;
    }

    public void setBanner1(String banner1) {
        this.banner1 = banner1;
    }

    public String getBanner2() {
        return banner2;
    }

    public void setBanner2(String banner2) {
        this.banner2 = banner2;
    }

    public String getBanner3() {
        return banner3;
    }

    public void setBanner3(String banner3) {
        this.banner3 = banner3;
    }

    public String getBanner4() {
        return banner4;
    }

    public void setBanner4(String banner4) {
        this.banner4 = banner4;
    }

    public String getBanner5() {
        return banner5;
    }

    public void setBanner5(String banner5) {
        this.banner5 = banner5;
    }

    @Override
    public String toString() {
        return "BannerDTO{" +
                "storeNo=" + storeNo +
                ", banner1='" + banner1 + '\'' +
                ", banner2='" + banner2 + '\'' +
                ", banner3='" + banner3 + '\'' +
                ", banner4='" + banner4 + '\'' +
                ", banner5='" + banner5 + '\'' +
                '}';
    }

    // DB에서 가져온 필드의 값들이 존재하면 bannerList에 추가하기
    public List<String> getBannerList(){
        List<String> bannerList = new ArrayList<>();
        if(banner1 != null && !banner1.isEmpty()) bannerList.add(banner1);
        if(banner2 != null && !banner2.isEmpty()) bannerList.add(banner2);
        if(banner3 != null && !banner3.isEmpty()) bannerList.add(banner3);
        if(banner4 != null && !banner4.isEmpty()) bannerList.add(banner4);
        if(banner5 != null && !banner5.isEmpty()) bannerList.add(banner5);

        return bannerList;
    }

}
