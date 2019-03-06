package com.mali.crypfy.moneymanager.persistence.repository;

import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.TypeDepositWithdraw;

import java.util.List;

/**
 * Custom Deposit Withdrawal Request Brl Repository Interface
 */
public interface DepositWithdrawRequestBrlCustomRepository {

    /**
     * Find All Withdrawals and Deposits Brl Requests by Email, Type and Status
     * @param email
     * @param type
     * @param status
     * @return
     */
    public List<DepositWithdrawRequestBrl> findAll(String email, TypeDepositWithdraw type, StatusDepositWithdrawBrl status);
}
