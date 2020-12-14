package com.bear.atm.controller.service;

import com.bear.atm.controller.model.MoneyInfo;

import java.util.List;
import java.util.Optional;

public interface AtmService {

    Long insertCard(String cardNum);
    boolean checkPin(Long id, String pin);
    List<String> getAccountNumbers(Long id);
    Optional<MoneyInfo> selectAccount(String number);
    void cancelJob();
    void done();

}
