package com.mali.exchanger.communication.impl.binance.implementations;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.OrderSide;
import com.binance.api.client.domain.OrderStatus;
import com.binance.api.client.domain.OrderType;
import com.binance.api.client.domain.account.AssetBalance;
import com.binance.api.client.domain.account.DepositAddress;
import com.binance.api.client.domain.account.DepositHistory;
import com.binance.api.client.domain.account.Trade;
import com.binance.api.client.domain.account.request.AllOrdersRequest;
import com.binance.api.client.domain.account.request.CancelOrderRequest;
import com.binance.api.client.domain.account.request.OrderStatusRequest;
import com.binance.api.client.domain.general.Asset;
import com.binance.api.client.domain.general.ExchangeInfo;
import com.binance.api.client.domain.general.SymbolFilter;
import com.binance.api.client.domain.general.SymbolInfo;
import com.binance.api.client.domain.market.TickerStatistics;
import com.mali.entity.*;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.api.ExchangerServices;
import com.mali.exchanger.communication.exceptions.*;
import com.mali.exchanger.communication.factories.DealFactory;
import com.mali.exchanger.communication.factories.DealPriceFactory;
import com.mali.exchanger.communication.factories.TotalBalanceFactory;
import com.mali.exchanger.communication.impl.binance.caches.BinanceFactoryCache;
import com.mali.exchanger.communication.impl.binance.caches.BinanceMarketDetailsCache;
import com.mali.exchanger.communication.impl.binance.caches.BinanceMarketsCache;
import com.mali.exchanger.communication.impl.binance.exceptions.BinanceFactoryException;
import com.mali.exchanger.communication.impl.binance.exceptions.BinanceOrderBookException;
import com.mali.exchanger.communication.impl.binance.factories.BinanceOrderBookFactory;
import com.mali.exchanger.communication.impl.binance.sub.entity.MarketDetails;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareMarketHistoryException;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.MarketHistoryFactoryException;
import com.mali.exchanger.communication.impl.cryptocompare.factories.MarketHistoryTimeFrameFactory;
import com.mali.utils.ExchangerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class BinanceExchangeServicesImpl implements ExchangerServices {

    @Autowired
    private MarketHistoryTimeFrameFactory historyTimeFrameFactory;
    @Autowired
    private DealPriceFactory dealPriceFactory;
    @Autowired
    private TotalBalanceFactory totalBalanceFactory;
    @Autowired
    private BinanceOrderBookFactory orderBookFactory;
    @Autowired
    private DealFactory dealFactory;
    @Autowired
    private ExchangerUtils utils;
    @Autowired
    private BinanceMarketsCache marketsCache;
    @Autowired
    private BinanceFactoryCache factoryCache;
    @Autowired
    private BinanceMarketDetailsCache binanceMarketDetailsCache;

    @Override
    public String getDepositAddress(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BINANCE,coin);
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            DepositAddress depositAddress = client.getDepositAddress(coin.toUpperCase());
            return depositAddress.getAddress();
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro ao coletar deposit address no binance");
        }
    }

    @Override
    public boolean doWithdraw(String password, String address, String coin, double quantity, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BINANCE,coin);
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            return client.withdraw(coin,address,String.valueOf(quantity),null,null).isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no withdraw do binance");
        }
    }

    @Override
    public double getCurrentTotalBalance(String key, String secret, String baseCurrency) throws ExchangerServicesException {
        try {
            baseCurrency = utils.correctCoin(Exchanger.BINANCE,baseCurrency);
            return totalBalanceFactory.getService(baseCurrency).getCurrentTotalBalance(Exchanger.BINANCE,key,secret);
        } catch (TotalBalanceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no total balance do binance (factory)");
        } catch (TotalBalanceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no total balance do binance");
        }
    }

    @Override
    public double getCoinBalance(String coin, String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BINANCE,coin);
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            return Double.valueOf(client.getAccount().getAssetBalance(coin.toUpperCase()).getFree());
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no coin balance do binance");
        }
    }

    @Override
    public List<Balance> getAllCoinBalances(String key, String secret) throws ExchangerServicesException {
        try {
            List<Balance> balances = new ArrayList<>();
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            List<AssetBalance> assets = client.getAccount().getBalances();
            for (AssetBalance assetBalance : assets){
                Balance balance = new Balance();
                balance.setCoin(assetBalance.getAsset());
                balance.setBalance(Double.valueOf(assetBalance.getFree())+Double.valueOf(assetBalance.getLocked()));
                if (balance.getBalance()>0) balances.add(balance);
            }
            return balances;
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no balances do binance");
        }
    }

    public DealType correctOrderType(Boolean isBuyer,boolean isMaker){
        if (isBuyer) {
            if (isMaker){
                return DealType.BUY;
            }else return DealType.BUY_MARKET;
        } else {
            if (isMaker){
                return DealType.SELL;
            } else return DealType.SELL_MARKET;
        }
    }

    @Override
    public List<Order> getOrderHistory(String key, String secret) throws ExchangerServicesException {
        try {
            List<Order> orders = new ArrayList<>();
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            List<Market> markets = getMarkets();
            for (Market market : markets){
                List<Trade> myTrades = client.getMyTrades(market.getToCoin()+utils.correctBaseCoin(Exchanger.BINANCE,market.getBaseCoin()));
                for (Trade trade : myTrades){
                    Order order = new Order();
                    order.setMarket(market.getMarketSymbol());
                    order.setCosts(Double.valueOf(trade.getQty())*Double.valueOf(trade.getPrice()));
                    order.setQuantity(Double.valueOf(trade.getQty()));
                    order.setPrice(Double.valueOf(trade.getPrice()));
                    order.setUuid(trade.getOrderId());
                    order.setType(correctOrderType(trade.isBuyer(),trade.isMaker()).toString());
                    order.setLive(false);
                    order.setTimestamp(String.valueOf(trade.getTime()));
                    orders.add(order);
                }
            }
            return orders;
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order history do binance");
        }
    }

    @Override
    public List<Deposit> getDepositHistory(String coin,String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BINANCE,coin);
            List<Deposit> deposits = new ArrayList<>();
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            List<com.binance.api.client.domain.account.Deposit> depositList = client.getDepositHistory(coin.toUpperCase()).getDepositList();
            for (com.binance.api.client.domain.account.Deposit depositBinance: depositList){
                Deposit deposit = new Deposit();
                deposit.setTxId(depositBinance.getTxId());
                deposit.setAmount(Double.valueOf(depositBinance.getAmount()));
                deposit.setTimestamp(depositBinance.getInsertTime());
                deposits.add(deposit);
            }
            return deposits;
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no deposit history do binance");
        }
    }

    @Override
    public List<Withdraw> getWithdrawHistory(String coin,String key, String secret) throws ExchangerServicesException {
        try {
            coin = utils.correctCoin(Exchanger.BINANCE,coin);
            List<Withdraw> withdraws = new ArrayList<>();
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            List<com.binance.api.client.domain.account.Withdraw> withdrawList = client.getWithdrawHistory(coin.toUpperCase()).getWithdrawList();
            for (com.binance.api.client.domain.account.Withdraw withdrawBinance: withdrawList){
                Withdraw withdraw = new Withdraw();
                withdraw.setTxId(withdrawBinance.getTxId());
                withdraw.setAmount(Double.valueOf(withdrawBinance.getAmount()));
                withdraw.setTimestamp(withdrawBinance.getApplyTime());
                withdraws.add(withdraw);
            }
            return withdraws;
        } catch (Exception e){
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no withdraw history do binance");
        }
    }

    @Override
    public CandleHistory getMarketHistory(Market market, int nBars, int timeframeMultiplier, TimeFrame timeFrame) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            return historyTimeFrameFactory.getService(timeFrame).getMarketHistory(market,nBars,timeframeMultiplier,"Binance");
        } catch (MarketHistoryFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no market history do binance (factory)");
        } catch (CryptocompareMarketHistoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no market history do binance");
        }
    }

    public DealType correctType(OrderType orderType, OrderSide side){

        if (side.equals(OrderSide.BUY)){
            if (orderType.equals(OrderType.LIMIT)){
                return DealType.BUY;
            } else return DealType.BUY_MARKET;
        } else {
            if (orderType.equals(OrderType.LIMIT)){
                return DealType.SELL;
            } else return DealType.SELL_MARKET;
        }

    }

    @Override
    public Order orderInfo(Market market, String uuid, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            com.binance.api.client.domain.account.Order binanceOrder = client.getOrderStatus(new OrderStatusRequest(market.getToCoin()+market.getBaseCoin(),uuid));
            List<Trade> trades = client.getMyTrades(market.getToCoin()+market.getBaseCoin());
            Order order = new Order();
            order.setLive( Double.valueOf(binanceOrder.getOrigQty())>Double.valueOf(binanceOrder.getExecutedQty() ) ? true : false);
            order.setType(correctType(binanceOrder.getType(),binanceOrder.getSide()).toString());
            order.setMarket(market.toString());
            order.setQuantity(Double.valueOf(Double.valueOf(binanceOrder.getOrigQty())));
            double orderPrice = 0;
            int nTrades = 0;
            for (Trade trade : trades){
                if (trade.getOrderId().equals(String.valueOf(binanceOrder.getOrderId()))){
                    order.setTimestamp(String.valueOf(trade.getTime()));
                    order.setUuid(trade.getOrderId());
                    nTrades++;
                    orderPrice += Double.valueOf(trade.getPrice());
                }
            }
            orderPrice = orderPrice/nTrades;
            order.setPrice(orderPrice);
            order.setCosts(Double.valueOf(binanceOrder.getExecutedQty())*orderPrice);
            order.setRemaining(Double.valueOf(binanceOrder.getOrigQty())-Double.valueOf(binanceOrder.getExecutedQty()));

            return order;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order info do binance");
        }
    }

    @Override
    public List<Market> getMarkets() throws ExchangerServicesException {
        try {
            return marketsCache.getMarkets();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no markets do binance");
        }
    }

    @Override
    public List<SimpleOrder> getOrderBook(Market market, String side) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            return orderBookFactory.getService(side).getOrderBook(market);
        } catch (BinanceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get order book do binance (factory)");
        } catch (BinanceOrderBookException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no get order book do binance");
        }
    }

    @Override
    public Ticker getTicker(Market market) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            BinanceApiRestClient client = factoryCache.getFactory("","").newRestClient();
            TickerStatistics tickerStatistics = client.get24HrPriceStatistics(market.getToCoin()+market.getBaseCoin());
            Ticker ticker = new Ticker();
            ticker.setLast24Volume( market.getBaseCoin().equals("BTC") ? Double.valueOf(tickerStatistics.getVolume())/Double.valueOf(client.get24HrPriceStatistics("BTCUSDT").getLastPrice()) : Double.valueOf(tickerStatistics.getVolume()));
            ticker.setCurrentPrice(Double.valueOf(tickerStatistics.getLastPrice()));
            return ticker;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no ticker do binance");
        }
    }

    @Override
    public TradeResult newDeal(Market market, DealType dealType, double quantity, double price, String key, String secret) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            return dealFactory.getService(Exchanger.BINANCE,dealType).newDeal(Exchanger.BINANCE,market,quantity,price,key,secret);
        } catch (DealFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no new deal do binance (factory)");
        } catch (DealException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no new deal do binance");
        }
    }

    @Override
    public double dealPrice(Market market, DealType dealType, double coinAmount) throws ExchangerServicesException {
        try {
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            return dealPriceFactory.getService(dealType).dealPrice(market, Exchanger.BINANCE,coinAmount);
        } catch (DealPriceException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no deal price do binance");
        } catch (DealPriceFactoryException e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no deal price do binance (factory)");
        }
    }

    @Override
    public boolean orderCancel(String orderId, Market market, String key, String secret) throws ExchangerServicesException {

        try {
            BinanceApiRestClient client = factoryCache.getFactory(key, secret).newRestClient();
            market = utils.correctMarketCurrencies(Exchanger.BINANCE,market);
            client.cancelOrder(new CancelOrderRequest(market.getToCoin()+market.getBaseCoin(),Long.valueOf(orderId)));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro no order cancel do binance");
        }
    }

    public MarketDetails getMarketDetails(String market) throws ExchangerServicesException {
        try {
            ExchangeInfo info = binanceMarketDetailsCache.getExchangerInfo();
            SymbolInfo symbolInfo = info.getSymbolInfo(market);
            List<SymbolFilter> filters = symbolInfo.getFilters();
            SymbolFilter priceFilter = filters.get(0);
            SymbolFilter quantityFilter = filters.get(1);
            MarketDetails marketDetails = new MarketDetails();
            //set this sh
            marketDetails.setMarket(market);
            marketDetails.setPriceMinSize(Double.valueOf(priceFilter.getMinPrice()));
            marketDetails.setPriceStep(Double.valueOf(priceFilter.getTickSize()));
            marketDetails.setQuantityMinSize(Double.valueOf(quantityFilter.getMinQty()));
            marketDetails.setQuantityStep(Double.valueOf(quantityFilter.getStepSize()));
            return marketDetails;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExchangerServicesException("Erro ao coletar market details no binance");
        }
    }
}
