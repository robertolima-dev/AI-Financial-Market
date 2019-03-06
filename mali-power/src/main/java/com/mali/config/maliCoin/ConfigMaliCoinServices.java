package com.mali.config.maliCoin;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "mali-coin.services")
public class ConfigMaliCoinServices {

    private String listCoinsService;
    private String listCoinsByMarketCapService;
    private String listCoinHistoryByIdcoinService;

    public String getListCoinsService() {
        return listCoinsService;
    }
    public String getListCoinsByMarketCapService() { return listCoinsByMarketCapService; }
    public String getListCoinHistoryByIdcoinService() { return listCoinHistoryByIdcoinService; }

    public void setListCoinsService(String listCoinsService) {
        this.listCoinsService = listCoinsService;
    }
    public void setListCoinsByMarketCapService(String listCoinsByMarketCapService) { this.listCoinsByMarketCapService = listCoinsByMarketCapService; }
    public void setListCoinHistoryByIdcoinService(String listCoinHistoryByIdcoinService) { this.listCoinHistoryByIdcoinService = listCoinHistoryByIdcoinService; }
}
