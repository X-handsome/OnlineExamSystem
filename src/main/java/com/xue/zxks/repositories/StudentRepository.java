package com.xue.zxks.repositories;

import com.xue.zxks.models.Student;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select s from Student s where s.available is true and s.id = ?1")
    Optional<Student> findById(Long id);

    @Query(
        "select s from Student s where s.available is true and s.enrollYear = ?1 and s.academyId = ?2 and s.majorId = ?3 and s.universityId = ?4"
    )
    Optional<List<Student>> findByMajor(
        Integer enrollYear,
        Long academyId,
        Long majorId,
        Long universityId
    );

    @Query(
        "select s from Student s where s.available is true and s.universityId = ?1 and s.academyId = ?2"
    )
    Optional<List<Student>> findByTeacher(Long universityId, Long academyId);
}
