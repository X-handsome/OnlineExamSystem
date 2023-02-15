package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Major;
import com.xue.zxks.repositories.MajorRepository;
import com.xue.zxks.utils.JWTUtil;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/major")
public class MajorController {

    @Resource
    private MajorRepository repository;

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET major");
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
        List<Major> majorList = repository.findAll();
        return ResponseEntity.ok(majorList);
    }
}
