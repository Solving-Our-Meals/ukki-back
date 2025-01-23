package com.ohgiraffers.ukki.reservation.model.dao;

import com.ohgiraffers.ukki.reservation.model.dto.ReservationInfoDTO;
import com.ohgiraffers.ukki.reservation.model.dto.ReservationStoreDTO;
import com.ohgiraffers.ukki.reservation.model.dto.StoreBannerDTO;
import org.apache.ibatis.annotations.Mapper;
@Mapper
public interface ReservationMapper {
    // 가게의 대표 사진 정보 조회
    StoreBannerDTO getRepPhotoName(long storeNo);

    // 예약된 가게 정보 조회
    ReservationStoreDTO getReservationedStoreInfo(long storeNo);

    // 예약 정보 삽입
    void insertReservation(ReservationInfoDTO reservationInfoDTO);

    // 이메일 조회
    String getEmail(ReservationInfoDTO reservationInfoDTO);

    // 예약 수 증가
    void increaseReservation(long userNo);

    // 예약 가능한 인원 조회
    Integer getAvailablePosNum(int storeNo, String reservationDate, String reservationTime);

    // 예약 가능한 인원 업데이트
    void updateReservationPosNum(int storeNo, String reservationDate, String reservationTime, int newPosNumber);

    ReservationInfoDTO checkExistReservation(ReservationInfoDTO reservationInfoDTO);
}
