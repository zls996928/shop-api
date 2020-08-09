package com.fh.shop.api.mq;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.Config.MQConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MQSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendMail(String info){
        amqpTemplate.convertAndSend(MQConfig.MAILEXCHANGE,MQConfig.MAILROUTEKEY,info);
    }

    public void sendMailMessage(MailMessage mailMessage){
        String mailJson =  JSONObject.toJSONString(mailMessage);
        amqpTemplate.convertAndSend(MQConfig.MAILEXCHANGE,MQConfig.MAILROUTEKEY,mailJson);
    }
}
