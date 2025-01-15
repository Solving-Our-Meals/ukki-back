package com.ohgiraffers.ukki.notice.controller;

import com.ohgiraffers.ukki.notice.model.dto.NoticeDTO;
import com.ohgiraffers.ukki.notice.model.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService){
        this.noticeService = noticeService;
    }

    @GetMapping(value = "/user")
    public List<NoticeDTO> getUserNoticeList(@ModelAttribute NoticeDTO noticeDTO){

        List<NoticeDTO> userNoticeList = noticeService.getUserNoticeList();

        return userNoticeList;
    }

    @GetMapping(value = "/boss")
    public List<NoticeDTO> getBossNoticeList(@ModelAttribute NoticeDTO noticeDTO){

        List<NoticeDTO> bossNoticeList = noticeService.getBossNoticeList();

        System.out.println("bossNoticeList = " + bossNoticeList);

        return bossNoticeList;
    }

    @GetMapping(value = "/getSpecificNotice")
    public NoticeDTO getSpecificNotice(@ModelAttribute NoticeDTO noticeDTO, @RequestParam("noticeNo") long noticeNo){

        noticeDTO = noticeService.getSpecificNotice(noticeNo);

        System.out.println("noticeDTO = " + noticeDTO);

        return noticeDTO;
    }

}
