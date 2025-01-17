package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.controller.ReservationDTO;
import com.ohgiraffers.ukki.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BossMapper {

    List<ReservationDTO> selectReservationStatusByStore(int storeNo);

    List<ReservationDTO> selectReservationPeopleList(int storeNo);

    int selectAvailableReservationPeople(int storeNo, String reservationDate);

    WeeklyReservationCountDTO selectWeeklyReservationCount(int storeNo);

    int selectTodayReservationCount(int storeNo);



    String getNextAvailableTime(int storeNo, String resDate, String currentTime);

    List<DayResPosNumDTO> getResPosNum(StoreResPosNumDTO storeResPosNumDTO);
}
