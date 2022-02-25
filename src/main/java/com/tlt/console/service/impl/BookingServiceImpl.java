package com.tlt.console.service.impl;

import com.tlt.console.dao.*;
import com.tlt.console.data.*;
import com.tlt.console.entity.*;
import com.tlt.console.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ServicesDao mServicesDao;

    public List<SummaryData> getSummaryDataForGrid(Date pFromDate) throws Exception {
        List<SummaryData> summaryDataList = new ArrayList<>();

        Set<Long> pClientIdList = null;
        if (pFromDate == null) {
//            LocalDate fromDate = LocalDate.now().withDayOfMonth(1);
//            pFromDate = Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            pClientIdList = mClientDetailDao.getClientIdList();
        } else {
            pClientIdList = mClientCheckInCalendarDao.getClientIdListForCurrentMonth(pFromDate);
        }

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

            if (cashInCashOutData != null) {
                summaryData.setTotalCashIn(cashInCashOutData.getCash_in());
                summaryData.setTotalCashOut(cashInCashOutData.getCash_out());
            }
            summaryData.setCheckIn(data.getCheckin());
            summaryData.setCheckout(data.getCheckout());
            summaryData.setIdProof(entity.getIdProofType());
            summaryData.setIdProofNumber(entity.getIdProofUniqueNumber());
            summaryDataList.add(summaryData);
        }

        return summaryDataList;
    }

    public List<ExpenseData> getExpensesOfClient(Long pClientId) throws Exception {
        List<ClientExpenseEntity> entityList = mClientExpenseDao.findByClientId(pClientId);
        List<ExpenseData> expanseDataList = new ArrayList<>();

        for (ClientExpenseEntity entity : entityList) {
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

    public List<String> getServiceList() throws Exception {
        List<String> serviceList = new ArrayList<>();
        List<ServicesEntity> serviceEntiyList = mServicesDao.findAll();
        serviceEntiyList.stream().forEach(servicesEntity -> serviceList.add(servicesEntity.getName()));
        return serviceList;
    }

    public void saveExpenseData(ClientExpenseEntity pExpenseEntity, String pServiceType) throws Exception {
        ServicesEntity servicesEntity = mServicesDao.findByName(pServiceType.toUpperCase());

        if (servicesEntity == null) {
            servicesEntity = new ServicesEntity();
            servicesEntity.setName(pServiceType.toUpperCase());
            servicesEntity.setDescription(pServiceType.toUpperCase());
            servicesEntity.setActive("Y");
            servicesEntity.setUpdateDate(new Date());
            servicesEntity.setUpdateUser("1");

            servicesEntity = mServicesDao.saveAndFlush(servicesEntity);
        }

        pExpenseEntity.setServiceId(servicesEntity);
        mClientExpenseDao.save(pExpenseEntity);
    }

    public List<UnitsEntity> getParentUnits() throws Exception {
        return mUnitDao.findByUnitType("UNIT");
    }

    public List<UnitsEntity> getSubUnits(Long parentUnitId) throws Exception {
        return mUnitDao.findByParentUnitId(parentUnitId);
    }

    public List<ClientCheckInCalendarEntity> getCalendarEventsByUnitId(Long unitId, Long parentUnitId) throws Exception {
        if (unitId != null) {
            return mClientCheckInCalendarDao.findByUnitId_UnitId(unitId);
        } else if (parentUnitId != null) {
            return mClientCheckInCalendarDao.findByUnitId_ParentUnitId(parentUnitId);
        } else {
            return mClientCheckInCalendarDao.findAll();
        }
    }

    public List<String> getIdProofTypeList() throws Exception {
        return mClientDetailDao.getDistinctIdProofType();
    }

    public ClientDetailEntity saveClientDetail(ClientDetailEntity entity) {
        return mClientDetailDao.saveAndFlush(entity);
    }

    public void saveClientCheckInCalendar(ClientCheckInCalendarEntity checkinEntity) throws Exception {
        mClientCheckInCalendarDao.save(checkinEntity);
    }
}
