package com.ohgiraffers.ukki.admin.reservation.model.dao;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserResDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminReservationMapper {
    ThisWeekReservationDTO weeklyReservation();

    int todayReservation();

    MonthlyNoShowDTO monthlyNoShowReservation();

    List<AdminUserResDTO> userResList();
}
