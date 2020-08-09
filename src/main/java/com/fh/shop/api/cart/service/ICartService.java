package com.fh.shop.api.cart.service;

import com.fh.shop.api.common.ServerResponse;

import java.util.List;

public interface ICartService {
    ServerResponse addItem(Long memberId, Long goodsId, int num);

    ServerResponse findItemList(Long memberId);

    ServerResponse del(Long memberId, Long goodsId);

    ServerResponse delBatch(Long memberId, String ids);

    ServerResponse findItemCount(Long memberId);
}
