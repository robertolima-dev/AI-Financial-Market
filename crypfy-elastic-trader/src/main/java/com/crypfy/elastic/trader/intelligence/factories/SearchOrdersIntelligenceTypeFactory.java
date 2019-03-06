package com.crypfy.elastic.trader.intelligence.factories;

import com.crypfy.elastic.trader.intelligence.IntelligenceOrdersOpportunities;
import com.crypfy.elastic.trader.intelligence.exceptions.OppSearcherFactoryException;
import com.crypfy.elastic.trader.intelligence.intelligence.source.AntoinettePatternsOrdersImpl;
import com.crypfy.elastic.trader.intelligence.intelligence.source.CapPortfolioOrdersImpl;
import com.crypfy.elastic.trader.intelligence.intelligence.source.VolPortfolioOrdersImpl;
import com.crypfy.elastic.trader.persistance.enums.IntelligenceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchOrdersIntelligenceTypeFactory {

    @Autowired
    AntoinettePatternsOrdersImpl antoinettePatternsOrder;
    @Autowired
    CapPortfolioOrdersImpl capPortfolioOrder;
    @Autowired
    VolPortfolioOrdersImpl volPortfolioOrder;

    public IntelligenceOrdersOpportunities getImpl(IntelligenceType type) throws OppSearcherFactoryException {

        if (type.equals(IntelligenceType.ANTOINETTE_PATTERNS)) return antoinettePatternsOrder;

        if (type.equals(IntelligenceType.MARKET_CAP_PORTFOLIO)) return capPortfolioOrder;

        if (type.equals(IntelligenceType.VOLATILITY_PORTFOLIO)) return volPortfolioOrder;

        throw new OppSearcherFactoryException("Não foi possivel selecionar a implementacao de ORDER OPPORTUNITIES SEARCHER");

    }

}
