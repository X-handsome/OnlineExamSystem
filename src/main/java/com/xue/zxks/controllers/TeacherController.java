package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.records.TeacherRecord;
import com.xue.zxks.repositories.TeacherRepository;
import com.xue.zxks.utils.JWTUtil;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/teacher")
@Slf4j
public class TeacherController {

    @Resource
    private TeacherRepository repository;

    @Resource
    private RedisTemplate<String, String> template;

    @GetMapping
    public ResponseEntity<Object> getOne(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET teacher");
        String token = JWTUtil.getTokenFrom(authorization);
        if (token == null) {
            return ResponseEntity.badRequest().build();
        }
        DecodedJWT decodedToken = JWT.decode(token);
        if (decodedToken.getClaim("id").isNull()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "token missing or invalid"));
        }
        Long id = decodedToken.getClaim("id").asLong();
        Teacher teacher = repository.findById(id).orElse(new Teacher());
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TeacherRecord record) {
        log.info("POST teacher");
        if (repository.existsByEmail(record.email())) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "registered"));
        }
        String captcha = template.opsForValue().get(record.email());
        if (StringUtils.isBlank(captcha) || !captcha.equals(record.captcha())) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "captcha invalid"));
        }
        String saltRounds = BCrypt.gensalt(10);
        String passwordHash = BCrypt.hashpw(record.password(), saltRounds);
        Teacher teacher = (Teacher) new Teacher()
            .setAcademyId(record.academyId())
            .setUniversityId(record.universityId())
            .setEmail(record.email())
            .setNumber(record.number())
            .setName(record.name())
            .setPasswordHash(passwordHash);
        repository.save(teacher);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping
    public ResponseEntity<Object> update(
        @RequestBody Map<String, String> body,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("PUT teacher");
        String token = JWTUtil.getTokenFrom(authorization);
        if (token == null) {
            return ResponseEntity.badRequest().build();
        }
        DecodedJWT decodedToken = JWT.decode(token);
        if (decodedToken.getClaim("id").isNull()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "token missing or invalid"));
        }
        Long id = decodedToken.getClaim("id").asLong();
        Teacher teacher = repository.getReferenceById(id);
        String oldPassword = Optional
            .ofNullable(body.get("oldPassword"))
            .orElse("");
        boolean passwordCorrect =
            !oldPassword.isEmpty() &&
            BCrypt.checkpw(oldPassword, teacher.getPasswordHash());
        Optional<String> optional = Optional.ofNullable(
            body.get("newPassword")
        );
        if (!passwordCorrect || optional.isEmpty()) {
            return ResponseEntity
                .badRequest()
                .body(Map.of("error", "incorrect password"));
        }
        String saltRounds = BCrypt.gensalt(10);
        String passwordHash = BCrypt.hashpw(optional.get(), saltRounds);
        teacher.setPasswordHash(passwordHash);
        repository.save(teacher);
        return ResponseEntity.ok(teacher);
    }
}
