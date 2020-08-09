package com.fh.shop.api.common;

public class SystemConst {


    public static final String SECRET ="asdasdgre211212fwtds12@1*+12131asdasasd" ;
    public static final String MEMBERINFO ="member" ;
    public static final Integer TIME =10*60 ;

    public static final int PRODUCT_IS_DOWN =0;

    public interface OrderStatus{
        int WAIT_PAY=10;
        int PAY_SUCCESS=20;
        int SEND_GOODS=30;
        int TRADE_SUCCESS=40;
        int TRADE_CLOSE=50;
        int COMMENT_OVER=60;
    }

    public interface PayStatus{
        int WAIT_PAY=0;
        int PAY_SUCCESS=1;
    }
}
