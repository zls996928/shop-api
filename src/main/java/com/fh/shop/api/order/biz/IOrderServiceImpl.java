package com.fh.shop.api.order.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fh.shop.api.Config.MQConfig;
import com.fh.shop.api.Product.mapper.ProductMapper;
import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.cart.vo.CartItem;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.exception.StockLessException;
import com.fh.shop.api.order.mapper.AddressMapper;
import com.fh.shop.api.order.mapper.IOrderItemMapper;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.param.OrderParam;
import com.fh.shop.api.order.po.Order;

import com.fh.shop.api.order.po.OrderInfo;
import com.fh.shop.api.order.po.OrderItem;
import com.fh.shop.api.order.vo.OrderConfirmVo;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.utils.KeyUtils;
import com.fh.shop.api.utils.RedisUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("orderService")
public class IOrderServiceImpl implements IOrderService {

    @Autowired
    private AddressService addressService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private IOrderMapper orderMapper;

    @Autowired
    private IOrderItemMapper orderItemMapper;

    @Autowired
    private IPayLogMapper payLogMapper;




    @Override
    public ServerResponse generateOrder(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();
        //清除之前的标志位
        RedisUtil.del(KeyUtils.buildOrderKey(memberId));
        RedisUtil.del(KeyUtils.buildStockLessKey(memberId));
        RedisUtil.del(KeyUtils.buildOrderErrorKey(memberId));
        //将订单信息发送到消息队列中
        String orderParamJson= JSONObject.toJSONString(orderParam);
        rabbitTemplate.convertAndSend(MQConfig.ORDEREXCHANGE,MQConfig.ORDERROUTEKEY,orderParamJson);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse generateOrderConfirm(Long memberId) {
        //获取会员对应的收件人列表
        List<OrderInfo> recipientList = addressService.findList(memberId);
        //获取会员对应的购物车信息
        String cartJson = RedisUtil.get(KeyUtils.buildCartKey(memberId));
        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        //组装要返回的信息
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        orderConfirmVo.setCart(cart);
        orderConfirmVo.setRecipientList(recipientList);
        return ServerResponse.success(orderConfirmVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(OrderParam orderParam) {
        Long memberId = orderParam.getMemberId();
        String cartJson = RedisUtil.get(KeyUtils.buildCartKey(memberId));

        Cart cart = JSONObject.parseObject(cartJson, Cart.class);
        List<CartItem> cartItemList = cart.getCartItemList();
        //减库存【数据库的乐观锁】
        //update t_product set stock=stock-num where id=productId and stock>=num
        //考虑到并发
        for (CartItem cartItem : cartItemList) {
            Long goodsId = cartItem.getGoodsId();
            int num = cartItem.getNum();
            int rowCount = productMapper.updateStock(goodsId,num);
            if(rowCount==0){
              //没有更新成功，库存不足
              //RedisUtil.set(KeyUtils.buildStockLessKey(memberId),"stock less");
              //既要起到回滚的作用，又要起到提示的作用
                throw new StockLessException("stock less");
            }
        }
        //获取对应的收件人信息
        Long recipientId = orderParam.getRecipientId();
        OrderInfo orderInfo = addressMapper.selectById(recipientId);
        //插入订单表
        Order order = new Order();
        //手工设置id【通过雪花算法生成的唯一标识】
        String orderId = IdWorker.getIdStr();
        order.setId(orderId);
        order.setCreateTime(new Date());
        order.setRecipientor(orderInfo.getConsignee());
        order.setPhone(orderInfo.getPhone());
        order.setAddress(orderInfo.getAddress());
        order.setUserId(memberId);
        order.setTotalPrice(cart.getTotalPrice());
        order.setRecipientId(recipientId);
        order.setPayType(orderParam.getPayType());
        order.setStatus(SystemConst.OrderStatus.WAIT_PAY);//未支付
        order.setTotalNum(cart.getTotalNum());
        orderMapper.insert(order);
        //插入订单明细表
        //批量插入
        //insert into 表名(字段1，字段2...) values (值1，值2,...),(值1，值2,...),(值1，值2,...)
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cartItem.getGoodsId());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setProductName(cartItem.getGoodsName());
            orderItem.setUserId(memberId);
            orderItem.setImageUrl(cartItem.getImageUrl());
            orderItem.setNum(cartItem.getNum());
            orderItem.setSubPrice(cartItem.getSubPrice());
            orderItemList.add(orderItem);
         }
         //批量插入订单明细表
        orderItemMapper.batchInsert(orderItemList);
        //插入支付日志表
        PayLog payLog = new PayLog();
        payLog.setOrderId(orderId);
        payLog.setCreateTime(new Date());
        payLog.setUserId(memberId);
        payLog.setPayStatus(SystemConst.PayStatus.WAIT_PAY);
        payLog.setPayType(orderParam.getPayType());
        String outTradeNo = IdWorker.getIdStr();
        payLog.setOutTradeNo(outTradeNo);
        payLog.setPayMoney(cart.getTotalPrice());
        payLogMapper.insert(payLog);
        //插入redis中
        String payLogJson = JSONObject.toJSONString(payLog);
        RedisUtil.set(KeyUtils.buildPayLogKey(memberId),payLogJson);
        //删除redis中购物车
        RedisUtil.del(KeyUtils.buildCartKey(memberId));
        //下单成功
        RedisUtil.set(KeyUtils.buildOrderKey(memberId),"ok");

    }

    @Override
    public ServerResponse getResult(Long memberId) {
        if(RedisUtil.exists(KeyUtils.buildStockLessKey(memberId))){
            RedisUtil.del(KeyUtils.buildStockLessKey(memberId));
            //证明库存不足
          return ServerResponse.error(ResponseEnum.ORDER_STOCK_LESS);
        }
        if(RedisUtil.exists(KeyUtils.buildOrderKey(memberId))){
            //删除redis中的订单成功的标志位
            RedisUtil.del(KeyUtils.buildOrderKey(memberId));
            //证明下订单成功
          return ServerResponse.success();
        }
        if(RedisUtil.exists(KeyUtils.buildOrderErrorKey(memberId))){
            //证明下订单失败
            RedisUtil.del(KeyUtils.buildOrderErrorKey(memberId));
            return ServerResponse.error(ResponseEnum.ORDER_IS_ERROR);
        }
        return ServerResponse.error(ResponseEnum.ORDER_IS_QUEUE);
    }


}
