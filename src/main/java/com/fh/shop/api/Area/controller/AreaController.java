package com.fh.shop.api.Area.controller;

import com.fh.shop.api.Area.biz.AreaService;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/areas")
public class AreaController {

    @Autowired
    private AreaService areaService;


    @GetMapping("/{id}")
    public ServerResponse queryArea(@PathVariable Long id){
        return areaService.queryArea(id);
    }

}
