package com.mali.api;

import com.mali.core.api.WalletService;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class WalletRestService {

    @Autowired
    private WalletService walletService;

    @GetMapping(path = "/wallet/new-address")
    public String newAddress() throws BitcoindException, CommunicationException {
        return walletService.newAddress();
    }

    @GetMapping(path = "/wallet/new-address/{addressName}")
    public String newAddressWithName(@PathVariable("addressName") String addressName) throws BitcoindException, CommunicationException {
        return walletService.newAddressWithName(addressName);
    }

    @GetMapping(path = "/wallet/addresses/{accName}")
    public List<String> getAdressesByAccName(@PathVariable("accName") String accName) throws BitcoindException, CommunicationException {
        return walletService.getAddressByAccountName(accName);
    }

    @GetMapping(path = "/wallet/balance")
    public BigDecimal getBalance() throws BitcoindException, CommunicationException {
        return walletService.getBalance();
    }

    @GetMapping(path = "/wallet/balance/{account}")
    public BigDecimal getBalanceByAccount(@PathVariable("account") String account) throws BitcoindException, CommunicationException {
        return walletService.getBalanceByAccount(account);
    }

    @GetMapping(path = "/wallet/received/{address}")
    public BigDecimal getReceiveidByAddress(@PathVariable("address") String address) throws BitcoindException, CommunicationException {
        return walletService.receivedByAddress(address);
    }

}
