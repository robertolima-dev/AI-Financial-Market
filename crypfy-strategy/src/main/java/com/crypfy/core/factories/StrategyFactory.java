package com.crypfy.core.factories;

import com.crypfy.core.enumerations.Strategies;
import com.crypfy.core.strategies.api.Strategy;
import com.crypfy.core.strategies.cap.strategies.MarketCapStrategy;
import com.crypfy.core.strategies.vol.strategies.BetoVolStrategy;
import com.crypfy.core.strategies.vol.strategies.RangedVolStrategy;
import com.crypfy.core.strategies.vol.strategies.VolStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {

    @Autowired
    MarketCapStrategy marketCapStrategy;
    @Autowired
    VolStrategy volStrategy;
    @Autowired
    BetoVolStrategy betoVolStrategy;
    @Autowired
    RangedVolStrategy rangedVolStrategy;

    public Strategy getService(Strategies strategies){

        switch (strategies){
            case CAP:
                return  marketCapStrategy;
            case VOL:
                return volStrategy;
            case BETOVOL:
                return betoVolStrategy;
            case RVOL:
                return  rangedVolStrategy;
            default:
                return null;
        }
    }
}
