package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Question;
import com.xue.zxks.models.QuestionType;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.records.QuestionRecord;
import com.xue.zxks.repositories.QuestionRepository;
import com.xue.zxks.services.PaperExtendService;
import com.xue.zxks.services.TeacherService;
import com.xue.zxks.utils.JWTUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/question")
@Slf4j
public class QuestionController {

    @Resource
    private QuestionRepository repository;

    @Resource
    private PaperExtendService paperExtendService;

    @Resource
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET question");
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
        Optional<Teacher> optional = teacherService.getById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<Question> questionList = repository
            .findByTeacher(optional.get())
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(questionList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET question/{}", id);
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
        Question question = repository.getReferenceById(id);
        return ResponseEntity.ok(question);
    }

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestBody QuestionRecord record,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("POST question");
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
        Optional<Teacher> optional = teacherService.getById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        QuestionType questionType = QuestionType.valueOf(record.type());
        Question question = new Question()
            .setTitle(record.title())
            .setTeacher(optional.get())
            .setQuestionType(questionType)
            .setAnswer(record.answer())
            .setOpenness(record.openness());
        question = repository.save(question);
        return ResponseEntity.status(HttpStatus.CREATED).body(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("PUT question/{}", id);
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
        repository
            .findById(id)
            .ifPresent(question -> {
                question.setOpenness(!question.isOpenness());
                repository.save(question);
            });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("DELETE question/{}", id);
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
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
