package com.mali.utils;

import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import org.springframework.stereotype.Service;

@Service
public class ExchangerUtils {

    private final String usd = "USD";
    private final String tether = "USDT";

    public String correctBaseCurrrency(Exchanger exchanger) throws ExchangerServicesException {

        switch (exchanger){

            case BITTREX:
                return tether;
            case BITFINEX:
                return usd;
            case OKEX:
                return tether;
            case BINANCE:
                return tether;
        }
        throw new ExchangerServicesException("Implementação não encontrada (exchanger correct base currency)");
    }

    public Market correctMarketCurrencies(Exchanger exchanger, Market market) throws ExchangerServicesException {

        switch (exchanger){

            case BITTREX:
                market.setBaseCoin(market.getBaseCoin().equals("USD") ? tether : market.getBaseCoin());
                return market;
            case BITFINEX:
                market.setBaseCoin(market.getBaseCoin().equals("USDT") ? usd : market.getBaseCoin());
                return market;
            case OKEX:
                market.setBaseCoin(market.getBaseCoin().equals("USD") ? tether : market.getBaseCoin());
                return market;
            case BINANCE:
                market.setBaseCoin(market.getBaseCoin().equals("USD") ? tether : market.getBaseCoin());
                market.setToCoin(market.getToCoin().equals("BCH") ? "BCC" : market.getToCoin());
                return market;
        }
        throw new ExchangerServicesException("Implementação não encontrada (exchanger market correct base currency)");
    }

    public String correctBaseCoin(Exchanger exchanger, String coin) throws ExchangerServicesException {

        switch (exchanger){

            case BITTREX:
                return (coin.toUpperCase().equals("USD")) ? tether : coin;
            case BITFINEX:
                return (coin.toUpperCase().equals("USDT")) ? usd : coin;
            case OKEX:
                return (coin.toUpperCase().equals("USD")) ? tether : coin;
            case BINANCE:
                return (coin.toUpperCase().equals("USD")) ? tether : coin;
        }
        throw new ExchangerServicesException("Implementação não encontrada (exchanger correct base coin)");
    }

    public String correctCoin(Exchanger exchanger, String coin) throws ExchangerServicesException {

        switch (exchanger){

            case BITTREX:
                coin = (coin.toUpperCase().equals("USD")) ? "USDT" : coin;
                return coin;
            case BITFINEX:
                coin = (coin.toUpperCase().equals("USDT")) ? "USD" : coin;
                return coin;
            case OKEX:
                coin = (coin.toUpperCase().equals("USD")) ? "USDT" : coin;
                return coin;
            case BINANCE:
                coin = (coin.toUpperCase().equals("BCH")) ? "BCC" : coin;
                coin = (coin.toUpperCase().equals("USD")) ? "USDT" : coin;
                return coin;
        }
        throw new ExchangerServicesException("Implementação não encontrada (exchanger correct base coin)");
    }


}
