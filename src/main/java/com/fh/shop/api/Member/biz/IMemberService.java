package com.fh.shop.api.Member.biz;

import com.fh.shop.api.Member.po.Member;
import com.fh.shop.api.common.ServerResponse;

public interface IMemberService {
    ServerResponse register(Member member);

    ServerResponse query(Member member);

    ServerResponse login(Member member);
}
