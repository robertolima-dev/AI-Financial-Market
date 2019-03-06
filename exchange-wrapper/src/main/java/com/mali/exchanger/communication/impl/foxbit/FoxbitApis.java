package com.mali.exchanger.communication.impl.foxbit;

import com.mali.config.foxbit.ConfigFoxbitApi;
import com.mali.exchanger.communication.impl.bitfinex.utils.EncryptionUtil;
import com.mali.exchanger.communication.impl.foxbit.exceptions.FoxbitApiException;
import com.mali.exchanger.communication.impl.foxbit.sub.entity.*;
import com.mali.validator.ServiceErrorItem;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class FoxbitApis {

    @Autowired
    ConfigFoxbitApi configFoxbitApi;

    public static final int CODE_ERROR_KEY_NOT_NULL = 1000;
    public static final int CODE_ERROR_KEY_INVALID = 1001;
    public static final int CODE_ERROR_SECRET_NOT_NULL = 1002;
    public static final int CODE_ERROR_SECRET_INVALID = 1003;
    public static final int CODE_ERROR_ORDER_INVALID = 1004;
    public static final int CODE_ERROR_GENERIC = 9000;

    public List<String> getMarketList() {

        List<String> markets = new ArrayList<String>();
        markets.add("BTCBRL");

        return markets;
    }

    public List<MarketHistoryDetails> getTradeHistoty() throws FoxbitApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MarketHistoryDetails[]> response = restTemplate.getForEntity(configFoxbitApi.getMarketHistory(),MarketHistoryDetails[].class);
            return new ArrayList<MarketHistoryDetails>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }

    }

    public OrderBook getOrderBook() throws FoxbitApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OrderBook> response = restTemplate.getForEntity(configFoxbitApi.getOrderBook(),OrderBook.class);
            return (response.getBody());
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
    }

    public Balances getAccountBalances(String key, String secret) throws FoxbitApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new FoxbitApiException("error on foxbit balances api",errors);

        //set data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String  url = configFoxbitApi.getGeneral(),
                payload = String.valueOf(System.currentTimeMillis()),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA256");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("APIKey", key);
        headers.add("Nonce", payload);
        headers.add("Signature", signature);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //add body
        JSONObject jo = new JSONObject();
        try {
            jo.put("MsgType", "U2");
            jo.put("BalanceReqID", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
        HttpEntity<String> entity = new HttpEntity<String>(jo.toString(), headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<Balances> response = restTemplate.postForEntity(url, entity, Balances.class);
            if(response.getBody().getDescription().equals("OK")) {
                return response.getBody();
            }else{
                if(response.getBody().getDescription().equals("Invalid ApiKey")){
                    errors.add(new ServiceErrorItem("key ou secret inválida",CODE_ERROR_KEY_INVALID));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }else{
                    errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
    }

    public Order getOrderCancel(String key, String secret, long orderId) throws FoxbitApiException {
        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(orderId == 0 || orderId < 0)
            errors.add(new ServiceErrorItem("Order id não pode ser vazio, nem negativo",CODE_ERROR_ORDER_INVALID));
        if(!errors.isEmpty())
            throw new FoxbitApiException("error on foxbit order cancel api",errors);

        //set data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String  url = configFoxbitApi.getGeneral(),
                payload = String.valueOf(System.currentTimeMillis()),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA256");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("APIKey", key);
        headers.add("Nonce", payload);
        headers.add("Signature", signature);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //add body
        JSONObject jo = new JSONObject();
        try {
            jo.put("MsgType", "F");
            jo.put("ClOrdID", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
        HttpEntity<String> entity = new HttpEntity<String>(jo.toString(), headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<Order> response = restTemplate.postForEntity(url, entity, Order.class);
            if(response.getBody().getDescription().equals("OK")) {
                return response.getBody();
            }else{
                if(response.getBody().getDescription().equals("Invalid ApiKey")){
                    errors.add(new ServiceErrorItem("key ou secret inválida",CODE_ERROR_KEY_INVALID));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }else{
                    errors.add(new ServiceErrorItem("Order id inválido",CODE_ERROR_ORDER_INVALID));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
    }

    public OrderHistory getOrderHistory(String key, String secret) throws FoxbitApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new FoxbitApiException("error on foxbit open orders api",errors);

        //set data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String  url = configFoxbitApi.getGeneral(),
                payload = String.valueOf(System.currentTimeMillis()),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA256");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("APIKey", key);
        headers.add("Nonce", payload);
        headers.add("Signature", signature);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //add body
        JSONObject jo = new JSONObject();
        try {
            jo.put("MsgType", "U4");
            jo.put("OrdersReqID", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
        HttpEntity<String> entity = new HttpEntity<String>(jo.toString(), headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderHistory> response = restTemplate.postForEntity(url, entity, OrderHistory.class);
            if(response.getBody().getDescription().equals("OK")) {
                return response.getBody();
            }else{
                if(response.getBody().getDescription().equals("Invalid ApiKey")){
                    errors.add(new ServiceErrorItem("key ou secret inválida",CODE_ERROR_KEY_INVALID));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }else{
                    errors.add(new ServiceErrorItem("Erro ao acessar api foxbit",CODE_ERROR_GENERIC));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }

    }

    public Object getNewOrder(String key, String secret,String side, double price,double quaantity) throws FoxbitApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(side.equals("") || side == null || (side.equals("")) )
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new FoxbitApiException("error on foxbit open orders api",errors);

        //set data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String  url = configFoxbitApi.getGeneral(),
                payload = String.valueOf(System.currentTimeMillis()),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA256");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("APIKey", key);
        headers.add("Nonce", payload);
        headers.add("Signature", signature);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //add body
        JSONObject jo = new JSONObject();
        try {
            jo.put("MsgType", "U4");
            jo.put("OrdersReqID", 1);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }
        HttpEntity<String> entity = new HttpEntity<String>(jo.toString(), headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderHistory> response = restTemplate.postForEntity(url, entity, OrderHistory.class);
            if(response.getBody().getDescription().equals("OK")) {
                return response.getBody();
            }else{
                if(response.getBody().getDescription().equals("Invalid ApiKey")){
                    errors.add(new ServiceErrorItem("key ou secret inválida",CODE_ERROR_KEY_INVALID));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }else{
                    errors.add(new ServiceErrorItem("Erro ao acessar api foxbit",CODE_ERROR_GENERIC));
                    throw new FoxbitApiException("Foxbit Api error",errors);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api foxbit",CODE_ERROR_GENERIC));
            throw new FoxbitApiException("Foxbit Api error",errors);
        }

    }

}
