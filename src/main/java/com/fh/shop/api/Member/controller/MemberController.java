package com.fh.shop.api.Member.controller;

import com.fh.shop.api.Member.biz.IMemberService;
import com.fh.shop.api.Member.po.Member;
import com.fh.shop.api.Member.vo.MemberVo;
import com.fh.shop.api.common.Login;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.common.SystemConst;
import com.fh.shop.api.utils.KeyUtils;
import com.fh.shop.api.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/members")

@Api(tags = "会员接口")
public class MemberController {


    @Autowired
    private IMemberService memberService;

    @PostMapping("add")
    @ApiOperation("会员注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberName",value = "会员名称",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="password",value = "密码",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="realName",value = "真实姓名",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="birthday",value = "生日",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="mail",value = "邮箱",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="phone",value = "手机",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="shengId",value = "省id",type="long",required = false,paramType = "query"),
            @ApiImplicitParam(name="shiId",value = "市id",type="long",required = false,paramType = "query"),
            @ApiImplicitParam(name="xianId",value = "县id",type="long",required = false,paramType = "query"),
            @ApiImplicitParam(name="areaName",value = "地区名称",type="string",required = false,paramType = "query")
        })
    public ServerResponse register(Member member){

        return  memberService.register(member);
    }


    @GetMapping("query")
    public ServerResponse query(Member member){

        return  memberService.query(member);
    }


    @PostMapping("login")
    @ApiOperation("会员登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberName",value = "会员名",type="string",required = true,paramType = "query"),
            @ApiImplicitParam(name="password",value = "密码",type="string",required = true,paramType = "query")
        })
    public ServerResponse login(Member member){

        return  memberService.login(member);
    }

    @GetMapping("/findMember")
    @ApiOperation("获取会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="token",value = "头信息",type="string",required = true,paramType = "header"),
        })
    @Login
    public ServerResponse findMember(HttpServletRequest request){
        MemberVo member = (MemberVo)request.getAttribute(SystemConst.MEMBERINFO);
        return ServerResponse.success(member);
    }

    @GetMapping("/logout")
    @Login
    public ServerResponse logout(HttpServletRequest request){
        MemberVo member = (MemberVo)request.getAttribute(SystemConst.MEMBERINFO);
        Long memberId = member.getId();
        String uuid = member.getUuid();
        //清楚redis中的数据
        RedisUtil.del(KeyUtils.memberKey(memberId,uuid));
        return ServerResponse.success();
    }
}
