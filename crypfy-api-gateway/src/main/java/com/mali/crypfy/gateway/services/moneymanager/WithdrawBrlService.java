package com.mali.crypfy.gateway.services.moneymanager;

import com.mali.crypfy.gateway.services.moneymanager.exceptions.WithdrawBrlException;
import com.mali.crypfy.gateway.services.moneymanager.json.DepositWithdrawRequestlBrlJSON;

import java.math.BigDecimal;
import java.util.List;

public interface WithdrawBrlService {
    public DepositWithdrawRequestlBrlJSON addWithdraw(DepositWithdrawRequestlBrlJSON depositWithdrawRequestlBrlJSON) throws WithdrawBrlException;
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToProcessing(Integer iddepositWithdrawlRequestBrl) throws WithdrawBrlException;
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToConfirmed(Integer iddepositWithdrawlRequestBrl) throws WithdrawBrlException;
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToDenied(Integer iddepositWithdrawlRequestBrl, String deniedReason) throws WithdrawBrlException;
    public DepositWithdrawRequestlBrlJSON changeWithdrawStatusToCancelled(Integer iddepositWithdrawlRequestBrl, String email) throws WithdrawBrlException;
    public List<DepositWithdrawRequestlBrlJSON> list(String email, String status) throws WithdrawBrlException;
    public void delete (Integer iddepositWithdrawRequestBrl, String email) throws WithdrawBrlException;
    public BigDecimal getDoneSumAmount(String email) throws WithdrawBrlException;
    public Long count(String email, String status) throws WithdrawBrlException;
}
