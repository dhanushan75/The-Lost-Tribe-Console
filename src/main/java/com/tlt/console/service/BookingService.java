package com.tlt.console.service;

import com.tlt.console.data.SummaryData;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface BookingService {

    public List<SummaryData> getSummaryDataForGrid(Date pFromDate) throws Exception;

}
