package com.lyc.reggie.service.impl;

import com.lyc.reggie.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Date: 2022/5/10
 * Author: 3378
 * Description:
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

//    发送人
    private String from="1146296739@qq.com";
//    接收人
    private String to="";
//    邮件标题
    private String subject="[瑞吉外卖]";
//    邮件内容
    private String contextPre="您的验证码为:";

    private String code="";

    private String contextSuf=",请在5分钟的有效期内使用,并不要告诉他人.";


    @Override
    public void sendMail(String receiver,String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        to=receiver;
        this.code=code;
        message.setFrom(from+"(瑞吉外卖)");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(contextPre+code+contextSuf);

        javaMailSender.send(message);
    }
}
