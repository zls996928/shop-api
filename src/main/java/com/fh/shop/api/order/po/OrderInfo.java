package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_recipient")
public class OrderInfo implements Serializable {

    private Long id;

    private String consignee;

    private String address;

    private String phone;

    private String mail;

    private String alias;

    private int status;

    private Long memberId;
}
