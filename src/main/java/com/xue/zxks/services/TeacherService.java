package com.xue.zxks.services;

import com.xue.zxks.models.Teacher;
import java.util.Optional;

public interface TeacherService {
    Optional<Teacher> getById(Long id);
}
