package com.mali.entity;

public class Market {

    private String baseCoin;
    private String toCoin;
    private String marketSymbol;

    public Market(String marketName){
        this.baseCoin = marketName.substring(0,marketName.indexOf("-")).toUpperCase();
        this.toCoin = (marketName.substring(marketName.indexOf("-")+1)).toUpperCase();
    }

    public Market() {
    }

    public String getMarketSymbol(){
        return baseCoin+"-"+toCoin;
    }

    public String getBaseCoin() {
        return baseCoin;
    }

    public void setBaseCoin(String baseCoin) {
        this.baseCoin = baseCoin;
    }

    public String getToCoin() {
        return toCoin;
    }

    public void setToCoin(String toCoin) {
        this.toCoin = toCoin;
    }
}
