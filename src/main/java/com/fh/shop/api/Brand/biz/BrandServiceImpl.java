package com.fh.shop.api.Brand.biz;

import com.fh.shop.api.Brand.mapper.BrandDao;
import com.fh.shop.api.Brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service("brandService")
@Transactional(rollbackFor = Exception.class)
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;


    @Override
    public ServerResponse addBrand(Brand brand) {
        brandDao.addBrand(brand);
      //  System.out.println(3/0);
        return ServerResponse.success();
    }

    @Override
    @Transactional(readOnly = true)
    public ServerResponse selectBrand() {
        List<Brand> brands =brandDao.selectList();
        return ServerResponse.success(brands);
    }

    @Override
    public ServerResponse deleteBrand(Long id) {
        brandDao.deleteBrand(id);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse updateBrand(Brand brand) {
        brandDao.updateBrand(brand);
        return ServerResponse.success();
    }

    @Override
    public ServerResponse delBach(String ids) {
        String[] arr = ids.split(",");
        List<Long> list = Arrays.stream(arr).map(x -> Long.parseLong(x)).collect(Collectors.toList());
        brandDao.delBach(list);
        return ServerResponse.success();
    }
}
