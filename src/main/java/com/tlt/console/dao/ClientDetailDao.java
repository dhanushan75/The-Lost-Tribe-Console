package com.tlt.console.dao;

import com.tlt.console.entity.ClientDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailDao extends JpaRepository<ClientDetailEntity, Long> {
}
