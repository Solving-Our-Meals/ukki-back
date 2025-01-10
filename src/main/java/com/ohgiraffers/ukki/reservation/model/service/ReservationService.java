package com.ohgiraffers.ukki.reservation.model.service;

import com.ohgiraffers.ukki.reservation.model.dao.ReservationMapper;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationMapper reservationMapper){
        this.reservationMapper = reservationMapper;
    }

    public StoreBannerDTO getRepPhotoName(long storeNo) {
        return reservationMapper.getRepPhotoName(storeNo);
    }

    public ReservationStoreDTO getReservationedStoreInfo(long storeNo) {
        return reservationMapper.getReservationedStoreInfo(storeNo);
    }

    public void insertReservation(ReservationInfoDTO reservationInfoDTO) {
        reservationMapper.insertReservation(reservationInfoDTO);
    }

    public String getEmail(ReservationInfoDTO reservationInfoDTO) {
        return reservationMapper.getEmail(reservationInfoDTO);
    }
}
