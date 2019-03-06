package com.crypfy.elastic.trader.order.impl.manager;

import com.crypfy.elastic.trader.order.OrderManager;
import com.crypfy.elastic.trader.order.exceptions.OrderExecutionFactoryException;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.order.factories.OrderExecutionManagerFactory;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderManagerImpl implements OrderManager {

    @Autowired
    OrderExecutionManagerFactory managerFactory;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void manage() throws OrderManagerException {

        List<Order> ordersToCheck = orderRepository.findByOrderStatusNot(OrderStatus.CLOSED);

        for (Order order : ordersToCheck) {

            try {
                managerFactory.getImpl(order.getOrderStatus(), order.getOrderType()).manageOrder(order);
            } catch (OrderManagerException e) {
                e.printStackTrace();
                throw new OrderManagerException("Erro ao tentar controlar order",e.getErrors(),e.getStatus());
            } catch (OrderExecutionFactoryException e) {
                e.printStackTrace();
                throw new OrderManagerException("Erro ao solicitar implementação de order manager",e.getErrors(),e.getStatus());
            }
        }
    }

    @Override
    public List<String> distinctCallCoinsByStrategyNameAndSubStrategyNameAndOrderStatusNot(String strategyName, String subStrategyName) {
        OrderStatus status = OrderStatus.CLOSED;
        DBObject dbObject = (DBObject) JSON.parse("{strategyName:'"+strategyName+"',subStrategyName:'"+subStrategyName+"',orderStatus:{$ne:'"+status.toString()+"'}}");
        return mongoTemplate.getCollection("order").distinct("callCoin",dbObject);
    }
}
