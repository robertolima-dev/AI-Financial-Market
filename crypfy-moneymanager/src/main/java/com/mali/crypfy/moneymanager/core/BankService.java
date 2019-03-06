package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.exception.BankException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.persistence.entity.Bank;

import java.util.List;

/**
 * Bank Service Interface
 */
public interface BankService {
    /**
     * List All Banks
     * @return
     * @throws BankException
     */
    public List<Bank> list() throws BankException;

    /**
     * Find Bank By Id
     * @param idBank
     * @return
     * @throws BankException
     */
    public Bank findById(Integer idBank) throws BankException, NoResultException;
}
