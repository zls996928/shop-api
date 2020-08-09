package com.fh.shop.api.token.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.utils.RedisUtil;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("tokenService")
public class ITokenServiceImpl implements ITokenService {


    @Override
    public ServerResponse createToken() {
        //生成token
        String token = UUID.randomUUID().toString();
        //存入redis(value值可以随便写)
        RedisUtil.set(token,token);
        //响应给客户端
        return ServerResponse.success(token);
    }
}
