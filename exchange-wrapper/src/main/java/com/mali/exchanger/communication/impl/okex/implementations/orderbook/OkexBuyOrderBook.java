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
import java.util.List;

@Service
public class OkexBuyOrderBook implements OkexOrderBook {

    @Autowired
    private OkexApis apis;

    @Override
    public List<SimpleOrder> getOrderBook(Market market) throws OkexOrderBookException {

        try {
            List<SimpleOrder> orders = new ArrayList<>();
            OrderBooks orderBooks = apis.getOrderBook(market);
            double[][] bids = orderBooks.getBids();
            for(double [] details : bids){
                SimpleOrder simpleOrder = new SimpleOrder();
                simpleOrder.setPrice(details[0]);
                simpleOrder.setQuantity(details[1]);
                //add it
                orders.add(simpleOrder);
            }
            return orders;
        } catch (OkexApiException e) {
            e.printStackTrace();
            throw new OkexOrderBookException("Erro no okex buy orderbook impl");
        }
    }
}
