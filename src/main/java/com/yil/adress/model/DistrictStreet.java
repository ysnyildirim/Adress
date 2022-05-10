package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "DistrictStreet")
public class DistrictStreet implements IEntity {
    @EmbeddedId
    private DistrictStreetPk id;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class DistrictStreetPk implements Serializable {

        @ManyToOne
        @JoinColumn(name = "DistrictId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_DistrictStreet_DistrictId_District_Id"))
        private District district;

        @ManyToOne
        @JoinColumn(name = "StreetId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_DistrictStreet_StreetId_Street_Id"))
        private Street street;

    }
}
