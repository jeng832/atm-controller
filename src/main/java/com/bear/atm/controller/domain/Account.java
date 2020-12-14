package com.bear.atm.controller.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Long id;
    private String number;
    private int balance;
    private int deposit;
    private int withdraw;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    private Card card;

    public Account(String number) {
        this(number, 0, 0, 0, null);
    }

    public Account(String number, int balance, int deposit, int withdraw, Customer customer) {
        this.number = number;
        this.balance = balance;
        this.deposit = deposit;
        this.withdraw = withdraw;
        this.customer = customer;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
