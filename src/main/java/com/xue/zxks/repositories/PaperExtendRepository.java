package com.xue.zxks.repositories;

import com.xue.zxks.models.PaperExtend;
import com.xue.zxks.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperExtendRepository
    extends JpaRepository<PaperExtend, Long> {
    void deleteByQuestion(Question question);
}
