package com.mali.exchanger.communication.impl.totalBalance;

import com.mali.entity.Balance;
import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.TotalBalance;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesFactoryException;
import com.mali.exchanger.communication.exceptions.TotalBalanceException;
import com.mali.exchanger.communication.factories.ExchangerServicesFactory;
import com.mali.utils.ExchangerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BTCTotalBalanceImpl implements TotalBalance {

    @Autowired
    ExchangerServicesFactory exchangerServicesFactory;
    @Autowired
    ExchangerUtils exchangerUtils;

    @Override
    public double getCurrentTotalBalance(Exchanger exchanger, String key, String secret) throws TotalBalanceException {
        try {
            double totalBalance = 0;
            List<Balance> balances = exchangerServicesFactory.getService(exchanger).getAllCoinBalances(key, secret);
            for (Balance balance : balances){
                if (balance.getCoin().toLowerCase().equals("btc")) {
                    totalBalance += balance.getBalance();
                } else {
                    Market market = new Market();
                    if (balance.getCoin().toLowerCase().equals("usdt") || balance.getCoin().toLowerCase().equals("usd")){
                        market.setBaseCoin(exchangerUtils.correctBaseCurrrency(exchanger));
                        market.setToCoin("BTC");
                        double balanceToAdd = 0;
                        try {
                            balanceToAdd = exchangerServicesFactory.getService(exchanger).getTicker(market).getCurrentPrice();
                        } catch (ExchangerServicesException e) {}
                        totalBalance += balance.getBalance()/balanceToAdd;
                    } else {
                        market.setBaseCoin("BTC");
                        market.setToCoin(balance.getCoin().toUpperCase());
                        double balanceToAdd = 0;
                        try {
                            balanceToAdd = exchangerServicesFactory.getService(exchanger).getTicker(market).getCurrentPrice();
                        } catch (ExchangerServicesException e) {}
                        totalBalance += balanceToAdd*balance.getBalance();
                    }
                }
            }
            return totalBalance;
        }catch (ExchangerServicesFactoryException e) {
            e.printStackTrace();
            throw new TotalBalanceException("Erro no total balance btc! No exchanger: "+exchanger.toString());
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new TotalBalanceException("Erro no total balance btc! No exchanger: "+exchanger.toString());
        }
    }
}
