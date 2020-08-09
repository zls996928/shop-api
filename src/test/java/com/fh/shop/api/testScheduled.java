package com.fh.shop.api;

import com.fh.shop.api.utils.EmailHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootTest
public class testScheduled {
    @Autowired
    private EmailHelper emailHelper;
    @Test
    @Scheduled(cron = "*/5 * * * * * *")
    public void test(){
        emailHelper.sendEmail("409112398@qq.com","定时器test","5秒钟一个");
    }
}
