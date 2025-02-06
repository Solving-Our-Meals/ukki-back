package com.ohgiraffers.ukki.admin.reservation.model.dao;

import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationListDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserResDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminReservationMapper {
    ThisWeekReservationDTO weeklyReservation();

    int todayReservation();

    MonthlyNoShowDTO monthlyNoShowReservation();

    List<AdminUserResDTO> userResList();

    List<AdminReservationListDTO> searchRes(String category, String word);

    int totalTodayReservation();

    int totalEndReservation();

    List<AdminReservationListDTO> searchEndRes(String category, String word);

    AdminReservationInfoDTO endResInfo(int resNo);

    AdminReservationInfoDTO todayResInfo(int resNo);

    void deleteTodayRes(int resNo);

    void deleteEndRes(int resNo);

    void decreaseResCount(int userNo);
}
