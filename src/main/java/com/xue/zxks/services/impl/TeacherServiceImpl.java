package com.xue.zxks.services.impl;

import com.xue.zxks.models.Teacher;
import com.xue.zxks.repositories.TeacherRepository;
import com.xue.zxks.services.TeacherService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Resource
    private TeacherRepository repository;

    @Override
    public Optional<Teacher> getById(Long id) {
        return repository.findById(id);
    }
}
