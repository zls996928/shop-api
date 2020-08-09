package com.fh.shop.api.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Component
public class EmailHelper {
    @Value("${mail.host}")
    private String host;

    @Value("${mail.transport.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;


    public  void sendEmail(String recipient,String title,String info) {
        //创建一个配置文件并保存
        Properties properties = new Properties();

        properties.setProperty("mail.host",host);

        properties.setProperty("mail.transport.protocol",protocol);

        properties.setProperty("mail.smtp.auth","true");


        //QQ存在一个特性设置SSL加密
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        //创建一个session对象
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        //开启debug模式
        session.setDebug(true);

        //获取连接对象
        Transport transport = null;
        try {
            transport = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        //连接服务器
        try {
            transport.connect(host,username,password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //创建邮件对象
        MimeMessage mimeMessage = new MimeMessage(session);

        //邮件发送人
        try {
            mimeMessage.setFrom(new InternetAddress(username));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //邮件接收人
        try {
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(recipient));
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //邮件标题
        try {
            mimeMessage.setSubject(title);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //邮件内容
        try {
            mimeMessage.setContent(info,"text/html;charset=UTF-8");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送邮件
        try {
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //关闭连接
        try {
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    }

