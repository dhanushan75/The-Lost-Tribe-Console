package com.tlt.console.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "client_detail")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "num_of_people")
    private Long numOfPeople;

    @Column(name = "id_proof_type")
    private String idProofType;

    @Column(name = "id_proof_unique_number")
    private String idProofUniqueNumber;

    @Column(name = "additional_request")
    private String additionalRequest;

    @OneToOne
    @JoinColumn(name = "unit_id")
    private UnitsEntity unitId;

    @Column(name = "booked_date")
    private Date bookedDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "update_user")
    private String updateUser;

}
