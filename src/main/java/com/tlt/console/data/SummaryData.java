package com.tlt.console.data;

import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummaryData {

    private Long clientId;

    private String name;

    private String Unit;

    private String Room;

    private Date bookedDate;

    private Double totalCashIn;

    private Double totalCashOut;

    private Date checkIn;

    private Date checkout;

    private String idProof;

    private String idProofNumber;

}
