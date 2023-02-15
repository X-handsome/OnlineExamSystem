package com.xue.zxks.repositories;

import com.xue.zxks.models.Question;
import com.xue.zxks.models.Teacher;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(
        "select q from Question q where (q.teacher = ?1 or q.openness is true) and q.available is true order by q.createTime desc"
    )
    Optional<List<Question>> findByTeacher(Teacher teacher);

    @Modifying
    @Query("update Question q set q.available = false where q.id = ?1")
    void deleteById(Long id);
}
