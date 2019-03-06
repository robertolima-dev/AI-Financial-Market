package com.mali.crypfy.moneymanager.persistence.repository.impl;

import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.TypeDepositWithdraw;
import com.mali.crypfy.moneymanager.persistence.repository.DepositWithdrawRequestBrlCustomRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DepositWithdrawRequestBrlRepositoryImpl implements DepositWithdrawRequestBrlCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<DepositWithdrawRequestBrl> findAll(String email, TypeDepositWithdraw type, StatusDepositWithdrawBrl status) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<DepositWithdrawRequestBrl> query = criteriaBuilder.createQuery(DepositWithdrawRequestBrl.class);

        Root<DepositWithdrawRequestBrl> from = query.from(DepositWithdrawRequestBrl.class);

        List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate = null;

        if(email != null && !email.equals("")) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<String>get("email"),email));
            predicates.add(predicate);
        }

        if(type != null) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<TypeDepositWithdraw>get("type"),type));
            predicates.add(predicate);
        }

        if(status != null) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<StatusDepositWithdrawBrl>get("status"),status));
            predicates.add(predicate);
        }

        TypedQuery<DepositWithdrawRequestBrl> typedQuery = em.createQuery(query.select(from).where(predicates.toArray(new Predicate[]{})).orderBy(criteriaBuilder.desc(from.<Date>get("lastUpdated"))));

        return typedQuery.getResultList();
    }
}
