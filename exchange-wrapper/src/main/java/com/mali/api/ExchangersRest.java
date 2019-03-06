package com.mali.api;

import com.mali.api.json.RestResponse;
import com.mali.entity.*;
import com.mali.enumerations.DealType;
import com.mali.enumerations.Exchanger;
import com.mali.enumerations.TimeFrame;
import com.mali.exchanger.communication.exceptions.*;
import com.mali.exchanger.communication.factories.ExchangerServicesFactory;
import com.mali.exchanger.communication.impl.bittrex.implementations.BittrexApis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ExchangersRest {

    @Autowired
    private ExchangerServicesFactory factory;

    @RequestMapping("/exchangers")
    public ResponseEntity<?> list(){

        RestResponse restResponse = new RestResponse();
        try {
            List<String> exchangers = Stream.of(Exchanger.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());
            ImplementedExchangers implementedExchangers = new ImplementedExchangers(exchangers,"Essa é a lista dos exchangers com implementação no momento");
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(implementedExchangers);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (Exception e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Houve alguma falha inesperada, por favor tente mais tarde.");
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/coin-address/{coinName}")
    public ResponseEntity<?> getCoinAddress(@PathVariable("coinName") String coinName, @RequestParam("key") String key, @RequestParam("secret") String secret, @PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        try {
            String address = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getDepositAddress(coinName,key,secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(address);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/exchangers/{exchangeName}/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest,@PathVariable("exchangeName") String exchangeName,@RequestParam("key") String key, @RequestParam("secret") String secret){

        RestResponse restResponse = new RestResponse();
        try {
            boolean sucsses = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).doWithdraw(withdrawRequest.getPassword(),withdrawRequest.getAddress(),withdrawRequest.getCoinName(),withdrawRequest.getQuantity(),key,secret);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setResponse(sucsses);
            return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/total-balance/{baseCurrency}")
    public ResponseEntity<?> getCurrentTotalBalance(@RequestParam("key") String key,@RequestParam("secret") String secret,@PathVariable("exchangeName") String exchangeName,@PathVariable("baseCurrency") String baseCurrency){

        RestResponse restResponse = new RestResponse();
        try {
            double balance = 0;
            balance = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getCurrentTotalBalance(key, secret,baseCurrency);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(balance);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/balance/{coinName}")
    public ResponseEntity<?> getCoinBalance(@PathVariable("coinName") String coinName,@RequestParam("key") String key,@RequestParam("secret") String secret,@PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        try {
            double balance =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getCoinBalance(coinName,key,secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(balance);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/balances")
    public ResponseEntity<?> getCoinsBalance(@RequestParam("key") String key, @RequestParam("secret") String secret, @PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        try {
            List<Balance> balances =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getAllCoinBalances(key, secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(balances);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/order-history")
    public ResponseEntity<?> getOrderHistory(@RequestParam("key") String key, @RequestParam("secret") String secret, @PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        try {
            List<Order> orders =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getOrderHistory(key, secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(orders);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/deposit-history")
    public ResponseEntity<?> getDepositHistory(@RequestParam("key") String key, @RequestParam("secret") String secret, @PathVariable("exchangeName") String exchangeName, @RequestParam String coin){

        RestResponse restResponse = new RestResponse();
        try {
            List<Deposit> deposits = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getDepositHistory(coin,key, secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(deposits);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/withdraw-history")
    public ResponseEntity<?> getWithdrawHistory(@RequestParam("key") String key, @RequestParam("secret") String secret, @PathVariable("exchangeName") String exchangeName, @RequestParam String coin){

        RestResponse restResponse = new RestResponse();
        try {
            List<Withdraw> withdraws = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getWithdrawHistory(coin,key, secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(withdraws);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/markets")
    public ResponseEntity<?> getMarkets(@PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        try {
            List<Market> markets = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getMarkets();
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(markets);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/market/{marketName}/history")
    public ResponseEntity<?> getMarketHistory(@PathVariable("marketName") String marketName,@RequestParam("nBars") int nBars,@RequestParam("timeframeMultiplier") int timeframeMultiplier,@RequestParam("timefrarme") String timefrarme,@PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            CandleHistory candleHistory =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getMarketHistory(market,nBars,timeframeMultiplier, TimeFrame.valueOf(timefrarme.toUpperCase()));
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(candleHistory);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/market/{marketName}/side/{side}")
    public ResponseEntity<?> getBuyOrderBook(@PathVariable("marketName") String marketName, @PathVariable("exchangeName") String exchangeName, @PathVariable("side") String side){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            List<SimpleOrder> orders = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getOrderBook(market,side);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(orders);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping("/exchangers/{exchangeName}/market/{marketName}/ticker")
    public ResponseEntity<?> getTicker(@PathVariable("marketName") String marketName, @PathVariable("exchangeName") String exchangeName){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            Ticker ticker =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).getTicker(market);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(ticker);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/exchangers/{exchangeName}/market/{marketName}/new-order")
    public ResponseEntity<?> newDeal(@RequestBody DealRequest dealRequest,@PathVariable("marketName") String marketName, @PathVariable("exchangeName") String exchangeName,@RequestParam("key") String key, @RequestParam("secret") String secret){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            TradeResult result =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).newDeal(market,dealRequest.getDealType(),dealRequest.getQuantity(),dealRequest.getPrice(),key,secret);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setResponse(result);
            return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST,path = "/exchangers/{exchangeName}/order-cancel")
    public ResponseEntity<?> orderCancel(@PathVariable("exchangeName") String exchangeName,@RequestParam("key") String key, @RequestParam("secret") String secret, @RequestParam("orderId") String orderId,@RequestParam() String marketName){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            boolean result =  factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).orderCancel(orderId,market, key, secret);
            restResponse.setStatus(201);
            restResponse.setSuccess(true);
            restResponse.setResponse(result);
            return new ResponseEntity<>(restResponse, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET,path = "/exchangers/{exchangeName}/market/{marketName}/deal-price")
    public ResponseEntity<?> dealPrice(@PathVariable("marketName") String marketName, @PathVariable("exchangeName") String exchangeName,@RequestParam("amount") double amount,@RequestParam("type") String type){

        RestResponse restResponse = new RestResponse();
        Market market = new Market(marketName);
        try {
            double dealPrice = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).dealPrice(market,DealType.valueOf(type.toUpperCase()),amount);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(dealPrice);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET,path = "/exchangers/{exchangeName}/order/{orderId}/info")
    public ResponseEntity<?> getOrder(@PathVariable("orderId") String orderId, @PathVariable("exchangeName") String exchangeName,@RequestParam("key") String key, @RequestParam("secret") String secret, @RequestParam() String marketName){

        RestResponse restResponse = new RestResponse();

        Market market = new Market(marketName);
        try {
            Object result = factory.getService(Exchanger.valueOf(exchangeName.toUpperCase())).orderInfo(market,orderId,key,secret);
            restResponse.setStatus(200);
            restResponse.setSuccess(true);
            restResponse.setResponse(result);
            return new ResponseEntity<>(restResponse, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse("Não existe implementação do exchanger: "+exchangeName);
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesFactoryException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        } catch (ExchangerServicesException e) {
            restResponse.setSuccess(false);
            restResponse.setStatus(400);
            restResponse.setResponse(e.getErrors());
            return new ResponseEntity<>(restResponse,HttpStatus.OK);
        }
    }

}
