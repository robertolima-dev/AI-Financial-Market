package com.mali.core.impl;

import com.mali.connection.BtcdRPCConnector;
import com.mali.core.api.WalletService;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private BtcdRPCConnector connector;

    @Override
    public String newAddress() throws BitcoindException, CommunicationException {
        return connector.getClient().getNewAddress();
    }

    @Override
    public String newAddressWithName(String name) throws BitcoindException, CommunicationException {
        return connector.getClient().getNewAddress(name);
    }

    @Override
    public List<String> getAddressByAccountName(String accName) throws BitcoindException, CommunicationException {
        return connector.getClient().getAddressesByAccount(accName);
    }

    @Override
    public BigDecimal receivedByAddress(String address) throws BitcoindException, CommunicationException {
        return connector.getClient().getReceivedByAccount(address);
    }

    @Override
    public BigDecimal receivedByAccount(String accName) throws BitcoindException, CommunicationException {
        return connector.getClient().getReceivedByAccount(accName);
    }

    @Override
    public BigDecimal getBalance() throws BitcoindException, CommunicationException {
        return connector.getClient().getBalance();
    }

    @Override
    public BigDecimal getBalanceByAccount(String account) throws BitcoindException, CommunicationException {
        return connector.getClient().getBalance(account);
    }

    @Override
    public void accountSetName(String adress,String newName) throws BitcoindException, CommunicationException {
        connector.getClient().setAccount(adress,newName);
    }

    @Override
    public void walletBackup(String fileLocation) throws BitcoindException, CommunicationException {
        connector.getClient().backupWallet(fileLocation);
    }

    @Override
    public void walletImport(String fileLocation) throws BitcoindException, CommunicationException {
        connector.getClient().importWallet(fileLocation);
    }

    @Override
    public Map<String, BigDecimal> listAccounts() throws BitcoindException, CommunicationException {
        return connector.getClient().listAccounts();
    }
}
