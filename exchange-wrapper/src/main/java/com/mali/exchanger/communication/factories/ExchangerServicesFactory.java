package com.mali.exchanger.communication.factories;

import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.ExchangerServices;
import com.mali.exchanger.communication.exceptions.ExchangerServicesFactoryException;
import com.mali.exchanger.communication.impl.binance.implementations.BinanceExchangeServicesImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.BitfinexExchangerServicesImpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.BittrexExchangerServicesImpl;
import com.mali.exchanger.communication.impl.okex.implementations.OkexExchangerServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangerServicesFactory {

    @Autowired
    BittrexExchangerServicesImpl bittrexExchangerServices;
    @Autowired
    BitfinexExchangerServicesImpl bitfinexExchangerServices;
    @Autowired
    OkexExchangerServicesImpl okexExchangerServices;
    @Autowired
    BinanceExchangeServicesImpl binanceExchangeServices;

    public ExchangerServices getService(Exchanger exchanger) throws ExchangerServicesFactoryException {

        if (exchanger.equals(Exchanger.BITTREX)) return bittrexExchangerServices;

        if (exchanger.equals(Exchanger.BITFINEX)) return bitfinexExchangerServices;

        if (exchanger.equals(Exchanger.OKEX)) return okexExchangerServices;

        if (exchanger.equals(Exchanger.BINANCE)) return binanceExchangeServices;

        throw new ExchangerServicesFactoryException("Implementação de Exchanger Services não encontrada!");
    }

}
