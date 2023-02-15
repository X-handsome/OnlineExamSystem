package com.xue.zxks.controllers;

import com.xue.zxks.repositories.TeacherRepository;
import com.xue.zxks.utils.JavaMailUtil;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/captcha")
@Slf4j
public class CaptchaController {

    @Resource
    private TeacherRepository repository;

    @Resource
    private JavaMailUtil javaMailUtil;

    @Resource
    private RedisTemplate<String, String> template;

    @GetMapping
    public ResponseEntity<Object> getCaptcha(@RequestParam String email) {
        log.info("GET captcha");
        if (repository.existsByEmail(email)) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "registered"));
        }
        String captcha = String.valueOf(Math.round(Math.random() * 10000));
        template
            .opsForValue()
            .set(email, captcha, Duration.of(5, ChronoUnit.MINUTES));
        javaMailUtil.send(
            email,
            "欢迎注册在线考试系统",
            "您的验证码为：<b>" + captcha + "</b>"
        );
        return ResponseEntity.ok(Map.of("captcha", captcha));
    }
}
