package com.fh.shop.api;

import com.fh.shop.api.utils.EmailHelper;
import org.junit.jupiter.api.Test;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileOutputStream;
import java.util.Properties;

public class test {

    @Test
    public void testSend(){
        Properties prop = new Properties();
        prop.setProperty("mail.host", "smtp.qq.com");
        prop.setProperty("mail.transport.protocol", "smtp");
        prop.setProperty("mail.smtp.auth", "true");
        //使用JavaMail发送邮件的5个步骤
        //1、创建session
        Session session = Session.getInstance(prop);
        //开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2、通过session得到transport对象
        Transport ts = null;
        try {
            ts = session.getTransport();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        //3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
        try {
            ts.connect("smtp.qq.com", "1179363482@qq.com", "tqhkuspesvnbhaig");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        //4、创建邮件
        Message message = null;
        try {
            message = createImageMail(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //5、发送邮件
        try {
            ts.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            ts.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Method: createImageMail
     * @Description: 生成一封邮件正文带图片的邮件
     * @Anthor:孤傲苍狼
     *
     * @param session
     * @return
     * @throws Exception
     */
    public static MimeMessage createImageMail(Session session) throws Exception {
        //创建邮件
        MimeMessage message = new MimeMessage(session);
        // 设置邮件的基本信息
        //发件人
        message.setFrom(new InternetAddress("1179363482@qq.com"));
        //收件人
        message.setRecipient(Message.RecipientType.TO, new InternetAddress("409112398@qq.com"));
        //邮件标题
        message.setSubject("带图片的邮件");

        // 准备邮件数据
        // 准备邮件正文数据
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("这是一封邮件正文带图片<img src='cid:151.jpg'>的邮件", "text/html;charset=UTF-8");
        // 准备图片数据
        MimeBodyPart image = new MimeBodyPart();
        DataHandler dh = new DataHandler(new FileDataSource("static/151.jpg"));
        image.setDataHandler(dh);
        image.setContentID("151.jpg");
        // 描述数据关系
        MimeMultipart mm = new MimeMultipart();
        mm.addBodyPart(text);
        mm.addBodyPart(image);
        mm.setSubType("related");
        message.setContent(mm);
        message.saveChanges();
/*        //将创建好的邮件写入到E盘以文件的形式进行保存
        message.writeTo(new FileOutputStream("G:\\ImageMail.eml"));*/
        //返回创建好的邮件
        return message;
    }
}
