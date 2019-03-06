package com.mali.core.api;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface WalletService {
    public String newAddress() throws BitcoindException, CommunicationException;
    public String newAddressWithName(String name) throws BitcoindException, CommunicationException;
    public List<String> getAddressByAccountName(String name) throws BitcoindException, CommunicationException;
    public BigDecimal receivedByAddress(String Address) throws BitcoindException, CommunicationException;
    public BigDecimal receivedByAccount(String name) throws BitcoindException, CommunicationException;
    public BigDecimal getBalance() throws BitcoindException, CommunicationException;
    public BigDecimal getBalanceByAccount(String account) throws BitcoindException, CommunicationException;
    public void accountSetName(String adress,String newName) throws BitcoindException, CommunicationException;
    public void walletBackup(String fileLocation) throws BitcoindException, CommunicationException;
    public void walletImport(String fileLocation) throws BitcoindException, CommunicationException;
    public Map<String,BigDecimal> listAccounts() throws BitcoindException, CommunicationException;
}
