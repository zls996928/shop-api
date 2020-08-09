package com.fh.shop.api.paylog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_paylog")
public class PayLog {
    @TableId(value="out_trade_no",type= IdType.INPUT)
    private String outTradeNo;

    private String orderId  ;

    private Date createTime ;

    private Integer payStatus;

    private String transactionId;

    private Long userId;

    private Date payTime;

    private BigDecimal payMoney;

    private Integer payType;
}
