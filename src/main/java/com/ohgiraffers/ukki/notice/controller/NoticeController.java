package com.ohgiraffers.ukki.notice.controller;

import com.ohgiraffers.ukki.notice.model.dto.NoticeDTO;
import com.ohgiraffers.ukki.notice.model.service.NoticeService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> getUserNoticeList(@ModelAttribute NoticeDTO noticeDTO, @RequestParam(value = "searchWord", required = false) String searchWord){
        try{

            List<NoticeDTO> userNoticeList = noticeService.getUserNoticeList(searchWord);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(userNoticeList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 공지사항 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    @GetMapping(value = "/boss")
    public ResponseEntity<?> getBossNoticeList(@ModelAttribute NoticeDTO noticeDTO, @RequestParam(value = "searchWord", required = false) String searchWord){
        try{
            List<NoticeDTO> bossNoticeList = noticeService.getBossNoticeList(searchWord);

            System.out.println("bossNoticeList = " + bossNoticeList);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bossNoticeList);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 공지사항 불러오는 도중에 에러가 발생했습니다.");
        }
    }

    @GetMapping(value = "/getSpecificNotice")
    public ResponseEntity<?> getSpecificNotice(@ModelAttribute NoticeDTO noticeDTO, @RequestParam("noticeNo") long noticeNo){
        try{
            noticeDTO = noticeService.getSpecificNotice(noticeNo);

            System.out.println("noticeDTO = " + noticeDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(noticeDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 공지사항 불러오는 도중에 에러가 발생했습니다.");
        }

    }

    @GetMapping(value = "/boss/recentNotice")
    public ResponseEntity<?> getRecentNotice(@ModelAttribute NoticeDTO noticeDTO){
        try{
            noticeDTO = noticeService.getRecentNotice();

            System.out.println("noticeDTO = " + noticeDTO);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(noticeDTO);
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 공지사항 불러오는 도중에 에러가 발생했습니다.");
        }
    }
}
