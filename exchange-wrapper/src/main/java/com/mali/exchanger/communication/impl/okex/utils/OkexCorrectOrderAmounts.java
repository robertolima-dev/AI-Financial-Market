package com.mali.exchanger.communication.impl.okex.utils;

import com.mali.exchanger.communication.impl.okex.sub.entity.OrderDetails;
import org.springframework.stereotype.Service;

@Service
public class OkexCorrectOrderAmounts {

    public double correctQuantity(OrderDetails orderDetails) throws Exception {

        if (orderDetails.getType().equals("buy") || orderDetails.getType().equals("sell")) return orderDetails.getAmount();
        if (orderDetails.getType().equals("buy_market")) return orderDetails.getDealAmount();
        if (orderDetails.getType().equals("sell_market")) return orderDetails.getAmount();

        throw new Exception("Não encontrei o método somehow");
    }

    public double correctRemaining(OrderDetails orderDetails) throws Exception {

        if (orderDetails.getType().equals("buy") || orderDetails.getType().equals("sell")) return orderDetails.getAmount()-orderDetails.getDealAmount();
        if (orderDetails.getType().equals("buy_market")) return (orderDetails.getPrice()/orderDetails.getAvgPrice())-(orderDetails.getDealAmount());
        if (orderDetails.getType().equals("sell_market")) return orderDetails.getAmount()-orderDetails.getDealAmount();

        throw new Exception("Não encontrei o método somehow");

    }

}
