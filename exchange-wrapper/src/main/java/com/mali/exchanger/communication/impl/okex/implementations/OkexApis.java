package com.mali.exchanger.communication.impl.okex.implementations;

import com.mali.config.okex.ConfigOkexApi;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.okex.exceptions.OkexApiException;
import com.mali.exchanger.communication.impl.okex.sub.entity.*;
import com.mali.exchanger.communication.impl.okex.utils.OkexErrorCodeMsgs;
import com.mali.utils.StringUtils;
import com.mali.validator.ServiceErrorItem;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import org.json.simple.parser.JSONParser;

@Service
public class OkexApis {

    @Autowired
    ConfigOkexApi configOkexApi;
    @Autowired
    StringUtils stringUtils;
    @Autowired
    OkexErrorCodeMsgs codeMsgs;

    public static final int CODE_ERROR_MARKET_NOT_NULL = 1000;
    public static final int CODE_ERROR_MARKET_INVALID = 1001;
    public static final int CODE_ERROR_TYPE_INVALID = 1002;
    public static final int CODE_ERROR_KEY_NOT_NULL = 1003;
    public static final int CODE_ERROR_SECRET_NOT_NULL = 1004;
    public static final int CODE_ERROR_COIN_NOT_NULL = 1005;
    public static final int CODE_ERROR_COIN_INVALID = 1006;
    public static final int CODE_ERROR_UUID_NOT_NULL = 1007;
    public static final int CODE_ERROR_QUANTITY_NOT_NULL = 1008;
    public static final int CODE_ERROR_PRICE_NOT_NULL = 1009;
    public static final int CODE_ERROR_QUANTITY_NOT_NEGATIVE = 1010;
    public static final int CODE_ERROR_PRICE_NOT_NEGATIVE = 1011;
    public static final int CODE_ERROR_ADDRESS_NOT_NULL = 1012;
    public static final int CODE_ERROR_PASS_NOT_NULL = 1013;
    public static final int CODE_ERROR_WALLET_INVALID = 1014;
    public static final int CODE_ERROR_GENERIC = 9000;
    public static final int CODE_ERROR_OKEX_API = 9001;

    JSONParser parser = new JSONParser();

