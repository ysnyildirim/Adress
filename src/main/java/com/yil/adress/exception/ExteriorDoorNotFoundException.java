package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class ExteriorDoorNotFoundException extends EntityNotFoundException {
    public ExteriorDoorNotFoundException() {
        super("Exterior door not found");
    }

    public ExteriorDoorNotFoundException(String message) {
        super(message);
    }
}
