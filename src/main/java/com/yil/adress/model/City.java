package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(schema = "ADR",
        name = "CITY")
public class City implements IEntity {
    @Id
    @SequenceGenerator(name = "CITY_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_CITY_ID",
            schema = "ADR",
            allocationSize = 1)
    @GeneratedValue(generator = "CITY_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "CODE", length = 100)
    private String code;
    @Column(name = "COUNTRY_ID", nullable = false)
    private Long countryId;
    @Column(name = "PHONE_CODE", length = 3)
    private String phoneCode;


    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    @Column(name = "LAST_MODIFY_USER_ID")
    private Long lastModifyUserId;


}
