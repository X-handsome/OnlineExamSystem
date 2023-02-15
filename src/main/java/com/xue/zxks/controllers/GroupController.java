package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Group;
import com.xue.zxks.models.Student;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.records.GroupRecord;
import com.xue.zxks.repositories.GroupRepository;
import com.xue.zxks.services.ExaminationService;
import com.xue.zxks.services.StudentService;
import com.xue.zxks.services.TeacherService;
import com.xue.zxks.utils.JWTUtil;
import java.time.LocalDateTime;
import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/group")
@Slf4j
public class GroupController {

    @Resource
    private GroupRepository repository;

    @Resource
    private StudentService studentService;

    @Resource
    private TeacherService teacherService;

    @Resource
    private ExaminationService examinationService;

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET group");
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
        List<Group> groupList = repository
            .findByTeacher(optional.get())
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(groupList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET group/{}", id);
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
        Group group = repository.getReferenceById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<Object> create(
        @RequestHeader("authorization") String authorization,
        @RequestBody GroupRecord record
    ) {
        log.info("POST group");
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
        Set<Student> studentSet = new HashSet<>(
            record
                .studentIdList()
                .stream()
                .map(studentId ->
                    studentService.getById(studentId).orElse(new Student())
                )
                .toList()
        );
        Group group = new Group()
            .setTitle(record.title())
            .setTeacher(optional.get())
            .setStudents(studentSet);
        repository.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(
        @RequestHeader("authorization") String authorization,
        @RequestBody GroupRecord record,
        @PathVariable Long id
    ) {
        log.info("PUT group/{}", id);
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
            .ifPresent(group -> {
                group.setTitle(record.title());
                Set<Student> studentSet = new HashSet<>(
                    record
                        .studentIdList()
                        .stream()
                        .map(studentId ->
                            studentService
                                .getById(studentId)
                                .orElse(new Student())
                        )
                        .toList()
                );
                group.setStudents(studentSet);
                repository.save(group);
            });
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(
        @RequestHeader("authorization") String authorization,
        @PathVariable Long id
    ) {
        log.info("DELETE group/{}", id);
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
        Optional<Group> optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Group group = optional.get();
        Optional<List<Examination>> optional1 = examinationService.getByGroup(
            group
        );
        if (optional1.isEmpty()) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        List<Examination> examinations = optional1.get();
        List<Examination> examinations1 = examinations
            .stream()
            .filter(examination ->
                examination.getEndTime().isAfter(LocalDateTime.now())
            )
            .toList();
        if (examinations1.size() == 0) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity
            .badRequest()
            .body(Map.of("error", "have examination"));
    }
}
