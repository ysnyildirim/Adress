package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "District")
public class District extends AbstractEntity {
    @Column(name = "Name", nullable = false, length = 100)
    public String name;
    @Id
    @SequenceGenerator(name = "District_Sequence_Generator",
            sequenceName = "Seq_District",
            allocationSize = 1)
    @GeneratedValue(generator = "District_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @Column(name = "Code", length = 100)
    private String code;
    @Column(name = "CityId", nullable = false)
    private Long cityId;
}
