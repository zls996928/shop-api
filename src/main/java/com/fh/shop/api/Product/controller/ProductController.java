package com.fh.shop.api.Product.controller;

import com.fh.shop.api.Product.biz.ProductService;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")

public class ProductController {

    @Autowired
    private ProductService productService;



    @GetMapping("queryHotList")

    public ServerResponse queryHotList(){

        return productService.queryHotList();
    }

}
