package com.crypfy.elastic.trader.persistance.enums;

public enum OrderStatus {
    WAITING_DIRECT_EXECUTION,
    WAITING_TWO_STEP_EXECUTION,
    WAITING_CLOSURE_CONDITIONS,
    PARTIALLY_FILLED,
    CLOSED,
    EXECUTION_PROBLEM,
    STALED
}
