package com.crypfy.crawler.jsoup;

import com.crypfy.crawler.api.CoinDataExtractor;
import com.crypfy.crawler.exception.ReadDataCoinException;
import com.crypfy.persistence.entity.HistoricalCoinSnapshot;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class JsoupCoinDataExtractor implements CoinDataExtractor{

    public static final String coinMarketCapUrl = "https://coinmarketcap.com/historical/";

    @Override
    public List<HistoricalCoinSnapshot> read(String dateString,int weak) throws ReadDataCoinException {

        try{

            Document doc= Jsoup.connect(coinMarketCapUrl+dateString).get();

            //getting table
            Elements table = doc.getElementsByClass("table");
            //getting tbody
            Elements tbody = table.get(0).getElementsByTag("tbody");
            //getting rows
            Elements rows = tbody.get(0).getElementsByTag("tr");

            //iterate through rows
            //instances
            List<HistoricalCoinSnapshot> snapshots = new ArrayList<HistoricalCoinSnapshot>();
            int neededRows = 0;
            for(Element row : rows) {
                HistoricalCoinSnapshot snapshot = new HistoricalCoinSnapshot();
                Elements columns = row.getElementsByTag("td");

                //fill the obj
                //coin name
                snapshot.setName(columns.get(2).text());

                //date
                snapshot.setDate(dateString);

                //cap
                String cap = columns.get(3).text().substring(1).replaceAll(",","");
                snapshot.setCap(Double.parseDouble(cap));

                //treat price -.-
                String price = columns.get(4).text().substring(1);
                if(StringUtils.countMatches(price,".")>1) price = price.replaceFirst(".","");
                if (StringUtils.countMatches(price,",")>0) price = price.replace(",",".");
                snapshot.setPrice(Double.parseDouble(price));

                //rank
                snapshot.setRank(Integer.parseInt(columns.get(0).text()));

                //supply
                String supply = columns.get(5).text().replaceAll(",","");
                if(StringUtils.countMatches(supply,"*")>0) supply = supply.substring(0,supply.indexOf("*"));
                snapshot.setSupply(Double.parseDouble(supply));

                //week
                snapshot.setWeakId(weak);

                //add
                snapshots.add(snapshot);

                //take only the first 150
                if(neededRows>=149) break;
                neededRows++;
            }

            return snapshots;

        }catch (Exception e){
            e.printStackTrace();
            throw new ReadDataCoinException("Erro ao tentar acessar o snapshot : "+dateString);
        }
    }
}
