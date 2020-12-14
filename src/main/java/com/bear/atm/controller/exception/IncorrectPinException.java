package com.bear.atm.controller.exception;

public class IncorrectPinException extends RuntimeException {
    public IncorrectPinException() {
        super();
    }

    public IncorrectPinException(String msg) {
        super(msg);
    }
}
