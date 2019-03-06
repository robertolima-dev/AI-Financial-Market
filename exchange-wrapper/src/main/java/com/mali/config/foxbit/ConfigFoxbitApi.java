package com.mali.config.foxbit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.services.foxbit-api")
public class ConfigFoxbitApi {

    private String marketHistory;
    private String orderBook;
    private String ticker;
    private String general;

    public String getMarketHistory() {
        return marketHistory;
    }

    public void setMarketHistory(String marketHistory) {
        this.marketHistory = marketHistory;
    }

    public String getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(String orderBook) {
        this.orderBook = orderBook;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getGeneral() {
        return general;
    }

    public void setGeneral(String general) {
        this.general = general;
    }
}
