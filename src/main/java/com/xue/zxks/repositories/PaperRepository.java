package com.xue.zxks.repositories;

import com.xue.zxks.models.Paper;
import com.xue.zxks.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PaperRepository extends JpaRepository<Paper, Long> {
    @Query("select p from Paper p where p.available is true and p.id = ?1")
    Optional<Paper> findById(Long id);

    @EntityGraph(attributePaths = { "teacher", "extend.question" })
    @Query(
        "select p from Paper p where p.available is true and p.teacher = ?1 order by p.createTime desc "
    )
    Optional<List<Paper>> findByTeacher(Teacher teacher);

    @Modifying
    @Query("update Paper p set p.available = false where p.id = ?1")
    void deleteById(Long id);
}
