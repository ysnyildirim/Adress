package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import com.yil.adress.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "InteriorDoor")
public class InteriorDoor extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "InteriorDoor_Sequence_Generator",
            sequenceName = "Seq_InteriorDoor",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "InteriorDoor_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "ExteriorDoorId", nullable = false)
    private Long exteriorDoorId;
}
