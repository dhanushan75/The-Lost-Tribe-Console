package com.tlt.console.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "client_check_in_calendar")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCheckInCalendarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "unit_id")
    private UnitsEntity unitId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private ClientDetailEntity clientId;

    @Column(name = "booked_date")
    private Date bookedDate;

    @Column(name = "description")
    private String description;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "update_user")
    private String updateUser;

}
