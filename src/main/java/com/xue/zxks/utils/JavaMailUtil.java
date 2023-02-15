package com.xue.zxks.utils;

import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JavaMailUtil {

    /// 暂时关闭邮件发送功能
    // @Resource
    // private JavaMailSender sender;

    @Async
    public void send(String email, String subject, String text) {
        var message = new SimpleMailMessage();
        message.setFrom("chenyang-z@foxmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setText(text);
        log.info("Mail To: {}, Subject: {}, Text: {}", email, subject, text);
        // sender.send(message);
    }
}
