package com.yil.adress.model;

import com.yil.adress.base.IEntity;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(schema = "ADR",
        name = "INTERIOR_DOOR")
public class InteriorDoor implements IEntity {
    @Id
    @SequenceGenerator(name = "INTERIOR_DOOR_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_INTERIOR_DOOR_ID",
            schema = "ADR")
    @GeneratedValue(generator = "INTERIOR_DOOR_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTERIOR_DOOR_ID", nullable = false)
    private Long exteriorDoorId;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    @Column(name = "LAST_MODIFY_USER_ID")
    private Long lastModifyUserId;
}
