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
public class SellDealPriceImpl implements DealPrice {

    @Autowired
    private ExchangerServicesFactory exchangerServicesFactory;

    @Override
    public double dealPrice(Market market, Exchanger exchanger, double coinAmount) throws DealPriceException {

        double price=0,amount=0,balance=coinAmount;
        List<Double> rates = new ArrayList<Double>(),amounts = new ArrayList<Double>();
        List<SimpleOrder> orders = null;
        try {
            orders = exchangerServicesFactory.getService(exchanger).getOrderBook(market,"buy");
        } catch (ExchangerServicesException e) {
            e.printStackTrace();
            throw new DealPriceException("Erro na implementaçao sell deal price",e.getErrors());
        } catch (ExchangerServicesFactoryException e) {
            e.printStackTrace();
        }
        boolean success = orders!= null;
        //iterate through order book
        if(success) {
            for (int i = 0; i < orders.size(); i++) {
                SimpleOrder order = orders.get(i);
                double quant = order.getQuantity();
                double rate = order.getPrice();
                if (balance < quant) {
                    rates.add(rate);
                    amounts.add(balance);
                    break;
                } else {
                    rates.add(rate);
                    amounts.add(quant);
                    balance = balance - quant;
                }
                if (i == orders.size() - 1 && balance > 0) {
                    System.out.println("O order-book não suporta essa ordem");
                    return 0;
                }
            }
            //do the math!!!
            if (rates.size() < 2) {
                if (rates.size() > 0) price = rates.get(0);
            } else {
                for (int i = 0; i < rates.size(); i++) {
                    price = price + rates.get(i) * amounts.get(i);
                    amount = amount + amounts.get(i);
                }
                price = price / amount;
            }
            return price;
        }else return 0;

    }
}
