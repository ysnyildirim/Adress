/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.adress.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RegionNotFound(1000000, "Region not found"),
    RegionTypeNotFound(1000001, "Region type not found");
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
