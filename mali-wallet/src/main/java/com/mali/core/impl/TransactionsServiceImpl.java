package com.mali.core.impl;

import com.mali.connection.BtcdRPCConnector;
import com.mali.core.api.TransactionsService;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransactionsServiceImpl implements TransactionsService{

    @Autowired
    private BtcdRPCConnector connector;

    @Override
    public boolean moveFromAccounts(String accountA, String accountB, BigDecimal value) throws BitcoindException, CommunicationException {
       return connector.getClient().move(accountA,accountB,value);
    }

    @Override
    public List<Payment> listTransactions(String addressName, Integer confirmations) throws BitcoindException, CommunicationException {
       return connector.getClient().listTransactions(addressName,confirmations);
    }

    @Override
    public String sendFromTo(String addressFrom, String addressTo, BigDecimal value) throws BitcoindException, CommunicationException {

        return connector.getClient().sendFrom(addressFrom,addressTo,value);
    }

}
