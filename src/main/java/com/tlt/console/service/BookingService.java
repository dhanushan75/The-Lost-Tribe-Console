package com.tlt.console.service;

import com.tlt.console.data.ExpenseData;
import com.tlt.console.data.SummaryData;
import com.tlt.console.entity.ClientExpenseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookingService {

    public List<SummaryData> getSummaryDataForGrid(Date pFromDate) throws Exception;

    public List<ExpenseData> getExpensesOfClient(Long pClientId) throws Exception;

}
