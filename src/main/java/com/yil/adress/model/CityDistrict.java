package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "CityDistrict")
public class CityDistrict implements IEntity {
    @EmbeddedId
    private CityDistrictPk id;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    @Setter
    public static class CityDistrictPk implements Serializable {

        @ManyToOne
        @JoinColumn(name = "DistrictId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_CityDistrict_DistrictId_District_Id"))
        private District district;

        @ManyToOne
        @JoinColumn(name = "CityId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_CityDistrict_CityId_City_Id"))
        private City city;

    }
}
