package com.tlt.console.dao;

import com.tlt.console.dao.constants.QueryConstants;
import com.tlt.console.entity.ClientDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ClientDetailDao extends JpaRepository<ClientDetailEntity, Long> {

    @Query(value = QueryConstants.GET_DISTINCT_ID_PROOF_TYPE, nativeQuery = true)
    List<String> getDistinctIdProofType();

    @Query(value = QueryConstants.GET_CLIENT_ID_LIST, nativeQuery = true)
    Set<Long> getClientIdList();
}
