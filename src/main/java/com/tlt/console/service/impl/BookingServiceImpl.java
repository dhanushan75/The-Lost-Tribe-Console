package com.tlt.console.service.impl;

import com.tlt.console.dao.ClientCheckInCalendarDao;
import com.tlt.console.dao.ClientDetailDao;
import com.tlt.console.dao.ClientExpenseDao;
import com.tlt.console.dao.UnitsDao;
import com.tlt.console.data.*;
import com.tlt.console.entity.ClientDetailEntity;
import com.tlt.console.entity.ClientExpenseEntity;
import com.tlt.console.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ClientCheckInCalendarDao mClientCheckInCalendarDao;

    @Autowired
    private ClientExpenseDao mClientExpenseDao;

    @Autowired
    private UnitsDao mUnitDao;

    @Autowired
    private ClientDetailDao mClientDetailDao;

    public List<SummaryData> getSummaryDataForGrid(Date pFromDate) throws Exception {
        List<SummaryData> summaryDataList = new ArrayList<>();

        if (pFromDate == null) {
            LocalDate fromDate = LocalDate.now().withDayOfMonth(1);
            pFromDate = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        Set<Long> pClientIdList = mClientCheckInCalendarDao.getClientIdListForCurrentMonth(pFromDate);

        List<ClientCheckInCheckoutData> checkInCheckoutDataList = mClientCheckInCalendarDao.getCheckInCheckOutDateFromClientIdList(pClientIdList);

        List<CashInCashOutData> cashInCashOutDataList = mClientExpenseDao.getTotalCashInCashOutFromClientIdList(pClientIdList);
        Map<Long, CashInCashOutData> cashInCashoutMap = cashInCashOutDataList.stream().map(data -> {
            return new CashInCashOutMapData(data.getClient_id(), data);
        }).collect(Collectors.toMap(CashInCashOutMapData::getClientId
                , CashInCashOutMapData::getCashInCashOutData));

        for (ClientCheckInCheckoutData data : checkInCheckoutDataList) {
            UnitData unitData = mUnitDao.getRoomAndUnitFromUnitId(data.getUnit_id());
            Optional<ClientDetailEntity> detailEntity = mClientDetailDao.findById(data.getClient_id());
            ClientDetailEntity entity = detailEntity.get();
            CashInCashOutData cashInCashOutData = cashInCashoutMap.get(data.getClient_id());

            SummaryData summaryData = new SummaryData();
            summaryData.setClientId(data.getClient_id());
            summaryData.setName(entity.getName());
            summaryData.setUnit(unitData.getUnit());
            summaryData.setRoom(unitData.getRoom());
            summaryData.setBookedDate(entity.getBookedDate());
            summaryData.setTotalCashIn(cashInCashOutData.getCash_in());
            summaryData.setTotalCashOut(cashInCashOutData.getCash_out());
            summaryData.setCheckIn(data.getCheckin());
            summaryData.setCheckout(data.getCheckout());
            summaryData.setIdProof(entity.getIdProofType());
            summaryData.setIdProofNumber(entity.getIdProofUniqueNumber());
            summaryDataList.add(summaryData);
        }

        return summaryDataList;
    }

    public List<ExpenseData> getExpensesOfClient(Long pClientId) throws Exception{
        List<ClientExpenseEntity> entityList = mClientExpenseDao.findByClientId(pClientId);
        List<ExpenseData> expanseDataList = new ArrayList<>();

        for (ClientExpenseEntity entity : entityList){
            ExpenseData data = new ExpenseData();
            data.setService(entity.getServiceId().getName());
            data.setCashIn(entity.getCashIn());
            data.setCashOut(entity.getCashOut());
            data.setDescription(entity.getDescription());
            data.setUpdateDate(entity.getUpdateDate());
            data.setUpdateUser(entity.getUpdateUser());
            expanseDataList.add(data);
        }
        return expanseDataList;
    }
}
