package com.xue.zxks.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.xue.zxks.models.Student;
import com.xue.zxks.models.Teacher;
import com.xue.zxks.repositories.StudentRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentRepository repository;

    @Resource
    private TeacherService teacherService;

    @GetMapping("/major/{id}/{year}")
    public ResponseEntity<Object> getStudentByMajor(
        @PathVariable Long id,
        @RequestHeader("authorization") String authorization,
        @PathVariable Integer year
    ) {
        log.info("GET student/major/{}/{}", id, year);
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
        Long teacherId = decodedToken.getClaim("id").asLong();
        Optional<Teacher> optional = teacherService.getById(teacherId);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Teacher teacher = optional.get();
        Long universityId = teacher.getUniversityId();
        Long academyId = teacher.getAcademyId();
        List<Student> studentList = repository
            .findByMajor(year, academyId, id, universityId)
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(studentList);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(
        @RequestHeader("authorization") String authorization
    ) {
        log.info("GET student");
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
        Long teacherId = decodedToken.getClaim("id").asLong();
        Optional<Teacher> optional = teacherService.getById(teacherId);
        if (optional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Teacher teacher = optional.get();
        Long universityId = teacher.getUniversityId();
        Long academyId = teacher.getAcademyId();
        List<Student> studentList = repository
            .findByTeacher(universityId, academyId)
            .orElse(new ArrayList<>());
        return ResponseEntity.ok(studentList);
    }
}
