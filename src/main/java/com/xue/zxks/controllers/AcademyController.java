package com.xue.zxks.controllers;

import com.xue.zxks.repositories.AcademyRepository;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/academy")
@Slf4j
public class AcademyController {

    @Resource
    private AcademyRepository repository;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET academy");
        return ResponseEntity.ok(repository.findAll());
    }
}
