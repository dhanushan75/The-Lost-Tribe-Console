package com.tlt.console.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "services")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServicesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long service_id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    private String active;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "update_user")
    private String updateUser;


}
