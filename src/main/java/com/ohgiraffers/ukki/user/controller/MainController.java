package com.ohgiraffers.ukki.user.controller;

import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/","/main"})
public class MainController {

    private final StoreService storeService;

    public MainController(StoreService storeService){
        this.storeService = storeService;
    }

    @GetMapping("/category")
    public List<StoreInfoDTO> getStoresLocation(@RequestParam("category") int category) {
        return storeService.getStoresLocation(category);
    }

}
