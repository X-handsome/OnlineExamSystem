package com.xue.zxks.services.impl;

import com.xue.zxks.models.Group;
import com.xue.zxks.repositories.GroupRepository;
import com.xue.zxks.services.GroupService;
import java.util.Optional;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    private GroupRepository repository;

    @Override
    public Optional<Group> getById(Long id) {
        return repository.findById(id);
    }
}
