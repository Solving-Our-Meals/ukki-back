package com.ohgiraffers.ukki.store.model.dto;

public class WeeklyReservationCountDTO {
    private int weeklyReservationCount;

    public WeeklyReservationCountDTO() {
        this.weeklyReservationCount = weeklyReservationCount;
    }

    public int getWeeklyReservationCount() {
        return weeklyReservationCount;
    }

    public void setWeeklyReservationCount(int weeklyReservationCount) {
        this.weeklyReservationCount = weeklyReservationCount;
    }

    @Override
    public String toString() {
        return "WeeklyReservationCountDTO{" +
                "weeklyReservationCount=" + weeklyReservationCount +
                '}';
    }
}
