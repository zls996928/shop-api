package com.fh.shop.api.Product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.Product.po.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ProductMapper extends BaseMapper<Product> {
    @Update("update t_product set stock=stock-#{num} where id=#{goodsId} and stock>=#{num}")
    int updateStock(@Param("goodsId") Long goodsId, @Param("num") int num);
}
