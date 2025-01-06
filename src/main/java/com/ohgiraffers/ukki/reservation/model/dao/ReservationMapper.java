package com.ohgiraffers.ukki.reservation.model.dao;

import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper {
    StoreBannerDTO getRepPhotoName(long storeNo);

    ReservationStoreDTO getReservationedStoreInfo(long storeNo);

    void insertReservation(ReservationInfoDTO reservationInfoDTO);

    String getEmail(ReservationInfoDTO reservationInfoDTO);
}
