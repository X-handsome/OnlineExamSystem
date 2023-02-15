package com.xue.zxks.repositories;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.ExaminationExtend;
import com.xue.zxks.models.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExaminationExtendRepository
    extends JpaRepository<ExaminationExtend, Long> {
    boolean existsByExamination(Examination examination);

    Optional<List<ExaminationExtend>> findByStudentAndExamination(
        Student student,
        Examination examination
    );
}
