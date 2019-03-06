package com.crypfy.elastic.trader.persistance.repository;

import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order,String> {

    public List<Order> findByStrategyNameAndSubStrategyNameAndExchangerAndCallCoinAndOrderStatusNot(String strategyName, String subStrategyName,Exchanger exchanger, String callCoin, OrderStatus status);
    public List<Order> findByStrategyNameAndOrderStatus(String strategyName,OrderStatus status);
    public List<Order> findByStrategyNameAndOrderStatusNot(String strategyName,OrderStatus status);
    public List<Order> findByOrderStatusNot(OrderStatus status);
    public List<Order> findByExchangerAndStrategyNameAndSubStrategyNameAndCallCoinAndOrderStatusNot(Exchanger exchanger,String strategyName, String subStrategyName, String callCoin, OrderStatus status);
    public List<Order> findByExchangerAndFromCoin(Exchanger exchanger, String fromCoin);
}
