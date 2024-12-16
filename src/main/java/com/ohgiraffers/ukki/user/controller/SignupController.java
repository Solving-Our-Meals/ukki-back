package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.EmailDTO;
import com.ohgiraffers.ukki.user.model.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @GetMapping("/signupid")
    @ResponseBody
    public boolean checkId(@RequestParam String userId) {
        return signupService.signupId(userId);
    }

    @PostMapping("/signuppwd")
    @ResponseBody
    public boolean checkPassword(@RequestBody String password) {
        return signupService.signupPwd(password);
    }
}
