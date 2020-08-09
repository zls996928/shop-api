package com.fh.shop.api.Classify.po;


import lombok.Data;

import java.io.Serializable;

@Data
public class Classify implements Serializable {

    private Long  id;

    private String classifyName;

    private Long parentid;



}
