package com.tlt.console.dao;

import com.tlt.console.dao.constants.QueryConstants;
import com.tlt.console.data.UnitData;
import com.tlt.console.entity.UnitsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
public interface UnitsDao extends JpaRepository<UnitsEntity, Long> {

    @Query(value = QueryConstants.GET_UNIT_AND_ROOM_FOR_UNIT_ID, nativeQuery = true)
    UnitData getRoomAndUnitFromUnitId(@RequestParam("pUnitId") Long pUnitId);
}
