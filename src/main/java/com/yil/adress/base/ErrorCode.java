/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.adress.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    CountryNotFound(1000000, "Country not found"),
    CityNotFound(1000001, "City not found"),
    DistrictNotFound(1000002, "District not found"),
    StreetNotFound(1000003, "Street not found"),
    ExteriorDoorNotFound(1000004, "Exterior door not found"),
    InteriorDoorNotFound(1000005, "Interior door not found");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
