package com.xue.zxks.services.impl;

import com.xue.zxks.models.PaperExtend;
import com.xue.zxks.repositories.PaperExtendRepository;
import com.xue.zxks.services.PaperExtendService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class PaperExtendServiceImpl implements PaperExtendService {

    @Resource
    private PaperExtendRepository repository;

    @Override
    public PaperExtend create(PaperExtend paperExtend) {
        return repository.save(paperExtend);
    }
}
