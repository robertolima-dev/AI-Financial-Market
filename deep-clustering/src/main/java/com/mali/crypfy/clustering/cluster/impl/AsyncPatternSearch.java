package com.mali.crypfy.clustering.cluster.impl;

import com.mali.crypfy.clustering.api.json.TradeJson;
import com.mali.crypfy.clustering.cluster.Clustering;
import com.mali.crypfy.clustering.cluster.ComparativeMetrics;
import com.mali.crypfy.clustering.cluster.exceptions.ComparativeMetricsException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.CoinService;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.persistence.repository.ClusterRepository;
import com.mali.crypfy.clustering.simulations.HistoricalSimulations;
import com.mali.crypfy.clustering.snapshots.SnapshotService;
import com.mali.crypfy.clustering.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class AsyncPatternSearch {

    @Autowired
    private HistoricalSimulations simulations;
    @Autowired
    private DateUtils dateUtils;
    @Autowired
    private ComparativeMetrics compare;
    @Autowired
    private SnapshotService snapshotService;
    @Autowired
    private Clustering clustering;
    @Autowired
    private ClusterRepository clusterRepository;
    @Autowired
    private CoinService coinService;

    @Async
    public CompletableFuture<TradeJson> check(String coin,Cluster clusterInfo,String name,List<Cluster> clusters) {
        TradeJson tradeJson = null;
        try {
            List<CoinHistoryJson> history = coinService.historyByCoin(coin,clusterInfo.getTimeframe(),clusterInfo.getShapeSize(),"desc");
            Collections.reverse(history);
            double[] shape = snapshotService.getShape(history);
            double[] vshape = snapshotService.getvShape(history);
            int size = history.size();
            boolean done = false;
            for (Cluster cluster : clusters){
                int index = simulations.correctIndex(cluster,dateUtils.addDayFromDate(new Date(),-6),cluster.getStatsSizeFilter());
                boolean tradeAllowed = simulations.correctChanges(cluster,dateUtils.addDayFromDate(new Date(),-6),cluster.getStatsSizeFilter())[index]>0 ? true : false;
                double distance = compare.absoluteDistancePercent(cluster.getCentroid(),shape);
                double volDistance = compare.absoluteDistancePercent(cluster.getVolumeCentroid(),vshape);
                if ( (10-distance) > 10*cluster.getSimilarity() && (10-volDistance) > 10*cluster.getSimilarity() && tradeAllowed){
                    tradeJson = new TradeJson();
                    tradeJson.setCallCoin(history.get(size-1).getSymbol());
                    tradeJson.setCallPrice(history.get(size-1).getClose());
                    tradeJson.setStopLoss(new BigDecimal(cluster.getClusterIndicatedSLs()[index]));
                    tradeJson.setTakeProfit(new BigDecimal(cluster.getClusterIndicatedTPs()[index]));
                    tradeJson.setOrderType("BUY_MARKET_ENFORCED");
                    tradeJson.setIntelligenceType("ANTOINETTE_PATTERNS");
                    tradeJson.setTimeFrame(timeFrameString(cluster.getTimeframe()));
                    tradeJson.setExpirationDate(correctExpirationDate(history.get(size-1).getDate(),cluster.getTimeframe(),index+1));
                    System.out.println(tradeJson.getExpirationDate());
                    done = true;
                }
                if (done) break;
            }

        } catch (CoinException e) {
            e.printStackTrace();
        } catch (ComparativeMetricsException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(tradeJson);
    }

    public String timeFrameString(int timeFrame){
        if (timeFrame==1) return "PERIOD_5M";
        if (timeFrame==6) return "PERIOD_30M";
        if (timeFrame==12) return "PERIOD_1H";
        if (timeFrame==48) return "PERIOD_4H";
        if (timeFrame==288) return "PERIOD_1D";
        if (timeFrame==2016) return "PERIOD_1W";
        return null;
    }

    public Date correctExpirationDate(Date date,int timeFrame, int period) throws CoinException {

        if (timeFrame == 6) return dateUtils.addMinuteFromDate(date,30*period);
        if (timeFrame == 12) return dateUtils.addHourFromDate(date,period);
        if (timeFrame == 48) return dateUtils.addHourFromDate(date,4*period);
        if (timeFrame == 288) return dateUtils.addDayFromDate(date,period);

        throw new CoinException("TimeFrame não encontrado!");
    }
}
