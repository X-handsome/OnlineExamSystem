package com.xue.zxks.repositories;

import com.xue.zxks.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository
    extends JpaRepository<Organization, Long> {}
