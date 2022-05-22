package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "City")
public class City extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "City_Sequence_Generator",
            sequenceName = "Seq_City",
            allocationSize = 1)
    @GeneratedValue(generator = "City_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "Code", length = 100)
    private String code;
    @Column(name = "CountryId", nullable = false)
    private Long countryId;


}
