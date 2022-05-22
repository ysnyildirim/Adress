package com.yil.adress.exception;

import javax.persistence.EntityNotFoundException;

public class InteriorDoorNotFoundException extends EntityNotFoundException {
    public InteriorDoorNotFoundException() {
        super("Interior door not found");
    }

    public InteriorDoorNotFoundException(String message) {
        super(message);
    }
}
