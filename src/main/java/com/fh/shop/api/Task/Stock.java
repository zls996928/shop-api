package com.fh.shop.api.Task;

import com.fh.shop.api.Product.biz.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Stock {

    @Autowired
    private ProductService productService;

/*
    @Scheduled(cron = "0/30 * * * * ?")
*/
    public void stock(){
        productService.sendMail();
    }
}
