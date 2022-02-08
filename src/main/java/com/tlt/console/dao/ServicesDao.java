package com.tlt.console.dao;

import com.tlt.console.entity.ServicesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicesDao extends JpaRepository<ServicesEntity, Long> {
}
