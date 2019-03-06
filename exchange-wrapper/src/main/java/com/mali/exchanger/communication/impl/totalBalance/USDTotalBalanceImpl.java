package com.mali.exchanger.communication.impl.totalBalance;

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

@Service
public class USDTotalBalanceImpl implements TotalBalance {

    @Autowired
    BTCTotalBalanceImpl usdTotalBalance;
    @Autowired
    ExchangerServicesFactory exchangerServicesFactory;
    @Autowired
    ExchangerUtils exchangerUtils;

    @Override
    public double getCurrentTotalBalance(Exchanger exchanger, String key, String secret) throws TotalBalanceException {
        try {
            Market market = new Market();
            market.setBaseCoin(exchangerUtils.correctBaseCurrrency(exchanger));
            market.setToCoin("BTC");
            return usdTotalBalance.getCurrentTotalBalance(exchanger,key, secret)*exchangerServicesFactory.getService(exchanger).getTicker(market).getCurrentPrice();
        } catch (ExchangerServicesFactoryException e) {
            e.printStackTrace();
            throw new TotalBalanceException("Erro no btc total balance (factory)");
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new TotalBalanceException("Erro no btc total balance ");
        }
    }
}
