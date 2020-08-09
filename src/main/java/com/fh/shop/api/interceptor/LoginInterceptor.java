package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ResponseEnum;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.utils.KeyUtils;
import com.fh.shop.api.utils.Md5Util;
import com.fh.shop.api.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Base64;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public  boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //处理跨域
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        //处理自定义的请求头
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,"token,content-type,x-token");
        //处理客户端发送的各种请求类型
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,"POST,PUT,DELETE,GET");
        //处理options请求【并不是真实要发送的请求，里面没有携带任何数据，所以对options请求直接阻止后续访问】
        String requestMethod = request.getMethod();
        if(requestMethod.equalsIgnoreCase("options")){
            //阻止后续访问
            return false;
        }
        //如果请求为静态资源请求时，类型转换会报错，类型不对应，所以应再请求时方法请求时再转换类型
        // 如果不是映射到方法直接通过   handler是不是HandlerMethod的实例
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //当请求为方法时，再转换类型
        HandlerMethod handlerMethod= (HandlerMethod) handler;
      //获取方法
       Method method = handlerMethod.getMethod();
       //判断方法上是否有注解  如果没有直接放行
       if (!method.isAnnotationPresent(Login.class)){
           return  true;
       }
       //获取头信息
       String header = request.getHeader("token");
       if (StringUtils.isEmpty(header)){
           throw  new GlobalException(ResponseEnum.HEADER_IS_NULL);
       }
       //已点的形式分割头信息
       String[] split = header.split("\\.");
       //判断分割后的数组是否完整
       if (split.length!=2){
           throw  new GlobalException(ResponseEnum.HEADER_IS_NOT_COMPLETE);
       }
       //json对象在base64编码
       String memberJsonBase64=split[0];
       //base64的json对象+秘钥在进行base64编码得到的数据
       String base64sign=split[1];
       //生成新的签名
       String newsign = Md5Util.secret(memberJsonBase64, SystemConst.SECRET);
       //在对新的签名进行base64编码
       String newsignBase64 = Base64.getEncoder().encodeToString(newsign.getBytes("utf-8"));
      //判断新签名和旧签名是否一致
       if (!newsignBase64.equals(base64sign)){
           throw  new GlobalException(ResponseEnum.DATA_IS_ERROR);
       }
       //对base64的json对象解码
        String member = new String(Base64.getDecoder().decode(memberJsonBase64),"utf-8");
       //把json字符串转为对象
        MemberVo memberVo = JSONObject.parseObject(member, MemberVo.class);
        //判断是否过期
        String memberjson = RedisUtil.get(KeyUtils.memberKey(memberVo.getId(), memberVo.getUuid()));
        if (StringUtils.isEmpty(memberjson)){
            throw  new GlobalException(ResponseEnum.LOGIN_TIME_IS_OUT);
        }
        RedisUtil.setEx(KeyUtils.memberKey(memberVo.getId(), memberVo.getUuid()),KeyUtils.MEMBER_EXPIRE,"csw");
        request.setAttribute(SystemConst.MEMBERINFO,memberVo);
        return true;
    }
}
