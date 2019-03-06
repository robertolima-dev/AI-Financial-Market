package com.mali.exchanger.communication.impl.bittrex.implementations;

import com.mali.entity.*;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.api.ExchangerServices;
import com.mali.exchanger.communication.exceptions.*;
import com.mali.exchanger.communication.factories.DealFactory;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.factories.TotalBalanceFactory;
import com.mali.exchanger.communication.impl.bittrex.caches.BittrexMarketsCache;
import com.mali.exchanger.communication.impl.bittrex.exceptions.*;
import com.mali.exchanger.communication.impl.bittrex.factories.BittrexOrderBookFactory;
import com.mali.exchanger.communication.impl.bittrex.sub.entity.*;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareMarketHistoryException;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.MarketHistoryFactoryException;
import com.mali.exchanger.communication.impl.cryptocompare.factories.MarketHistoryTimeFrameFactory;
import com.mali.utils.ExchangerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class BittrexExchangerServicesImpl implements ExchangerServices {

    @Autowired
    private BittrexApis apis;
    @Autowired
    private TotalBalanceFactory balanceFactory;
    @Autowired
    private MarketHistoryTimeFrameFactory historyTimeFrameFactory;
    @Autowired
    private BittrexOrderBookFactory orderBookFactory;
    @Autowired
    private DealPriceFactory dealPriceFactory;
    @Autowired
    private DealFactory dealFactory;
    @Autowired
    private ExchangerUtils utils;
    @Autowired
    private BittrexMarketsCache marketsCache;

    public static final String exchangeName = "BitTrex";

    @Override
    public String getDepositAddress(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BITTREX,coin);
            return apis.getDepositAddress(coin, key, secret).getResult().getAddress();
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
    }

    @Override
    public boolean doWithdraw(String password,String address, String coin, double quantity, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BITTREX,coin);
            return apis.getWithdraw(address, coin, quantity, key, secret).isSuccess();
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
    }

    @Override
    public double getCurrentTotalBalance(String key, String secret, String baseCurrency) throws ExchangerServicesException {
        try {
            baseCurrency = utils.correctCoin(Exchanger.BITTREX,baseCurrency);
            return balanceFactory.getService(baseCurrency).getCurrentTotalBalance(Exchanger.BITTREX,key,secret);
        } catch (TotalBalanceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação total balance do bittrex (factory)",e.getErrors());
        } catch (TotalBalanceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação total balance do bittrex",e.getErrors());
        }
    }

    @Override
    public double getCoinBalance(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BITTREX,coin);
            return apis.getCoinBalance(coin, key, secret).getResult().getBalance();
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
    }

    @Override
    public List<Balance> getAllCoinBalances(String key, String secret) throws ExchangerServicesException {
        List<Balance> balances = new ArrayList<Balance>();
        try {
            List<BalanceDetails> balanceDetailsList = apis.getAccountBalances(key, secret).getResult();
            for (BalanceDetails balanceDetails : balanceDetailsList){
                Balance balance = new Balance();
                balance.setAddress(balanceDetails.getAddress());
                balance.setBalance(balanceDetails.getBalance());
                balance.setCoin(balanceDetails.getCoinName());
                balance.setPending(balanceDetails.getPending());

                if (balance.getBalance()>0) balances.add(balance);
            }
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
        return balances;
    }

    @Override
    public List<Order> getOrderHistory(String key, String secret) throws ExchangerServicesException {
        List<Order> orders = new ArrayList<Order>();
        try {
            List<OrderHistoryDetails> orderHistoryDetailsList = apis.getOrderHistory(key, secret).getResult();
            for(OrderHistoryDetails orderHistoryDetails : orderHistoryDetailsList){
                Order order = new Order();
                order.setPrice(orderHistoryDetails.getPrice());
                order.setQuantity(orderHistoryDetails.getQuantity());
                order.setTimestamp(orderHistoryDetails.getOpenTime());
                order.setType(orderHistoryDetails.getOrderType());
                order.setUuid(orderHistoryDetails.getOrderUuid());
                order.setMarket(orderHistoryDetails.getMarketName());
                order.setCosts(orderHistoryDetails.getCosts());

                orders.add(order);
            }
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
        return orders;
    }

    @Override
    public List<Deposit> getDepositHistory(String coin,String key, String secret) throws ExchangerServicesException {
        List<Deposit> deposits = new ArrayList<Deposit>();
        try {
            coin = utils.correctCoin(Exchanger.BITTREX,coin);
            List<DepositHistoryDetails> depositHistoryDetailsList = apis.getDepositHistory(key, secret).getResult();
            for(DepositHistoryDetails depositHistoryDetails : depositHistoryDetailsList){
                Deposit deposit = new Deposit();
                deposit.setAddress(depositHistoryDetails.getAddress());
                deposit.setAmount(depositHistoryDetails.getAmount());
                deposit.setCurrency(depositHistoryDetails.getCoin());
                deposit.setTimestamp(depositHistoryDetails.getTimestamp());
                deposit.setTxCost(depositHistoryDetails.getTxCost());
                deposit.setTxId(depositHistoryDetails.getTxId());
                deposit.setUuid(depositHistoryDetails.getUuid());

                deposits.add(deposit);
            }
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
        return deposits;
    }

    @Override
    public List<Withdraw> getWithdrawHistory(String coin,String key, String secret) throws ExchangerServicesException {
        List<Withdraw> withdraws = new ArrayList<Withdraw>();
        try {
            coin = utils.correctCoin(Exchanger.BITTREX,coin);
            List<WithdrawHistoryDetails> withdrawHistoryDetailsList = apis.getWithdrawHistory(key, secret).getResult();
            for(WithdrawHistoryDetails withdrawHistoryDetails : withdrawHistoryDetailsList){
                Withdraw withdraw = new Withdraw();
                withdraw.setAddress(withdrawHistoryDetails.getAddress());
                withdraw.setAmount(withdrawHistoryDetails.getAmount());
                withdraw.setCurrency(withdrawHistoryDetails.getCoin());
                withdraw.setTimestamp(withdrawHistoryDetails.getTimestamp());
                withdraw.setTxCost(withdrawHistoryDetails.getTxCost());
                withdraw.setTxId(withdrawHistoryDetails.getTxId());
                withdraw.setUuid(withdrawHistoryDetails.getUuid());

                withdraws.add(withdraw);
            }
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação account do bittrex",e.getErrors());
        }
        return withdraws;
    }

    @Override
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, TimeFrame timeFrame) throws ExchangerServicesException {
        try{
            market = utils.correctMarketCurrencies(Exchanger.BITTREX,market);
            return historyTimeFrameFactory.getService(timeFrame).getMarketHistory(market,nBars,timeframeMultiplier,exchangeName);
        } catch (MarketHistoryFactoryException e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação history do bittrex",e.getErrors());
        } catch (CryptocompareMarketHistoryException e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação history do bittrex",e.getErrors());
        }
    }

    @Override
    public Order orderInfo(Market market,String uuid, String key, String secret) throws ExchangerServicesException {
        try {
            OrderDetails orderDetails = apis.getOrder(uuid, key, secret).getResult();
            Order order = new Order();
            order.setUuid(orderDetails.getUuid());
            order.setType(orderDetails.getOrderType());
            order.setTimestamp(orderDetails.getCloseTime());
            order.setRemaining(orderDetails.getRemaining());
            order.setQuantity(orderDetails.getQuantity());
            order.setPrice(orderDetails.getPrice());
            order.setMarket(orderDetails.getMarket());
            order.setLive( orderDetails.getCloseTime() == null ? true : false );
            order.setCosts( orderDetails.getOrderType().toLowerCase().equals("buy") ? orderDetails.getQuantity() : orderDetails.getQuantity()*orderDetails.getPrice());
            return order;
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação orderinfo do bittrex",e.getErrors());
        }
    }

    @Override
    public List<Market> getMarkets() throws ExchangerServicesException {
        try {
            return marketsCache.getMarkets();
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao markets do bittrex",e.getErrors());
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao markets do bittrex (cache)");
        }
    }

    @Override
    public List<SimpleOrder> getOrderBook(Market market, String side) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITTREX,market);
            return orderBookFactory.getService(side).getBook(market);
        } catch (BittrexOrderBookFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao orderbook do bittrex",e.getErrors());
        } catch (BittrexOrderBookException e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao orderbook do bittrex",e.getErrors());
        }
    }

    @Override
    public Ticker getTicker(Market market) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITTREX,market);
            TickerData tickerData = apis.getMarketTicker(market.getMarketSymbol());
            Ticker ticker = new Ticker();
            ticker.setCurrentPrice(tickerData.getResult().get(0).getLastPrice());
            ticker.setLast24Volume(market.getBaseCoin().equals("BTC") ? tickerData.getResult().get(0).getVolume()/apis.getMarketTicker("USDT-BTC").getResult().get(0).getLastPrice() : tickerData.getResult().get(0).getVolume());
            return ticker;
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao markets do bittrex",e.getErrors());
        }
    }

    @Override
    public TradeResult newDeal(Market market, DealType dealType, double quantity,double price, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITTREX,market);
            return dealFactory.getService(Exchanger.BITTREX,dealType).newDeal(Exchanger.BITTREX,market,quantity,price,key,secret);
        } catch (DealFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao newdeal do bittrex (deal factory)",e.getErrors());
        } catch (DealException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao newdeal do bittrex",e.getErrors());
        }
    }

    @Override
    public double dealPrice(Market market, DealType dealType, double coinAmount) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITTREX,market);
            return dealPriceFactory.getService(dealType).dealPrice(market, Exchanger.BITTREX,coinAmount);
        } catch (DealPriceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao deal price",e.getErrors());
        } catch (DealPriceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao deal price",e.getErrors());
        }
    }

    @Override
    public boolean orderCancel(String orderId,Market market, String key, String secret) throws ExchangerServicesException {
        try {
            return apis.getOrderCancel(orderId,key,secret).isSuccess();
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação OkexTrade do bittrex",e.getErrors());
        }
    }
}
