package com.mali.crypfy.moneymanager.persistence.repository;

import com.mali.crypfy.moneymanager.persistence.entity.Bank;
import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Bank Repository Interface
 */
public interface BankRepository extends CrudRepository<Bank,Integer> {
    public List<Bank> findAll();
}
