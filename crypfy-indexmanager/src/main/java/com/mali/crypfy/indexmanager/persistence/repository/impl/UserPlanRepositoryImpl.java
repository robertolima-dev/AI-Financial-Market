package com.mali.crypfy.indexmanager.persistence.repository.impl;

import com.mali.crypfy.indexmanager.persistence.entity.Plan;
import com.mali.crypfy.indexmanager.persistence.entity.UserPlan;
import com.mali.crypfy.indexmanager.persistence.enumeration.UserPlanStatus;
import com.mali.crypfy.indexmanager.persistence.repository.UserPlanCustomRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserPlanRepositoryImpl implements UserPlanCustomRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserPlan> list(String email, UserPlanStatus status) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserPlan> query = criteriaBuilder.createQuery(UserPlan.class);
        Root<UserPlan> from = query.from(UserPlan.class);
        Join<UserPlan,Plan> plan = (Join) from.fetch("plan");

                List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate = null;

        if(email != null && !email.equals("")) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<String>get("email"),email));
            predicates.add(predicate);
        }

        if(status != null) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<UserPlanStatus>get("status"),status));
            predicates.add(predicate);
        }

        TypedQuery<UserPlan> typedQuery = em.createQuery(query.select(from).where(predicates.toArray(new Predicate[]{})).orderBy(criteriaBuilder.desc(from.<Date>get("created"))));

        return typedQuery.getResultList();
    }
}
