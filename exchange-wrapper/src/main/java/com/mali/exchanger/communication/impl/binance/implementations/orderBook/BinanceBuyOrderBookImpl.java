package com.mali.exchanger.communication.impl.binance.implementations.orderBook;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.market.OrderBookEntry;
import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.binance.BinanceOrderBook;
import com.mali.exchanger.communication.impl.binance.caches.BinanceFactoryCache;
import com.mali.exchanger.communication.impl.binance.exceptions.BinanceOrderBookException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinanceBuyOrderBookImpl implements BinanceOrderBook {

    @Autowired
    BinanceFactoryCache factoryCache;

    @Override
    public List<SimpleOrder> getOrderBook(Market market) throws BinanceOrderBookException {
        try {
            List<SimpleOrder> book = new ArrayList<>();
            BinanceApiRestClient client = factoryCache.getFactory("","").newRestClient();
            List<OrderBookEntry> entries = client.getOrderBook(market.getToCoin()+market.getBaseCoin(),500).getBids();
            for (OrderBookEntry bookEntry :  entries) {
                SimpleOrder order = new SimpleOrder();
                order.setQuantity(Double.valueOf(bookEntry.getQty()));
                order.setPrice(Double.valueOf(bookEntry.getPrice()));
                book.add(order);
            }
            return book;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BinanceOrderBookException("Problema no buy order book do binance!");
        }
    }
}
