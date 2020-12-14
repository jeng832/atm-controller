package com.bear.atm.controller.repository;

import com.bear.atm.controller.domain.Card;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class CardRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Card card) {
        em.persist(card);
    }

    public Card findOne(Long id) {
        return em.find(Card.class, id);
    }

    @Nullable
    public Card findByNumber(String number) {
        TypedQuery<Card> query = em.createQuery("select c from Card c where c.number = :number", Card.class)
                .setParameter("number", number);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Card> findAll() {
        return em.createQuery("select c from Card c", Card.class).getResultList();
    }
}
