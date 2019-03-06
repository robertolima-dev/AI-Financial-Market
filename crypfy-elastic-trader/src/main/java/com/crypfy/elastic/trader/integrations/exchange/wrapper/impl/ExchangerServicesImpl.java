package com.crypfy.elastic.trader.integrations.exchange.wrapper.impl;

import com.crypfy.elastic.trader.integrations.ServiceItemError;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.*;
import com.crypfy.elastic.trader.messages.MessageSender;
import com.crypfy.elastic.trader.persistance.enums.BaseCurrency;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.rest.json.RestResponseJSON;
import com.crypfy.elastic.trader.rest.json.RestResponseListJSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangerServicesImpl implements ExchangerServices{

    @Value("${spring.services.exchanger-wrapper.markets}")
    private String getMarketsService;
    @Value("${spring.services.exchanger-wrapper.total-balance}")
    private String getTotalBalanceService;
    @Value("${spring.services.exchanger-wrapper.new-order}")
    private String newOrderService;
    @Value("${spring.services.exchanger-wrapper.cancel-order}")
    private String cancelOrderService;
    @Value("${spring.services.exchanger-wrapper.balances}")
    private String getBalancesService;
    @Value("${spring.services.exchanger-wrapper.deal-price}")
    private String getDealPriceService;
    @Value("${spring.services.exchanger-wrapper.balance}")
    private String getBalanceService;
    @Value("${spring.services.exchanger-wrapper.ticker}")
    private String getTickerService;

    @Autowired
    MessageSender msgSender;

    private ObjectMapper mapper = new ObjectMapper();
    private HttpStatus status;

    @Override
    public List<Market> getMarkets(Exchanger exchanger) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getMarketsService.replace("{exchangeName}", exchanger.toString());
        ResponseEntity<RestResponseListJSON<Market>> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url, HttpMethod.GET,null,new ParameterizedTypeReference<RestResponseListJSON<Market>>() {});

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseListJSON<Market> restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return (List<Market>) restResponseJSON.getResponse();
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

    @Override
    public BigDecimal getTotalBalance(Exchanger exchanger, BaseCurrency baseCurrency,String key,String secret) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getTotalBalanceService.replace("{exchangeName}", exchanger.toString());
        url = url.replace("{baseCurrency}",baseCurrency.toString());
        ResponseEntity<RestResponseJSON> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url+"?key="+key+"&secret="+secret, HttpMethod.GET,null,RestResponseJSON.class);


                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseJSON restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return new BigDecimal(String.valueOf(restResponseJSON.getResponse()));
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            e.printStackTrace();
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }

    }

    @Override
    public TradeResult newOrder(Exchanger exchanger, Market market, DealRequest dealRequest,String key,String secret) throws ExchangerException {

        //vars
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<DealRequest> request = new HttpEntity<>(dealRequest);
        String url = newOrderService.replace("{exchangeName}", exchanger.toString());
        url = url.replace("{marketName}",market.getMarketSymbol().toString())+"?key="+key+"&secret="+secret;
        ResponseEntity<RestResponseJSON> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response =  restTemplate.exchange(url, HttpMethod.POST,request,RestResponseJSON.class);

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseJSON restResponseJSON = response.getBody();

            if(restResponseJSON.isSuccess())
                return mapper.convertValue(restResponseJSON.getResponse(),TradeResult.class);
            else {
                List<ServiceItemError> errors = (List<ServiceItemError>) restResponseJSON.getResponse();
                throw new ExchangerException(restResponseJSON.getMessage(),errors,restResponseJSON.getStatus());
            }
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

    @Override
    public List<Balance> coinBalances(Exchanger exchanger,String key,String secret) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getBalancesService.replace("{exchangeName}", exchanger.toString());
        ResponseEntity<RestResponseListJSON<Balance>> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url+"?key="+key+"&secret="+secret, HttpMethod.POST,null,new ParameterizedTypeReference<RestResponseListJSON<Balance>>() {});

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseListJSON<Balance> restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return (List<Balance>) restResponseJSON.getResponse();
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

    @Override
    public BigDecimal getCoinBalance(Exchanger exchanger, String coin,String key,String secret) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getBalanceService.replace("{exchangeName}", exchanger.toString());
        ResponseEntity<RestResponseJSON> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url+coin+"?key="+key+"&secret="+secret, HttpMethod.GET,null,RestResponseJSON.class);

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseJSON restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return new BigDecimal(String.valueOf(restResponseJSON.getResponse()));
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

    @Override
    public BigDecimal dealPrice(Exchanger exchanger,Market market, String type, double amount) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getDealPriceService.replace("{exchangeName}", exchanger.toString());
        url = url.replace("{marketName}",market.getMarketSymbol());
        ResponseEntity<RestResponseJSON> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url+"?amount="+amount+"&type="+type, HttpMethod.GET,null,RestResponseJSON.class);

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseJSON restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return new BigDecimal(String.valueOf(restResponseJSON.getResponse()));
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

    @Override
    public Ticker getTicker(Exchanger exchanger, Market market) throws ExchangerException {
        //vars
        RestTemplate restTemplate = new RestTemplate();
        String url = getTickerService.replace("{exchangeName}", exchanger.toString());
        url = url.replace("{marketName}",market.getMarketSymbol());
        ResponseEntity<RestResponseJSON> response = null;
        boolean serverIsOk = false;
        int attempts = 0;

        try {
            while (!serverIsOk) {
                response = restTemplate.exchange(url, HttpMethod.GET,null,RestResponseJSON.class);

                //if execution not 200/201/20
                if (response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) serverIsOk = true;
                // give a break
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                }
                attempts++;
                if (attempts > 5)  throw new ExchangerException("Ocorreu um erro no servidor",500);
            }

            RestResponseJSON restResponseJSON = response.getBody();
            if(restResponseJSON.isSuccess())
                return mapper.convertValue(restResponseJSON.getResponse(),Ticker.class);
            else
                throw new ExchangerException(restResponseJSON.getMessage(),restResponseJSON.getStatus());
        } catch (Exception e){
            if (response == null ) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            if (response.getStatusCodeValue() == 503) msgSender.sendMsg("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
            throw new ExchangerException("Problemas de conexão com o projeto exchanger ("+exchanger.toString()+")");
        }
    }

}
