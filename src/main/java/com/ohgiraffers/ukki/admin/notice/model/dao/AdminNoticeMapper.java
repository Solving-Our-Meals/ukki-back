package com.ohgiraffers.ukki.admin.notice.model.dao;

import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeCategoryDTO;
import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminNoticeMapper {
    List<AdminNoticeDTO> searchUserNotice(String word);

    List<AdminNoticeDTO> searchStoreNotice(String word);

    List<AdminNoticeCategoryDTO> searchNoticeCategory();

    AdminNoticeDTO searchNoticeInfo(int noticeNo);

    void editNoticeInfo(AdminNoticeDTO adminNoticeDTO);

    void deleteNotice(int noticeNo);

    void registNotice(AdminNoticeDTO adminNoticeDTO);
}
