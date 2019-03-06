package com.mali.crypfy.gateway.services.moneymanager;

import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankAccountException;
import com.mali.crypfy.gateway.services.moneymanager.exceptions.BankException;
import com.mali.crypfy.gateway.services.moneymanager.json.BankAccountJSON;
import com.mali.crypfy.gateway.services.moneymanager.json.BankJSON;

import java.util.List;

public interface BankAccountService {
    public BankAccountJSON add(BankAccountJSON bankAccountJSON) throws BankAccountException;
    public BankAccountJSON update(BankAccountJSON bankAccountJSON) throws BankAccountException;
    public void delete(Integer id, String email) throws BankAccountException;
    public List<BankAccountJSON> list(String email) throws BankAccountException;
    public List<BankJSON> listBanks() throws BankException;
    public BankAccountJSON find(Integer id, String email) throws BankAccountException;

}
