package com.mali.crypfy.moneymanager.persistence.repository;

import com.mali.crypfy.moneymanager.persistence.entity.BankAccount;
import com.mali.crypfy.moneymanager.persistence.enumeration.BankAccountType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Bank Account Repository Interface
 */
public interface BankAccountRepository extends CrudRepository<BankAccount,Integer> {

    /**
     * Find a Specific Bank Account By Account Number, Account Number Digit, Type and Id Bank
     * @param agency
     * @param accountNumber
     * @param accountNumberDigit
     * @param type
     * @param idbank
     * @return
     */
    public BankAccount findByAgencyAndAccountNumberAndAccountNumberDigitAndTypeAndIdbank(Integer agency, Integer accountNumber,Integer accountNumberDigit,BankAccountType type, Integer idbank);

    /**
     * Find Especific Bank Account By Email and Id Bank Account
     * @param email
     * @param idbankAccount
     * @return
     */
    @EntityGraph(attributePaths = { "bank" }, type = EntityGraph.EntityGraphType.LOAD)
    public BankAccount findByEmailAndIdbankAccount(String email,Integer idbankAccount);

    /**
     * Find Bank Accounts by Email Ordered By Created Date Desc
     * @param email
     * @return
     */
    @EntityGraph(attributePaths = { "bank" }, type = EntityGraph.EntityGraphType.LOAD)
    public List<BankAccount> findByEmailOrderByCreatedDesc(String email);

    /**
     * Find Specific Bank Account By Id
     * @param integer
     * @return
     */
    @EntityGraph(attributePaths = { "bank" }, type = EntityGraph.EntityGraphType.LOAD)
    @Override
    public Optional<BankAccount> findById(Integer integer);
}
