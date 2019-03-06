package com.mali.crypfy.clustering.simulations;

import com.google.common.primitives.Doubles;
import com.mali.crypfy.clustering.cluster.ComparativeMetrics;
import com.mali.crypfy.clustering.integrations.mali.coin.data.CoinService;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinJson;
import com.mali.crypfy.clustering.persistence.entity.Cluster;
import com.mali.crypfy.clustering.snapshots.SnapshotService;
import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import com.mali.crypfy.clustering.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class HistoricalSimulations {

    @Autowired
    DateUtils dateUtils;
    @Autowired
    CoinService coinService;
    @Autowired
    SnapshotService snapshotService;

    public SimulationStatistics doHistoricalSimulation(List<Cluster> clusters, double initialBalance, int allowedSimultTransactions,int startYear, int endYear,int minCalls,int timeFrame) {
        //variables
        List<SimulationTransaction> currentTransactions = new ArrayList<>();
        List<SnapshotData> snaps = new ArrayList<>();
        List<List<SnapshotData>> possiblesTransactionsByDay = new ArrayList<>();
        for (int init = 0; init < 100000; init++) {
            List<SnapshotData> list = new ArrayList<>();
            possiblesTransactionsByDay.add(list);

        }
        Date inicialDate = new GregorianCalendar(startYear, 0, 1).getTime();
        Date finalDate = new GregorianCalendar(endYear, 0, 1).getTime();
        List<Double> balanceUpdates = new ArrayList<>();
        balanceUpdates.add(initialBalance);
        double balance = initialBalance, aproxTotalBalance = initialBalance;
        SimulationStatistics statistics = new SimulationStatistics();
        int nTransactions = 0;
        long keyUpdate = 0;
        List<String> negociatedCoins = new ArrayList<>();

        //collect the snaps
        for (Cluster cluster : clusters) {
            for (SnapshotData snap : cluster.getPoints()) if (snap.getDate().after(inicialDate) && snap.getDate().before(finalDate)) snaps.add(snap);
        }

        //sort by date
        snaps.sort(Comparator.comparing(o -> o.getDate()));
        System.out.println("Snaps collected and sorted by date...");

        //separate data by day
        for (SnapshotData data : snaps){
            long key = correctKey(timeFrame,inicialDate,data.getDate());
            List<SnapshotData> dataList = possiblesTransactionsByDay.get((int) key);
            dataList.add(data);
            possiblesTransactionsByDay.set((int) key, dataList);
        }
        System.out.println("The snaps were separated by day...");

        List<MonthStats> monthStats = getMonthBreakPoints(snaps.get(0).getDate(),snaps.get(snaps.size()-1).getDate());

        //simulate
        for (int iterator = 0; iterator < possiblesTransactionsByDay.size(); iterator++) {

            //check for open transactions
            if (currentTransactions.size()>0){
                //check if its time to close it
                for (Iterator<SimulationTransaction> iter = currentTransactions.listIterator(); iter.hasNext(); ) {
                    SimulationTransaction transaction = iter.next();
                    if (iterator>=transaction.getExitDay() ||  possiblesTransactionsByDay.get(iterator).size()<minCalls){
                        balance += (1+transaction.getProfitPercent())*transaction.getInvestedAmount();
                        iter.remove();
                    }
                }
            }
            //check for new transactions
            if (currentTransactions.size()<allowedSimultTransactions){
                //check for open
                for (SnapshotData snap : possiblesTransactionsByDay.get(iterator)) {
                    double investedAmount = currentTransactions.size() == allowedSimultTransactions-1 ? balance : aproxTotalBalance/allowedSimultTransactions;
                    int duration = 0;
                    boolean tradeAllowed = false;
                    for (Cluster cluster : clusters) if (cluster.getPoints().contains(snap)) {
                        duration = correctIndex(cluster,dateUtils.addDayFromDate(snap.getDate(),-6),cluster.getStatsSizeFilter());
                        tradeAllowed = correctChanges(cluster,dateUtils.addDayFromDate(snap.getDate(),-6),cluster.getStatsSizeFilter())[duration]>0 && possiblesTransactionsByDay.get(iterator).size()>=minCalls ? true : false;
                    }
                    //dont open more than 1 trade per coin
                    for (SimulationTransaction transaction : currentTransactions) if (snap.getCoinName().equals(transaction.getCoinName())) tradeAllowed = false;
                    //do it
                    if (tradeAllowed) {
                        SimulationTransaction transaction = new SimulationTransaction();
                        transaction.setExitDay(iterator + duration + 1);
                        transaction.setProfitPercent(snap.getChanges()[duration]);
                        transaction.setInvestedAmount(investedAmount*0.99);
                        transaction.setCoinName(snap.getCoinName());
                        currentTransactions.add(transaction);
                        nTransactions++;
                        if (!negociatedCoins.contains(snap.getCoinName())) negociatedCoins.add(snap.getCoinName());
                        //update balance
                        balance = balance - investedAmount*0.99;
                    }
                    //check transactions size
                    if (currentTransactions.size()==allowedSimultTransactions) break;
                }

            }
            //update balance and stats
            double floatingBalance = 0;
            for (SimulationTransaction transaction : currentTransactions) floatingBalance += transaction.getInvestedAmount();
            aproxTotalBalance = balance + floatingBalance;
            //new update?
            if (aproxTotalBalance != balanceUpdates.get(balanceUpdates.size()-1)) {
                balanceUpdates.add(aproxTotalBalance);
                //fill month stats
                for (MonthStats stats : monthStats){
                    if (correctDate(timeFrame,iterator,inicialDate).after(stats.getInicialDate()) && correctDate(timeFrame,iterator,inicialDate).before(stats.getEndDate())){
                        if (keyUpdate != iterator) {
                            keyUpdate = iterator;
                            int currentIndex = monthStats.indexOf(stats);
                            stats.getBalanceUpdates().add(balanceUpdates.get(balanceUpdates.size()-2));
                        }
                        stats.getBalanceUpdates().add(aproxTotalBalance);
                    }
                }
            }
        }
        //set stats
        statistics.setBalanceUpdates(balanceUpdates);
        statistics.setFinalBalance(aproxTotalBalance);
        statistics.setnTransactions(nTransactions);
        statistics.setReturnPercent(100*((aproxTotalBalance-initialBalance)/initialBalance));
        statistics.setMaxDD(getMaxDD(balanceUpdates));
        statistics.setNegociatedCoins(negociatedCoins);
        statistics.setMonthStats(setStats(monthStats));

        return statistics;
    }

    public long correctKey(int timeFrame,Date inicialDate, Date dataDate){
        if (timeFrame == 6) return (int) dateUtils.diffBetweenDates(inicialDate,dataDate, TimeUnit.MINUTES)/30;
        if (timeFrame == 12) return (int) dateUtils.diffBetweenDates(inicialDate,dataDate, TimeUnit.HOURS);
        if (timeFrame == 48) return (int) dateUtils.diffBetweenDates(inicialDate,dataDate, TimeUnit.HOURS)/4;
        if (timeFrame == 288) return (int) dateUtils.diffBetweenDates(inicialDate,dataDate, TimeUnit.DAYS);

        return 0;
    }

    public Date correctDate(int timeFrame,int iterator,Date date){

        if (timeFrame == 6) return dateUtils.addMinuteFromDate(date,iterator*30);
        if (timeFrame == 12) return dateUtils.addHourFromDate(date,iterator);
        if (timeFrame == 48) return dateUtils.addHourFromDate(date,iterator*4);
        if (timeFrame == 288) return dateUtils.addDayFromDate(date,iterator);
        return null;
    }

    public double getMaxDD(List<Double> balanceUpdates){
        //vars
        double maxDD = 0, maxBalance = 0;
        for (int iterator = 0; iterator < balanceUpdates.size(); iterator ++){
            double balance = balanceUpdates.get(iterator);
            if (balance > maxBalance){
                maxBalance = balance;
            } else {
                if ((maxBalance-balance)/balance>maxDD) maxDD = (maxBalance-balance)/balance;
            }
        }
        return maxDD*100;
    }

    public int correctIndex(Cluster cluster, Date currentTestDate, long daysToConsider){
        int index = 0;
        List<SnapshotData> relevantData = new ArrayList<>();

        //collect data
        for (SnapshotData snap : cluster.getPoints()) if (snap.getDate().before(currentTestDate) && snap.getDate().after(dateUtils.addDayFromDate(currentTestDate,(int) -daysToConsider))) relevantData.add(snap);
        //sort by date
        relevantData.sort(Comparator.comparing(o -> o.getDate()));

        //get it
        double[]  changesMeanReturn = new double[5];
        int clusterInteration = 0, pointsSize = relevantData.size();
        if (relevantData.size()>0){
            for (SnapshotData point : relevantData) {
                    clusterInteration++;
                    for (int iterator = 0; iterator < 5; iterator++) {
                        if (clusterInteration < pointsSize) {
                            changesMeanReturn[iterator] = changesMeanReturn[iterator] + point.getChanges()[iterator];
                        } else {
                            changesMeanReturn[iterator] = (changesMeanReturn[iterator] + point.getChanges()[iterator]) / clusterInteration;
                        }
                    }
            }
            //get the index
            List<Double> list = Arrays.stream(changesMeanReturn).boxed().collect(Collectors.toList());
            index = list.indexOf(Collections.max(list));
        }else{
            List<Double> list = Arrays.stream(cluster.getChangesMeanReturn()).boxed().collect(Collectors.toList());
            index = list.indexOf(Collections.max(list));
        }

        return index;
    }

    public  double[] correctChanges(Cluster cluster, Date currentTestDate, long daysToConsider){
        int index = 0;
        List<SnapshotData> relevantData = new ArrayList<>();

        //collect data
        for (SnapshotData snap : cluster.getPoints()) if (snap.getDate().before(currentTestDate) && snap.getDate().after(dateUtils.addDayFromDate(currentTestDate,(int) -daysToConsider))) relevantData.add(snap);
        //sort by date
        relevantData.sort(Comparator.comparing(o -> o.getDate()));

        //get it
        double[]  changesMeanReturn = new double[5];
        int clusterInteration = 0, pointsSize = relevantData.size();
        if (relevantData.size()>0){
            for (SnapshotData point : relevantData) {
                    clusterInteration++;
                    for (int iterator = 0; iterator < 5; iterator++) {
                        if (clusterInteration < pointsSize) {
                            changesMeanReturn[iterator] = changesMeanReturn[iterator] + point.getChanges()[iterator];
                        } else {
                            changesMeanReturn[iterator] = (changesMeanReturn[iterator] + point.getChanges()[iterator]) / clusterInteration;
                        }
                    }
            }
            return changesMeanReturn;
        }else{
            return cluster.getChangesMeanReturn();
        }
    }

    public List<MonthStats> getMonthBreakPoints (Date initial,Date end){

        Date currentDate = dateUtils.getFirstDayOfCurrentMonth(initial);
        List<MonthStats> stats = new ArrayList<>();
        int iterations = 0;

        while (currentDate.before(end) || currentDate.equals(end)) {
            MonthStats monthStats = new MonthStats();
            monthStats.setInicialDate(currentDate);
            if (iterations == 0){
                List<Double> updates = new ArrayList<>();
                monthStats.setBalanceUpdates(updates);
            }
            //set new
            currentDate = dateUtils.getFirstDayOfNextMonth(currentDate);
            monthStats.setEndDate(currentDate);
            stats.add(monthStats);
        }
        return stats;
    }

    public List<MonthStats> setStats(List<MonthStats> monthStats){

        for (MonthStats stats : monthStats){
            stats.setMonthMaxDD(getMaxDD(stats.getBalanceUpdates()));
            stats.setnTransactions(stats.getBalanceUpdates().size());
            if (stats.getnTransactions()>0) {
                stats.setReturnTotal(stats.getBalanceUpdates().get(stats.getnTransactions() - 1) - stats.getBalanceUpdates().get(0));
                stats.setReturnTotalPercent(stats.getReturnTotal()/stats.getBalanceUpdates().get(0));
            }

        }
        return  monthStats;
    }

}


