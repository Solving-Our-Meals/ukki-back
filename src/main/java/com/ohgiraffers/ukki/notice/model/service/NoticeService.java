package com.ohgiraffers.ukki.notice.model.service;

import com.ohgiraffers.ukki.notice.model.dao.NoticeMapper;
import com.ohgiraffers.ukki.notice.model.dto.UserNoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper){
        this.noticeMapper = noticeMapper;
    }

    public List<UserNoticeDTO> getUserNoticeList() {
        return noticeMapper.getUserNoticeList();
    }
}
