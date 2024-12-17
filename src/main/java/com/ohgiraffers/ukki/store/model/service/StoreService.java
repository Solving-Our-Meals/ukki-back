package com.ohgiraffers.ukki.store.model.service;

import com.ohgiraffers.ukki.store.model.dao.StoreMapper;
import com.ohgiraffers.ukki.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final StoreMapper storeMapper;

    public StoreService(StoreMapper storeMapper){
        this.storeMapper = storeMapper;
    }


    public StoreInfoDTO getStoreInfo(StoreInfoDTO storeInfoDTO) {
        return storeMapper.getStoreInfo(storeInfoDTO);
    }

//    public KeywordDTO getKeyword(StoreInfoDTO storeInfoDTO) {
//        return storeMapper.getKeyword(storeInfoDTO);
//    }
}
