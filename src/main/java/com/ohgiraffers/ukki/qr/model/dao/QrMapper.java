package com.ohgiraffers.ukki.qr.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrMapper {

    String resStoreUserName(int resNo);

    void editQrConfirmRes(int resNo);

    Integer getLastReservationNo();

    String searchQr(int resNo);

    void moveResToEndRes(int resNo);
}
