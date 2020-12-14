package com.bear.atm.controller.exception;

public class InvalidCardException extends RuntimeException {
    public InvalidCardException() {
        super();
    }

    public InvalidCardException(String msg) {
        super(msg);
    }
}
