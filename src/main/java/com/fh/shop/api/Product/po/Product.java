package com.fh.shop.api.Product.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Product implements Serializable {

    private Long id            ;//bigint(20)
    private String productName   ;//varchar(50)
    private BigDecimal price         ;//decimal(10,2)
    private Long brandid       ;//bigint(20)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate    ;//date
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date insertTime    ;//datetime
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss" )
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime    ;//datetime
    private String mainImagePath ;//varchar(500)
    private Integer isHot         ;//int(11)
    private Integer status        ;//int(11)
    private Integer stock;


    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getBrandid() {
        return brandid;
    }

    public void setBrandid(Long brandid) {
        this.brandid = brandid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public Integer getIsHot() {
        return isHot;
    }

    public void setIsHot(Integer isHot) {
        this.isHot = isHot;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
