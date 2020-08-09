package com.fh.shop.api.Brand.biz;

import com.fh.shop.api.Brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;

public interface BrandService {
    ServerResponse addBrand(Brand brand);

    ServerResponse selectBrand();

    ServerResponse deleteBrand(Long id);

    ServerResponse updateBrand(Brand brand);

    ServerResponse delBach(String ids);
}
