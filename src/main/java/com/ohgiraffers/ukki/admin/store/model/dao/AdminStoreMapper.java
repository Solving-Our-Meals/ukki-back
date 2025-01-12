package com.ohgiraffers.ukki.admin.store.model.dao;

import com.ohgiraffers.ukki.admin.store.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdminStoreMapper {
    List<MonthlyRegistStoreDTO> monthlyRegistStore();

    int totalRegistStore();

    List<AdminStoreListDTO> searchBy(Map<String, String> params);

    AdminStoreInfoDTO searchStoreInfo(int storeNo);

    KeywordDTO getKeyword(long storeNo);

    OperationDTO getOperation(long storeNo);

    List<CategoryDTO> getCategory();

    void editOperationTime(OperationDTO operationTime);

    void editKeyword(KeywordDTO storeKeyword);

    void editStore(AdminStoreInfoDTO storeData);

    int deleteStoreInfo(int storeNo);

    void deleteStoreBanner(int storeNo);

    void deleteStoreOperation(int storeNo);

    void editBanner(BannerDTO bannerDTO);

    int lastStoreNo();

    int insertOperationTime(OperationDTO operationDTO);

    int insertKeyword(KeywordDTO keywordDTO);

    int insertBanner(BannerDTO bannerDTO);

    void insertStoreUser(AdminStoreUserDTO userData);

    void insertStore(AdminStoreInfoDTO storeData);

    int searchCurrentStoreUser(String userId);

    void deleteStoreKeyword(int storeNo);
}
