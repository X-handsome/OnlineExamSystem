package com.xue.zxks.services.impl;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.ExaminationExtend;
import com.xue.zxks.models.Student;
import com.xue.zxks.repositories.ExaminationExtendRepository;
import com.xue.zxks.services.ExaminationExtendService;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class ExaminationExtendServiceImpl implements ExaminationExtendService {

    @Resource
    private ExaminationExtendRepository repository;

    @Override
    public boolean isExamined(Examination examination) {
        return repository.existsByExamination(examination);
    }

    @Override
    public Optional<List<ExaminationExtend>> getByStudentAndExamination(
        Student student,
        Examination examination
    ) {
        return repository.findByStudentAndExamination(student, examination);
    }
}
