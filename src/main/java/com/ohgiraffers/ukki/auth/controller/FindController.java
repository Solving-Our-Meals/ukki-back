package com.ohgiraffers.ukki.auth.controller;

import com.ohgiraffers.ukki.auth.model.dto.FindDTO;
import com.ohgiraffers.ukki.auth.model.dto.FindResponseDTO;
import com.ohgiraffers.ukki.auth.model.service.FindService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class FindController {

    private final FindService findService;

    private FindController(FindService findService) {
        this.findService = findService;
    }

    @PostMapping("/finds1")
    public ResponseEntity<?> findUserId(@RequestBody FindDTO findDTO) {
        String userId = findService.findUserIdByEmail(findDTO.getEmail());

        if (userId != null) {
            FindResponseDTO responseDTO = new FindResponseDTO(userId, true);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).body(new FindResponseDTO(null, false));
        }
    }

    @PostMapping("/finds2")
    public ResponseEntity<?> changePassword(@RequestBody FindDTO findDTO) {
        boolean passwordChanged = findService.changePassword(findDTO.getEmail(), findDTO.getNewPassword());

        if (passwordChanged) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(400).body("ⓘ 비밀번호 변경에 실패했습니다.");
        }
    }

    }
