package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "INTERIOR_DOOR")
public class InteriorDoor extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "INTERIOR_DOOR_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_INTERIOR_DOOR_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "INTERIOR_DOOR_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTERIOR_DOOR_ID", nullable = false)
    private Long exteriorDoorId;
}
