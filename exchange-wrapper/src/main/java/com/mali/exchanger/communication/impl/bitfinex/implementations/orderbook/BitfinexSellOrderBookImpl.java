package com.mali.exchanger.communication.impl.bitfinex.implementations.orderbook;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.bitfinex.BitfinexOrderBook;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexApiException;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexOrderBookException;
import com.mali.exchanger.communication.impl.bitfinex.implementations.BitfinexApis;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.OrderBookDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BitfinexSellOrderBookImpl implements BitfinexOrderBook {

    @Autowired
    private BitfinexApis bitfinexApis;

    @Override
    public List<SimpleOrder> getOrderBook(Market market) throws BitfinexOrderBookException {
        List<SimpleOrder> orders = new ArrayList<SimpleOrder>();
        String marketName = market.getToCoin().toLowerCase() + market.getBaseCoin().toLowerCase();
        //iterate and transform
        try {
            List<OrderBookDetails> orderBookDetailsList = bitfinexApis.getMarketBook(marketName).getAsks();
            for(OrderBookDetails orderBookDetails : orderBookDetailsList){
                SimpleOrder order = new SimpleOrder();
                order.setPrice(orderBookDetails.getPrice());
                order.setQuantity(orderBookDetails.getAmount());

                orders.add(order);
            }
        } catch (BitfinexApiException e) {
            e.printStackTrace();
            throw new BitfinexOrderBookException("Erro na implementaçao sell orderbook do bitfinex",e.getErrors());
        }
        return orders;
    }
}
