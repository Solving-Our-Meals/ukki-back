//package com.ohgiraffers.ukki.store.controller;
//
//import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
//import com.ohgiraffers.ukki.store.model.service.StoreService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//
//@Controller
//@RequestMapping(value="/")
//public class StoreController {
//
//    private final StoreService storeService;
//
//    public StoreController(StoreService storeService){
//        this.storeService = storeService;
//    }
//
//    @GetMapping(value="/store/5")
//    public StoreInfoDTO getStoreInfo(ModelAndView mv, StoreInfoDTO storeInfoDTO){
//
//        System.out.println("getStoreInfo 넘어옴");
//        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
//
//        return storeInfoDTO;
//    }
//
//}
//
