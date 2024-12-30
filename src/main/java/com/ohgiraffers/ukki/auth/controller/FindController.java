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

    @PostMapping("/find/id")
    public ResponseEntity<?> findUserId(@RequestBody FindDTO findDTO) {
        String userId = findService.findUserIdByEmail(findDTO.getEmail());

        if (userId != null) {
            FindResponseDTO responseDTO = new FindResponseDTO(userId, true);
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(404).body(new FindResponseDTO(null, false));
        }
    }

    @PostMapping("/find/pwd")
    public ResponseEntity<?> changePassword(@RequestBody FindDTO findDTO) {
        // 단계를 나눠서 비밀번호 확인절차에서는 필요없을 것 같아 배제
//        String passwordValidationError = findService.validatePassword(findDTO.getNewPassword());
//        if (passwordValidationError != null) {
//            return ResponseEntity.status(400).body(new FindResponseDTO(false, passwordValidationError));
//        }

        boolean passwordChanged = findService.changePassword(findDTO.getEmail(), findDTO.getNewPassword());
        if (passwordChanged) {
            FindResponseDTO responseDTO = new FindResponseDTO(true, "비밀번호 변경 완료 !");
            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(400).body(false);
        }
    }

    }
