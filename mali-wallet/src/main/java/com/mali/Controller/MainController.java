package com.mali.Controller;


import com.mali.connection.BtcdRPCConnector;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.core.domain.MiningInfo;
import com.neemre.btcdcli4j.core.domain.WalletInfo;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

@RestController
public class MainController {

    @Autowired
    private BtcdRPCConnector btcdRPCConnector;

    @GetMapping(path = "/get-new-adress")
    public String createAdress() throws IOException, BitcoindException, CommunicationException {

        try{
            return btcdRPCConnector.getClient().getNewAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping(path = "/total-balance")
    public BigDecimal getBalance() throws IOException, BitcoindException, CommunicationException {

        try{
            return btcdRPCConnector.getClient().getBalance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BigDecimal(0);
    }

    @GetMapping(path = "/wallet-info")
    public Map<String,BigDecimal> generate() throws IOException, BitcoindException, CommunicationException {

        try{
            return btcdRPCConnector.getClient().listAccounts();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
