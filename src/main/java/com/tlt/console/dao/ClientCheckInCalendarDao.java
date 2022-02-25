package com.tlt.console.dao;

import com.tlt.console.dao.constants.QueryConstants;
import com.tlt.console.data.ClientCheckInCheckoutData;
import com.tlt.console.entity.ClientCheckInCalendarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface ClientCheckInCalendarDao extends JpaRepository<ClientCheckInCalendarEntity, Long> {

    @Query(value = QueryConstants.GET_CLIENT_ID_LIST_FOR_MONTH, nativeQuery = true)
    public Set<Long> getClientIdListForCurrentMonth(@RequestParam("pCurrentDate") Date pCurrentDate);

    @Query(value = QueryConstants.GET_CHECKIN_CHECKOUT_DATE_FOR_CLIENT_IDS, nativeQuery = true)
    public List<ClientCheckInCheckoutData> getCheckInCheckOutDateFromClientIdList(@RequestParam("pClientIdSet") Set<Long> pClientIdSet);

    public List<ClientCheckInCalendarEntity> findByUnitId_UnitId(Long unitId);

    public List<ClientCheckInCalendarEntity> findByUnitId_ParentUnitId(Long parentUnitId);
}
