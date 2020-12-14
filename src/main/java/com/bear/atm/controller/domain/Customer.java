package com.bear.atm.controller.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private Long id;
    private String name;

    private LocalDateTime createdTime;

    public Customer(String name) {
        this.name = name;
        createdTime = LocalDateTime.now();
    }
}
