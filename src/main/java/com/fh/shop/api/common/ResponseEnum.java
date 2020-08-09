package com.fh.shop.api.common;

public enum ResponseEnum {

    ORDER_STOCK_LESS(4000,"下订单时,库存不足"),
    ORDER_IS_QUEUE(4001,"正在排队中,没有被消费"),
    ORDER_IS_ERROR(4002,"下订单失败"),

    PAY_IS_FAIL(5000,"支付失败,超时了"),

    TOKEN_IS_MISS(6000,"token头信息丢失"),
    TOKEN_IS_ERROR(6001,"token错误"),
    TOKEN_REQUEST_REPET(6002,"请求重复"),

    CART_PRODUCT_IS_NULL(3000,"添加的商品不存在"),
    CART_PRODUCT_IS_DOWN(3001,"商品下架了"),
    CART_NUM_IS_ERROR(3002,"商品的数量不合法"),
    CART_DELETE_BATCH_IDS_IS_NULL(3003,"批量删除时ids必须传递"),

    MEMBERNAME_IS_NULL(1001,"用户名不能为空"),
    PASSWORD_IS_NULL(1002,"密码不能为空"),
    REALNAME_IS_NULL(1003,"真实姓名不能为空"),
    BIRTHDAY_IS_NULL(1004,"出生年月日为空"),
    MAIL_IS_NULL(1005,"邮箱不能为空"),
    PHONE_IS_NULL(1006,"电话号码不能为空"),
    MEMBERNAME_IS_CUNZAI(1007,"用户名已存在"),
    PHONE_IS_CUNZAI(1008,"手机号已存在"),
    MAIL_IS_CUNZAI(1009,"邮箱已存在"),
    MEMBER_IS_NULL(1010,"用户信息不能为空"),
    MEMBER_IS_ERROR(1011,"用户信息不存在"),
    PASSWORD_IS_ERROR(1012,"密码输入错误,请重新输入"),
    HEADER_IS_NULL(1013,"头信息丢失"),
    HEADER_IS_NOT_COMPLETE(1014,"头信息不完整"),
    DATA_IS_ERROR(1015,"数据被篡改"),
    LOGIN_TIME_IS_OUT(1016,"登录已失效请重新登录"),

    ;

    private Integer code;
    private String msg;





    private ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }



    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
