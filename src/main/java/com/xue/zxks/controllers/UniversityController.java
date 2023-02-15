package com.xue.zxks.controllers;

import com.xue.zxks.repositories.UniversityRepository;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/university")
@Slf4j
public class UniversityController {

    @Resource
    private UniversityRepository repository;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("GET university");
        return ResponseEntity.ok(repository.findAll());
    }
}
