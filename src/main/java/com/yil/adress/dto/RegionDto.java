package com.yil.adress.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegionDto implements Serializable {
    private Long id;
    private String name;
    private Long parentId;
    private Integer regionTypeId;
    private String code;
    private Boolean enabled;
}
