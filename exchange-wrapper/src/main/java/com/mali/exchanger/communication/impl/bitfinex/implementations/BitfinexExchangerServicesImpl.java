package com.mali.exchanger.communication.impl.bitfinex.implementations;

import com.mali.entity.*;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.api.ExchangerServices;
import com.mali.exchanger.communication.exceptions.*;
import com.mali.exchanger.communication.factories.DealFactory;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.factories.TotalBalanceFactory;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.*;
import com.mali.exchanger.communication.impl.bitfinex.factories.BitfinexOrderBookFactory;
import com.mali.exchanger.communication.impl.bitfinex.implementations.caches.BitfinexMarketsCache;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.BalancesDetails;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.MovementHistoryDetail;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.OrderDetails;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.TickerData;
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
public class BitfinexExchangerServicesImpl implements ExchangerServices {

    @Autowired
    private BitfinexApis bitfinexApis;
    @Autowired
    private TotalBalanceFactory totalBalanceFactory;
    @Autowired
    private MarketHistoryTimeFrameFactory historyTimeFrameFactory;
    @Autowired
    private BitfinexOrderBookFactory orderBookFactory;
    @Autowired
    private DealPriceFactory dealPriceFactory;
    @Autowired
    private DealFactory dealFactory;
    @Autowired
    private ExchangerUtils utils;
    @Autowired
    private BitfinexMarketsCache marketsCache;

    public static final String exchangeName = "Bitfinex";

