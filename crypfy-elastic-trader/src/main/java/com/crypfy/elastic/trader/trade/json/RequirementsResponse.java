package com.crypfy.elastic.trader.trade.json;

public class RequirementsResponse {

    private int currentStratNOrders;
    private double availableBaseBalance;
    private boolean minimumConditionsMet;

    public int getCurrentStratNOrders() {
        return currentStratNOrders;
    }

    public void setCurrentStratNOrders(int currentStratNOrders) {
        this.currentStratNOrders = currentStratNOrders;
    }

    public double getAvailableBaseBalance() {
        return availableBaseBalance;
    }

    public void setAvailableBaseBalance(double availableBaseBalance) {
        this.availableBaseBalance = availableBaseBalance;
    }

    public boolean isMinimumConditionsMet() {
        return minimumConditionsMet;
    }

    public void setMinimumConditionsMet(boolean minimumConditionsMet) {
        this.minimumConditionsMet = minimumConditionsMet;
    }
}
