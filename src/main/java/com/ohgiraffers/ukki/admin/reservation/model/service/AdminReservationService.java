package com.ohgiraffers.ukki.admin.reservation.model.service;

import com.ohgiraffers.ukki.admin.reservation.model.dao.AdminReservationMapper;
import com.ohgiraffers.ukki.admin.reservation.model.dto.MonthlyNoShowDTO;
import com.ohgiraffers.ukki.admin.reservation.model.dto.ThisWeekReservationDTO;
import com.ohgiraffers.ukki.admin.user.model.dto.AdminUserResDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReservationService {

    private final AdminReservationMapper adminReservationMapper;

    public AdminReservationService(AdminReservationMapper adminReservationMapper){
        this.adminReservationMapper = adminReservationMapper;
    }

    public ThisWeekReservationDTO weeklyReservation() {
        return adminReservationMapper.weeklyReservation();
    }

    public int todayReservation() {
        return adminReservationMapper.todayReservation();
    }

    public MonthlyNoShowDTO monthlyNoShowReservation() {
        return adminReservationMapper.monthlyNoShowReservation();
    }

    public List<AdminUserResDTO> userResList() { return adminReservationMapper.userResList();
    }
}
