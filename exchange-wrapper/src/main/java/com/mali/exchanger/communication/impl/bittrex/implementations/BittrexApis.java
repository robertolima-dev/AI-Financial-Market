package com.mali.exchanger.communication.impl.bittrex.implementations;

import com.mali.config.bittrex.ConfigBittrexApi;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.bittrex.exceptions.BittrexApiException;
import com.mali.exchanger.communication.impl.bittrex.sub.entity.*;
import com.mali.exchanger.communication.impl.bittrex.utils.EncryptionUtility;
import com.mali.validator.ServiceErrorItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Component
public class BittrexApis {

    @Autowired
    private ConfigBittrexApi configBittrexApi;

    public static final int CODE_ERROR_MARKET_NOT_NULL = 1000;
    public static final int CODE_ERROR_MARKET_INVALID = 1001;
    public static final int CODE_ERROR_TYPE_NOT_NULL = 1002;
    public static final int CODE_ERROR_TYPE_INVALID = 1003;
    public static final int CODE_ERROR_KEY_NOT_NULL = 1004;
    public static final int CODE_ERROR_KEY_INVALID = 1005;
    public static final int CODE_ERROR_SECRET_INVALID = 1006;
    public static final int CODE_ERROR_SECRET_NOT_NULL = 1007;
    public static final int CODE_ERROR_COIN_NOT_NULL = 1008;
    public static final int CODE_ERROR_COIN_INVALID = 1009;
    public static final int CODE_ERROR_UUID_NOT_NULL = 1010;
    public static final int CODE_ERROR_UUID_INVALID = 1011;
    public static final int CODE_ERROR_QUANTITY_NOT_NULL = 1012;
    public static final int CODE_ERROR_PRICE_NOT_NULL = 1013;
    public static final int CODE_ERROR_QUANTITY_NOT_NEGATIVE = 1014;
    public static final int CODE_ERROR_PRICE_NOT_NEGATIVE = 1015;
    public static final int CODE_ERROR_QUANTITY_TOO_SMALL = 1016;
    public static final int CODE_ERROR_ADDRESS_NOT_NULL = 1017;
    public static final int CODE_ERROR_GENERIC = 9000;

