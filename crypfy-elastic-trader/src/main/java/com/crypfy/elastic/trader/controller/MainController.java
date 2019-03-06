package com.crypfy.elastic.trader.controller;

import com.crypfy.elastic.trader.integrations.exchange.wrapper.ExchangerServices;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.exceptions.ExchangerException;
import com.crypfy.elastic.trader.integrations.exchange.wrapper.json.Market;
import com.crypfy.elastic.trader.intelligence.intelligence.source.AntoinettePatternsOrdersImpl;
import com.crypfy.elastic.trader.order.OrderManager;
import com.crypfy.elastic.trader.persistance.entity.*;
import com.crypfy.elastic.trader.persistance.enums.BaseCurrency;
import com.crypfy.elastic.trader.persistance.enums.Exchanger;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import com.crypfy.elastic.trader.persistance.repository.OrderRepository;
import com.crypfy.elastic.trader.persistance.repository.PortfolioRepository;
import com.crypfy.elastic.trader.persistance.repository.StrategyRepository;
import com.crypfy.elastic.trader.rest.json.RestResponseJSON;
import com.crypfy.elastic.trader.trade.TradeServices;
import com.crypfy.elastic.trader.trade.exceptions.TradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    ExchangerServices exchangerServices;
    @Autowired
    PortfolioRepository portfolioRepository;
    @Autowired
    AntoinettePatternsOrdersImpl currentOrderOpportunities;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderManager orderManager;
    @Autowired
    TradeServices tradeServices;
    @Autowired
    StrategyRepository strategyRepository;

    @RequestMapping(value = "pattern/search",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> searchForTrades(@RequestParam int timeFrame, @RequestParam String name){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<Order> orders = currentOrderOpportunities.searchForOrders(timeFrame,name);
            System.out.println(orders.size());
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Operação realizada com sucesso");
            restResponseJSON.setResponse(orders);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage(e.getMessage());
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "extrato",method = RequestMethod.GET)
    public ResponseEntity<RestResponseJSON> getHistory(@RequestParam int ano,@RequestParam int mes,@RequestParam String portfolio){
        RestResponseJSON restResponseJSON = new RestResponseJSON();
        try {
            List<Portfolio> portfolios = portfolioRepository.findByMonthAndYearAndPorfolioName(mes,ano, portfolio);
            if (portfolios.size()==0) throw new Exception();
            restResponseJSON.setSuccess(true);
            restResponseJSON.setStatus(200);
            restResponseJSON.setMessage("Extrato gerado com sucesso");
            restResponseJSON.setResponse(portfolios);
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            restResponseJSON.setSuccess(false);
            restResponseJSON.setStatus(e.hashCode());
            restResponseJSON.setMessage("Parametros inválidos");
            return new ResponseEntity<RestResponseJSON>(restResponseJSON, HttpStatus.OK);
        }
    }

}
