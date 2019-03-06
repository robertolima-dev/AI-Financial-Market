package com.crypfy.utils;

import com.crypfy.core.enumerations.StrategyPeriod;
import org.springframework.stereotype.Service;

@Service
public class PeriodConverter {

    public int periodToInt(StrategyPeriod period){

        switch (period){
            case WEEKLY:
                return 1;
            case BIWEEKLY:
                return 2;
            case MONTHLY:
                return 4;
            case BIMONTHLY:
                return 8;
            default:
                return 0;
        }
    }

}
