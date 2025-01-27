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

    AdminStoreInfoDTO searchStoreInfo(long storeNo);

    AdminStoreKeywordDTO getKeyword(long storeNo);

    AdminStoreOperationDTO getOperation(long storeNo);

    List<AdminStoreCategoryDTO> getCategory();

    void editOperationTime(AdminStoreOperationDTO operationTime);

    void editKeyword(AdminStoreKeywordDTO storeKeyword);

    void editStore(AdminStoreInfoDTO storeData);

    int deleteStoreInfo(int storeNo);

    void deleteStoreBanner(int storeNo);

    void deleteStoreOperation(int storeNo);

    void editBanner(AdminStoreBannerDTO adminStoreBannerDTO);

    int lastStoreNo();

    int insertOperationTime(AdminStoreOperationDTO adminStoreOperationDTO);

    int insertKeyword(AdminStoreKeywordDTO adminStoreKeywordDTO);

    int insertBanner(AdminStoreBannerDTO adminStoreBannerDTO);

    void insertStoreUser(AdminStoreUserDTO userData);

    void insertStore(AdminStoreInfoDTO storeData);

    int searchCurrentStoreUser(String userId);

    void deleteStoreKeyword(int storeNo);

    String[] getReviewImgStoreNo(int storeNo);

    void deleteReviewWithStore(int storeNo);

    AdminStoreBannerDTO getBanner(Long storeNo);
}
