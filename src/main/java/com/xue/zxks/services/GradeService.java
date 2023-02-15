package com.xue.zxks.services;

import com.xue.zxks.models.Examination;
import org.springframework.scheduling.annotation.Async;

public interface GradeService {
    @Async
    void calculate(Examination examination);
}
