package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ExteriorDoor")
public class ExteriorDoor extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ExteriorDoor_Sequence_Generator",
            sequenceName = "Seq_ExteriorDoor",
            allocationSize = 1)
    @GeneratedValue(generator = "ExteriorDoor_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "StreetId", nullable = false)
    private Long streetId;
}
