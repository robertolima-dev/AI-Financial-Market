package com.mali.exchanger.communication.factories;

import com.mali.enumerations.DealType;
import com.mali.exchanger.communication.api.DealPrice;
import com.mali.exchanger.communication.exceptions.DealPriceFactoryException;
import com.mali.exchanger.communication.impl.dealPrice.BuyDealPriceImpl;
import com.mali.exchanger.communication.impl.dealPrice.SellDealPriceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DealPriceFactory {

    @Autowired
    BuyDealPriceImpl buyDealPrice;
    @Autowired
    SellDealPriceImpl sellDealPrice;

    public DealPrice getService(DealType type) throws DealPriceFactoryException {

        if (type.equals(DealType.BUY) || type.equals(DealType.BUY_MARKET) || type.equals(DealType.BUY_MARKET_ENFORCED)) return buyDealPrice;
        if (type.equals(DealType.SELL) || type.equals(DealType.SELL_MARKET) || type.equals(DealType.SELL_MARKET_ENFORCED)) return sellDealPrice;

        throw new DealPriceFactoryException("Implementação não encontrada. " +
                "Implemetações atuais: buy,buy_market,buy_market_enforced,sell,sell_market e sell_market_enforced");
    }
}
