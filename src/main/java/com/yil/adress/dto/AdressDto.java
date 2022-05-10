package com.yil.adress.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdressDto {
    private Long id;
    private CountryDto country;
    private CityDto city;
    private DistrictDto district;
    private StreetDto street;
    private ExteriorDoorDto exteriorDoor;
    private InteriorDoorDto interiorDoor;
}
