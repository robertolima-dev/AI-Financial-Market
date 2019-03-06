package com.mali.exchanger.communication.impl.okex.implementations;

import com.mali.entity.*;
import com.mali.entity.Ticker;
import com.mali.entity.TradeResult;
import com.mali.entity.Withdraw;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.api.ExchangerServices;
import com.mali.exchanger.communication.exceptions.*;
import com.mali.exchanger.communication.factories.DealFactory;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.factories.TotalBalanceFactory;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareMarketHistoryException;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.MarketHistoryFactoryException;
import com.mali.exchanger.communication.impl.cryptocompare.factories.MarketHistoryTimeFrameFactory;
import com.mali.exchanger.communication.impl.okex.caches.OkexMarketsCache;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexFactoryException;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexOrderBookException;
import com.mali.exchanger.communication.impl.okex.factories.OkexOrderBookFactory;
import com.mali.exchanger.communication.impl.okex.sub.entity.*;
import com.mali.exchanger.communication.impl.okex.utils.OkexCorrectOrderAmounts;
import com.mali.utils.ExchangerUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class OkexExchangerServicesImpl implements ExchangerServices {

    @Autowired
    private OkexApis apis;
    @Autowired
    private TotalBalanceFactory totalBalanceFactory;
    @Autowired
    private OkexOrderBookFactory orderBookFactory;
    @Autowired
    private OkexCorrectOrderAmounts correctOrderAmounts;
    @Autowired
    private MarketHistoryTimeFrameFactory historyTimeFrameFactory;
    @Autowired
    private DealPriceFactory dealPriceFactory;
    @Autowired
    private DealFactory dealFactory;
    @Autowired
    private ExchangerUtils utils;
    @Autowired
    private OkexMarketsCache marketsCache;

    public static final String exchangeName = "OKCoin";

    @Override
    public String getDepositAddress(String coin, String key, String secret) throws ExchangerServicesException {
        throw new ExchangerServicesException("Okex não possui api para consulta de endereçoes de depósito :'(");
    }

    @Override
    public boolean doWithdraw(String password,String address, String coin, double quantity, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.OKEX,coin);
            return apis.doWithdraw(password,address,quantity,coin,key,secret).isResult();
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro na api withdraw do okex",e.getErrors());
        }
    }

    @Override
    public double getCurrentTotalBalance(String key, String secret, String baseCurrency) throws ExchangerServicesException {
        try {
            baseCurrency = utils.correctCoin(Exchanger.OKEX,baseCurrency);
            return totalBalanceFactory.getService(baseCurrency).getCurrentTotalBalance(Exchanger.OKEX,key,secret);
        } catch (TotalBalanceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no total balance do okex (factory)");
        } catch (TotalBalanceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no total balance do okex");
        }
    }

    @Override
    public double getCoinBalance(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.OKEX,coin);
            List<Balance> balances = getAllCoinBalances(key, secret);
            for ( Balance balance : balances) if (balance.getCoin().equals(coin.toLowerCase())) return balance.getBalance();
            return 0;
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no balance do okex");
        }
    }

    @Override
    public List<Balance> getAllCoinBalances(String key, String secret) throws ExchangerServicesException {
        try {
            List<Balance> balances = new ArrayList<>();
            JSONObject info = (JSONObject) apis.getBalances(key, secret).get("info");
            JSONObject funds = (JSONObject) info.get("funds");
            JSONObject free = (JSONObject) funds.get("free");
            //iterate through both
            //free
            for(Iterator iterator = free.keySet().iterator(); iterator.hasNext();) {
                String coin = (String) iterator.next();
                double amount = Double.valueOf((String) free.get(coin));
                if (amount>0){
                    Balance balance = new Balance();
                    balance.setBalance(amount);
                    balance.setCoin(coin);
                    if (balance.getBalance()>0) balances.add(balance);
                }
            }
            return balances;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no balances do okex");
        }
    }

    @Override
    public List<Order> getOrderHistory(String key, String secret) throws ExchangerServicesException {
        try {
            List<Order> orders = new ArrayList<>();
            List<Market> markets = getMarkets();
            for (Market market : markets) {
                int currentPage = 1;
                boolean isDataComplete = false;
                while (isDataComplete) {
                    OrderHistory orderHistory = apis.getOrderHistory(2, currentPage,market,key,secret);
                    if (orderHistory.getTotal()<200) isDataComplete = true;
                    for (OrderDetails orderDetails : orderHistory.getOrders()) {
                        Order order = new Order();
                        order.setLive(false);
                        order.setMarket(orderDetails.getSymbol());
                        order.setCosts(orderDetails.getDealAmount()*orderDetails.getAvgPrice());
                        order.setPrice(orderDetails.getAvgPrice());
                        order.setQuantity(orderDetails.getDealAmount());
                        order.setRemaining(0);
                        order.setTimestamp(new Date((Long)orderDetails.getTimestamp()).toString());
                        order.setUuid(String.valueOf(orderDetails.getOrderId()));
                        order.setType(orderDetails.getType());
                        orders.add(order);
                    }
                    currentPage ++;
                }
            }
            return orders;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order history do okex");
        }
    }

    @Override
    public List<Deposit> getDepositHistory(String coin,String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.OKEX,coin);
            List<Deposit> deposits = new ArrayList<>();
            int currentPage = 1;
            boolean isDataCompleted = false;
            while(isDataCompleted){
                Movements movements = apis.getDepositWithdrawHistory(0,currentPage,coin,key,secret);
                if (movements.getRecords().size()<50) isDataCompleted = true;
                List<MovementsDetail> movementsDetails = movements.getRecords();
                for (MovementsDetail detail : movementsDetails){
                    Deposit deposit = new Deposit();
                    deposit.setAddress(detail.getAddr());
                    deposit.setAmount(detail.getAmount());
                    deposit.setCurrency(coin);
                    deposit.setTimestamp(String.valueOf(detail.getDate()));
                    deposit.setTxCost(detail.getFee());
                    deposits.add(deposit);
                }
                currentPage ++;
            }
            return deposits;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no deposit history do okex");
        }
    }

    @Override
    public List<Withdraw> getWithdrawHistory(String coin,String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.OKEX,coin);
            List<Withdraw> withdraws = new ArrayList<>();
            int currentPage = 1;
            boolean isDataCompleted = false;
            while(isDataCompleted){
                Movements movements = apis.getDepositWithdrawHistory(1,currentPage,coin,key,secret);
                if (movements.getRecords().size()<50) isDataCompleted = true;
                List<MovementsDetail> movementsDetails = movements.getRecords();
                for (MovementsDetail detail : movementsDetails){
                    Withdraw withdraw = new Withdraw();
                    withdraw.setAddress(detail.getAddr());
                    withdraw.setAmount(detail.getAmount());
                    withdraw.setCurrency(coin);
                    withdraw.setTimestamp(String.valueOf(detail.getDate()));
                    withdraw.setTxCost(detail.getFee());
                    withdraws.add(withdraw);
                }
                currentPage ++;
                }
            return withdraws;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no withdraw history do okex");
        }
    }

    @Override
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, TimeFrame timeFrame) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
            return historyTimeFrameFactory.getService(timeFrame).getMarketHistory(market,nBars,timeframeMultiplier,exchangeName);
        } catch (MarketHistoryFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no history do okex");
        } catch (CryptocompareMarketHistoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no history do okex");
        }
    }

    @Override
    public Order orderInfo(Market market,String uuid, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
            OrderInfo orderInfo = apis.getOrder(uuid,market,key,secret);
            Order order = new Order();
            OrderDetails orderDetails = orderInfo.getOrders().get(0);
            //set it
            order.setLive( (orderDetails.getStatus()== -1 || orderDetails.getStatus()==2) ? false : true );
            order.setMarket(orderDetails.getSymbol());
            order.setCosts(orderDetails.getDealAmount()*orderDetails.getAvgPrice());
            order.setPrice(orderDetails.getAvgPrice());
            order.setQuantity(correctOrderAmounts.correctQuantity(orderDetails)*0.998);
            order.setRemaining(order.isLive() ? correctOrderAmounts.correctRemaining(orderDetails) : 0);
            order.setTimestamp(new Date((Long)orderDetails.getTimestamp()).toString());
            order.setUuid(String.valueOf(orderDetails.getOrderId()));
            order.setType(orderDetails.getType());

            return order;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order info do okex");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order info do okex");
        }
    }

    @Override
    public List<Market> getMarkets() throws ExchangerServicesException {
        try {
            return marketsCache.getMarkets();
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro get markets do okex");
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro get markets do okex (cache)");
        }
    }

    @Override
    public List<SimpleOrder> getOrderBook(Market market, String side) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
            return orderBookFactory.getService(side.toLowerCase()).getOrderBook(market);
        } catch (OkexOrderBookException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get orderbook do okex");
        } catch (OkexFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get orderbook do okex(factory)");
        }
    }

    @Override
    public Ticker getTicker(Market market) throws ExchangerServicesException {
        market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
        try {
            TickerRes tickerRes = apis.getTicker(market);
            Ticker ticker = new Ticker();
            ticker.setCurrentPrice(tickerRes.getTicker().getLast());
            ticker.setLast24Volume(tickerRes.getTicker().getVol());
            return ticker;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get ticker do okex");
        }
    }

    @Override
    public TradeResult newDeal(Market market, DealType dealType, double quantity, double price, String key, String secret) throws ExchangerServicesException {
        market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
        try {
            return dealFactory.getService(Exchanger.OKEX,dealType).newDeal(Exchanger.OKEX,market, quantity, price, key, secret);
        } catch (DealFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get new deal do okex");
        } catch (DealException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public double dealPrice(Market market, DealType dealType, double coinAmount) throws ExchangerServicesException {
        market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
        try {
            return dealPriceFactory.getService(dealType).dealPrice(market, Exchanger.OKEX,coinAmount);
        } catch (DealPriceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get deal price do okex");
        } catch (DealPriceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get deal price do okex");
        }
    }

    @Override
    public boolean orderCancel(String orderId,Market market, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.OKEX,market);
            return apis.getOrderCancel(orderId,market,key,secret).isResult();
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order cancel do okex");
        }
    }
}
