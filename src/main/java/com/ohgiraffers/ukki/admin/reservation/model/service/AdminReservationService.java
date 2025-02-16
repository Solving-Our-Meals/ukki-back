package com.ohgiraffers.ukki.admin.reservation.model.service;

import com.ohgiraffers.ukki.admin.reservation.model.dao.AdminReservationMapper;
import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationListDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserResDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.AdminReservationInfoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReservationService {

    private final AdminReservationMapper adminReservationMapper;

    public AdminReservationService(AdminReservationMapper adminReservationMapper){
        this.adminReservationMapper = adminReservationMapper;
    }

    public ThisWeekReservationDTO weeklyReservation() {
        return adminReservationMapper.weeklyReservation() != null ? 
            adminReservationMapper.weeklyReservation() : 
            new ThisWeekReservationDTO();
    }

    public int todayReservation() {
        return adminReservationMapper.todayReservation() != 0 ? 
            adminReservationMapper.todayReservation() : 
            0;
    }

    public MonthlyNoShowDTO monthlyNoShowReservation() {
        return adminReservationMapper.monthlyNoShowReservation() != null ? 
            adminReservationMapper.monthlyNoShowReservation() : 
            new MonthlyNoShowDTO();
    }

    public List<AdminUserResDTO> userResList() { return adminReservationMapper.userResList();
    }

    public List<AdminReservationListDTO> searchRes(String category, String word) {
        System.out.println("today"+category);
        System.out.println(word);
        return adminReservationMapper.searchRes(category, word);
    }

    public int totalTodayReservation() {
        return adminReservationMapper.totalTodayReservation();
    }

    public int totalEndReservation() {
        return adminReservationMapper.totalEndReservation();
    }

    public List<AdminReservationListDTO> searchEndRes(String category, String word) {
        System.out.println("end"+category);
        System.out.println(word);
        return adminReservationMapper.searchEndRes(category, word);
    }

    public AdminReservationInfoDTO todayResInfo(int resNo) {
        return adminReservationMapper.todayResInfo(resNo);
    }

    public AdminReservationInfoDTO endResInfo(int resNo) {
        return adminReservationMapper.endResInfo(resNo);
    }

    public void deleteTodayRes(int resNo) {
        adminReservationMapper.deleteTodayRes(resNo);
    }

    public void deleteEndRes(int resNo) {
        adminReservationMapper.deleteEndRes(resNo);
    }

    public void decreaseResCount(int userNo) {
        adminReservationMapper.decreaseResCount(userNo);
    }
}
