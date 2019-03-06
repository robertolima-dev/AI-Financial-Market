package com.crypfy.elastic.trader.order;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.DealRequest;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.TradeResult;
import com.crypfy.elastic.trader.order.exceptions.OrderModeManagerException;
import com.crypfy.elastic.trader.persistance.entity.ExchangerDetails;
import com.crypfy.elastic.trader.persistance.entity.Order;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;

public interface OrderModeManager {

    public TradeResult manageOrderMode(ExchangerDetails exchanger, Market market, DealRequest dealRequest) throws OrderModeManagerException;

}
