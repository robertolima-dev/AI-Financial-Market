package com.mali.exchanger.communication.impl.bittrex.implementations.orderbook;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.exchanger.communication.impl.bittrex.BittrexOrderBook;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexApiException;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexOrderBookException;
import com.mali.exchanger.communication.impl.bittrex.implementations.BittrexApis;
import com.mali.exchanger.communication.impl.bittrex.sub.entity.BookDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BittrexSellOrderBookImpl implements BittrexOrderBook {

    @Autowired
    private BittrexApis apis;

    @Override
    public List<SimpleOrder> getBook(Market market) throws BittrexOrderBookException {
        List<SimpleOrder> orders = new ArrayList<SimpleOrder>();
        try {
            List<BookDetails> bookDetailsList = apis.getMarketBook(market.getMarketSymbol(),"sell").getResult();
            for(BookDetails bookDetails : bookDetailsList){
                SimpleOrder order = new SimpleOrder();
                order.setPrice(bookDetails.getRate());
                order.setQuantity(bookDetails.getQuantity());

                orders.add(order);
            }
        } catch (BittrexApiException e) {
            e.printStackTrace();
            throw new BittrexOrderBookException("Erro na implementaçao markets do bittrex",e.getErrors());
        }
        return orders;
    }
}
