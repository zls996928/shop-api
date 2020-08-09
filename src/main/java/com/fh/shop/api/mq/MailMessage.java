package com.fh.shop.api.mq;

import lombok.Data;

import java.io.Serializable;
@Data
public class MailMessage implements Serializable {

    private String mail;

    private String realName;

    private String content;

    private String title;
}


