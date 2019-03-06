package com.mali.crypfy.moneymanager.core;

import com.mali.crypfy.moneymanager.core.exception.DepositBrlException;
import com.mali.crypfy.moneymanager.core.exception.NoResultException;
import com.mali.crypfy.moneymanager.persistence.entity.DepositWithdrawRequestBrl;
import com.mali.crypfy.moneymanager.persistence.enumeration.StatusDepositWithdrawBrl;

import java.math.BigDecimal;
import java.util.List;

public interface DepositBrlService {

    /**
     * Add a new brl deposit
     * @param depositWithdrawRequestBrl
     * @return DepositBrlRequest
     * @throws DepositBrlException
     */
    public DepositWithdrawRequestBrl add(DepositWithdrawRequestBrl depositWithdrawRequestBrl) throws DepositBrlException;

    /**
     * Change brl deposit to waiting approval
     * @param idDeposit
     * @param email
     * @param photoUploadUrl
     * @return DepositBrlRequest
     * @throws DepositBrlException
     */
    public DepositWithdrawRequestBrl changeToWaitingApproval(Integer idDeposit, String email, String photoUploadUrl) throws DepositBrlException;

    /**
     * Change brl deposit to confirmed
     * @param idDeposit
     * @return DepositBrlRequest
     * @throws DepositBrlException
     */
    public DepositWithdrawRequestBrl changeToConfirmed(Integer idDeposit) throws DepositBrlException;

    /**
     * Change brl deposit to denied
     * @param idDeposit
     * @param deniedReason
     * @return DepositBrlRequest
     * @throws DepositBrlException
     */
    public DepositWithdrawRequestBrl changeToDenied(Integer idDeposit,String deniedReason) throws DepositBrlException;

    /**
     * Change deposit brl to cancelled
     * @param idDeposit
     * @param email
     * @return
     * @throws DepositBrlException
     */
    public DepositWithdrawRequestBrl changeToCancelled(Integer idDeposit, String email) throws DepositBrlException;

    /**
     * Find a brl deposit by id
     * @param idDeposit
     * @return
     * @throws DepositBrlException
     * @throws NoResultException
     */
    public DepositWithdrawRequestBrl findById(Integer idDeposit) throws DepositBrlException, NoResultException;

    /**
     * Find a Brl Deposit By Id and Email
     * @param idDeposit
     * @param email
     * @return
     * @throws DepositBrlException
     * @throws NoResultException
     */
    public DepositWithdrawRequestBrl findByIdAndEmail(Integer idDeposit, String email) throws DepositBrlException, NoResultException;

    /**
     * Delete brl deposit by id
     * @param idDeposit
     * @param email
     * @throws DepositBrlException
     */
    public void delete (Integer idDeposit, String email) throws DepositBrlException;

    /**
     * List deposits brl
     * @param email
     * @param status
     * @return List of brl deposits
     * @throws DepositBrlException
     */
    public List<DepositWithdrawRequestBrl> list(String email, StatusDepositWithdrawBrl status) throws DepositBrlException;

    /**
     * Get sum amount of confirmed brl deposits
     * @param email
     * @return Sum amount of confirmed brl deposits
     * @throws DepositBrlException
     */
    public BigDecimal sumAmountConfirmed(String email) throws DepositBrlException;

    /**
     * Count Brl deposits by email and status
     * @param email
     * @param status
     * @return
     * @throws DepositBrlException
     */
    public Long count(String email, StatusDepositWithdrawBrl status) throws DepositBrlException;
}
