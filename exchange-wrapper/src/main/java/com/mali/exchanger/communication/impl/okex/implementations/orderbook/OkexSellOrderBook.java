package com.mali.exchanger.communication.impl.okex.implementations.orderbook;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.okex.OkexOrderBook;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexOrderBookException;
import com.mali.exchanger.communication.impl.okex.implementations.OkexApis;
import com.mali.exchanger.communication.impl.okex.sub.entity.OrderBooks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OkexSellOrderBook implements OkexOrderBook {

    @Autowired
    private OkexApis apis;

    @Override
    public List<SimpleOrder> getOrderBook(Market market) throws OkexOrderBookException {
        try {
            List<SimpleOrder> orders = new ArrayList<>();
            OrderBooks orderBooks = apis.getOrderBook(market);
            double [][] asks = orderBooks.getAsks();
            for(double [] details : asks){
                SimpleOrder simpleOrder = new SimpleOrder();
                simpleOrder.setPrice(details[0]);
                simpleOrder.setQuantity(details[1]);
                //add it
                orders.add(simpleOrder);
                Collections.reverse(orders);
            }
            return orders;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new OkexOrderBookException("Erro no okex sell orderbook impl");
        }
    }
}
