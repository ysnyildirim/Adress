package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(schema = "ADR",
        name = "STREET")
public class Street implements IEntity {
    @Id
    @SequenceGenerator(name = "STREET_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_STREET_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "STREET_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DISTRICT_ID", nullable = false)
    private Long districtId;
    @Column(name = "POST_CODE", length = 5)
    private String postCode;
}
