package com.mali.crypfy.auth.persistence.repository.impl;

import com.mali.crypfy.auth.persistence.entity.User;
import com.mali.crypfy.auth.persistence.enumeration.DocumentVerificationStatus;
import com.mali.crypfy.auth.persistence.enumeration.IdentityVerificationStatus;
import com.mali.crypfy.auth.persistence.repository.CustomUserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> list(DocumentVerificationStatus documentStatus, IdentityVerificationStatus identityStatus,String email) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> from = query.from(User.class);

        List<Predicate> predicates = new ArrayList<Predicate>();
        Predicate predicate = null;

        if(documentStatus != null) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<DocumentVerificationStatus>get("documentVerificationStatus"),documentStatus));
            predicates.add(predicate);
        }

        if(identityStatus != null) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<IdentityVerificationStatus>get("identityVerificationStatus"),identityStatus));
            predicates.add(predicate);
        }

        if(email != null && !email.equals("")) {
            predicate = criteriaBuilder.and(criteriaBuilder.equal(from.<String>get("email"),email));
            predicates.add(predicate);
        }

        TypedQuery<User> typedQuery = em.createQuery(query.select(from).where(predicates.toArray(new Predicate[]{})).orderBy(criteriaBuilder.desc(from.<Date>get("created"))));

        return typedQuery.getResultList();
    }


}
