package com.fh.shop.api.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.Config.MyConfig;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.utils.BigDecimalUtil;
import com.fh.shop.api.utils.DateTimeUtils;
import com.fh.shop.api.utils.KeyUtils;
import com.fh.shop.api.utils.RedisUtil;
import com.github.wxpay.sdk.WXPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payService")
public class PayServiceImpl implements PayService {

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IPayLogMapper payLogMapper;

    @Override
    public ServerResponse createNative(Long memberId) {
        //获取会员对应的支付日志
        String payLogJson = RedisUtil.get(KeyUtils.buildPayLogKey(memberId));
        PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
        //获取支付的相关信息
        String outTradeNo = payLog.getOutTradeNo();
        BigDecimal payMoney = payLog.getPayMoney();
        String orderId = payLog.getOrderId();
        //调用微信接口进行统一下单
        try {
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            data.put("body", "飞狐商城");
            int money = BigDecimalUtil.mul(payMoney.toString(), "100").intValue();
            data.put("out_trade_no", outTradeNo);
            //要把项目中的元转为分
            data.put("total_fee", money+"");
            data.put("notify_url", "http://www.example.com/wxpay/notify");
            data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
            String time = DateTimeUtils.addMinutes(new Date(), 2, DateTimeUtils.FULLTIMEINFO);
            data.put("time_expire",time);
            Map<String, String> resp = wxpay.unifiedOrder(data);
            System.out.println(resp);
            String return_code = resp.get("return_code");
            String return_msg = resp.get("return_msg");
            if(!return_code.equals("SUCCESS")){
                //错误消息是微信平台返回的
                return ServerResponse.error(99999,return_msg,null);
            }
            String result_code = resp.get("result_code");
            String error_code_des = resp.get("error_code_des");
            if(!result_code.equals("SUCCESS")){
                //错误消息是微信平台返回的
                return ServerResponse.error(99999,error_code_des,null);
            }
            //证明return_code和result_code都是success
            String code_url = resp.get("code_url");
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("codeUrl",code_url);
            resultMap.put("orderId",orderId);
            resultMap.put("totalPrice",payMoney.toString());
            return ServerResponse.success(resultMap);
        }catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }

    @Override
    public ServerResponse queryStatus(Long memberId) {

        try {
           //获取会员对应的支付日志
            String payLogJson = RedisUtil.get(KeyUtils.buildPayLogKey(memberId));
            PayLog payLog = JSONObject.parseObject(payLogJson, PayLog.class);
            String outTradeNo = payLog.getOutTradeNo();
            String orderId = payLog.getOrderId();
            MyConfig config = new MyConfig();
            WXPay wxpay = new WXPay(config);


            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", outTradeNo);
            int count = 0;
            while(true){
                Map<String, String> resp = wxpay.orderQuery(data);
                System.out.println(resp);
                String return_code = resp.get("return_code");
                String return_msg = resp.get("return_msg");
                if(!return_code.equals("SUCCESS")){
                    //错误消息是微信平台返回的
                    return ServerResponse.error(99999,return_msg,null);
                }
                String result_code = resp.get("result_code");
                String error_code_des = resp.get("error_code_des");
                if(!result_code.equals("SUCCESS")){
                    //错误消息是微信平台返回的
                    return ServerResponse.error(99999,error_code_des,null);
                }
                String trade_state = resp.get("trade_state");
                if(trade_state.equals("SUCCESS")){
                    //证明支付成功
                    String transaction_id = resp.get("transaction_id");
                    //更新订单
                    Order order = new Order();
                    order.setId(orderId);
                    order.setPayTime(new Date());
                    order.setStatus(SystemConst.OrderStatus.PAY_SUCCESS);
                    orderMapper.updateById(order);
                    //更新支付日志
                    PayLog payLogInfo = new PayLog();
                    payLogInfo.setOutTradeNo(outTradeNo);
                    payLogInfo.setPayTime(new Date());
                    payLogInfo.setPayStatus(SystemConst.PayStatus.PAY_SUCCESS);
                    payLogMapper.updateById(payLogInfo);
                    //删除redis中的支付日志
                    RedisUtil.del(KeyUtils.buildPayLogKey(memberId));
                    //响应客户端
                    return ServerResponse.success();
                }else{
                    Thread.sleep(2000);
                    count++;
                    if(count>60){
                        //未支付成功
                        return ServerResponse.error(ResponseEnum.PAY_IS_FAIL);
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }

    }


}
