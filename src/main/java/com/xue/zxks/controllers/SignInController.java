package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.repositories.TeacherRepository;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign_in")
@Slf4j
public class SignInController {

    @Value("${secret}")
    private String secret;

    @Resource
    private TeacherRepository repository;

    @PostMapping
    public ResponseEntity<Object> signIn(
        @RequestBody Map<String, String> body
    ) {
        log.info("POST sign_in");
        Teacher teacher = repository
            .findByEmail(body.get("email"))
            .orElse(null);
        boolean passwordCorrect =
            teacher != null &&
            BCrypt.checkpw(body.get("password"), teacher.getPasswordHash());
        if (teacher == null || !passwordCorrect) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "invalid username or password"));
        }
        if (!teacher.isAudit()) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "not approved or failed"));
        }
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT
            .create()
            .withClaim("id", teacher.getId())
            .sign(algorithm);
        return ResponseEntity.ok(
            Map.of(
                "token",
                token,
                "id",
                teacher.getId().toString(),
                "name",
                teacher.getName()
            )
        );
    }
}
