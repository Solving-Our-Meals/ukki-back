package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.user.model.dto.EmailDTO;
import com.ohgiraffers.ukki.user.model.dto.SignupUserDTO;
import com.ohgiraffers.ukki.user.model.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class SignupController {

    @Autowired
    private SignupService signupService;

    @PostMapping("/signupid")
    public ResponseEntity<Map<String, Boolean>> signup(@RequestBody SignupUserDTO signupUserDTO) {
        boolean isValid = signupService.signupId(signupUserDTO);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        System.out.println(signupUserDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signuppwd")
    @ResponseBody
    public boolean checkPassword(@RequestBody String password) {
        return signupService.signupPwd(password);
    }
}
