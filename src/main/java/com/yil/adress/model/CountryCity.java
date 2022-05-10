package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "CountryCity")
public class CountryCity implements IEntity {
    @EmbeddedId
    private CountryCityPk id;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @ToString
    @EqualsAndHashCode
    @Data
    public static class CountryCityPk implements Serializable {

        @ManyToOne
        @JoinColumn(name = "CountryId",
                referencedColumnName = "Id",
                updatable = false,
                insertable = false,
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_CountryCity_CountryId_Country_Id"))
        private Country country;

        @ManyToOne
        @JoinColumn(name = "CityId",
                referencedColumnName = "Id",
                updatable = false,
                insertable = false,
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_CountryCity_CityId_City_Id"))
        private City city;

    }
}
