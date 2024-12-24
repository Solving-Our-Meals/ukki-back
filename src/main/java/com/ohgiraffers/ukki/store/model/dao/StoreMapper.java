package com.ohgiraffers.ukki.store.model.dao;

import com.ohgiraffers.ukki.store.model.dto.BannerDTO;
import com.ohgiraffers.ukki.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.store.model.dto.OperationDTO;
import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreMapper {

    StoreInfoDTO getStoreInfo(StoreInfoDTO storeInfoDTO);

    KeywordDTO getKeyword(StoreInfoDTO storeInfoDTO);

    OperationDTO getOperation(StoreInfoDTO storeInfoDTO);

    BannerDTO getBannerList(StoreInfoDTO storeInfoDTO);
}
