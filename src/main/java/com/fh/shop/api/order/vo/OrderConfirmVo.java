package com.fh.shop.api.order.vo;

import com.fh.shop.api.cart.vo.Cart;
import com.fh.shop.api.order.po.OrderInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderConfirmVo implements Serializable {

    private List<OrderInfo> recipientList= new ArrayList<>();

    private Cart cart;
}
