package com.xue.zxks.repositories;

import com.xue.zxks.models.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorRepository
    extends JpaRepository<Administrator, Long> {}
