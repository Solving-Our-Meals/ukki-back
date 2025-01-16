package com.ohgiraffers.ukki.qr.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrMapper {

    String resStoreUserName(String qr);

    void editQrConfirmRes(String qr);
}
