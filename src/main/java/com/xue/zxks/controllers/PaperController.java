package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Paper;
import com.xue.zxks.models.PaperExtend;
import com.xue.zxks.models.Question;
import com.xue.zxks.models.QuestionType;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.records.PaperRecord;
import com.xue.zxks.repositories.PaperRepository;
import com.xue.zxks.services.PaperExtendService;
import com.xue.zxks.services.QuestionService;
import com.xue.zxks.services.TeacherService;
import com.xue.zxks.utils.JWTUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
@RequestMapping("/paper")
@Slf4j
public class PaperController {

    @Resource
    private PaperRepository repository;

    @Resource
    private PaperExtendService paperExtendService;

    @Resource
    private QuestionService questionService;

    @Resource
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET paper");
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
        List<Paper> paperList = repository
            .findByTeacher(optional.get())
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(paperList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET paper/{}", id);
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
        Paper paper = repository.getReferenceById(id);
        return ResponseEntity.ok(paper);
    }

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestBody PaperRecord record,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("POST paper");
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
        Paper paper = new Paper()
            .setTitle(record.title())
            .setOpenness(record.openness())
            .setTeacher(optional.get());
        paper = repository.save(paper);
        Set<PaperExtend> paperExtendSet = new HashSet<>();
        Paper finalPaper = paper;
        record
            .paperExtendRecordList()
            .forEach(paperExtendRecord -> {
                Question question = questionService
                    .getById(paperExtendRecord.id())
                    .orElse(new Question());
                int score = 0;
                if (question.getQuestionType() == QuestionType.Single) {
                    score = record.single();
                }
                if (question.getQuestionType() == QuestionType.Multiple) {
                    score = record.multiple();
                }
                if (question.getQuestionType() == QuestionType.Judgment) {
                    score = record.judgment();
                }
                PaperExtend paperExtend = new PaperExtend()
                    .setPaper(finalPaper)
                    .setNumber(paperExtendRecord.number())
                    .setScore(score)
                    .setQuestion(question);
                paperExtend = paperExtendService.create(paperExtend);
                paperExtendSet.add(paperExtend);
            });
        paper.setExtend(paperExtendSet);
        repository.save(paper);
        return ResponseEntity.status(HttpStatus.CREATED).body(paper);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("PUT paper/{}", id);
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
            .ifPresent(paper -> {
                paper.setOpenness(!paper.isOpenness());
                repository.save(paper);
            });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("DELETE paper/{}", id);
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
