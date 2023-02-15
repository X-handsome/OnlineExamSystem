package com.xue.zxks.services;

import com.xue.zxks.models.Group;
import java.util.Optional;

public interface GroupService {
    Optional<Group> getById(Long id);
}
