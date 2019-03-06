package com.mali.exchanger.communication.api;

import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.exceptions.TotalBalanceException;

public interface TotalBalance {
    public double getCurrentTotalBalance(Exchanger exchanger,String key, String secret) throws TotalBalanceException;
}
