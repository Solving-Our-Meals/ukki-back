package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.FindDTO;
import com.ohgiraffers.ukki.auth.model.service.FindService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class FindController {

    private final FindService findService;

    private FindController(FindService findService) {
        this.findService = findService;
    }

    @PostMapping("/find")
    public ResponseEntity<String> find(@RequestBody FindDTO findDTO) {
        String type = findDTO.getType();

        boolean result;
        String message;

        if ("id".equals(type)) {
            result = findService.findUserId(findDTO.getEmail());
            message = result ? "아이디를 찾았습니다." : "아이디가 존재하지 않습니다.";
        } else if ("password".equals(type)) {
            result = findService.findUserPassword(findDTO.getEmail());
            message = result ? "비밀번호 찾기 이메일이 전송되었습니다." : "등록된 이메일이 없습니다.";
        } else {
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }

        return result ? ResponseEntity.ok(message) : ResponseEntity.badRequest().body(message);
    }

    }
