package com.fh.shop.api.Brand.controller;

import com.fh.shop.api.Brand.biz.BrandService;
import com.fh.shop.api.Brand.po.Brand;
import com.fh.shop.api.common.ServerResponse;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@Api(tags = "品牌操作相关的接口")
public class BrandController {

    @Autowired
    private BrandService brandService;


    @PostMapping
    @ApiOperation("添加品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="brandName",value = "品牌名",type="string",required = true,paramType = "query")
    })
    public ServerResponse addBrand(Brand brand){

        return brandService.addBrand(brand);
    }

    @GetMapping
    @ApiOperation("获取所有品牌列表")
    public ServerResponse selectBrand(){

        return brandService.selectBrand();
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据品牌id删除指定的品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "品牌id",type="long",required = true,paramType = "path")
    })
    public ServerResponse deleteBrand(@PathVariable Long id){

        return brandService.deleteBrand(id);
    }

    @PutMapping
    @ApiOperation("更新品牌")
    public ServerResponse updateBrand(@ApiParam(value="json格式的字符串",required = true) @RequestBody Brand brand){

        return brandService.updateBrand(brand);
    }

    @DeleteMapping
    @ApiOperation("批量删除品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="ids",value = "品牌id集合，逗号分割，如1,2,3",type="string",required = true,paramType = "query")
    })
    public ServerResponse delBach(String ids){

        return brandService.delBach(ids);
    }

}
