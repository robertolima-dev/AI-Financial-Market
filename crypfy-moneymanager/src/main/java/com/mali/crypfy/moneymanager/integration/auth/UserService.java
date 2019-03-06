package com.mali.crypfy.moneymanager.integration.auth;

import com.mali.crypfy.moneymanager.integration.auth.exception.UserException;

import java.math.BigDecimal;

/**
 * Interface for User Service
 */
public interface UserService {

    /**
     * Get Available Brl Balance By User
     * @param email
     * @return
     * @throws UserException
     */
    public BigDecimal getAvailableBalanceBrl(String email) throws UserException;

    /**
     * Get User Info Details
     * @param email
     * @return
     * @throws UserException
     */
    public User getInfo(String email) throws UserException;

    /**
     * Check if account were verified
     * @param email
     * @return
     * @throws UserException
     */
    public boolean isAccountVerified(String email) throws UserException;
}
