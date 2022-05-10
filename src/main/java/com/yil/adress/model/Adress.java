package com.yil.adress.model;

import com.yil.adress.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Adress")
public class Adress extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Adress_Sequence_Generator",
            sequenceName = "Seq_Adress",
            initialValue = 1,
            allocationSize = 1)
    @GeneratedValue(generator = "Adress_Sequence_Generator")
    @Column(name = "Id", nullable = false, unique = true)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "CountryId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_CountryId_Country_Id"))
    private Country country;
    @ManyToOne
    @JoinColumn(name = "CityId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_CityId_City_Id"))
    private City city;
    @ManyToOne
    @JoinColumn(name = "DistrictId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_DistrictId_District_Id"))
    private District district;
    @ManyToOne
    @JoinColumn(name = "StreetId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_StreetId_Street_Id"))
    private Street street;
    @ManyToOne
    @JoinColumn(name = "ExteriorDoorId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_ExteriorDoorId_ExteriorDoor_Id"))
    private ExteriorDoor exteriorDoor;
    @ManyToOne
    @JoinColumn(name = "InteriorDoorId",
            referencedColumnName = "Id",
            foreignKey = @ForeignKey(name = "FK_Address_InteriorDoorId_InteriorDoor_Id"))
    private InteriorDoor interiorDoor;
}
