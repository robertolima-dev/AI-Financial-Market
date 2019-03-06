package com.crypfy.elastic.trader.trade.impl;

import com.crypfy.elastic.trader.integrations.crypto.indexes.IndexesServices;
import com.crypfy.elastic.trader.integrations.crypto.indexes.exceptions.CryptoIndexesException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Ticker;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.order.OrderManager;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.*;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TradeServicesImpl implements TradeServices{

    @Autowired
    private OrderManager orderManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ExchangerServices exchangerServices;
    @Autowired
    private IndexesServices indexesServices;
    @Autowired
    private MessageSender msgSender;

    public static final int CODE_ERROR_GENERIC = 9000;

    @Override
    public boolean exchangeHasMarket(Market market, ExchangerDetails exchanger) throws TradeException {

        try {
            List<Market> markets = exchangerServices.getMarkets(exchanger.getExchanger());
            for (Market possibleMarket: markets) if (possibleMarket.getMarketSymbol().equals(market.getMarketSymbol())) return true;
            return false;
        } catch (ExchangerException e) {
            e.printStackTrace();
            throw new TradeException("Erro ao coletar mercados na implementação market",CODE_ERROR_GENERIC);
        }

    }

    @Override
    public boolean exchangeHasCoin(Market market, ExchangerDetails exchanger) throws TradeException {
        try {
            List<Market> markets = exchangerServices.getMarkets(exchanger.getExchanger());
            for (Market possibleMarket: markets) if (possibleMarket.getToCoin().equals(market.getToCoin())) return true;
            return false;
        } catch (ExchangerException e) {
            e.printStackTrace();
            throw new TradeException("Erro ao coletar mercados (coin) na implementação market",CODE_ERROR_GENERIC);
        }
    }

    @Override
    public boolean coinInTrade(String callCoin,String strategyName,String subStrategyName,Exchanger exchanger) throws TradeException {

        try{
            if (orderRepository.findByStrategyNameAndSubStrategyNameAndExchangerAndCallCoinAndOrderStatusNot(strategyName,subStrategyName,exchanger,callCoin,OrderStatus.CLOSED).size()>0) return true;
            return false;
        } catch (Exception e){
            e.printStackTrace();
            throw new TradeException("Erro ao checar ordens no banco!");
        }

    }

    @Override
    public double coinBalance(ExchangerDetails exchanger, String coinName) throws TradeException {

        try {
            return exchangerServices.getCoinBalance(exchanger.getExchanger(), coinName,exchanger.getKey(),exchanger.getSecret()).doubleValue();
        } catch (ExchangerException e) {
            throw new TradeException("Problema ao consultar saldo de moedas!");
        }
    }

    @Override
    public boolean hitTP(Order order,Market market,ExchangerDetails exchanger) throws TradeException {
        try{
            if (exchangeHasMarket(market,exchanger)) {
                double tpPrice = ((order.getSecondStepOpenPrice().doubleValue()*(order.getTakeProfit().doubleValue()+1)));
                double currentPrice = exchangerServices.dealPrice(order.getExchanger(), market, "SELL", order.getOrderAmount().doubleValue()).doubleValue();
                return tpPrice <= currentPrice ? true : false;
            } else {
                double tpPrice = ((order.getFirstStepOpenPrice().doubleValue()*(order.getTakeProfit().doubleValue()+1)));
                market.setBaseCoin("BTC");
                double currentPrice = exchangerServices.dealPrice(order.getExchanger(), market, "SELL", order.getOrderAmount().doubleValue()).doubleValue();
                return tpPrice <= currentPrice ? true : false;
            }
            } catch (ExchangerException e){
            throw new TradeException("Problema ao consultar valor de negociacao para conferencia de TP");
        }

    }

    @Override
    public boolean hitSL(Order order,Market market,ExchangerDetails exchanger) throws TradeException {
        try{
            if (exchangeHasMarket(market,exchanger)) {
                double slPrice = ((order.getSecondStepOpenPrice().doubleValue()*(order.getStopLoss().doubleValue()+1)));
                double currentPrice = exchangerServices.dealPrice(order.getExchanger(), market, "SELL", order.getOrderAmount().doubleValue()).doubleValue();
                return slPrice >= currentPrice ? true : false;
            } else {
                double slPrice = ((order.getFirstStepOpenPrice().doubleValue()*(order.getStopLoss().doubleValue()+1)));
                market.setBaseCoin("BTC");
                double currentPrice = exchangerServices.dealPrice(order.getExchanger(), market, "SELL", order.getOrderAmount().doubleValue()).doubleValue();
                return slPrice >= currentPrice ? true : false;
            }
            } catch (ExchangerException e){
            throw new TradeException("Problema ao consultar valor de negociacao para conferencia de SL");
        }
    }

    @Override
    public boolean closeConditionsMet(Order order, Market market,ExchangerDetails exchangerDetails) throws TradeException {
        try {
            if (order.isTpSlNeeded()){
                return (hitSL(order, market,exchangerDetails) || hitTP(order, market,exchangerDetails) || new Date().equals(order.getExpirationDate()) || new Date().after(order.getExpirationDate())) ? true : false;
            } else return new Date().after(order.getExpirationDate()) ? true : false;
        } catch (TradeException e){
            throw new TradeException("Problema ao tentar checar condicão multipla de fechamento");
        }
    }

    @Override
    public OrderCloseReason closeReason(Order order, Market market,ExchangerDetails exchanger) throws TradeException {

        try {
            if (order.isTpSlNeeded()){
                if (hitSL(order, market,exchanger)) return OrderCloseReason.CLOSED_BY_SL;
                if (hitTP(order, market,exchanger)) return OrderCloseReason.CLOSED_BY_TP;
                if (new Date().equals(order.getExpirationDate()) || new Date().after(order.getExpirationDate())) return OrderCloseReason.CLOSED_BY_TIME;
            } else {
                if (new Date().equals(order.getExpirationDate()) || new Date().after(order.getExpirationDate())) return OrderCloseReason.CLOSED_BY_TIME;
            }
            throw new TradeException("Ordem fechada mas condição de fechamento não identificada!!!");
        } catch (TradeException e){
            throw new TradeException("Problema ao tentar checar por causa de fechamento de transação");
        }
    }

    public boolean liquidityAndPriceCheck(Exchanger exchanger,Order order) {

        try {
            Ticker callCoinTicker = new Ticker();
            if (!order.isTwoStep()){
                callCoinTicker = exchangerServices.getTicker(exchanger,new Market("USD",order.getCallCoin()));
                double baseVol = callCoinTicker.getLast24Volume()*callCoinTicker.getCurrentPrice();
                double price = callCoinTicker.getCurrentPrice();
                //we need liquidity
                if (baseVol>50*order.getOrderAmount().doubleValue() && order.getCallPrice().doubleValue() > price*1.01) return true;
            } else {
                Ticker callBtcTicker = exchangerServices.getTicker(exchanger,new Market("BTC",order.getCallCoin()));
                double btcUsdprice = exchangerServices.getTicker(exchanger,new Market("USD","BTC")).getCurrentPrice();
                double vol = callBtcTicker.getLast24Volume()*btcUsdprice;
                double coinPrice = callBtcTicker.getCurrentPrice();
                if (vol>50*order.getOrderAmount().doubleValue() && order.getCallPrice().doubleValue()/btcUsdprice > coinPrice*1.01 ) return true;
            }
        } catch (ExchangerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean minimumConditionsMet(Order order,SubStrategy subStrategy,ExchangerDetails exchangerDetails) throws TradeException {

        try {
            boolean didIt = true;
            List<String> currentCallCoins = orderManager.distinctCallCoinsByStrategyNameAndSubStrategyNameAndOrderStatusNot(order.getStrategyName(),order.getSubStrategyName());
            int currentCoins = currentCallCoins.size();
            // respect maximum distinct simultaneous coins
            if ( currentCoins >= subStrategy.getAllowedSimultaneousTransaction()) didIt = false;
            // allow exchanger that still don't have all the call coins to buy
            if ( currentCallCoins.contains(order.getCallCoin())) didIt = true;
            if (coinInTrade(order.getCallCoin(),order.getStrategyName(),order.getSubStrategyName(),order.getExchanger())) didIt = false;
            // check if the market has enough liquidity
            if ( !liquidityAndPriceCheck(order.getExchanger(),order) ) didIt = false;
            // check if there's enough base coin to fill this order
            double baseBalance = coinBalance(exchangerDetails,order.getBaseCurrency().toString()) - committedMoneyByExchanger(order.getExchanger(),order.getBaseCurrency().toString());
            if ( baseBalance < order.getOrderAmount().doubleValue()*0.99 ) didIt = false;

            return didIt;
        } catch (TradeException e) {
            e.printStackTrace();
            throw new TradeException("Erro ao checar por condicoes minimas da ordem!",e.getErrors(),e.getStatus());
        }
    }

    public double committedMoneyByExchanger(Exchanger exchanger, String baseCurrency) {

        List<Order> orders = orderRepository.findByExchangerAndFromCoin(exchanger,baseCurrency);

        double balance = 0;
        for (Order order : orders){
            if (order.getOrderType().toString().contains("BUY") && ( order.getOrderStatus().equals(OrderStatus.WAITING_DIRECT_EXECUTION) || order.getOrderStatus().equals(OrderStatus.WAITING_TWO_STEP_EXECUTION) )){
                balance += order.getOrderAmount().doubleValue();
            }
        }
        return balance;
    }

    @Override
    public Market marketFromOrder(Order order) {
        Market market = new Market();
        market.setBaseCoin(order.getFromCoin());
        market.setToCoin(order.getToCoin());
        return market;
    }

    @Override
    public double exchangersTotalBalance(List<ExchangerDetails> exchangers, BaseCurrency baseCurrency, double percentOfTotal) throws TradeException {
        try {
            double balance = 0;
            for (ExchangerDetails exchangerDetails : exchangers) {
                balance += percentOfTotal*exchangerServices.getTotalBalance(exchangerDetails.getExchanger(), baseCurrency,exchangerDetails.getKey(),exchangerDetails.getSecret()).doubleValue();
            }
            return balance;
        } catch (ExchangerException e) {
            e.printStackTrace();
            throw new TradeException("Problema ao consultar saldo de todos os exchangers da estratégia",e.getErrors(),e.getStatus());
        }

    }

    @Override
    public List<ExchangerDetails> exchangersWithMarketOrCoin(List<ExchangerDetails> exchangers, Market market) throws TradeException {

        try {
            List<ExchangerDetails> exchangersWithMarket = new ArrayList<>();
            for (ExchangerDetails exchangerDetails : exchangers) {
                if (exchangeHasMarket(market, exchangerDetails) || exchangeHasCoin(market, exchangerDetails)){
                    exchangersWithMarket.add(exchangerDetails);
                }
            }
            return  exchangersWithMarket;
        } catch (TradeException e) {
            e.printStackTrace();
            throw new TradeException("Erro ao verificar quais exchangers possuem mercado ou coin!",e.getErrors(),e.getStatus());
        }

    }

    @Override
    public double currentUSDBRLRatio() throws TradeException {
        try {
            double btcBrl = indexesServices.getLastUpdate("BITVALOR","BITCOIN").getValue();
            double btcUsd  = indexesServices.getLastUpdate("COINMARKETCAP","BITCOIN").getValue();
            return btcBrl/btcUsd;
        } catch (CryptoIndexesException e) {
            e.printStackTrace();
            throw new TradeException("Erro ao consultar currentUSDBRLRatio",e.getErrors(),e.getStatus());
        }
    }

}
