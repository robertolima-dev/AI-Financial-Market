package com.mali.exchanger.communication.impl.dealPrice;

import com.mali.entity.Market;
import com.mali.entity.SimpleOrder;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.api.DealPrice;
import com.mali.exchanger.communication.exceptions.DealPriceException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesException;
import com.mali.exchanger.communication.exceptions.ExchangerServicesFactoryException;
import com.mali.exchanger.communication.factories.ExchangerServicesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BuyDealPriceImpl implements DealPrice {

    @Autowired
    private ExchangerServicesFactory exchangerServicesFactory;

    @Override
    public double dealPrice(Market market, Exchanger exchanger, double coinAmount) throws DealPriceException {
        double buyPrice=0,buyAmount=0,buyBalance=coinAmount;
        List<Double> buyRates = new ArrayList<Double>(),buyAmounts = new ArrayList<Double>();
        List<SimpleOrder> buyOrders = null;
        try {
            buyOrders = exchangerServicesFactory.getService(exchanger).getOrderBook(market,"sell");
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new DealPriceException("Erro na implementaçao buy dealprice",e.getErrors());
        } catch (ExchangerServicesFactoryException e) {
            e.printStackTrace();
        }
        boolean buySuccess = buyOrders!= null;
        //iterate through the book
        if(buySuccess) {
            for (int i = 0; i < buyOrders.size(); i++) {
                SimpleOrder order = buyOrders.get(i);
                double quantity = order.getQuantity();
                double rate = order.getPrice();
                if (buyBalance < quantity*rate) {
                    buyRates.add(rate);
                    buyAmounts.add(buyBalance);
                    break;
                } else {
                    buyRates.add(rate);
                    buyAmounts.add(quantity*rate);
                    buyBalance = buyBalance - quantity * rate;
                }
                if (i == buyOrders.size() - 1 && buyBalance > 0) {
                    System.out.println("O order-book não suporta essa ordem");
                    return 0;
                }
            }
            //lets do the magic
            if (buyRates.size() < 2) {
                if (buyRates.size() > 0) buyPrice = buyRates.get(0);
            } else {
                for (int i = 0; i < buyRates.size(); i++) {
                    buyPrice += buyRates.get(i) * buyAmounts.get(i);
                    buyAmount += buyAmounts.get(i);
                }
                buyPrice = buyPrice / buyAmount;
            }
        } else return 0;
        return buyPrice;
    }
}
