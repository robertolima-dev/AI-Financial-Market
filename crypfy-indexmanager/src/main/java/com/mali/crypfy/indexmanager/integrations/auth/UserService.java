package com.mali.crypfy.indexmanager.integrations.auth;

import com.mali.crypfy.indexmanager.integrations.auth.exceptions.UserException;

import java.math.BigDecimal;

public interface UserService {
    public BigDecimal getAvaiableBalance(String email) throws UserException;
    public BigDecimal addAvailableBalance(String email,BigDecimal amount) throws UserException;
    public boolean isAccountVerified(String email) throws UserException;
}
