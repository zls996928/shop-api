package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
@TableName("t_order_detail")
public class OrderItem implements Serializable {

    private Long id;

    private String orderId;

    private Long userId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private BigDecimal price;

    private BigDecimal subPrice;

    private int num;


}
