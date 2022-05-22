package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class DistrictNotFoundException extends EntityNotFoundException {
    public DistrictNotFoundException() {
        super("District not found");
    }

    public DistrictNotFoundException(String message) {
        super(message);
    }
}