    public Markets getMarkets() throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Markets> response = restTemplate.exchange(configOkexApi.getMarkets(), HttpMethod.GET, entity, Markets.class);
            return  response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex (markets)",CODE_ERROR_GENERIC));
            throw new OkexApiException("Api error",errors);
        }

    }

    public List<OkexTrade> getTradeHistory(Market market) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex OkexTrade history api",errors);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String trueMarket = market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OkexTrade[]> response = restTemplate.exchange(configOkexApi.getMarketHistory()+"?symbol="+trueMarket, HttpMethod.GET, entity, OkexTrade[].class);
            return new ArrayList<OkexTrade>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex (Trade-history)",CODE_ERROR_GENERIC));
            throw new OkexApiException("Api error",errors);
        }
    }

    public OrderBooks getOrderBook(Market market) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex orderbook api",errors);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        String trueMarket = market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OrderBooks> response = restTemplate.exchange(configOkexApi.getOrderBook()+"?symbol="+trueMarket, HttpMethod.GET, entity, OrderBooks.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex (orderbook-history)",CODE_ERROR_GENERIC));
            throw new OkexApiException("Api error",errors);
        }

    }

    public JSONObject getBalances(String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex balances api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("api_key", key);
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.exchange(configOkexApi.getBalances(), HttpMethod.POST, entity, String.class);
            return (JSONObject) parser.parse(response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public TradeResult newBuyMarketOrder(double quantity, Market market, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex balances api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("price",String.valueOf(quantity));
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            params.add("type","buy_market");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TradeResult> response = restTemplate.exchange(configOkexApi.getNewOrder(), HttpMethod.POST, entity, TradeResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex buy api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public TradeResult newSellMarketOrder(double quantity, Market market,String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex balances api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("amount",String.valueOf(quantity));
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            params.add("type","sell_market");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TradeResult> response = restTemplate.exchange(configOkexApi.getNewOrder(), HttpMethod.POST, entity, TradeResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex sell api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public TradeResult newBuyOrder(double price,double quantity, Market market, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(price==0)
            errors.add(new ServiceErrorItem("price não pode ser zero",CODE_ERROR_PRICE_NOT_NULL));
        if(price<0)
            errors.add(new ServiceErrorItem("price não pode ser negativo",CODE_ERROR_PRICE_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex balances api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("price",String.valueOf(price));
            params.add("amount",String.valueOf(quantity));
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            params.add("type","buy");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TradeResult> response = restTemplate.exchange(configOkexApi.getNewOrder(), HttpMethod.POST, entity, TradeResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex buy api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public TradeResult newSellOrder(double price,double quantity, Market market,String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(quantity==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(quantity<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(price==0)
            errors.add(new ServiceErrorItem("price não pode ser zero",CODE_ERROR_PRICE_NOT_NULL));
        if(price<0)
            errors.add(new ServiceErrorItem("price não pode ser negativo",CODE_ERROR_PRICE_NOT_NEGATIVE));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex balances api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("price",String.valueOf(price));
            params.add("amount",String.valueOf(quantity));
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            params.add("type","sell");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TradeResult> response = restTemplate.exchange(configOkexApi.getNewOrder(), HttpMethod.POST, entity, TradeResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex sell api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public TradeResult getOrderCancel(String uuid,Market market,String key,String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(uuid.equals("") || uuid==null)
            errors.add(new ServiceErrorItem("id não pode ser vazio",CODE_ERROR_UUID_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex order cancel api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("order_id",uuid);
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TradeResult> response = restTemplate.exchange(configOkexApi.getOrderCancel(), HttpMethod.POST, entity, TradeResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex order cancel api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public OrderInfo getOrder(String uuid, Market market, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(uuid.equals("") || uuid==null)
            errors.add(new ServiceErrorItem("id não pode ser vazio",CODE_ERROR_UUID_NOT_NULL));
        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex get order api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("order_id",uuid);
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<OrderInfo> response = restTemplate.exchange(configOkexApi.getGetOrder(), HttpMethod.POST, entity, OrderInfo.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex get order api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }

    }

    public OrderHistory getOrderHistory(long status, int currentPage, Market market, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex order history api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("status",String.valueOf(status));
            params.add("page_length","200");
            params.add("current_page",String.valueOf(currentPage));
            params.add("symbol",market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase());
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<OrderHistory> response = restTemplate.exchange(configOkexApi.getOrderHistory(), HttpMethod.POST, entity, OrderHistory.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex get order history api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }
    }

    public Withdraw doWithdraw(String pass,String toAddress,double amount, String coin, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(amount==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(amount<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(toAddress.equals("") || toAddress==null)
            errors.add(new ServiceErrorItem("address não pode ser vazio",CODE_ERROR_ADDRESS_NOT_NULL));
        if(pass.equals("") || pass==null)
            errors.add(new ServiceErrorItem("pass não pode ser vazio",CODE_ERROR_PASS_NOT_NULL));
        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if( !(coin.toUpperCase().equals("BTC") || coin.toUpperCase().equals("LTC") || coin.toUpperCase().equals("ETH") || coin.toUpperCase().equals("ETC") || coin.toUpperCase().equals("BCH")) )
            errors.add(new ServiceErrorItem("Só podem ser sacados BTC,LTC,ETH,ETC,BCH",CODE_ERROR_MARKET_INVALID));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex withdraw api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("symbol",coin.toLowerCase()+"_cny");
            //params.add("charge_fee","0");
            params.add("trade_pwd",pass);
            params.add("withdraw_address",toAddress);
            params.add("withdraw_amount",String.valueOf(amount));
            params.add("target","okex");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<Withdraw> response = restTemplate.exchange(configOkexApi.getOrderHistory(), HttpMethod.POST, entity, Withdraw.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex get withdraw api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }
    }

    public Movements getDepositWithdrawHistory(int type, int currentPage, String coin, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if ( !(type==0 || type==1) )
            errors.add(new ServiceErrorItem("type so pode ser 0 (deposito) ou 1 (saque)",CODE_ERROR_TYPE_INVALID));
        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if(!(coin.toLowerCase().equals("btc") || coin.toLowerCase().equals("ltc")))
            errors.add(new ServiceErrorItem("coin não pode ser vazio",CODE_ERROR_COIN_INVALID));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex deposits history api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("type",String.valueOf(type));
            params.add("page_length","50");
            params.add("current_page",String.valueOf(currentPage));
            params.add("symbol",coin.toLowerCase()+"_usd");
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<Movements> response = restTemplate.exchange(configOkexApi.getDepositWithdrawHistory(), HttpMethod.POST, entity, Movements.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex get deposits history api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }
    }

    public TickerRes getTicker(Market market) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex OkexTrade history api",errors);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        String trueMarket = market.getToCoin().toLowerCase()+"_"+market.getBaseCoin().toLowerCase();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TickerRes> response = restTemplate.exchange(configOkexApi.getTicker()+"?symbol="+trueMarket, HttpMethod.GET, entity, TickerRes.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex (Trade-history)",CODE_ERROR_GENERIC));
            throw new OkexApiException("Api error",errors);
        }
    }

    public TransferResult doMoveCoin(String coin,double amount,int from,int to, String key, String secret) throws OkexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        if(amount==0)
            errors.add(new ServiceErrorItem("quantity não pode ser zero",CODE_ERROR_QUANTITY_NOT_NULL));
        if(amount<0)
            errors.add(new ServiceErrorItem("quantity não pode ser negativo",CODE_ERROR_QUANTITY_NOT_NEGATIVE));
        if(!(from == 1 || from == 3 || from == 6) || !(to == 1 || to == 3 || to == 6) || (from == to))
            errors.add(new ServiceErrorItem("wallets so podem ser 1,3 e 6",CODE_ERROR_WALLET_INVALID));
        if(coin.equals("") || coin==null)
            errors.add(new ServiceErrorItem("coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if( !(coin.toLowerCase().equals("btc") || coin.toLowerCase().equals("ltc")) )
            errors.add(new ServiceErrorItem("coin so pode ser btc ou ltc",CODE_ERROR_COIN_INVALID));
        if(key.equals("") || key==null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret==null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new OkexApiException("error on okex deposits history api",errors);

        try {
            //parameters
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.add("api_key", key);
            params.add("symbol",coin.toLowerCase()+"_usd");
            params.add("amount",String.valueOf(amount));
            params.add("from",String.valueOf(from));
            params.add("to",String.valueOf(to));
            String sign = stringUtils.buildMysignV1(params, secret);
            params.add("sign", sign);

            //response
            RestTemplate restTemplate = new RestTemplate();
            //add header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<TransferResult> response = restTemplate.exchange(configOkexApi.getTransfer(), HttpMethod.POST, entity, TransferResult.class);
            if (response.getBody().isResult()){
                return response.getBody();
            } else {
                errors.add(new ServiceErrorItem(codeMsgs.getCorrectMessage(response.getBody().getErrorCode()),CODE_ERROR_OKEX_API));
                throw new OkexApiException("Okex get deposits history api error",errors);
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api okex",CODE_ERROR_GENERIC));
            throw new OkexApiException("Okex Api error",errors);
        }
    }

}
