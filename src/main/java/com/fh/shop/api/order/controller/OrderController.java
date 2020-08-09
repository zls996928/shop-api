package com.fh.shop.api.order.controller;

import com.fh.shop.api.Member.po.Member;
import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.annotation.Idempotent;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.order.biz.IOrderService;
import com.fh.shop.api.order.param.OrderParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/generateOrderConfirm")
    @Login
    @ApiOperation("生成确认订单页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="头信息",required = true,type = "string",paramType = "header")
    })
    public ServerResponse generateOrderConfirm(HttpServletRequest request) {
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return orderService.generateOrderConfirm(memberId);
    }

    @PostMapping("/generateOrder")
    @Login
    @Idempotent
    @ApiOperation("生成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="头信息",required = true,type = "string",paramType = "header"),
            @ApiImplicitParam(name="recipientId",value="收件人id",required = true,type = "long",paramType = "query"),
            @ApiImplicitParam(name="payType",value="支付类型",required = true,type = "int",paramType = "query")
    })
    public ServerResponse generateOrder(HttpServletRequest request, OrderParam orderParam){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        orderParam.setMemberId(memberId);
        return orderService.generateOrder(orderParam);
    }

    @GetMapping("/getResult")
    @Login
    @ApiOperation("获取订单的结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value="头信息",required = true,type = "string",paramType = "header")
    })
    public ServerResponse getResult(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return orderService.getResult(memberId);
    }
}
