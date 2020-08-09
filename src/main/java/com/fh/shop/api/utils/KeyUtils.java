package com.fh.shop.api.utils;

public class KeyUtils {
    public static  final int MEMBER_EXPIRE=30*60;
    public static  String memberKey(Long id,String uuid){
        return "member:"+id+":"+uuid;
    }

    public static String buildCartKey(Long memberId) {
        return "cart:"+memberId;
    }

    public static String buildOrderKey(Long memberId) {
        return "order:"+memberId;
    }

    public static String buildStockLessKey(Long memberId) {
        return "order:stock:less:"+memberId;
    }

    public static String buildOrderErrorKey(Long memberId) {
        return "order:error:less:"+memberId;
    }

    public static String buildPayLogKey(Long memberId) {
        return "paylog:"+memberId;
    }
}
