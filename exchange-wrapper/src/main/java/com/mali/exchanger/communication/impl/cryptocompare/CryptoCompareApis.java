package com.mali.exchanger.communication.impl.cryptocompare;

import com.mali.config.cryptocompare.ConfigCryptocompareApi;
import com.mali.entity.Market;
import com.mali.enumerations.Exchanger;
import com.mali.exchanger.communication.impl.cryptocompare.exceptions.CryptocompareApiException;
import com.mali.exchanger.communication.impl.cryptocompare.sub.entity.HistoryData;
import com.mali.validator.ServiceErrorItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoCompareApis {

    @Autowired
    ConfigCryptocompareApi configCryptocompareApi;

    public static final int CODE_ERROR_MARKET_NOT_NULL = 1000;
    public static final int CODE_ERROR_MARKET_INVALID = 1001;
    public static final int CODE_ERROR_EXCHANGE_NOT_NULL = 1002;
    public static final int CODE_ERROR_EXCHANGE_INVALID = 1003;
    public static final int CODE_ERROR_NBARS_INVALID = 1004;
    public static final int CODE_ERROR_MULTIPLIER_INVALID = 1005;
    public static final int CODE_ERROR_GENERIC = 9000;

    public HistoryData getMinuteHistory(String exchangeName, Market market, int nBars,int minuteMultiplier) throws CryptocompareApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(exchangeName.equals("") || exchangeName == null)
            errors.add(new ServiceErrorItem("Exchange não pode ser vazio",CODE_ERROR_EXCHANGE_NOT_NULL));
        if(Exchanger.valueOf(exchangeName.toUpperCase()) == null || Exchanger.valueOf(exchangeName.toUpperCase()).equals(""))
            errors.add(new ServiceErrorItem("Exchange inválido",CODE_ERROR_EXCHANGE_INVALID));
        if(market==null || market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(nBars==0 || nBars<0)
            errors.add(new ServiceErrorItem("nNars deve ser maior que zero",CODE_ERROR_NBARS_INVALID));
        if(minuteMultiplier==0 || minuteMultiplier<0)
            errors.add(new ServiceErrorItem("multiplier deve ser maior que zero",CODE_ERROR_MULTIPLIER_INVALID));
        if(!errors.isEmpty())
            throw new CryptocompareApiException("error on cryptocompare minute history api",errors);

        if(market.getBaseCoin()=="USDT") market.setBaseCoin("USD");
        if(market.getToCoin()=="USDT") market.setToCoin("USD");

        try {
            String url = configCryptocompareApi.getMinuteHistory()+nBars+"&fsym="+market.getToCoin()+"&tsym="+market.getBaseCoin()+"&e="+exchangeName+"&aggregate="+minuteMultiplier;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<HistoryData> response = restTemplate.getForEntity(url,HistoryData.class);
            if(response.getBody().getResponse().equals("Success")) {
                return (response.getBody());
            }else{
                errors.add(new ServiceErrorItem("Mercado inválido",CODE_ERROR_MARKET_INVALID));
                throw new CryptocompareApiException("Erro na api cryptocompare",errors);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api cryptocompare",CODE_ERROR_GENERIC));
            throw new CryptocompareApiException("Cryptocompare Api error",errors);
        }
    }

    public HistoryData getHourHistory(String exchangeName, Market market, int nBars,int hourMultiplier) throws CryptocompareApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(exchangeName.equals("") || exchangeName == null)
            errors.add(new ServiceErrorItem("Exchange não pode ser vazio",CODE_ERROR_EXCHANGE_NOT_NULL));
        if(Exchanger.valueOf(exchangeName.toUpperCase()) == null || Exchanger.valueOf(exchangeName.toUpperCase()).equals(""))
            errors.add(new ServiceErrorItem("Exchange inválido",CODE_ERROR_EXCHANGE_INVALID));
        if(market==null || market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(nBars==0 || nBars<0)
            errors.add(new ServiceErrorItem("nNars deve ser maior que zero",CODE_ERROR_NBARS_INVALID));
        if(hourMultiplier==0 || hourMultiplier<0)
            errors.add(new ServiceErrorItem("multiplier deve ser maior que zero",CODE_ERROR_MULTIPLIER_INVALID));
        if(!errors.isEmpty())
            throw new CryptocompareApiException("error on cryptocompare hour history api",errors);

        if(market.getBaseCoin()=="USDT") market.setBaseCoin("USD");
        if(market.getToCoin()=="USDT") market.setToCoin("USD");

        try {
            String url = configCryptocompareApi.getHourHistory()+nBars+"&fsym="+market.getToCoin()+"&tsym="+market.getBaseCoin()+"&e="+exchangeName+"&aggregate="+hourMultiplier;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<HistoryData> response = restTemplate.getForEntity(url,HistoryData.class);
            if(response.getBody().getResponse().equals("Success")) {
                return (response.getBody());
            }else{
                errors.add(new ServiceErrorItem("Mercado inválido",CODE_ERROR_MARKET_INVALID));
                throw new CryptocompareApiException("Erro na api cryptocompare",errors);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api cryptocompare",CODE_ERROR_GENERIC));
            throw new CryptocompareApiException("Cryptocompare Api error",errors);
        }
    }

    public HistoryData getDailyHistory(String exchangeName, Market market, int nBars,int dayMultiplier) throws CryptocompareApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(exchangeName.equals("") || exchangeName == null)
            errors.add(new ServiceErrorItem("Exchange não pode ser vazio",CODE_ERROR_EXCHANGE_NOT_NULL));
        if(Exchanger.valueOf(exchangeName.toUpperCase()) == null || Exchanger.valueOf(exchangeName.toUpperCase()).equals(""))
            errors.add(new ServiceErrorItem("Exchange inválido",CODE_ERROR_EXCHANGE_INVALID));
        if(market==null || market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(nBars==0 || nBars<0)
            errors.add(new ServiceErrorItem("nNars deve ser maior que zero",CODE_ERROR_NBARS_INVALID));
        if(dayMultiplier==0 || dayMultiplier<0)
            errors.add(new ServiceErrorItem("multiplier deve ser maior que zero",CODE_ERROR_MULTIPLIER_INVALID));
        if(!errors.isEmpty())
            throw new CryptocompareApiException("error on cryptocompare daily history api",errors);

        if(market.getBaseCoin()=="USDT") market.setBaseCoin("USD");
        if(market.getToCoin()=="USDT") market.setToCoin("USD");

        try {
            String url = configCryptocompareApi.getDayHistory()+nBars+"&fsym="+market.getToCoin()+"&tsym="+market.getBaseCoin()+"&e="+exchangeName+"&aggregate="+dayMultiplier;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<HistoryData> response = restTemplate.getForEntity(url,HistoryData.class);
            if(response.getBody().getResponse().equals("Success")) {
                return (response.getBody());
            }else{
                errors.add(new ServiceErrorItem("Mercado inválido",CODE_ERROR_MARKET_INVALID));
                throw new CryptocompareApiException("Erro na api cryptocompare",errors);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api cryptocompare",CODE_ERROR_GENERIC));
            throw new CryptocompareApiException("Cryptocompare Api error",errors);
        }
    }
}
