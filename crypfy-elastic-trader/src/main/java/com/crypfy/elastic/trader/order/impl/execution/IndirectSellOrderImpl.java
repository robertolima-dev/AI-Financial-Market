package com.crypfy.elastic.trader.order.impl.execution;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.DealRequest;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.TradeResult;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.order.OrderExecutionManager;
import com.crypfy.elastic.trader.order.exceptions.ModeFactoryException;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import com.crypfy.elastic.trader.order.exceptions.OrderModeManagerException;
import com.crypfy.elastic.trader.order.exceptions.PartiallyFilledException;
import com.crypfy.elastic.trader.order.factories.OrderModeManagerFactory;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IndirectSellOrderImpl implements OrderExecutionManager {

    @Autowired
    TradeServices tradeServices;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MessageSender msgSender;
    @Autowired
    OrderModeManagerFactory modeManagerFactory;
    @Autowired
    CorrectBalance correctBalance;

    private double neededAmount;

    @Override
    public void manageOrder(Order order) throws OrderManagerException {

        DealRequest dealRequest = new DealRequest();
        TradeResult tradeResult = new TradeResult();

        try {
            neededAmount = correctBalance.correctSellBalance(order);
            dealRequest.setDealType(order.getOrderType().toString());
            dealRequest.setQuantity(neededAmount);
            dealRequest.setPrice(order.getCallPrice().doubleValue());
            //get trade result
            tradeResult = modeManagerFactory.getImpl(order.getMode()).manageOrderMode(order.getExchangerDetails(), tradeServices.marketFromOrder(order), dealRequest);
            msgSender.sendMsg("Estratégia "+order.getStrategyName()+": Nova ordem de venda de "+tradeResult.getVolumeToCoin()+" "+order.getToCoin()+" no exchanger: "+order.getExchanger().toString()+"");
            //partially_filled
            order.setOrderAmount(new BigDecimal(tradeResult.getVolumeBaseCoin()));
            if (neededAmount > tradeResult.getVolumeToCoin()*1.05) throw new PartiallyFilledException("A ordem de venda foi parcialmente executada");
            order.setOrderStatus(OrderStatus.WAITING_DIRECT_EXECUTION);
            order.setExchangerFirstStepSellOrderId(tradeResult.getUuid());
            order.setFirstStepClosePrice(new BigDecimal(tradeResult.getPrice()));
            order.setToCoin(order.getFromCoin());
            order.setFromCoin(order.getBaseCurrency().toString());
            order.setOrderAmount(new BigDecimal(tradeResult.getVolumeBaseCoin()));
            orderRepository.save(order);
        } catch (OrderModeManagerException e ){
            order.setOrderStatus(OrderStatus.EXECUTION_PROBLEM);
            orderRepository.save(order);
            throw new OrderManagerException("Problema ao abrir ordem de compra direta",e.getErrors(),e.getStatus());
        } catch (ModeFactoryException e ){
            order.setOrderStatus(OrderStatus.EXECUTION_PROBLEM);
            orderRepository.save(order);
            throw new OrderManagerException("Problema ao abrir ordem de compra direta",e.getErrors(),e.getStatus());
        } catch (NullPointerException e ){
            order.setOrderStatus(OrderStatus.EXECUTION_PROBLEM);
            orderRepository.save(order);
            throw new OrderManagerException("Problema ao abrir ordem de venda indireta (null pointer)");
        } catch (OrderManagerException e ){
            order.setOrderStatus(OrderStatus.EXECUTION_PROBLEM);
            orderRepository.save(order);
            throw new OrderManagerException("Problema ao abrir ordem de compra direta (saldo)",e.getErrors(),e.getStatus());
        } catch (PartiallyFilledException e) {
            order.setOrderStatus(OrderStatus.EXECUTION_PROBLEM);
            orderRepository.save(order);
            throw new OrderManagerException("Problema ao abrir ordem de venda direta (partially filled)");
        }

    }

}
