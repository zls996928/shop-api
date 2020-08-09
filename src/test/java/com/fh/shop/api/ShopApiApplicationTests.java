package com.fh.shop.api;

import com.fh.shop.api.Config.MyConfig;
import com.fh.shop.api.mq.MQSender;
import com.github.wxpay.sdk.WXPay;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@EnableScheduling
class ShopApiApplicationTests {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Autowired
    private MQSender mqSender;

    @Test
    void contextLoads() {
    }


    @Test
    public void testSendMsg() {
        mqSender.sendMail("你好！！！");
    }

    @Test
    public void WXPayExample(){
        MyConfig config = new MyConfig();
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("body", "飞狐商城");
        data.put("out_trade_no", "20201010105900000015");
        //data.put("device_info", "");
        //data.put("fee_type", "CNY");
        data.put("total_fee", "100");
        //data.put("spbill_create_ip", "123.12.12.123");
        data.put("notify_url", "http://www.example.com/wxpay/notify");
        data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
        //data.put("product_id", "12");

        try {
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void WXPayQuery(){
        MyConfig config = new MyConfig();
        WXPay wxpay = null;
        try {
            wxpay = new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", "2020101010595900000015");

        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            System.out.println(resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
