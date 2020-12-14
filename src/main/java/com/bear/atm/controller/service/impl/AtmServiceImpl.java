package com.bear.atm.controller.service.impl;

import com.bear.atm.controller.domain.Account;
import com.bear.atm.controller.domain.Card;
import com.bear.atm.controller.exception.IncorrectPinException;
import com.bear.atm.controller.exception.InvalidCardException;
import com.bear.atm.controller.manager.RepositoryManager;
import com.bear.atm.controller.manager.StateMachineManager;
import com.bear.atm.controller.model.MoneyInfo;
import com.bear.atm.controller.service.AtmService;
import com.bear.atm.controller.statemachine.Events;
import com.bear.atm.controller.statemachine.States;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtmServiceImpl implements AtmService {

    private RepositoryManager repositoryManager;
    private StateMachineManager stateMachineManager;

    @Autowired
    public AtmServiceImpl(RepositoryManager repositoryManager, StateMachineManager stateMachineManager) {
        this.repositoryManager = repositoryManager;
        this.stateMachineManager = stateMachineManager;
    }


    @Override
    @Nullable
    public Long insertCard(String cardNum) {
        if (stateMachineManager.getCurrentState() != States.IDLE) return null;
        Card card = repositoryManager.getCard(cardNum);
        if (card == null) {
            throw new InvalidCardException(cardNum + " is NOT valid card number");
        }

        stateMachineManager.sendEvent(Events.INSERT_CARD);
        return card.getId();
    }

    @Override
    public boolean checkPin(Long cardId, String pin) {
        if ((stateMachineManager.getCurrentState() != States.PIN_CHECK)
                && (stateMachineManager.getCurrentState() != States.PIN_CHECK2)
                && (stateMachineManager.getCurrentState() != States.PIN_CHECK3)) return false;
        // Check PIN correctness to bank API
        // In this project, "CORRECT-PIN" is correct pin.
        boolean correct = (pin == "CORRECT-PIN");

        if (!correct) {
            boolean lastPinCheck = false;
            if (stateMachineManager.getCurrentState() == States.PIN_CHECK3) {
                repositoryManager.blockCard(cardId);
                lastPinCheck = true;
            }
            stateMachineManager.sendEvent(Events.INVALID_PIN);
            if (lastPinCheck) throw new IncorrectPinException("The PIN is NOT correct");
            return false;
        }

        stateMachineManager.sendEvent(Events.VALID_PIN);
        return true;
    }

    @Override
    public List<String> getAccountNumbers(Long id) {
        return repositoryManager.getCard(id).getAccounts()
                .stream()
                .map(account -> account.getNumber())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MoneyInfo> selectAccount(String number) {
        if (stateMachineManager.getCurrentState() != States.ACCOUNT_SELECTION) return Optional.empty();
        Account account = repositoryManager.getAccount(number);
        if (account == null) return Optional.empty();

        stateMachineManager.sendEvent(Events.SELECT_ACCOUNT);

        MoneyInfo moneyInfo = new MoneyInfo(account.getBalance(), account.getDeposit(), account.getWithdraw());

        return Optional.of(moneyInfo);
    }

    @Override
    public void cancelJob() {
        stateMachineManager.sendEvent(Events.DONE);
    }

    @Override
    public void done() {
        stateMachineManager.sendEvent(Events.DONE);
    }


}
