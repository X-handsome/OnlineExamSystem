package com.xue.zxks.services;

import com.xue.zxks.models.Question;
import java.util.Optional;

public interface QuestionService {
    Optional<Question> getById(Long id);
}
