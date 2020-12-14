package com.bear.atm.controller.statemachine;

public enum States {
    IDLE,
    PIN_CHECK,
    PIN_CHECK2,
    PIN_CHECK3,
    ACCOUNT_SELECTION,
    ACCOUNT_INFO
}
