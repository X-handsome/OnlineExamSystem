package com.xue.zxks.services;

import com.xue.zxks.models.Examination;
import com.xue.zxks.models.Group;
import java.util.List;
import java.util.Optional;

public interface ExaminationService {
    /**
     * Get an examination by id, or return null if it doesn't exist.
     *
     * @param id The id of the examination to be retrieved.
     * @return Optional<Examination>
     */
    Optional<Examination> getById(Long id);

    Optional<List<Examination>> getByGroup(Group group);
}
