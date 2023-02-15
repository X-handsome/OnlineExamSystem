package com.xue.zxks.repositories;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Grade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<List<Grade>> findByExamination(Examination examination);
}
