package com.mali.crawler.jsoup;

import com.mali.crawler.CoinDataCoinMarketCapSite;
import com.mali.crawler.api.CoinDataExtractor;
import com.mali.crawler.exception.ReadDataCoinException;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsoupCoinDataExtractor implements CoinDataExtractor<CoinDataCoinMarketCapSite>{

    public static final String coinMarketCapUrl = "https://coinmarketcap.com/currencies/";

    private SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");

    @Override
    public List<CoinDataCoinMarketCapSite> read(String coinName) throws ReadDataCoinException {
        try {
            Document doc= Jsoup.connect(coinMarketCapUrl + coinName + "/historical-data/?start=20180524&end=20180613").get();

            //getting table
            Elements table = doc.getElementsByClass("table");
            //getting tbody
            Elements tbody = table.get(0).getElementsByTag("tbody");
            //getting rows
            Elements rows = tbody.get(0).getElementsByTag("tr");

            List<CoinDataCoinMarketCapSite> coinDataList = new ArrayList<CoinDataCoinMarketCapSite>();

            //each rows
            for(Element row : rows) {
                Elements columns = row.getElementsByTag("td");

                CoinDataCoinMarketCapSite coinData = new CoinDataCoinMarketCapSite();

                //try parse date
                try {
                    coinData.setDate(sdf.parse(columns.get(0).text()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new ReadDataCoinException("ocorreu um erro ao ler a data do tick da moeda " + coinName + " no site coinmarketcap, =(");
                }
                if(!columns.get(1).text().equals("-"))
                    coinData.setOpen(new BigDecimal(columns.get(1).text()));
                else
                    coinData.setOpen(null);

                if(!columns.get(2).text().equals("-"))
                    coinData.setHigh(new BigDecimal(columns.get(2).text()));
                else
                    coinData.setHigh(null);

                if(!columns.get(3).text().equals("-"))
                    coinData.setLow(new BigDecimal(columns.get(3).text()));
                else
                    coinData.setLow(null);

                if(!columns.get(4).text().equals("-"))
                    coinData.setClose(new BigDecimal(columns.get(4).text()));
                else
                    coinData.setClose(null);

                if(!columns.get(5).text().equals("-"))
                    coinData.setVolume(new BigDecimal(columns.get(5).text().replaceAll(",","")).setScale(8));
                else
                    coinData.setVolume(null);

                if(!columns.get(6).text().equals("-"))
                    coinData.setMarketcap(new BigDecimal(columns.get(6).text().replaceAll(",","")));
                else
                    coinData.setMarketcap(null);

                //add to list
                coinDataList.add(coinData);
            }
            return coinDataList;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadDataCoinException("ocorreu um erro ao ler os dados da moeda " + coinName + " no site coinmarketcap, =(");
        }
    }
}
