package com.ohgiraffers.ukki.notice.model.service;

import com.ohgiraffers.ukki.notice.model.dao.NoticeMapper;
import com.ohgiraffers.ukki.notice.model.dto.NoticeDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public NoticeService(NoticeMapper noticeMapper){
        this.noticeMapper = noticeMapper;
    }

    public List<NoticeDTO> getUserNoticeList() {
        return noticeMapper.getUserNoticeList();
    }

    public List<NoticeDTO> getBossNoticeList() {
        return noticeMapper.getBossNoticeList();
    }
}
