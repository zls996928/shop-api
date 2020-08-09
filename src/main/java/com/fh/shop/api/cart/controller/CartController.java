package com.fh.shop.api.cart.controller;

import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.cart.service.ICartService;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@Api(tags = "购物车接口")
public class CartController {

    @Autowired
    private ICartService cartService;

    @PostMapping("/addItem")
    @ApiOperation("添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header"),
            @ApiImplicitParam(name="goodsId",value = "商品id",type="long",required = true,paramType = "query"),
            @ApiImplicitParam(name="num",value = "商品数量",type="int",required = true,paramType = "query")
    })
    @Login
    public ServerResponse addItem(HttpServletRequest request,Long goodsId,int num){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return cartService.addItem(memberId,goodsId,num);
    }

    @GetMapping("/findItemList")
    @ApiOperation("获取指定会员对应的购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header")
    })
    @Login
    public ServerResponse findItemList(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return cartService.findItemList(memberId);
    }

    @GetMapping("/findItemCount")
    @ApiOperation("获取指定会员对应的购物车中的商品个数")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header")
    })
    @Login
    public ServerResponse findItemCount(HttpServletRequest request){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return cartService.findItemCount(memberId);

    }

    @DeleteMapping("/del/{id}")
    @ApiOperation("删除购物车的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header"),
            @ApiImplicitParam(name="id",value = "商品id",type="long",required = true,paramType = "query")
    })
    @Login
    public ServerResponse del(HttpServletRequest request,@PathVariable("id") Long goodsId){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return cartService.del(memberId,goodsId);
    }

    @DeleteMapping("/delBatch")
    @ApiOperation("删除会员对应的购物车中的多个商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header"),
            @ApiImplicitParam(name="ids",value = "商品id集合，逗号分割，如1,2,3",type="string",required = true,paramType = "query")
    })
    @Login
    public ServerResponse delBatch(HttpServletRequest request,String ids){
        MemberVo member = (MemberVo) request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        return cartService.delBatch(memberId,ids);
    }
}
