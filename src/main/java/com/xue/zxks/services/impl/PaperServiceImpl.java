package com.xue.zxks.services.impl;

import com.xue.zxks.models.Paper;
import com.xue.zxks.repositories.PaperRepository;
import com.xue.zxks.services.PaperService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaperServiceImpl implements PaperService {

    @Resource
    private PaperRepository repository;

    @Override
    public Optional<Paper> getById(Long id) {
        return repository.findById(id);
    }
}
