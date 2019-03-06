package com.mali.crypfy.gateway.services.moneymanager;

import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToConfirmedJSON;
import com.mali.crypfy.gateway.rest.json.ChangeDepositStatusToDeniedJSON;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.DepositBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.ChangeDepositStatusToCancelledJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;

import java.math.BigDecimal;
import java.util.List;

public interface DepositBrlService {

    public DepositWithdrawRequestlBrlJSON addDeposit(DepositWithdrawRequestlBrlJSON depositWithdrawlRequestlBrl) throws DepositBrlException;
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToWaitingApproval(Integer iddepositWithdrawlRequestBrl, String email, String photo) throws DepositBrlException;
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToConfirmed(Integer iddepositWithdrawRequestBrl) throws DepositBrlException;
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToDenied(Integer iddepositWithdrawRequestBrl, String deniedReason) throws DepositBrlException;
    public DepositWithdrawRequestlBrlJSON changeDepositStatusToCancelled(Integer iddepositWithdrawRequestBrl, String email) throws DepositBrlException;
    public void delete (Integer iddepositWithdrawRequestBrl, String email) throws DepositBrlException;
    public List<DepositWithdrawRequestlBrlJSON> list(String email, String status) throws DepositBrlException;
    public BigDecimal getDoneSumAmount(String email) throws DepositBrlException;
    public BigDecimal getMonthSumAmount(List<DepositWithdrawRequestlBrlJSON> deposits) throws DepositBrlException;
    public Long count(String email, String status) throws DepositBrlException;
}
