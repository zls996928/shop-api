package com.fh.shop.api.Area.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class Area implements Serializable {

    private Long  id;

    private String areaName;

    private Long parentid;


}
