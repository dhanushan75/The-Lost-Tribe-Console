package com.tlt.console.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ExpenseData {

    private String service;

    private Double cashIn;

    private Double cashOut;

    private String description;

    private Date updateDate;

    private String updateUser;
}
