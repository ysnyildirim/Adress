/*
 * Copyright (c) 2023. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.adress.exception;

import com.yil.adress.base.ApiException;
import com.yil.adress.base.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;

@ResponseStatus(HttpStatus.NOT_FOUND)
@ApiException(code = ErrorCode.RegionTypeNotFound)
public class RegionTypeNotFoundException extends EntityNotFoundException {
}
