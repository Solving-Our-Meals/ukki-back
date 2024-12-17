package com.ohgiraffers.ukki.store.controller;

import com.ohgiraffers.ukki.store.model.dto.KeywordDTO;
import com.ohgiraffers.ukki.store.model.dto.StoreInfoDTO;
import com.ohgiraffers.ukki.store.model.service.StoreService;
import org.apache.ibatis.javassist.compiler.ast.Keyword;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/store")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService){
        this.storeService = storeService;
    }

    // 검색 페이지 만들어지면 pathvariable로 변경하기
    @GetMapping(value="/test", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public StoreInfoDTO getStoreInfo(ModelAndView mv, @ModelAttribute StoreInfoDTO storeInfoDTO){

        // storeDB 연결
        System.out.println("getStoreInfo 넘어옴");
        storeInfoDTO = storeService.getStoreInfo(storeInfoDTO);
        mv.addObject("getStoreInfo", storeInfoDTO);

        System.out.println(storeInfoDTO);

        // keywordDB 연결
//        KeywordDTO keywordDTO = storeService.getKeyword(storeInfoDTO);
//        storeInfoDTO.setStoreKeyword(storeService.getKeyword(storeInfoDTO));



        return storeInfoDTO;
    }

}

