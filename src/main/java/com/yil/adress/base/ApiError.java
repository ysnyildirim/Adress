package com.yil.adress.base;

import lombok.Getter;

@Getter
public enum ApiError {

    InteriorDoorNotFound(1000005, "Interior door not found"),
    ExteriorDoorNotFound(1000004, "Exterior door not found"),
    StreetNotFound(1000003, "Street not found"),
    DistrictNotFound(1000002, "District not found"),
    CityNotFound(1000001, "City not found"),
    CountryNotFound(1000000, "Country not found");

    private final int code;
    private final String message;

    ApiError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