    @Override
    public String getDepositAddress(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BITFINEX,coin);
            return bitfinexApis.getDepositAddress(coin, key, secret).getAddress();
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação deposits do bitfinex",e.getErrors());
        }
    }

    @Override
    public boolean doWithdraw(String password,String address, String coin, double quantity, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BITFINEX,coin);
            return bitfinexApis.getWithdraw(address, coin, quantity, key, secret).getStatus().equals("success");
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação withdraws do bitfinex",e.getErrors());
        }
    }

    @Override
    public double getCurrentTotalBalance(String key, String secret, String baseCurrency) throws ExchangerServicesException {
        try {
            baseCurrency = utils.correctCoin(Exchanger.BITFINEX,baseCurrency);
            return totalBalanceFactory.getService(baseCurrency).getCurrentTotalBalance(Exchanger.BITFINEX,key,secret);
        } catch (TotalBalanceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação total balance do bitfinex (factory)",e.getErrors());
        } catch (TotalBalanceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação total balance do bitfinex",e.getErrors());
        }
    }

    @Override
    public double getCoinBalance(String coin, String key, String secret) throws ExchangerServicesException {
        double balance=0;
        try {
            coin = utils.correctCoin(Exchanger.BITFINEX,coin);
            List<BalancesDetails> balancesDetailsList = bitfinexApis.getAccountBalances(key, secret);
            for(BalancesDetails balancesDetails : balancesDetailsList){
                if(balancesDetails.getCurrency().toLowerCase().equals(coin.toLowerCase())) balance = balancesDetails.getAmount();
            }
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação coin balance do bitfinex",e.getErrors());
        }
        return balance;
    }

    @Override
    public List<Balance> getAllCoinBalances(String key, String secret) throws ExchangerServicesException {
        List<Balance> balances = new ArrayList<Balance>();
        try {
            List<BalancesDetails> balancesDetailsList = bitfinexApis.getAccountBalances(key, secret);
            for(BalancesDetails balancesDetails : balancesDetailsList){
                Balance balance = new Balance();
                balance.setBalance(balancesDetails.getAmount());
                balance.setCoin(balancesDetails.getCurrency());

                if (balance.getBalance()>0) balances.add(balance);
            }
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação coin balances do bitfinex",e.getErrors());
        }
        return balances;
    }

    @Override
    public List<Order> getOrderHistory(String key, String secret) throws ExchangerServicesException {
        List<Order> orders = new ArrayList<Order>();

        try {
            List<OrderDetails> orderDetailsList = bitfinexApis.getOrderHistory(key, secret);
            for(OrderDetails orderDetails : orderDetailsList){
                Order order = new Order();
                order.setPrice(orderDetails.getPrice());
                order.setQuantity(orderDetails.getOriginalAmount());
                order.setTimestamp(orderDetails.getTimestamp());
                order.setType(orderDetails.getType());
                order.setUuid(String.valueOf(orderDetails.getId()));
                order.setMarket(orderDetails.getSymbol());

                orders.add(order);
            }
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação order history do bitfinex",e.getErrors());
        }

        return orders;
    }

    @Override
    public List<Deposit> getDepositHistory(String coin,String key, String secret) throws ExchangerServicesException {
        List<Deposit> deposits = new ArrayList<Deposit>();
        //iterate through all coins
        try {
            coin = utils.correctCoin(Exchanger.BITFINEX,coin);
            List<MovementHistoryDetail> movementHistoryDetails = bitfinexApis.getMovementsHistory(coin,key,secret);
            for (MovementHistoryDetail movements : movementHistoryDetails) {
                if(movements.getType().equals("DEPOSIT")) {
                    Deposit deposit = new Deposit();
                    deposit.setAddress(movements.getAddress());
                    deposit.setAmount(movements.getAmount());
                    deposit.setCurrency(movements.getCurrency());
                    deposit.setTimestamp(movements.getTimestamp());
                    deposit.setTxCost(movements.getFee());
                    deposit.setTxId(movements.getTxid());
                    deposit.setUuid(String.valueOf(movements.getId()));

                    deposits.add(deposit);
                }
            }
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação deposit history do bitfinex",e.getErrors());
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação deposit history do bitfinex",e.getErrors());
        }

        return deposits;
    }

    @Override
    public List<Withdraw> getWithdrawHistory(String coin,String key, String secret) throws ExchangerServicesException {
        List<Withdraw> withdraws = new ArrayList<Withdraw>();
        //iterate through all coins
        try {
            coin = utils.correctCoin(Exchanger.BITFINEX,coin);
            List<MovementHistoryDetail> movementHistoryDetails = bitfinexApis.getMovementsHistory(coin,key,secret);
            for (MovementHistoryDetail movements : movementHistoryDetails) {
                if(movements.getType().equals("WITHDRAWAL")) {
                    Withdraw withdraw = new Withdraw();
                    withdraw.setAddress(movements.getAddress());
                    withdraw.setAmount(movements.getAmount());
                    withdraw.setCurrency(movements.getCurrency());
                    withdraw.setTimestamp(movements.getTimestamp());
                    withdraw.setTxCost(movements.getFee());
                    withdraw.setTxId(movements.getTxid());
                    withdraw.setUuid(String.valueOf(movements.getId()));

                    withdraws.add(withdraw);
                }
            }
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação withdraw history do bitfinex",e.getErrors());
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação withdraw history do bitfinex",e.getErrors());
        }
        return withdraws;
    }

    @Override
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, TimeFrame timeFrame) throws ExchangerServicesException {
        try{
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
            return historyTimeFrameFactory.getService(timeFrame).getMarketHistory(market,nBars,timeframeMultiplier,exchangeName);
        } catch (MarketHistoryFactoryException e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação history do bitfinex",e.getErrors());
        } catch (CryptocompareMarketHistoryException e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação history do bitfinex",e.getErrors());
        }
    }

    @Override
    public Order orderInfo(Market market,String uuid, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
           OrderDetails orderDetails = bitfinexApis.getOrder(Long.valueOf(uuid),key,secret);
           Order order = new Order();
           order.setMarket(orderDetails.getSymbol());
           order.setPrice(orderDetails.getPrice());
           order.setQuantity(orderDetails.getOriginalAmount());
           order.setRemaining(orderDetails.getRemainingAmount());
           order.setTimestamp(orderDetails.getTimestamp());
           order.setType(orderDetails.getType());
           order.setUuid(String.valueOf(orderDetails.getId()));
           order.setLive(orderDetails.isLive());
           order.setCosts( orderDetails.getOriginalAmount()*order.getPrice() );
           return order;
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementação order info do bitfinex",e.getErrors());
        }
    }

    @Override
    public List<Market> getMarkets() throws ExchangerServicesException {

        try {
            return marketsCache.getMarkets();
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao markets do bitfinex",e.getErrors());
        } catch (ExecutionException e) {
            throw new ExchangerServicesException("Erro na implementaçao markets do bitfinex (execution exception)");
        }
    }

    @Override
    public List<SimpleOrder> getOrderBook(Market market, String side) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
            return orderBookFactory.getService(side.toLowerCase()).getOrderBook(market);
        } catch (BitfinexOrderBookFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao orderbook do bitfinex",e.getErrors());
        } catch (BitfinexOrderBookException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao orderbook do bitfinex",e.getErrors());
        }
    }

    @Override
    public Ticker getTicker(Market market) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
            Ticker ticker = new Ticker();
            TickerData tickerData = bitfinexApis.getMarketTicker(market);
            ticker.setLast24Volume(tickerData.getVolume());
            ticker.setCurrentPrice(tickerData.getLastPrice());
            return ticker;
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao ticker do bitfinex",e.getErrors());
        }
    }

    @Override
    public TradeResult newDeal(Market market, DealType dealType, double quantity, double price, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
            return dealFactory.getService(Exchanger.BITFINEX,dealType).newDeal(Exchanger.BITFINEX,market, quantity, price, key, secret);
        } catch (DealFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao new deal do bitfinex (factory)",e.getErrors());
        } catch (DealException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao new deal do bitfinex",e.getErrors());
        }
    }

    @Override
    public double dealPrice(Market market, DealType dealType, double coinAmount) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BITFINEX,market);
            return dealPriceFactory.getService(dealType).dealPrice(market, Exchanger.BITFINEX,coinAmount);
        } catch (DealPriceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao dealprice (factory)",e.getErrors());
        } catch (DealPriceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao dealprice",e.getErrors());
        }
    }

    @Override
    public boolean orderCancel(String orderId,Market market, String key, String secret) throws ExchangerServicesException {
        try {
            return bitfinexApis.getOrderCancel(key,secret,Long.valueOf(orderId)).isCancelled();
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na implementaçao order cancel do bitfinex",e.getErrors());
        }
    }
}
