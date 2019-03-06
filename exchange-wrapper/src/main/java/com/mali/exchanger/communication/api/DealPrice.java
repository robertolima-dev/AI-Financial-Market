package com.mali.exchanger.communication.api;

import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.exceptions.DealPriceException;

public interface DealPrice {

    public double dealPrice(Market market, Exchanger exchanger, double coinAmount) throws DealPriceException;

}
