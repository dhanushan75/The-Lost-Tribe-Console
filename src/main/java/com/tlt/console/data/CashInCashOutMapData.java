package com.tlt.console.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CashInCashOutMapData {

    private Long clientId;

    private CashInCashOutData cashInCashOutData;
}
