package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.exception.DepositBrlException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.core.exception.WithdrawBrlException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawBrlService {

    /**
     * Add new Withdraw Brl Request
     * @param depositWithdrawRequestBrl
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl add(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws WithdrawBrlException;

    /**
     * Change Withdraw to Processing
     * @param idWithdraw
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl changeToProcessing(Integer idWithdraw) throws WithdrawBrlException;


    /**
     * Change Withdraw to Confirmed
     * @param idWithdraw
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl  changeToConfirmed(Integer idWithdraw) throws WithdrawBrlException;

    /**
     * Change Withdraw to Denied
     * @param idWithdraw
     * @param deniedReason
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl changeToDenied(Integer idWithdraw, String deniedReason) throws WithdrawBrlException;

    /**
     * Change Withdraw to Cancelled
     * @param idWithdraw
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl changeToCancelled(Integer idWithdraw, String email) throws WithdrawBrlException;

    /**
     * Find Withdraw By Id
     * @param idWithdraw
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl findById(Integer idWithdraw) throws WithdrawBrlException, NoResultException;

    /**
     * Find Withdraw By Id and Email
     * @param idWithdraw
     * @param email
     * @return Withdraw Brl Object
     * @throws WithdrawBrlException
     */
    public DepositWithdrawRequestBrl findByIdAndEmail(Integer idWithdraw, String email) throws WithdrawBrlException, NoResultException;

    /**
     * Remove Withdraw Brl
     * @param idWithdraw
     * @throws WithdrawBrlException
     */
    public void delete(Integer idWithdraw, String email) throws WithdrawBrlException;

    /**
     * List withdrawals brl
     * @param email
     * @param status
     * @return List of brl withdrawals
     * @throws WithdrawBrlException
     */
    public List<DepositWithdrawRequestBrl> list(String email, StatusDepositWithdrawBrl status) throws WithdrawBrlException;

    /**
     * Get sum amount of confirmed brl withdrawals
     * @param email
     * @return Sum amount of confirmed brl withdrawals
     * @throws WithdrawBrlException
     */
    public BigDecimal sumAmountConfirmed(String email) throws WithdrawBrlException;

    /**
     * Count Brl withdrawals by email and status
     * @param email
     * @param status
     * @return Count of Brl Withdrawals
     * @throws WithdrawBrlException
     */
    public Long count(String email, StatusDepositWithdrawBrl status) throws WithdrawBrlException;
}
