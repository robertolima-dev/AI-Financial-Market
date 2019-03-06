package com.crypfy.elastic.trader.intelligence.factories;

import com.crypfy.elastic.trader.intelligence.OrderDetails;
import com.crypfy.elastic.trader.intelligence.details.impl.DirectOrderDetailsImpl;
import com.crypfy.elastic.trader.intelligence.details.impl.IndirectOrderDetailsImpl;
import com.crypfy.elastic.trader.intelligence.exceptions.OppSearcherFactoryException;
import com.crypfy.elastic.trader.persistance.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetOrderExecutionFactory {

    @Autowired
    DirectOrderDetailsImpl setDirectOrderDetails;
    @Autowired
    IndirectOrderDetailsImpl setIndirectOrderDetails;

    public OrderDetails getImpl(OrderStatus orderStatus) throws OppSearcherFactoryException {

        if (orderStatus.equals(OrderStatus.WAITING_DIRECT_EXECUTION)) return setDirectOrderDetails;

        if (orderStatus.equals(OrderStatus.WAITING_TWO_STEP_EXECUTION)) return setIndirectOrderDetails;

        throw new OppSearcherFactoryException("Não foi possivel selecionar a implementação de SET ORDER DETAILS!");
    }

}
