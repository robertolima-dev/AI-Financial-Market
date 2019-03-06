package com.crypfy.elastic.trader.persistance.enums;

import java.util.HashMap;
import java.util.Map;

public enum TimeFrame {
    PERIOD_5M(1),PERIOD_30M(6),PERIOD_1H(12),PERIOD_4H(48),PERIOD_1D(288),PERIOD_1W(2016);

    private int value;
    private static Map map = new HashMap<>();

    private TimeFrame(int value) {
        this.value = value;
    }

    static {
        for (TimeFrame timeFrame : TimeFrame.values()) {
            map.put(timeFrame.value, timeFrame);
        }
    }

    public static TimeFrame valueOf(int timeFrame) {
        return (TimeFrame) map.get(timeFrame);
    }

    public int getValue() {
        return value;
    }
}
