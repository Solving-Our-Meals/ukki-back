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

    }
