package com.crypfy.elastic.trader.integrations.exchange.wrapper.json;

public class Market {

    private String baseCoin;
    private String toCoin;
    private String marketSymbol;

    public Market(String baseCoin,String toCoin) {
        this.baseCoin = baseCoin;
        this.toCoin = toCoin;
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

