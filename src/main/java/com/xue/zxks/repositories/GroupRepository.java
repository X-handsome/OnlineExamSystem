package com.xue.zxks.repositories;

import com.xue.zxks.models.Group;
import com.xue.zxks.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GroupRepository extends JpaRepository<Group, Long> {
    @EntityGraph(attributePaths = { "students" })
    @Query("select g from t_group g where g.available is true and g.id = ?1")
    Optional<Group> findById(Long id);

    @EntityGraph(attributePaths = { "students" })
    @Query(
        "select g from t_group g where g.available is true and g.teacher = ?1 order by g.createTime desc"
    )
    Optional<List<Group>> findByTeacher(Teacher teacher);

    @Modifying
    @Query("update t_group g set g.available = false where g.id = ?1")
    void deleteById(Long id);
}
