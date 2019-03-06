package com.crypfy.elastic.trader.schedulers;

import com.crypfy.elastic.trader.order.OrderManager;
import com.crypfy.elastic.trader.order.exceptions.OrderManagerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderManagerScheduler {

    @Autowired
    private OrderManager orderManager;

    @Scheduled(fixedDelay = 1000*30)
    public void manageOrders() {
        try {
            orderManager.manage();
        } catch (OrderManagerException e) {
            e.printStackTrace();
        }
    }
}
