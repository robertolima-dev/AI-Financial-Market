package com.mali.exchanger.communication.factories;

import com.mali.exchanger.communication.api.TotalBalance;
import com.mali.exchanger.communication.exceptions.TotalBalanceFactoryException;
import com.mali.exchanger.communication.impl.totalBalance.USDTotalBalanceImpl;
import com.mali.exchanger.communication.impl.totalBalance.BTCTotalBalanceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TotalBalanceFactory {

    @Autowired
    BTCTotalBalanceImpl btcTotalBalance;
    @Autowired
    USDTotalBalanceImpl usdTotalBalance;

    public TotalBalance getService(String baseCurrency) throws TotalBalanceFactoryException {

        if (baseCurrency.toUpperCase().equals("USD") || baseCurrency.toUpperCase().equals("USDT")) return usdTotalBalance;
        if (baseCurrency.toUpperCase().equals("BTC")) return btcTotalBalance;

        throw new TotalBalanceFactoryException("Implementação não encontrada. " +
                "Implementações atuais são: USD e BTC");

    }

}
