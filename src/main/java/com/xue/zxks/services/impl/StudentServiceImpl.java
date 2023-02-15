package com.xue.zxks.services.impl;

import com.xue.zxks.models.Student;
import com.xue.zxks.repositories.StudentRepository;
import com.xue.zxks.services.StudentService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

    @Resource
    private StudentRepository repository;

    @Override
    public Optional<Student> getById(Long id) {
        return repository.findById(id);
    }
}
