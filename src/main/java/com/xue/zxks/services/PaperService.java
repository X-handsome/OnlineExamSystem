package com.xue.zxks.services;

import com.xue.zxks.models.Paper;
import java.util.Optional;

public interface PaperService {
    Optional<Paper> getById(Long id);
}
