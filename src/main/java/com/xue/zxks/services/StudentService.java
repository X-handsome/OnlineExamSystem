package com.xue.zxks.services;

import com.xue.zxks.models.Student;
import java.util.Optional;

public interface StudentService {
    Optional<Student> getById(Long id);
}
