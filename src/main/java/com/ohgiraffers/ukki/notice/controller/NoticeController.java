package com.ohgiraffers.ukki.notice.controller;

import com.ohgiraffers.ukki.notice.model.dto.UserNoticeDTO;
import com.ohgiraffers.ukki.notice.model.service.NoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    @GetMapping(value = "/user")
    public List<UserNoticeDTO> getUserNoticeList(@ModelAttribute UserNoticeDTO userNoticeDTO){

        List<UserNoticeDTO> userNoticeList = noticeService.getUserNoticeList();

        System.out.println("userNoticeList = " + userNoticeList);

        return userNoticeList;
    }

}
