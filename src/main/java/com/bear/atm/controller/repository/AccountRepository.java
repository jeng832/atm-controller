package com.bear.atm.controller.repository;

import com.bear.atm.controller.domain.Account;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AccountRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Account account) {
        em.persist(account);
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    public Account findByNumber(String number) {
        return em.createQuery("select a from Account a where a.number = :number", Account.class)
                .setParameter("number", number).getSingleResult();
    }
}
