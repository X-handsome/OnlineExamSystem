package com.xue.zxks.repositories;

import com.xue.zxks.models.Teacher;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("select t from Teacher t where t.available is true and t.email = ?1")
    Optional<Teacher> findByEmail(String email);

    @Query("select t from Teacher t where t.available is true and t.id = ?1")
    Optional<Teacher> findById(Long id);

    boolean existsByEmail(String email);
}
