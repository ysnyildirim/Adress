package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ExteriorDoor")
public class ExteriorDoor implements IEntity {
    @Id
    @SequenceGenerator(name = "ExteriorDoor_Sequence_Generator",
            sequenceName = "Seq_ExteriorDoor",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "ExteriorDoor_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
}
