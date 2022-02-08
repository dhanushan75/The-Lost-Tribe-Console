package com.tlt.console.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "client_expense")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long expenseId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServicesEntity serviceId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientDetailEntity clientId;

    @Column(name = "cash_in")
    private Double cashIn;

    @Column(name = "cash_out")
    private Double cashOut;

    @Column(name = "description")
    private String description;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "update_user")
    private String updateUser;

}
