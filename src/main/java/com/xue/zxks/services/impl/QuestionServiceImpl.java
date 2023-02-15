package com.xue.zxks.services.impl;

import com.xue.zxks.models.Question;
import com.xue.zxks.repositories.QuestionRepository;
import com.xue.zxks.services.QuestionService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionRepository repository;

    @Override
    public Optional<Question> getById(Long id) {
        return repository.findById(id);
    }
}
