package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.exception.BankAccountException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;

import java.util.List;

/**
 * Bank Account Service Interface
 */
public interface BankAccountService {

    /**
     * Add a New Bank Account
     * @param bankAccount
     * @return Bank Account Object
     * @throws BankAccountException
     */
    public BankAccount add(BankAccount bankAccount) throws BankAccountException;

    /**
     * Update a Bank Account
     * @param bankAccount
     * @return Bank Account Object
     * @throws BankAccountException
     */
    public BankAccount update(BankAccount bankAccount) throws BankAccountException;

    /**
     * Remove Bank Account By Email
     * @param email
     * @param idbankAccount
     * @throws BankAccountException
     */
    public void delete(String email,Integer idbankAccount) throws BankAccountException;

    /**
     * List Bank Accounts
     * @param email
     * @return List of Bank Accounts
     * @throws BankAccountException
     */
    public List<BankAccount> list(String email) throws BankAccountException;

    /**
     * Find Bank Account By Email And Id
     * @param email
     * @param id
     * @return
     * @throws BankAccountException
     * @throws NoResultException
     */
    public BankAccount findByIdAndEmail(String email, Integer id) throws BankAccountException, NoResultException;

    /**
     * Find Bank Account By Id
     * @param id
     * @return Bank Account Object
     * @throws BankAccountException
     * @throws NoResultException
     */
    public BankAccount findById(Integer id) throws BankAccountException, NoResultException;
}
