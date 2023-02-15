package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Grade;
import com.xue.zxks.repositories.GradeRepository;
import com.xue.zxks.services.ExaminationService;
import com.xue.zxks.services.GradeService;
import com.xue.zxks.utils.JWTUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/grade")
public class GradeController {

    @Resource
    private GradeRepository repository;

    @Resource
    private ExaminationService examinationService;

    @Resource
    private GradeService service;

    @GetMapping("/examination/{id}")
    public ResponseEntity<Object> getGradeByExamination(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET grade/examination/{}", id);
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
        Optional<Examination> examinationOptional = examinationService.getById(
            id
        );
        if (examinationOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Optional<List<Grade>> optional = repository.findByExamination(
            examinationOptional.get()
        );
        if (optional.isPresent()) {
            return ResponseEntity.ok().body(Map.of("boolean", true));
        }
        return ResponseEntity.ok().body(Map.of("boolean", false));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> create(
        @RequestHeader("authorization") String authorization,
        @PathVariable Long id
    ) {
        log.info("POST grade");
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
        Optional<Examination> optional = examinationService.getById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        service.calculate(optional.get());
        return ResponseEntity.ok().build();
    }
}
