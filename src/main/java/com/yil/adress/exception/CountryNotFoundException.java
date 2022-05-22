package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class CountryNotFoundException extends EntityNotFoundException {
    public CountryNotFoundException() {
        super("Country not found");
    }

    public CountryNotFoundException(String message) {
        super(message);
    }
}
