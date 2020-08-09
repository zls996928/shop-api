package com.fh.shop.api.order.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.po.OrderInfo;

import java.util.List;

public interface AddressService {

    public List<OrderInfo> findList(Long memberId);

    ServerResponse addAddress(OrderInfo address);



    ServerResponse addOrderData(Long memberId, OrderInfo orderInfo);

    ServerResponse getOrderById(Long memberId, OrderInfo orderInfo);
}
