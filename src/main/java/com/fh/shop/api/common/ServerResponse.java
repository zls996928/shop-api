package com.fh.shop.api.common;

import java.io.Serializable;

public class ServerResponse implements Serializable {

     private Integer  code;
     private String  msg;
     private  Object data;

     public ServerResponse(){

      }

    public ServerResponse(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ServerResponse success(){

         return new ServerResponse(200,"ok",null);
    }
    public static ServerResponse success(Object data){

         return new ServerResponse(200,"ok",data);
    }

    public static ServerResponse error(){

        return new ServerResponse(-1,"操作失败",null);
    }
    public static ServerResponse error(Integer code,String msg,Object data){

        return new ServerResponse(code,msg,data);
    }



    public static ServerResponse error(ResponseEnum responseEnum){

        return new ServerResponse(responseEnum.getCode(),responseEnum.getMsg(),null);
    }

  public static ServerResponse errorPassword(int code,String msg){

        return new ServerResponse(-code,msg,null);
    }




    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
