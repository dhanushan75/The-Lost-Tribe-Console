package com.tlt.console.dao;

import com.tlt.console.dao.constants.QueryConstants;
import com.tlt.console.data.CashInCashOutData;
import com.tlt.console.entity.ClientExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

@Repository
public interface ClientExpenseDao extends JpaRepository<ClientExpenseEntity, Long> {

    @Query(value = QueryConstants.GET_CASHIN_CASHOUT_FOR_CLIENT_IDS, nativeQuery = true)
    public List<CashInCashOutData> getTotalCashInCashOutFromClientIdList(@RequestParam("pClientIdSet") Set<Long> pClientIdSet);

    List<ClientExpenseEntity> findByClientId(Long pClientId);
}
