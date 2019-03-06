package com.mali.core.api;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionsService {
    public boolean moveFromAccounts(String accountA, String accountB, BigDecimal value) throws BitcoindException, CommunicationException;
    public String sendFromTo(String addressFrom,String addressTo,BigDecimal value) throws BitcoindException, CommunicationException;
    public List<Payment> listTransactions(String addressName, Integer confirmations) throws BitcoindException, CommunicationException;
}
