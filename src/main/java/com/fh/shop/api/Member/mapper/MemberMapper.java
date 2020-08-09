package com.fh.shop.api.Member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.Member.po.Member;
import com.fh.shop.api.common.ServerResponse;
import org.apache.ibatis.annotations.Insert;

public interface MemberMapper  extends BaseMapper<Member> {

    @Insert(
            "insert into t_member (memberName,password,realName,birthday,mail,phone,shengId,shiId,xianId,areaName)" +
                    " values (#{memberName},#{password},#{realName},#{birthday},#{mail},#{phone},#{shengId},#{shiId},#{xianId},#{areaName}) ")
    void addMember(Member member);
}
