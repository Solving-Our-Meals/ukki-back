package com.ohgiraffers.ukki.admin.reservation.model.dao;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminReservationMapper {
    ThisWeekReservationDTO weeklyReservation();

    int todayReservation();

    MonthlyNoShowDTO monthlyNoShowReservation();
}
