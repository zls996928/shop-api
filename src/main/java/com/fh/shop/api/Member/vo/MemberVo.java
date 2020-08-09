package com.fh.shop.api.Member.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberVo  implements Serializable {
    private Long id;
    private String memberName;// 【会员名】
    private String realName ;//    【真实姓名】
    private String uuid;
}
