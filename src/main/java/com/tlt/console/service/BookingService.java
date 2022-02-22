package com.tlt.console.service;

import com.tlt.console.data.ExpenseData;
import com.tlt.console.data.SummaryData;
import com.tlt.console.entity.ClientCheckInCalendarEntity;
import com.tlt.console.entity.ClientExpenseEntity;
import com.tlt.console.entity.UnitsEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookingService {

    public List<SummaryData> getSummaryDataForGrid(Date pFromDate) throws Exception;

    public List<ExpenseData> getExpensesOfClient(Long pClientId) throws Exception;

    public List<String> getServiceList() throws Exception;

    public void saveExpenseData(ClientExpenseEntity pExpenseEntity, String pServiceType) throws Exception;

    public List<UnitsEntity> getParentUnits() throws Exception;

    public List<UnitsEntity> getSubUnits(Long parentUnitId) throws Exception;

    public List<ClientCheckInCalendarEntity> getCalendarEventsByUnitId(Long unitId) throws Exception;
}
