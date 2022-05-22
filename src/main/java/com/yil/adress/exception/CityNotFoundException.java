package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class CityNotFoundException extends EntityNotFoundException {
    public CityNotFoundException() {
        super("City not found");
    }

    public CityNotFoundException(String message) {
        super(message);
    }
}
