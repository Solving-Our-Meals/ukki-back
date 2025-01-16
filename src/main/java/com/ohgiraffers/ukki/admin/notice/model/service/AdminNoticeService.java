package com.ohgiraffers.ukki.admin.notice.model.service;

import com.ohgiraffers.ukki.admin.notice.model.dao.AdminNoticeMapper;
import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeCategoryDTO;
import com.ohgiraffers.ukki.admin.notice.model.dto.AdminNoticeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNoticeService {

    private final AdminNoticeMapper adminNoticeMapper;

    @Autowired
    public AdminNoticeService(AdminNoticeMapper adminNoticeMapper){
        this.adminNoticeMapper = adminNoticeMapper;
    }

    public List<AdminNoticeDTO> searchUserNotice(String word) {
        return adminNoticeMapper.searchUserNotice(word);
    }

    public List<AdminNoticeDTO> searchStoreNotice(String word) {
        return adminNoticeMapper.searchStoreNotice(word);
    }

    public List<AdminNoticeCategoryDTO> searchNoticeCategory() {
        return adminNoticeMapper.searchNoticeCategory();
    }

    public AdminNoticeDTO searchNoticeInfo(int noticeNo) {
        return adminNoticeMapper.searchNoticeInfo(noticeNo);
    }
}
