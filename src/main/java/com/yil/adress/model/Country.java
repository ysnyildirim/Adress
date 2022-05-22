package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Country")
public class Country extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Country_Sequence_Generator",
            sequenceName = "Seq_Country",
            allocationSize = 1)
    @GeneratedValue(generator = "Country_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "Code", nullable = false, length = 3)
    private String code;
}
