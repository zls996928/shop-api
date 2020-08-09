package com.fh.shop.api.pay.controller;

import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.pay.biz.PayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/pay")
@Api(tags = "支付接口")
public class PayController {

    @Resource(name = "payService")
    private PayService payService;

    @PostMapping("/createNative")
    @Login
    @ApiOperation("统一下单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="头信息",required = true,type = "string",paramType = "header")
    })
    public ServerResponse createNative(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return payService.createNative(memberId);
    }

    @GetMapping("/queryStatus")
    @Login
    @ApiOperation("查询支付状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="头信息",required = true,type = "string",paramType = "header")
    })
    public ServerResponse queryStatus(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return payService.queryStatus(memberId);
    }

}
