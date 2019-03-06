package com.mali.config.cryptocompare;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.services.cryptocompare-api")
public class ConfigCryptocompareApi {

    private String minuteHistory;
    private String hourHistory;
    private String dayHistory;
    private String ticker;

    public String getMinuteHistory() {
        return minuteHistory;
    }

    public void setMinuteHistory(String minuteHistory) {
        this.minuteHistory = minuteHistory;
    }

    public String getHourHistory() {
        return hourHistory;
    }

    public void setHourHistory(String hourHistory) {
        this.hourHistory = hourHistory;
    }

    public String getDayHistory() {
        return dayHistory;
    }

    public void setDayHistory(String dayHistory) {
        this.dayHistory = dayHistory;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
