package com.fh.shop.api.order.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.param.OrderParam;

public interface IOrderService {


    ServerResponse generateOrder(OrderParam orderParam);

    ServerResponse generateOrderConfirm(Long memberId);

    void createOrder(OrderParam orderParam);

    ServerResponse getResult(Long memberId);
}
