package com.ohgiraffers.ukki.admin.store.model.dto;

public class MonthlyRegistStoreDTO {
    private int year;
    private int month;
    private int registStore;

    public MonthlyRegistStoreDTO(){}

    public MonthlyRegistStoreDTO(int year, int month, int registStore) {
        this.year = year;
        this.month = month;
        this.registStore = registStore;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getRegistStore() {
        return registStore;
    }

    public void setRegistStore(int registStore) {
        this.registStore = registStore;
    }

    @Override
    public String toString() {
        return "MonthlyRegistStoreDTO{" +
                "year=" + year +
                ", month=" + month +
                ", registStore=" + registStore +
                '}';
    }
}
