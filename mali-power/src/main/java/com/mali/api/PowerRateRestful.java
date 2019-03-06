package com.mali.api;

import com.mali.collector.exceptions.CoinDataAPIException;
import com.mali.core.rateCalculations.api.RatesCalculator;
import com.mali.persistence.entity.PowerRate;
import com.mali.persistence.repository.PowerRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class PowerRateRestful {

    @Autowired
    PowerRateRepository powerRateRepository;
    @Autowired
    private RatesCalculator calculator;

    @RequestMapping("/power-rates")
    public List<PowerRate> listAll(){
        return powerRateRepository.findAll();
    }

    @RequestMapping("/power-rates/{category}")
    public List<PowerRate> listByCategory(@PathVariable("category") String category){
        return powerRateRepository.findByCategory(category);
    }

    @GetMapping(path = "/power-rates/generate/{minimumCap}")
    public List<PowerRate> getAndSavePowerRates(@PathVariable("minimumCap") BigDecimal minimumCap) throws CoinDataAPIException {

        List<PowerRate> powerRates = calculator.calculatePowerRates(minimumCap);
        //delete old data
        powerRateRepository.deleteAll();
        //save these things
        for(PowerRate powerRate : powerRates){

            //save new
            powerRateRepository.save(powerRate);
        }
        return powerRates;
    }
}
