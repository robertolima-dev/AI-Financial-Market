package com.mali.api;

import com.mali.core.api.TransactionsService;
import com.mali.entity.MoveData;
import com.mali.entity.SendData;
import com.mali.entity.SendToManyData;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransactionsRestService {

    @Autowired
    private TransactionsService transactionsService;

    @RequestMapping(method = RequestMethod.POST,path = "/transactions/move")
    public boolean move(@RequestBody MoveData moveData) throws BitcoindException, CommunicationException {
        return transactionsService.moveFromAccounts(moveData.getFromAccount(),moveData.getToAccount(),moveData.getValue());
    }

    @RequestMapping(method = RequestMethod.POST,path = "/transactions/send-from-to")
    public String sendFromTo(@RequestBody MoveData moveData) throws BitcoindException, CommunicationException {
        return transactionsService.sendFromTo(moveData.getFromAccount(),moveData.getToAccount(),moveData.getValue());
    }

    @RequestMapping(path = "/transactions/list/{addressName}/{confirmations}")
    public List<Payment> listTransactions(@PathVariable("addressName") String addressName, @PathVariable("confirmations") int confirmations) throws BitcoindException, CommunicationException {
        return transactionsService.listTransactions(addressName,confirmations);
    }

}
