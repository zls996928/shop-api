package com.fh.shop.api.Brand.mapper;


import com.fh.shop.api.Brand.po.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
public interface BrandDao {

    @Insert("insert into t_brand (brandName) values(#{brandName})")
    void addBrand(Brand brand);

    @Select("select * from t_brand")
    @Transactional(readOnly = true)
    List<Brand> selectList();

    @Delete("delete from t_brand where id=#{id}")
    void deleteBrand(Long id);

    @Update("update t_brand set brandName=#{brandName} where id=#{id}")
    void updateBrand(Brand brand);

    void delBach(List<Long> list);

}
