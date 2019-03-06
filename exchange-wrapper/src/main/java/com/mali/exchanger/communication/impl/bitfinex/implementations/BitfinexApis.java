package com.mali.exchanger.communication.impl.bitfinex.implementations;

import com.mali.config.bitfinex.ConfigBitfinexApi;
import com.mali.entity.Market;
import com.mali.exchanger.communication.impl.bitfinex.exceptions.BitfinexApiException;
import com.mali.exchanger.communication.impl.bitfinex.sub.entity.*;
import com.mali.exchanger.communication.impl.bitfinex.utils.EncryptionUtil;
import com.mali.exchanger.communication.impl.bitfinex.utils.StringConverter;
import com.mali.validator.ServiceErrorItem;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class BitfinexApis {

    @Autowired
    ConfigBitfinexApi configBitfinexApi;

    public static final int CODE_ERROR_MARKET_NOT_NULL = 1000;
    public static final int CODE_ERROR_MARKET_INVALID = 1001;
    public static final int CODE_ERROR_KEY_NOT_NULL = 1002;
    public static final int CODE_ERROR_KEY_INVALID = 1003;
    public static final int CODE_ERROR_SECRET_NOT_NULL = 1004;
    public static final int CODE_ERROR_SECRET_INVALID = 1005;
    public static final int CODE_ERROR_ORDER_ID_INVALID = 1006;
    public static final int CODE_ERROR_PRICE_INVALID = 1007;
    public static final int CODE_ERROR_QUANTITY_INVALID = 1008;
    public static final int CODE_ERROR_TYPE_NOT_NULL = 1009;
    public static final int CODE_ERROR_TYPE_INVALID = 1010;
    public static final int CODE_ERROR_COIN_NOT_NULL = 1011;
    public static final int CODE_ERROR_COIN_INVALID = 1012;
    public static final int CODE_ERROR_ADDRESS_NOT_NULL = 1012;
    public static final int CODE_ERROR_GENERIC = 9000;

    public List<String> getMarketList() throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String[]> response = restTemplate.getForEntity(configBitfinexApi.getMarkets(),String[].class);
            return new ArrayList<String>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex",CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error",errors);
        }
    }

    public List<TradeDetails> getMarketHistory(String market) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex market history api",errors);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TradeDetails[]> response = restTemplate.getForEntity(configBitfinexApi.getMarketHistory()+market,TradeDetails[].class);
            return new ArrayList<TradeDetails>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            //what cause this
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MessageResponse> response = restTemplate.getForEntity(configBitfinexApi.getMarketHistory()+market,MessageResponse.class);
            if(response.getBody().getMessage().equals("Unknown symbol")){
                errors.add(new ServiceErrorItem("Market inválido",CODE_ERROR_MARKET_INVALID));
                throw new BitfinexApiException("error on bitfinex market history api",errors);
            }
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex",CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error",errors);
        }
    }

    public OrderBook getMarketBook(String market) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(market.equals("") || market == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex market book api",errors);


        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<OrderBook> response = restTemplate.getForEntity(configBitfinexApi.getOrderBook()+market,OrderBook.class);
            return (response.getBody());
        } catch (Exception e) {
            //what cause this
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MessageResponse> response = restTemplate.getForEntity(configBitfinexApi.getOrderBook()+market+"&limit_bids=500&limit_asks=500",MessageResponse.class);
            if(response.getBody().getMessage().equals("Unknown symbol")){
                errors.add(new ServiceErrorItem("Market inválido",CODE_ERROR_MARKET_INVALID));
                throw new BitfinexApiException("error on bitfinex market book api",errors);
            }
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex",CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error",errors);
        }
    }

    public List<BalancesDetails> getAccountBalances(String key, String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex balances api",errors);

        //set data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getBalances(),
                url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payload(urlPath),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<BalancesDetails[]> response = restTemplate.postForEntity(url, entity, BalancesDetails[].class);
            return new ArrayList<BalancesDetails>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            //what cause this
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
            //invalid key
            if(response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")){
                errors.add(new ServiceErrorItem("Key inválida",CODE_ERROR_KEY_INVALID));
                throw new BitfinexApiException("error on bitfinex balances api",errors);
            }
            //invalid secret
            if(response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")){
                errors.add(new ServiceErrorItem("Secret inválido",CODE_ERROR_SECRET_INVALID));
                throw new BitfinexApiException("error on bitfinex balances api",errors);
            }
            //else
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex",CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error",errors);
        }
    }

    public OrderStatus getOrderCancel(String key,String secret,long orderId) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(orderId<0 || orderId == 0)
            errors.add(new ServiceErrorItem("Orderid tem que ser maior que zero",CODE_ERROR_ORDER_ID_INVALID));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex order cancel api",errors);

        //set the needed data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getOrderCancel();

        JSONObject jo = new JSONObject();
        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("id", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex",CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error",errors);
        }

        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");

        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderStatus> response = restTemplate.postForEntity(url, entity, OrderStatus.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex order cancel api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex order cancel api", errors);
                }
                //here it can only be
                errors.add(new ServiceErrorItem("Order id inválido", CODE_ERROR_ORDER_ID_INVALID));
                throw new BitfinexApiException("error on bitfinex order cancel api", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public List<OrderDetails> getOpenOrders(String key, String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex open orders api",errors);

        //set up the needed data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getOpenedOrders(),
                url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payload(urlPath),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderDetails[]> response = restTemplate.postForEntity(url, entity, OrderDetails[].class);
            return new ArrayList<OrderDetails>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex open orders api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex open orders api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public List<OrderDetails> getOrderHistory(String key, String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex order history api",errors);

        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getOrderHistory(),
                url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payload(urlPath),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderDetails[]> response = restTemplate.postForEntity(url, entity, OrderDetails[].class);
            return new ArrayList<OrderDetails>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex order history api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex order history api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public OrderDetails getNewLimitOrder(double quantity, double price, Market market,String type, String key,String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(quantity<0 || quantity==0)
            errors.add(new ServiceErrorItem("A quantidade deve ser maior que zero",CODE_ERROR_QUANTITY_INVALID));
        if(price<0 || price==0)
            errors.add(new ServiceErrorItem("A price deve ser maior que zero",CODE_ERROR_PRICE_INVALID));
        if(market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Mercado não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(type.equals("") || type == null)
            errors.add(new ServiceErrorItem("Type não pode ser vazio",CODE_ERROR_TYPE_NOT_NULL));
        if( !(type.equals("buy") || type.equals("sell")) )
            errors.add(new ServiceErrorItem("Type inválido",CODE_ERROR_TYPE_INVALID));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex new order api",errors);

        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getNewOrder();
        String symbol = market.getToCoin().toLowerCase()+market.getBaseCoin().toLowerCase();
        JSONObject jo = new JSONObject();

        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("symbol", symbol.toUpperCase());
            jo.put("amount", String.valueOf(quantity));
            jo.put("price", price);
            jo.put("exchange", "bitfinex");
            jo.put("side", type);
            jo.put("type", "exchange limit");
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error", errors);
        }
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderDetails> response = restTemplate.postForEntity(url, entity, OrderDetails.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid market
                if (response.getBody().getMessage().equals("Unknown symbol")) {
                    errors.add(new ServiceErrorItem("mercado inválido", CODE_ERROR_MARKET_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Quantidade muito pequena ou excede o saldo", CODE_ERROR_QUANTITY_INVALID));
                throw new BitfinexApiException("error on bitfinex new order api", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    //market order
    public OrderDetails getNewMarketOrder(double quantity, Market market,String type, String key,String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(quantity<0 || quantity==0)
            errors.add(new ServiceErrorItem("A quantidade deve ser maior que zero",CODE_ERROR_QUANTITY_INVALID));
        if(market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Mercado não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(type.equals("") || type == null)
            errors.add(new ServiceErrorItem("Type não pode ser vazio",CODE_ERROR_TYPE_NOT_NULL));
        if( !(type.equals("buy") || type.equals("sell")) )
            errors.add(new ServiceErrorItem("Type inválido",CODE_ERROR_TYPE_INVALID));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex new order api",errors);

        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getNewOrder();
        String symbol = market.getToCoin().toLowerCase()+market.getBaseCoin().toLowerCase();
        JSONObject jo = new JSONObject();

        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("symbol", symbol.toUpperCase());
            jo.put("amount", String.valueOf(quantity));
            jo.put("price", String.valueOf(1));
            jo.put("exchange", "bitfinex");
            jo.put("side", type);
            jo.put("type", "exchange market");
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error", errors);
        }
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderDetails> response = restTemplate.postForEntity(url, entity, OrderDetails.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid market
                if (response.getBody().getMessage().equals("Unknown symbol")) {
                    errors.add(new ServiceErrorItem("mercado inválido", CODE_ERROR_MARKET_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex new order api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Quantidade muito pequena ou excede o saldo", CODE_ERROR_QUANTITY_INVALID));
                throw new BitfinexApiException("error on bitfinex new order api", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public DepositDetails getDepositAddress(String coin,String key,String secret) throws BitfinexApiException {

        String urlPath = configBitfinexApi.getDepositAddress(), method = StringConverter.coinSymbolToMethod(coin);

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(method == null || method.equals(""))
            errors.add(new ServiceErrorItem("Coin inválida, ou não é aceita pelo bitfinex",CODE_ERROR_COIN_INVALID));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex deposit api",errors);

        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        JSONObject jo = new JSONObject();
        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("method", method);
            jo.put("wallet_name", "exchange");
            jo.put("renew", 0);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error", errors);
        }
        //url
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<DepositDetails> response = restTemplate.postForEntity(url, entity, DepositDetails.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex deposit api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex deposit api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public List<MovementHistoryDetail> getMovementsHistory(String coin,String key,String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(coin == null || coin.equals(""))
            errors.add(new ServiceErrorItem("Coin não pode ser vazio",CODE_ERROR_COIN_NOT_NULL));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex deposit api",errors);

        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        String urlPath = configBitfinexApi.getMovementsHistory();
        JSONObject jo = new JSONObject();
        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("currency", coin);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error", errors);
        }
        //url
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<MovementHistoryDetail[]> response = restTemplate.postForEntity(url, entity, MovementHistoryDetail[].class);
            return new ArrayList<MovementHistoryDetail>(Arrays.asList(response.getBody()));
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex movements api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex movements api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Coin inválida", CODE_ERROR_COIN_INVALID));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public WithdrawDetails getWithdraw(String address, String coin, double quantity, String key, String secret) throws BitfinexApiException {

        String urlPath = configBitfinexApi.getWithdraw(), withdrawType = StringConverter.coinSymbolToWithdrawType(coin);
        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(withdrawType == null || withdrawType.equals(""))
            errors.add(new ServiceErrorItem("Coin inválida, ou não serve como opção de saque no bitfinex",CODE_ERROR_COIN_INVALID));
        if(quantity<0 || quantity==0)
            errors.add(new ServiceErrorItem("Quantidade deve ser maior que zero",CODE_ERROR_QUANTITY_INVALID));
        if(address == null || address.equals(""))
            errors.add(new ServiceErrorItem("Address não pode ser vazio",CODE_ERROR_ADDRESS_NOT_NULL));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex deposit api",errors);
        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        //url
        JSONObject jo = new JSONObject();
        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("withdraw_type", withdrawType);
            jo.put("walletselected", "exchange");
            jo.put("amount", quantity);
            jo.put("walletto", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<WithdrawDetails> response = restTemplate.postForEntity(url, entity, WithdrawDetails.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                //invalid key
                if (response.getBody().getMessage().equals("Could not find a key matching the given X-BFX-APIKEY.")) {
                    errors.add(new ServiceErrorItem("Key inválida", CODE_ERROR_KEY_INVALID));
                    throw new BitfinexApiException("error on bitfinex movements api", errors);
                }
                //invalid secret
                if (response.getBody().getMessage().equals("Invalid X-BFX-SIGNATURE.")) {
                    errors.add(new ServiceErrorItem("Secret inválido", CODE_ERROR_SECRET_INVALID));
                    throw new BitfinexApiException("error on bitfinex movements api", errors);
                }
                //else
                errors.add(new ServiceErrorItem("Quantidade muita pequena ou inválida", CODE_ERROR_QUANTITY_INVALID));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public TickerData getMarketTicker(Market market) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(market.getMarketSymbol().equals("") || market.getMarketSymbol() == null)
            errors.add(new ServiceErrorItem("Market não pode ser vazio",CODE_ERROR_MARKET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex ticker api",errors);
        String symbol = market.getToCoin().toLowerCase()+market.getBaseCoin().toLowerCase();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<TickerData> response = restTemplate.getForEntity(configBitfinexApi.getTicker()+"/"+symbol,TickerData.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.getForEntity(configBitfinexApi.getTicker()+"/"+symbol,MessageResponse.class);
                //here it can only be
                errors.add(new ServiceErrorItem("Mercado inválido", CODE_ERROR_MARKET_INVALID));
                throw new BitfinexApiException("error on bitfinex ticker api", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

    public OrderDetails getOrder(long id, String key,String secret) throws BitfinexApiException {

        List<ServiceErrorItem> errors = new ArrayList<ServiceErrorItem>();
        if(id<0 || id==0)
            errors.add(new ServiceErrorItem("A quantidade deve ser maior que zero",CODE_ERROR_ORDER_ID_INVALID));
        if(key.equals("") || key == null)
            errors.add(new ServiceErrorItem("Key não pode ser vazio",CODE_ERROR_KEY_NOT_NULL));
        if(secret.equals("") || secret == null)
            errors.add(new ServiceErrorItem("Secret não pode ser vazio",CODE_ERROR_SECRET_NOT_NULL));
        if(!errors.isEmpty())
            throw new BitfinexApiException("error on bitfinex new order api",errors);

        //set up the data
        EncryptionUtil util = new EncryptionUtil();
        //url
        String urlPath = configBitfinexApi.getOrderInfo();
        JSONObject jo = new JSONObject();

        try {
            jo.put("request", urlPath);
            jo.put("nonce", Long.toString(util.getNonce()));
            jo.put("order_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
            errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
            throw new BitfinexApiException("Bitfinex Api error", errors);
        }
        String url = configBitfinexApi.getBaseUrl()+urlPath,
                payload = util.payloadFromJSON(jo),
                signature = EncryptionUtil.hmacDigest(payload,secret,"HmacSHA384");
        //add header
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-BFX-APIKEY", key);
        headers.add("X-BFX-PAYLOAD", payload);
        headers.add("X-BFX-SIGNATURE", signature);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        try {
            //response
            RestTemplate restTemplate = new RestTemplate();
            //do it
            ResponseEntity<OrderDetails> response = restTemplate.postForEntity(url, entity, OrderDetails.class);
            return response.getBody();
        } catch (Exception e) {
            try {
                //what cause this
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<MessageResponse> response = restTemplate.postForEntity(url, entity, MessageResponse.class);
                errors.add(new ServiceErrorItem(response.getBody().getMessage(), CODE_ERROR_GENERIC));
                throw new BitfinexApiException("error on bitfinex order info api", errors);
            }catch (Exception e1) {
                //else
                e1.printStackTrace();
                errors.add(new ServiceErrorItem("Ocorreu um erro ao acessar a api bitfinex", CODE_ERROR_GENERIC));
                throw new BitfinexApiException("Bitfinex Api error", errors);
            }
        }
    }

}
