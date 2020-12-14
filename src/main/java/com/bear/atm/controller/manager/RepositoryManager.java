package com.bear.atm.controller.manager;

import com.bear.atm.controller.domain.Account;
import com.bear.atm.controller.domain.Card;
import com.bear.atm.controller.domain.Customer;
import com.bear.atm.controller.repository.AccountRepository;
import com.bear.atm.controller.repository.CardRepository;
import com.bear.atm.controller.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class RepositoryManager {

    private AccountRepository accountRepository;
    private CardRepository cardRepository;
    private CustomerRepository customerRepository;

    @Autowired
    public RepositoryManager(AccountRepository accountRepository, CardRepository cardRepository, CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
    }

    public Card getCard(Long id) {
        return cardRepository.findOne(id);
    }

    public Card getCard(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Transactional
    public Long saveCard(Card card) {
        for (Account acc : card.getAccounts()) {
            acc.setCard(card);
        }
        cardRepository.save(card);
        return card.getId();
    }

    @Transactional
    public void blockCard(Long id) {
        Card card = cardRepository.findOne(id);
        card.block();
    }

    public Account getAccount(Long id) {
        return accountRepository.findOne(id);
    }

    public Account getAccount(String number) {
        return accountRepository.findByNumber(number);
    }

    @Transactional
    public Long saveAccount(Account account) {
        accountRepository.save(account);
        return account.getId();
    }

    public Customer getCustomer(Long id) {
        return customerRepository.findOne(id);
    }

    public Customer getCustomer(String name) {
        return customerRepository.findByName(name);
    }

    @Transactional
    public Long saveCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer.getId();
    }
}
