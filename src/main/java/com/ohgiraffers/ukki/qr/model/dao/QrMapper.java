package com.ohgiraffers.ukki.qr.model.dao;

import com.ohgiraffers.ukki.qr.model.dto.QrConfirmDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QrMapper {
    QrConfirmDTO qrConfirmation(String qr);

    int qrConfirmSuccess(String qr);
}
