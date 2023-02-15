package com.xue.zxks.services;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.ExaminationExtend;
import com.xue.zxks.models.Student;
import java.util.List;
import java.util.Optional;

public interface ExaminationExtendService {
    /**
     * Returns true if the given examination has been examined, false otherwise.
     *
     * @param examination The examination to check.
     * @return A boolean value.
     */
    boolean isExamined(Examination examination);

    Optional<List<ExaminationExtend>> getByStudentAndExamination(
        Student student,
        Examination examination
    );
}
