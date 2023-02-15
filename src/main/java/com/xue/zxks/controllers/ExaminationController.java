package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Group;
import com.xue.zxks.models.Paper;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.records.ExaminationRecord;
import com.xue.zxks.repositories.ExaminationRepository;
import com.xue.zxks.services.ExaminationExtendService;
import com.xue.zxks.services.GroupService;
import com.xue.zxks.services.PaperService;
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
@RequestMapping("/examination")
@Slf4j
public class ExaminationController {

    @Resource
    private ExaminationRepository repository;

    @Resource
    private PaperService paperService;

    @Resource
    private ExaminationExtendService examinationExtendService;

    @Resource
    private GroupService groupService;

    @Resource
    private TeacherService teacherService;

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET examination");
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
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "forge token"));
        }
        List<Examination> examinationList = repository
            .findByTeacher(optional.get())
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(examinationList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(
        @RequestHeader("authorization") String authorization,
        @PathVariable Long id
    ) {
        log.info("GET examination/{}", id);
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
        Examination examination = repository.getReferenceById(id);
        return ResponseEntity.ok(examination);
    }

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestHeader("authorization") String authorization,
        @RequestBody ExaminationRecord record
    ) {
        log.info("POST examination");
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
        Optional<Teacher> teacherOptional = teacherService.getById(id);
        if (teacherOptional.isEmpty()) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "forge token"));
        }
        Optional<Paper> paperOptional = paperService.getById(record.paperId());
        Optional<Group> groupOptional = groupService.getById(record.groupId());
        if (paperOptional.isEmpty() || groupOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Examination examination = new Examination()
            .setTitle(record.title())
            .setBeginTime(record.beginTime())
            .setEndTime(record.endTime())
            .setUncoiling(record.uncoiling())
            .setGroup(groupOptional.get())
            .setDuration(record.duration())
            .setVisible(record.visible())
            .setPaper(paperOptional.get())
            .setTeacher(teacherOptional.get());
        examination = repository.save(examination);
        examination =
            repository.findById(examination.getId()).orElse(new Examination());
        return ResponseEntity.status(HttpStatus.CREATED).body(examination);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
        @RequestHeader("authorization") String authorization,
        @PathVariable Long id,
        @RequestBody ExaminationRecord record
    ) {
        log.info("PUT examination/{}", id);
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
        Optional<Examination> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (examinationExtendService.isExamined(optional.get())) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "examined"));
        }
        Optional<Paper> paperOptional = paperService.getById(record.paperId());
        Optional<Group> groupOptional = groupService.getById(record.groupId());
        if (paperOptional.isEmpty() || groupOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Examination examination = repository
            .getReferenceById(id)
            .setTitle(record.title())
            .setBeginTime(record.beginTime())
            .setEndTime(record.endTime())
            .setUncoiling(record.uncoiling())
            .setGroup(groupOptional.get())
            .setDuration(record.duration())
            .setVisible(record.visible())
            .setPaper(paperOptional.get());
        examination = repository.save(examination);
        return ResponseEntity.ok(examination);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("DELETE examination/{}", id);
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
        Optional<Examination> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (examinationExtendService.isExamined(optional.get())) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "examined"));
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
