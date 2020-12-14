package com.bear.atm.controller.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MoneyInfo {
    private int balance;
    private int deposit;
    private int withdraw;
}
