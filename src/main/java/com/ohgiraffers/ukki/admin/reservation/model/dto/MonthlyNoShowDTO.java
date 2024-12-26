package com.ohgiraffers.ukki.admin.reservation.model.dto;

public class MonthlyNoShowDTO {

    private int monthlyReservation;
    private int monthlyNoShow;

    public MonthlyNoShowDTO(){}

    public MonthlyNoShowDTO(int monthlyReservation, int monthlyNoShow) {
        this.monthlyReservation = monthlyReservation;
        this.monthlyNoShow = monthlyNoShow;
    }

    public int getMonthlyReservation() {
        return monthlyReservation;
    }

    public void setMonthlyReservation(int monthlyReservation) {
        this.monthlyReservation = monthlyReservation;
    }

    public int getMonthlyNoShow() {
        return monthlyNoShow;
    }

    public void setMonthlyNoShow(int monthlyNoShow) {
        this.monthlyNoShow = monthlyNoShow;
    }

    @Override
    public String toString() {
        return "monthlyNoShowDTO{" +
                "monthlyReservation=" + monthlyReservation +
                ", monthlyNoShow=" + monthlyNoShow +
                '}';
    }
}
