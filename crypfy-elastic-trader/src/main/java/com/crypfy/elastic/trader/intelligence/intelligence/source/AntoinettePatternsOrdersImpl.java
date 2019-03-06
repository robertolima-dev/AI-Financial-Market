package com.crypfy.elastic.trader.intelligence.intelligence.source;

import com.crypfy.elastic.trader.integrations.deep.clustering.ClusterServices;
import com.crypfy.elastic.trader.integrations.deep.clustering.exception.ClusterException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Ticker;
import com.crypfy.elastic.trader.integrations.mali.coin.data.CoinHistoryService;
import com.crypfy.elastic.trader.integrations.mali.coin.data.CoinService;
import com.crypfy.elastic.trader.integrations.mali.coin.data.exception.CoinException;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinHistoryJson;
import com.crypfy.elastic.trader.integrations.mali.coin.data.json.CoinJson;
import com.crypfy.elastic.trader.intelligence.IntelligenceOrdersOpportunities;
import com.crypfy.elastic.trader.intelligence.exceptions.OppSearcherFactoryException;
import com.crypfy.elastic.trader.intelligence.exceptions.OrderOppSaverException;
import com.crypfy.elastic.trader.intelligence.exceptions.OrderSearcherException;
import com.crypfy.elastic.trader.intelligence.factories.SetOrderExecutionFactory;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.entity.SubStrategy;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.OrderType;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.strategy.StrategyServices;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import com.crypfy.elastic.trader.trade.json.RequirementsResponse;
import com.crypfy.elastic.trader.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AntoinettePatternsOrdersImpl implements IntelligenceOrdersOpportunities {

    @Autowired
    private CoinHistoryService coinHistoryService;
    @Autowired
    private ClusterServices clusterServices;
    @Autowired
    private TradeServices tradeServices;
    @Autowired
    private StrategyServices strategyServices;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private SetOrderExecutionFactory setOrderFactory;
    @Autowired
    private ExchangerServices exchangerServices;
    @Autowired
    AsyncOrders asyncOrders;
    @Autowired
    DateUtils dateUtils;
    @Autowired
    CoinService coinService;

    @Override
    public List<Order> searchForOrders(int timeFrame, String name) throws OrderSearcherException {

        try {
            //check if there's new data
            String coin300 = coinService.findByMarketCapDesc(200).get(199).getIdcoin();
            Date lastCandleDate = coinHistoryService.historyByCoin(coin300,timeFrame,1).get(0).getDate();
            if (lastCandleDate.after(dateUtils.addMinuteFromDate(new Date(),-50))){
                return clusterServices.checkForPatterns(name);
            } else return new ArrayList<Order>();
        } catch (ClusterException e) {
            e.printStackTrace();
            throw new OrderSearcherException("Problema ao checar por padrões (antoinette)",e.getErrors(),e.getStatus());
        } catch (CoinException e) {
            e.printStackTrace();
            throw new OrderSearcherException("Problema ao checar por padrões (antoinette - history)",e.getErrors(),e.getStatus());
        }
    }

    /**
     *
     * @param strategy
     * @param subStrategy
     * @throws OrderOppSaverException
     */
    @Override
    public void saveOrderOpportunities(Strategy strategy, SubStrategy subStrategy) throws OrderOppSaverException {
        try {
            List<Order> opportunities = searchForOrders(subStrategy.getTimeFrame().getValue(),subStrategy.getName());
            strategy.getSubStrategies().get(strategy.getSubStrategies().indexOf(subStrategy)).setLastSearchUpdate(new Date());
            double totalBalance = tradeServices.exchangersTotalBalance(strategy.getExchangersDetails(), strategy.getBaseCurrency(),subStrategy.getBalanceWeight());
            if (opportunities.size() >= subStrategy.getMinimumSimultaneousOpp() ){
                for (Order order : opportunities) {
                    Market currentMarket = new Market(strategy.getBaseCurrency().toString(),order.getCallCoin());
                    List<ExchangerDetails> exchangersToTrade = tradeServices.exchangersWithMarketOrCoin(strategy.getExchangersDetails(),currentMarket);
                    for (ExchangerDetails exchanger : exchangersToTrade) {
                        asyncOrders.checkAndSaveOrdersByExchanger(order,strategy,subStrategy,totalBalance,exchanger,currentMarket,exchangersToTrade.size());
                    }
                }
            }
        } catch (TradeException e){
            throw new OrderOppSaverException("Erro ao tentar setar amount de negociacao!",e.getErrors(),e.getStatus());
        } catch (OppSearcherFactoryException e){
            throw new OrderOppSaverException("Erro ao buscar por implementação de set details!",e.getErrors(),e.getStatus());
        } catch (OrderSearcherException e){
            throw new OrderOppSaverException("Erro ao buscar por novas ordens!",e.getErrors(),e.getStatus());
        }
    }

}
