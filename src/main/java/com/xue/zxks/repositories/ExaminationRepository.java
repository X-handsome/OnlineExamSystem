package com.xue.zxks.repositories;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Group;
import com.xue.zxks.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExaminationRepository
    extends JpaRepository<Examination, Long> {
    @EntityGraph(
        attributePaths = {
            "teacher",
            "paper.extend.question",
            "group.students",
            "extend",
            "grades",
        }
    )
    @Query(
        "select e from Examination e where e.available is true and e.id = ?1"
    )
    Optional<Examination> findById(Long id);

    @Modifying
    @Query("update Examination e set e.available = false where e.id = ?1")
    void deleteById(Long id);

    @EntityGraph(
        attributePaths = {
            "paper.extend.question.teacher",
            "group.students",
            "extend",
            "grades.student",
        }
    )
    @Query(
        "select e from Examination e where e.available is true and e.teacher = ?1 order by e.createTime desc"
    )
    Optional<List<Examination>> findByTeacher(Teacher teacher);

    @Query(
        "select e from Examination e where e.available is true and e.group = ?1"
    )
    Optional<List<Examination>> findByGroup(Group group);
}
