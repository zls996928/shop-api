package com.fh.shop.api.Product.biz;

import com.fh.shop.api.common.ServerResponse;

public interface ProductService {
    ServerResponse queryHotList();

    ServerResponse sendMail();

}
