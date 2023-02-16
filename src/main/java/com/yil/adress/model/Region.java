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
        name = "REGION")
public class Region implements IEntity {
    @Id
//    @SequenceGenerator(name = "REGION_SEQUENCE_GENERATOR",
//            sequenceName = "SEQ_REGION_ID",
//            schema = "ADR")
//    @GeneratedValue(generator = "REGION_SEQUENCE_GENERATOR")
    @Column(name = "ID", nullable = false, unique = true)
    private Long id;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "REGION_TYPE_ID", nullable = false)
    private Integer regionTypeId;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "CODE", nullable = false, length = 3)
    private String code;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE", updatable = false)
    private Date createdDate;
    @Column(name = "CREATED_USER_ID", updatable = false)
    private Long createdUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    @Column(name = "LAST_MODIFY_USER_ID")
    private Long lastModifyUserId;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long parentId;
        private Integer regionTypeId;
        private String name;
        private String code;
        private Boolean enabled;
        private Date createdDate;
        private Long createdUserId;
        private Date lastModifyDate;
        private Long lastModifyUserId;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setParentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder setRegionTypeId(Integer regionTypeId) {
            this.regionTypeId = regionTypeId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder setEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Builder setCreatedUserId(Long createdUserId) {
            this.createdUserId = createdUserId;
            return this;
        }

        public Builder setLastModifyDate(Date lastModifyDate) {
            this.lastModifyDate = lastModifyDate;
            return this;
        }

        public Builder setLastModifyUserId(Long lastModifyUserId) {
            this.lastModifyUserId = lastModifyUserId;
            return this;
        }

        public Region build() {
            Region region = new Region();
            region.setId(id);

            return region;
        }
    }


}
