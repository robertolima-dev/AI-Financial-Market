package com.mali.exchanger.communication.factories;

import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.Deal;
import com.mali.exchanger.communication.exceptions.DealFactoryException;
import com.mali.exchanger.communication.impl.binance.implementations.deal.BinanceBuyDealImpl;
import com.mali.exchanger.communication.impl.binance.implementations.deal.BinanceBuyMarketDealImpl;
import com.mali.exchanger.communication.impl.binance.implementations.deal.BinanceSellDealImpl;
import com.mali.exchanger.communication.impl.binance.implementations.deal.BinanceSellMarketDealImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.deal.BitfinexBuyDealImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.deal.BitfinexBuyMarketDealImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.deal.BitfinexSellDealImpl;
import com.mali.exchanger.communication.impl.bitfinex.implementations.deal.BitfinexSellMarketDealImpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.deal.BittrexBuyDealmpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.deal.BittrexBuyMarketImpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.deal.BittrexSellDealImpl;
import com.mali.exchanger.communication.impl.bittrex.implementations.deal.BittrexSellMarketImpl;
import com.mali.exchanger.communication.impl.deal.EnforcedBuyImpl;
import com.mali.exchanger.communication.impl.deal.EnforcedSellImpl;
import com.mali.exchanger.communication.impl.okex.implementations.deal.OkexBuyDealImpl;
import com.mali.exchanger.communication.impl.okex.implementations.deal.OkexBuyMarketImpl;
import com.mali.exchanger.communication.impl.okex.implementations.deal.OkexSellDealImpl;
import com.mali.exchanger.communication.impl.okex.implementations.deal.OkexSellMarketImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealFactory {

    //General implementations of enforced deal
    @Autowired
    EnforcedBuyImpl enforcedBuy;
    @Autowired
    EnforcedSellImpl enforcedSell;

    //bittrex impl
    @Autowired
    BittrexBuyDealmpl bittrexBuyDealmpl;
    @Autowired
    BittrexBuyMarketImpl bittrexBuyMarket;
    @Autowired
    BittrexSellDealImpl bittrexSellDeal;
    @Autowired
    BittrexSellMarketImpl bittrexSellMarket;

    //bitfinex impl
    @Autowired
    BitfinexBuyDealImpl bitfinexBuyDeal;
    @Autowired
    BitfinexBuyMarketDealImpl bitfinexBuyMarketDeal;
    @Autowired
    BitfinexSellDealImpl bitfinexSellDeal;
    @Autowired
    BitfinexSellMarketDealImpl bitfinexSellMarket;

    //okex impl
    @Autowired
    OkexBuyDealImpl okexBuyDeal;
    @Autowired
    OkexBuyMarketImpl okexBuyMarket;
    @Autowired
    OkexSellDealImpl okexSellDeal;
    @Autowired
    OkexSellMarketImpl okexSellMarket;

    //binance impl
    @Autowired
    BinanceBuyDealImpl binanceBuyDeal;
    @Autowired
    BinanceBuyMarketDealImpl binanceBuyMarketDeal;
    @Autowired
    BinanceSellDealImpl binanceSellDeal;
    @Autowired
    BinanceSellMarketDealImpl binanceSellMarketDeal;

    public Deal getService(Exchanger exchanger, DealType dealType) throws DealFactoryException {

        //bitfinex
        if (exchanger.equals(Exchanger.BITFINEX)){
            if (dealType.equals(DealType.BUY)) return bitfinexBuyDeal;
            if (dealType.equals(DealType.BUY_MARKET)) return bitfinexBuyMarketDeal;
            if (dealType.equals(DealType.BUY_MARKET_ENFORCED)) return enforcedBuy;

            if (dealType.equals(DealType.SELL)) return bitfinexSellDeal;
            if (dealType.equals(DealType.SELL_MARKET)) return bitfinexSellMarket;
            if (dealType.equals(DealType.SELL_MARKET_ENFORCED)) return enforcedSell;

            throw new DealFactoryException("Não foi encontrada implementação de deal para o bitfinex. " +
                    "Implementações atuais: BUY,BUY_MARKET,BUY_MARKET_ENFORCED,SELL,SELL_MARKET e SELL_MARKET_ENFORCED");
        }

        //bittrex
        if (exchanger.equals(Exchanger.BITTREX)){
            if (dealType.equals(DealType.BUY)) return bittrexBuyDealmpl;
            if (dealType.equals(DealType.BUY_MARKET)) return bittrexBuyMarket;
            if (dealType.equals(DealType.BUY_MARKET_ENFORCED)) return enforcedBuy;

            if (dealType.equals(DealType.SELL)) return bittrexSellDeal;
            if (dealType.equals(DealType.SELL_MARKET)) return bittrexSellMarket;
            if (dealType.equals(DealType.SELL_MARKET_ENFORCED)) return enforcedSell;

            throw new DealFactoryException("Não foi encontrada implementação de deal para o bittrex. " +
                    "Implementações atuais: BUY,BUY_MARKET,BUY_MARKET_ENFORCED,SELL,SELL_MARKET e SELL_MARKET_ENFORCED");
        }

        //okex
        if (exchanger.equals(Exchanger.OKEX)){
            if (dealType.equals(DealType.BUY)) return okexBuyDeal;
            if (dealType.equals(DealType.BUY_MARKET)) return okexBuyMarket;
            if (dealType.equals(DealType.BUY_MARKET_ENFORCED)) return enforcedBuy;

            if (dealType.equals(DealType.SELL)) return okexSellDeal;
            if (dealType.equals(DealType.SELL_MARKET)) return okexSellMarket;
            if (dealType.equals(DealType.SELL_MARKET_ENFORCED)) return enforcedSell;

            throw new DealFactoryException("Não foi encontrada implementação de deal para o okex. " +
                    "Implementações atuais: BUY,BUY_MARKET,BUY_MARKET_ENFORCED,SELL,SELL_MARKET e SELL_MARKET_ENFORCED");
        }

        //binance
        if (exchanger.equals(Exchanger.BINANCE)){
            if (dealType.equals(DealType.BUY)) return binanceBuyDeal;
            if (dealType.equals(DealType.BUY_MARKET)) return binanceBuyMarketDeal;
            if (dealType.equals(DealType.BUY_MARKET_ENFORCED)) return enforcedBuy;

            if (dealType.equals(DealType.SELL)) return binanceSellDeal;
            if (dealType.equals(DealType.SELL_MARKET)) return binanceSellMarketDeal;
            if (dealType.equals(DealType.SELL_MARKET_ENFORCED)) return enforcedSell;

            throw new DealFactoryException("Não foi encontrada implementação de deal para o binance. " +
                    "Implementações atuais: BUY,BUY_MARKET,BUY_MARKET_ENFORCED,SELL,SELL_MARKET e SELL_MARKET_ENFORCED");
        }

        throw new DealFactoryException("Implementação de Deal não encontrada. " +
                "Implementações atuais: bitfinex, bittrex, okex e binance");
    }


}
