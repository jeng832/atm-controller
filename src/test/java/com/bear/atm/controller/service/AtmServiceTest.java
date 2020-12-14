package com.bear.atm.controller.service;

import com.bear.atm.controller.domain.Account;
import com.bear.atm.controller.domain.Card;
import com.bear.atm.controller.domain.Customer;
import com.bear.atm.controller.exception.IncorrectPinException;
import com.bear.atm.controller.exception.InvalidCardException;
import com.bear.atm.controller.manager.RepositoryManager;
import com.bear.atm.controller.manager.StateMachineManager;
import com.bear.atm.controller.model.MoneyInfo;
import com.bear.atm.controller.statemachine.Events;
import com.bear.atm.controller.statemachine.States;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AtmServiceTest {

    @Autowired
    private AtmService atmService;

    @Autowired
    private RepositoryManager repositoryManager;

    @Autowired
    private StateMachineManager stateMachineManager;

    private String customerName = "customer_A";
    private String cardNumber = "123-456-789-0";
    private String accountNumber = "account_number_A";

    @BeforeEach
    public void initNormalScenario() {
        Customer customer = new Customer(customerName);
        Account account = new Account(accountNumber, 100, 10, 5, customer);
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        Card card = new Card(cardNumber, accounts);
        repositoryManager.saveCard(card);
    }

    @AfterEach
    public void resetTest() {

        stateMachineManager.sendEvent(Events.CANCEL);
    }

    @Test
    public void testNormalScenario() {
        Long id = atmService.insertCard(cardNumber);
        Assertions.assertThat(id).isNotNull();
        Assertions.assertThat(atmService.checkPin(id, "CORRECT-PIN")).isEqualTo(true);
        Assertions.assertThat(atmService.getAccountNumbers(id).size()).isEqualTo(1);
        Assertions.assertThat(atmService.getAccountNumbers(id).get(0)).isEqualTo(accountNumber);
        Optional<MoneyInfo> acc = atmService.selectAccount(atmService.getAccountNumbers(id).get(0));
        Assert.assertTrue(acc.isPresent());
        Assertions.assertThat(acc.get().getBalance()).isEqualTo(100);
        Assertions.assertThat(acc.get().getDeposit()).isEqualTo(10);
        Assertions.assertThat(acc.get().getWithdraw()).isEqualTo(5);
        atmService.done();
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);
    }

    @Test
    public void testInvalidCardScenario() {
        Assertions.assertThatThrownBy(() -> atmService.insertCard("invalid_card_number"))
                .isInstanceOf(InvalidCardException.class);
    }

    @Test
    public void testIncorrectPinScenario() {
        Long id = atmService.insertCard(cardNumber);
        Assertions.assertThat(id).isNotNull();
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK);
        Assertions.assertThat(atmService.checkPin(id, "INCORRECT-PIN")).isEqualTo(false);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK2);
        Assertions.assertThat(atmService.checkPin(id, "INCORRECT-PIN")).isEqualTo(false);
        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.PIN_CHECK3);

        Assertions.assertThatThrownBy(() -> atmService.checkPin(id, "INCORRECT-PIN"))
                .isInstanceOf(IncorrectPinException.class);
        Assertions.assertThat(repositoryManager.getCard(id).isBlocked()).isEqualTo(true);

        Assertions.assertThat(stateMachineManager.getCurrentState()).isEqualTo(States.IDLE);
    }
}