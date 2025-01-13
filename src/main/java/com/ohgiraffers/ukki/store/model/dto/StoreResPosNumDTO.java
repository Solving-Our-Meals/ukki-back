package com.ohgiraffers.ukki.store.model.dto;

import java.util.List;

public class StoreResPosNumDTO {

    private long storeNo;
    private String date;
    private String day; // 요일
    private List<DayResPosNumDTO> listDayResPosNumDTO;

    public StoreResPosNumDTO(){}

    public StoreResPosNumDTO(long storeNo, String date, String day, List<DayResPosNumDTO> listDayResPosNumDTO) {
        this.storeNo = storeNo;
        this.date = date;
        this.day = day;
        this.listDayResPosNumDTO = listDayResPosNumDTO;
    }

    public long getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(long storeNo) {
        this.storeNo = storeNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<DayResPosNumDTO> getListDayResPosNumDTO() {
        return listDayResPosNumDTO;
    }

    public void setListDayResPosNumDTO(List<DayResPosNumDTO> listDayResPosNumDTO) {
        this.listDayResPosNumDTO = listDayResPosNumDTO;
    }

    @Override
    public String toString() {
        return "StoreResPosNumDTO{" +
                "storeNo=" + storeNo +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", listDayResPosNumDTO=" + listDayResPosNumDTO +
                '}';
    }
}