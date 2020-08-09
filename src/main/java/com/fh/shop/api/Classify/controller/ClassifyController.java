package com.fh.shop.api.Classify.controller;

import com.fh.shop.api.Classify.biz.ClassifyService;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/classifys")

public class ClassifyController {

    @Autowired
    private ClassifyService classifyService;
    @GetMapping("queryClassIfylist")
    public ServerResponse queryClassIfylist(){

        return   classifyService.queryClassIfylist();
    }
}
