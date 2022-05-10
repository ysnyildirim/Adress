package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "StreetExteriorDoor")
public class StreetExteriorDoor implements IEntity {
    @EmbeddedId
    private StreetExteriorDoorPk id;

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class StreetExteriorDoorPk implements Serializable {


        @ManyToOne
        @JoinColumn(name = "StreetId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_StreetExteriorDoor_StreetId_Street_Id"))
        private Street street;

        @ManyToOne
        @JoinColumn(name = "ExteriorDoorId",
                referencedColumnName = "Id",
                nullable = false,
                foreignKey = @ForeignKey(name = "FK_StreetExteriorDoor_ExteriorDoorId_ExteriorDoor_Id"))
        private ExteriorDoor exteriorDoor;

    }
}
