package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class StreetNotFoundException extends EntityNotFoundException {
    public StreetNotFoundException() {
        super("Street not found");
    }

    public StreetNotFoundException(String message) {
        super(message);
    }
}
