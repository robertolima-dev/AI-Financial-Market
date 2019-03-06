package com.mali.exchanger.communication.impl.cryptocompare.factories;

import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.impl.cryptocompare.MarketHistoryByTimeFrame;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.MarketHistoryFactoryException;
import com.mali.exchanger.communication.impl.cryptocompare.timeFrame.impl.DailyHistoryImpl;
import com.mali.exchanger.communication.impl.cryptocompare.timeFrame.impl.OneHourHistoryImpl;
import com.mali.exchanger.communication.impl.cryptocompare.timeFrame.impl.OneMinuteHistoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketHistoryTimeFrameFactory {

    @Autowired
    OneMinuteHistoryImpl oneMinuteHistory;
    @Autowired
    OneHourHistoryImpl oneHourHistory;
    @Autowired
    DailyHistoryImpl dailyHistory;

    public MarketHistoryByTimeFrame getService(TimeFrame timeFrame) throws MarketHistoryFactoryException {

        if (timeFrame.equals(TimeFrame.ONE_MINUTE)) return oneMinuteHistory;
        if (timeFrame.equals(TimeFrame.ONE_HOUR)) return oneHourHistory;
        if (timeFrame.equals(TimeFrame.ONE_DAY)) return dailyHistory;

        throw new MarketHistoryFactoryException("Não foi encontrada implementação para esse timeframe. " +
                "Implementações atuais: ONE_MINUTE,ONE_HOUR e ONE_DAY");

    }

}
