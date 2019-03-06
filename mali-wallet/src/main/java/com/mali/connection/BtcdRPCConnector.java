package com.mali.connection;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Properties;

@Component
public class BtcdRPCConnector {

    private BtcdClient client;

    public BtcdRPCConnector() throws IOException, BitcoindException, CommunicationException {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm)
                .build();
        Properties nodeConfig = new Properties();
        InputStream is = new BufferedInputStream(new FileInputStream("src/main/resources/node_config.properties"));
        nodeConfig.load(is);
        is.close();
        client = new BtcdClientImpl(httpProvider, nodeConfig);
    }

    public BtcdClient getClient() {
        return client;
    }

}
