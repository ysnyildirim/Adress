package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "EXTERIOR_DOOR")
public class ExteriorDoor extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "EXTERIOR_DOOR_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_EXTERIOR_DOOR",
            allocationSize = 1)
    @GeneratedValue(generator = "EXTERIOR_DOOR_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "STREET_ID", nullable = false)
    private Long streetId;
}