    public Coins getMarketList() throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Coins> response = restTemplate.getForEntity(configBittrexApi.getMarkets(),Coins.class);
            return  response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Api error",errors);
        }

    }

    public MarketHistory getTradeHistory(String market) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex OkexTrade history api",errors);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MarketHistory> response = restTemplate.getForEntity(configBittrexApi.getMarketHistory()+market,MarketHistory.class);
            if(response.getBody().isSuccess()) {
                return (response.getBody());
            }else{
                errors.add(new ServiceErrorItem("Mercado inválido",CODE_ERROR_MARKET_INVALID));
                throw new BittrexApiException("error on bittrex history api",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public OrderBook getMarketBook(String market, String type) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(type.equals("") || type==null)
            errors.add(new ServiceErrorItem("Type não pode ser vazio",CODE_ERROR_TYPE_NOT_NULL));
        if( !(type.equals("buy") || type.equals("sell") || type.equals("both")) )
            errors.add(new ServiceErrorItem("Type deve ser buy,sell ou both",CODE_ERROR_TYPE_INVALID));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex market book api",errors);


        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OrderBook> response = restTemplate.getForEntity(configBittrexApi.getOrderBook()+market+"&type="+type,OrderBook.class);
            if(response.getBody().isSuccess()) {
                return (response.getBody());
            }else{
                errors.add(new ServiceErrorItem("Mercado inválido",CODE_ERROR_MARKET_INVALID));
                throw new BittrexApiException("error on bittrex market book api",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }


    public Balances getAccountBalances(String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex balances api",errors);

        try {
            //url
            String url = configBittrexApi.getBalances()+key+"&nonce="+ EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<Balances> response = restTemplate.exchange(url, HttpMethod.GET, entity, Balances.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //coin invalid
                if(response.getBody().getMessage().equals("INVALID_CURRENCY")){
                    errors.add(new ServiceErrorItem("Coin inválida",CODE_ERROR_COIN_INVALID));
                    throw new BittrexApiException("error on bittrex balances api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex balances api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex balances api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }

    }

    public BalanceData getCoinBalance(String coin, String key, String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex balance api",errors);

        try {
            //consumes
            String url = configBittrexApi.getBalance()+key+"&currency="+coin+"&nonce="+EncryptionUtility.generateNonce();
            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<BalanceData> response = restTemplate.exchange(url, HttpMethod.GET, entity, BalanceData.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //coin invalid
                if(response.getBody().getMessage().equals("INVALID_CURRENCY")){
                    errors.add(new ServiceErrorItem("Coin inválida",CODE_ERROR_COIN_INVALID));
                    throw new BittrexApiException("error on bittrex balance api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex balance api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex balance api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public OrderCancel getOrderCancel(String uuid,String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(uuid.equals("") || uuid==null)
            errors.add(new ServiceErrorItem("Uuid não pode ser vazio",CODE_ERROR_UUID_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex order cancel api",errors);

        try {
            //consumes
            String url = configBittrexApi.getOrderCancel()+key+"&uuid="+uuid+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<OrderCancel> response = restTemplate.exchange(url, HttpMethod.GET, entity, OrderCancel.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //uuid invalid
                if( !(response.getBody().getMessage().equals("APIKEY_INVALID") || response.getBody().getMessage().equals("ORDER_NOT_OPEN") || response.getBody().getMessage().equals("INVALID_SIGNATURE")) ){
                    errors.add(new ServiceErrorItem("Uuid inválida",CODE_ERROR_UUID_INVALID));
                    throw new BittrexApiException("error on bittrex order cancel api",errors);
                }
                //already closed
                if(response.getBody().getMessage().equals("ORDER_NOT_OPEN")){
                    errors.add(new ServiceErrorItem("ORDER_NOT_OPEN",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex order cancel api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex order cancel api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex order cancel api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Api error",errors);
        }
    }

    public OpenOrders getOpenOrders(Market market,String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.getMarketSymbol().equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex open orders api",errors);


        try {
            //consumes
            String url = configBittrexApi.getOpenedOrders()+key+"&market="+market.getBaseCoin()+"-"+market.getToCoin()+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<OpenOrders> response = restTemplate.exchange(url, HttpMethod.GET, entity, OpenOrders.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //market invalid
                if( response.getBody().getMessage().equals("INVALID_MARKET") ){
                    errors.add(new ServiceErrorItem("Uuid inválida",CODE_ERROR_MARKET_INVALID));
                    throw new BittrexApiException("error on bittrexopen orders api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex open orders api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex open orders api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public DealStatus marketSell(double quantity, double price, Market market, String key, String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(price==0)
            errors.add(new ServiceErrorItem("price não pode ser zero",CODE_ERROR_PRICE_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(price<0)
            errors.add(new ServiceErrorItem("price não pode ser negativo",CODE_ERROR_PRICE_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex sell api",errors);

        try {
            //consume
            String url = configBittrexApi.getSell()+key+"&market="+market.getBaseCoin()+"-"+market.getToCoin()+"&quantity="+quantity+"&rate="+price+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<DealStatus> response = restTemplate.exchange(url, HttpMethod.GET, entity, DealStatus.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //quantity invalid
                if( !response.getBody().getMessage().equals("INVALID_MARKET") && !response.getBody().getMessage().equals("APIKEY_INVALID") && !response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Quantitade muito pequena, OkexTrade minima de 50k satoshis",CODE_ERROR_QUANTITY_TOO_SMALL));
                    throw new BittrexApiException("error on bittrex sell api",errors);
                }
                //market invalid
                if( response.getBody().getMessage().equals("INVALID_MARKET") ){
                    errors.add(new ServiceErrorItem("Uuid inválida",CODE_ERROR_MARKET_INVALID));
                    throw new BittrexApiException("error on bittrex sell api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex sell api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex sell api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public DealStatus marketBuy(double quantity,double price,Market market,String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(price==0)
            errors.add(new ServiceErrorItem("price não pode ser zero",CODE_ERROR_PRICE_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(price<0)
            errors.add(new ServiceErrorItem("price não pode ser negativo",CODE_ERROR_PRICE_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex buy api",errors);

        try {
            //consume
            String url = configBittrexApi.getBuy()+key+"&market="+market.getBaseCoin()+"-"+market.getToCoin()+"&quantity="+quantity+"&rate="+price+"&nonce="+EncryptionUtility.generateNonce();
            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<DealStatus> response = restTemplate.exchange(url, HttpMethod.GET, entity, DealStatus.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //quantity invalid
                if( !response.getBody().getMessage().equals("INVALID_MARKET") && !response.getBody().getMessage().equals("APIKEY_INVALID") && !response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Quantitade muito pequena, OkexTrade minima de 50k satoshis",CODE_ERROR_QUANTITY_TOO_SMALL));
                    throw new BittrexApiException("error on bittrex buy api",errors);
                }
                //market invalid
                if( response.getBody().getMessage().equals("INVALID_MARKET") ){
                    errors.add(new ServiceErrorItem("Uuid inválida",CODE_ERROR_MARKET_INVALID));
                    throw new BittrexApiException("error on bittrex buy api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex buy api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex buy api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public DepositData getDepositAddress(String coin, String key, String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("Coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex deposit address api",errors);

        try {
            //consume
            String url = configBittrexApi.getDepositAddress()+key+"&currency="+coin+"&nonce="+EncryptionUtility.generateNonce();
            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<DepositData> response = restTemplate.exchange(url, HttpMethod.GET, entity, DepositData.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //coin invalid
                if( response.getBody().getMessage().equals("INVALID_CURRENCY") ){
                    errors.add(new ServiceErrorItem("coin inválida",CODE_ERROR_COIN_INVALID));
                    throw new BittrexApiException("error on bittrex deposit address api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex deposit address api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex deposit address api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public OrderHistory getOrderHistory(String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex order history api",errors);

        try {
            //consume
            String url = configBittrexApi.getOrderHistory()+key+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<OrderHistory> response = restTemplate.exchange(url, HttpMethod.GET, entity, OrderHistory.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex order history api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex order history api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public WithdrawHistory getWithdrawHistory(String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex withdraw history api",errors);

        try {
            //consume
            String url = configBittrexApi.getWithdrawHistory()+key+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<WithdrawHistory> response = restTemplate.exchange(url, HttpMethod.GET, entity, WithdrawHistory.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex withdraw history api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex withdraw history api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public DepositHistory getDepositHistory(String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex deposit history api",errors);

        try {
            //consume
            String url = configBittrexApi.getDepositHistory()+key+"&currency"+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<DepositHistory> response = restTemplate.exchange(url, HttpMethod.GET, entity, DepositHistory.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex deposit history api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex deposit history api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public WithdrawData getWithdraw(String address, String coin, double quantity,String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(address.equals("") || address==null)
            errors.add(new ServiceErrorItem("Address não pode ser vazio",CODE_ERROR_ADDRESS_NOT_NULL));
        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("Coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex withdraw api",errors);


        try {
            //consume
            String url = configBittrexApi.getWithdraw()+key+"&currency"+coin+"&quantity="+quantity+"&address"+address+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<WithdrawData> response = restTemplate.exchange(url, HttpMethod.GET, entity, WithdrawData.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //quantity invalid
                if( !response.getBody().getMessage().equals("INVALID_CURRENCY") && !response.getBody().getMessage().equals("APIKEY_INVALID") && !response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Quantitade muito pequena, verifique a quantidade minima para saque no bittrex",CODE_ERROR_QUANTITY_TOO_SMALL));
                    throw new BittrexApiException("error on bittrex withdraw api",errors);
                }
                //coin invalid
                if(response.getBody().getMessage().equals("INVALID_CURRENCY")){
                    errors.add(new ServiceErrorItem("Coin inválida",CODE_ERROR_COIN_INVALID));
                    throw new BittrexApiException("error on bittrex withdraw api",errors);
                }
                //key invalid
                if(response.getBody().getMessage().equals("APIKEY_INVALID")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                    throw new BittrexApiException("error on bittrex withdraw api",errors);
                }
                //secret invalid
                if(response.getBody().getMessage().equals("INVALID_SIGNATURE")){
                    errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_SECRET_INVALID));
                    throw new BittrexApiException("error on bittrex withdraw api",errors);
                }
                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public TickerData getMarketTicker(String market) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("Coin não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex ticker api",errors);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TickerData> response = restTemplate.getForEntity(configBittrexApi.getTicker()+market,TickerData.class);
            if(response.getBody().isSuccess()) {
                return response.getBody();
            }else{
                //throw a generic
                errors.add(new ServiceErrorItem(response.getBody().getMessage(),CODE_ERROR_GENERIC));
                throw new BittrexApiException("Bittrex ticker api error error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

    public OrderInfo getOrder(String uuid,String key,String secret) throws BittrexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(uuid.equals("") || uuid==null)
            errors.add(new ServiceErrorItem("uuid não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BittrexApiException("error on bittrex open orders api",errors);


        try {
            //consumes
            String url = configBittrexApi.getGetOrder()+uuid+"&apikey="+key+"&nonce="+EncryptionUtility.generateNonce();

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("apisign", EncryptionUtility.calculateHash(secret, url,"HmacSHA512"));
            //do it
            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            ResponseEntity<OrderInfo> response = restTemplate.exchange(url, HttpMethod.GET, entity, OrderInfo.class);
            if(response.getBody() != null) {
                return response.getBody();
            }else{

                //throw a generic
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
                throw new BittrexApiException("Api error",errors);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bittrex",CODE_ERROR_GENERIC));
            throw new BittrexApiException("Bitrrex Api error",errors);
        }
    }

}
