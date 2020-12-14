package com.bear.atm.controller.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue
    @Column(name = "card_id")
    private Long id;

    private String number;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    private boolean blocked;

    public void block() {
        this.blocked = true;
    }

    public Card(String number) {
        this.number = number;
    }

    public Card(String number, List<Account> accounts) {
        this.number = number;
        this.accounts = accounts;
        this.blocked = false;
    }
}
