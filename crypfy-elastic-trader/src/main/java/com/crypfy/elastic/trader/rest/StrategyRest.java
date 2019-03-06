package com.crypfy.elastic.trader.rest;

import com.crypfy.elastic.trader.persistance.entity.Strategy;
import com.crypfy.elastic.trader.persistance.enums.BaseCurrency;
import com.crypfy.elastic.trader.persistance.enums.StrategyStatus;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.rest.json.RestResponseJSON;
import com.crypfy.elastic.trader.strategy.StrategyServices;
import com.crypfy.elastic.trader.trade.TradeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StrategyRest {

    @Autowired
    StrategyServices strategyServices;
    @Autowired
    TradeServices tradeServices;
    @Autowired
    StrategyRepository strategyRepository;

    @RequestMapping(value = "strategies",method = RequestMethod.POST)
    public ResponseEntity<RestResponseJSON> newStrategy(@RequestBody Strategy strategy){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            StrategyStatus status = strategyServices.newStrategy(strategy);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(201);
            restResponseJSON.setMessage("Nova estratégia adicionada com sucesso!");
            restResponseJSON.setResponse(status);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "strategies",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> list(@RequestParam String strategyName){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {

            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Nova estratégia adicionada com sucesso!");
            //restResponseJSON.setResponse();
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "strategy/{strategyName}/close-strategy",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToShutDown(@PathVariable String strategyName){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            StrategyStatus status = strategyServices.closeStrategy(strategyName);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Estratégia em processo de finalização!");
            restResponseJSON.setResponse(status);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "strategy/{strategyName}/close-strategy-now",method = RequestMethod.PUT)
    public ResponseEntity<RestResponseJSON> changeStatusToShutDownImmediately(@PathVariable String strategyName){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            StrategyStatus status = strategyServices.closeStrategyNow(strategyName);
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Estratégia sendo finalizada agora!");
            restResponseJSON.setResponse(status);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "strategy/{strategyName}/total-balance",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> searchForTrades(@PathVariable String strategyName){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Operação realizada com sucesso");
            restResponseJSON.setResponse(tradeServices.exchangersTotalBalance(strategyRepository.findByName(strategyName).getExchangersDetails(), BaseCurrency.BTC,1));
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

}
