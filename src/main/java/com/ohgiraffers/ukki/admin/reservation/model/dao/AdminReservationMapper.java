package com.ohgiraffers.ukki.admin.reservation.model.dao;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ReservationListDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserResDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ReservationInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminReservationMapper {
    ThisWeekReservationDTO weeklyReservation();

    int todayReservation();

    MonthlyNoShowDTO monthlyNoShowReservation();

    List<AdminUserResDTO> userResList();

    List<ReservationListDTO> searchRes(String category, String word);

    int totalTodayReservation();

    int totalEndReservation();

    List<ReservationListDTO> searchEndRes(String category, String word);

    ReservationInfoDTO endResInfo(int resNo);

    ReservationInfoDTO todayResInfo(int resNo);

    void deleteTodayRes(int resNo);

    void deleteEndRes(int resNo);
}
