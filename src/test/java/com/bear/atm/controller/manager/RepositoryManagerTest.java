package com.bear.atm.controller.manager;

import com.bear.atm.controller.domain.Account;
import com.bear.atm.controller.domain.Card;
import com.bear.atm.controller.domain.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
class RepositoryManagerTest {

    @Autowired
    private RepositoryManager repositoryManager;

    @Test
    public void testSaveCustomer() {
        String name = "tester";
        Customer customer = new Customer(name);
        Long id = repositoryManager.saveCustomer(customer);
        Assertions.assertThat(repositoryManager.getCustomer(id).getName()).isEqualTo(name);
    }

    @Test
    @Transactional
    public void testGetCustomer() {
        String name = "customer";
        Customer customer = new Customer(name);
        Long id = repositoryManager.saveCustomer(customer);
        Customer customerById = repositoryManager.getCustomer(id);
        Customer customerByName = repositoryManager.getCustomer(name);
        Assertions.assertThat(customerById).isEqualTo(customerByName);
    }

    @Test
    public void testSaveCard() {
        String cardNumber = "CARD-NUMBER";
        Card card = new Card(cardNumber);
        Long id = repositoryManager.saveCard(card);
        Assertions.assertThat(repositoryManager.getCard(id).getNumber()).isEqualTo(cardNumber);
    }

    @Test
    @Transactional
    public void testGetCard() {
        String cardNumber = "TEST-CARD-NUMBER";
        Card card = new Card(cardNumber);
        Long id = repositoryManager.saveCard(card);
        Card cardById = repositoryManager.getCard(id);
        Card cardByNumber = repositoryManager.getCard(cardNumber);
        Assertions.assertThat(cardById).isEqualTo(cardByNumber);
    }

    @Test
    public void testSaveAccount() {
        String accountNumber = "110-00111234-5";
        Account account = new Account(accountNumber);
        Long id = repositoryManager.saveAccount(account);
        Assertions.assertThat(repositoryManager.getAccount(id).getNumber()).isEqualTo(accountNumber);
    }

    @Test
    @Transactional
    public void testGetAccount() {
        String accountNumber = "110-00111234";
        Account account = new Account(accountNumber);
        Long id = repositoryManager.saveAccount(account);
        Account accountById = repositoryManager.getAccount(id);
        Account accountByNumber = repositoryManager.getAccount(accountNumber);
        Assertions.assertThat(accountById).isEqualTo(accountByNumber);
    }

    @Test
    @Transactional
    public void testSaveByCascade() {
        String customerName = "customer_A";
        Customer customer = new Customer(customerName);

        String accountNumber = "account_number_A";
        Account account = new Account(accountNumber, 100, 10, 5, customer);

        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        String cardNumber = "123-456-789-0";
        Card card = new Card(cardNumber, accounts);
        repositoryManager.saveCard(card);

        Assertions.assertThat(repositoryManager.getCard(cardNumber).getAccounts().get(0))
                .isEqualTo(repositoryManager.getAccount(accountNumber));

        Assertions.assertThat(repositoryManager.getAccount(accountNumber).getCustomer())
                .isEqualTo(repositoryManager.getCustomer(customerName));
    }

    @Test
    @Transactional
    public void testSaveMultipleAccounts() {
        String customerName = "customer_A";
        Customer customer = new Customer(customerName);

        List<Account> accounts = new ArrayList<>();
        String accountNumber = "account_number_A";
        Account account = new Account(accountNumber, 100, 10, 5, customer);
        accounts.add(account);

        String accountNumber2 = "account_number_B";
        Account account2 = new Account(accountNumber2, 200, 0, 500, customer);
        accounts.add(account2);

        String accountNumber3 = "account_number_C";
        Account account3 = new Account(accountNumber3, 370, 20, 60, customer);
        accounts.add(account3);

        String cardNumber = "123-456-789-0";
        Card card = new Card(cardNumber, accounts);
        repositoryManager.saveCard(card);

        Assertions.assertThat(repositoryManager.getAccount(accountNumber).getCard())
                .isEqualTo(repositoryManager.getCard(cardNumber));
        Assertions.assertThat(repositoryManager.getCard(cardNumber).getAccounts().size()).isEqualTo(3);

    }


}