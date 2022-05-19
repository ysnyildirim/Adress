package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Street")
public class Street extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Street_Sequence_Generator",
            sequenceName = "Seq_Street",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Street_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "DistrictId", nullable = false)
    private Long districtId;
}
