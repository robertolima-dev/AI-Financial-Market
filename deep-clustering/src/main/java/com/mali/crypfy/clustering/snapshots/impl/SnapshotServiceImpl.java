package com.mali.crypfy.clustering.snapshots.impl;

import com.google.common.primitives.Doubles;
import com.mali.crypfy.clustering.integrations.mali.coin.data.CoinService;
import com.mali.crypfy.clustering.integrations.mali.coin.data.exception.CoinException;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinHistoryJson;
import com.mali.crypfy.clustering.integrations.mali.coin.data.json.CoinJson;
import com.mali.crypfy.clustering.snapshots.SnapshotService;
import com.mali.crypfy.clustering.snapshots.entity.LinearizedCoinHistory;
import com.mali.crypfy.clustering.snapshots.entity.SnapshotData;
import com.mali.crypfy.clustering.snapshots.entity.TransientCandle;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Service
public class SnapshotServiceImpl implements SnapshotService {

    @Autowired
    CoinService coinService;

    @Override
    public List<SnapshotData> getClusterAndImgData(int shapeArraySize, int maximumAnalizedCoins,int timeFrame,int discountedCoins) throws CoinException {
        //get the MUST NEEDED data
        List<CoinJson> coins = coinService.coinsByMarketCapDesc(maximumAnalizedCoins+1);
        List<SnapshotData> data = new ArrayList<>();
        int count = 0;
        //iterate through all coins
        for(CoinJson coin : coins){
            count++;
            if (!coin.getIdcoin().equals("tether") && count>discountedCoins) {
                //get history
                List<CoinHistoryJson> coinHistoryList = coinService.historyByCoin(coin.getIdcoin(), timeFrame, 0,"asc");
                int historySize = coinHistoryList.size();

                //iterate through coin history
                for (int iteration = 0; iteration < historySize - 6; iteration++) {
                    //get objects
                    List<TransientCandle> transientCandles = new ArrayList<>();
                    List<LinearizedCoinHistory> linearizedCandles = new ArrayList<>();
                    SnapshotData snapshotData = new SnapshotData();
                    double[] imgShape = new double[shapeArraySize];
                    //we need specific size
                    if (iteration > shapeArraySize - 1) {
                        //variables
                        double max, min, minVol, maxVol;
                        DescriptiveStatistics highs = new DescriptiveStatistics();
                        DescriptiveStatistics lows = new DescriptiveStatistics();
                        DescriptiveStatistics volumes = new DescriptiveStatistics();
                        DescriptiveStatistics change = new DescriptiveStatistics();
                        //work through needed history
                        for (int candlePosition = shapeArraySize - 1; candlePosition >= 0; candlePosition--) {
                            //needed variables
                            TransientCandle transientCandle = new TransientCandle();
                            int currentCandlePos = iteration - candlePosition;
                            CoinHistoryJson currentCandle = coinHistoryList.get(currentCandlePos);
                            //fulfill transient data
                            transientCandle.setOpen(currentCandle.getOpen());
                            transientCandle.setLow(currentCandle.getLow());
                            transientCandle.setHigh(currentCandle.getHigh());
                            transientCandle.setClose(currentCandle.getClose());
                            transientCandle.setDate(currentCandle.getDate());
                            BigDecimal volume = currentCandle.getVolumeUSD24h() == null ? new BigDecimal(0) : currentCandle.getVolumeUSD24h();
                            transientCandle.setVolume(volume);
                            transientCandles.add(transientCandle);
                            //stats
                            highs.addValue(Double.parseDouble(transientCandle.getHigh().toString()));
                            lows.addValue(Double.parseDouble(transientCandle.getLow().toString()));
                            volumes.addValue(Double.parseDouble(transientCandle.getVolume().toString()));
                            double base = transientCandle.getClose().compareTo(transientCandle.getOpen()) == 1 ? Double.parseDouble(transientCandle.getOpen().toString()) : Double.parseDouble(transientCandle.getClose().toString());
                            change.addValue(Math.abs(Double.parseDouble(transientCandle.getClose().subtract(transientCandle.getOpen()).toString())) / base);
                        }
                        //max 'n min
                        max = highs.getMax();
                        min = lows.getMin();
                        minVol = volumes.getMin();
                        maxVol = volumes.getMax();
                        //linearize the data
                        for (TransientCandle candle : transientCandles) {
                            LinearizedCoinHistory linearizedCoinHistory = new LinearizedCoinHistory();
                            linearizedCoinHistory.setOpen(((((Double.parseDouble(candle.getOpen().toString()) - min)) / (max - min))));
                            linearizedCoinHistory.setLow(((((Double.parseDouble(candle.getLow().toString()) - min)) / (max - min))));
                            linearizedCoinHistory.setHigh(((((Double.parseDouble(candle.getHigh().toString()) - min)) / (max - min))));
                            linearizedCoinHistory.setClose((int) Math.round((0 + (((Double.parseDouble(candle.getClose().toString()) - min) * 9) / (max - min)))));
                            imgShape[transientCandles.indexOf(candle)] = (int) Math.round(((((Double.parseDouble(candle.getClose().toString()) - min) * shapeArraySize) / (max - min))));
                            linearizedCoinHistory.setVolume((int) Math.round((0 + (((Double.parseDouble(candle.getVolume().toString()) - minVol) * 9) / (maxVol - minVol)))));
                            linearizedCoinHistory.setDate(candle.getDate());
                            linearizedCandles.add(linearizedCoinHistory);
                        }

                        //collect arrays
                        CoinHistoryJson currentCandle = coinHistoryList.get(iteration);
                        double[] changesArray = new double[5], possibleTPs = new double[5], possibleSLs = new double[5];
                        for (int arrayIterator = 0; arrayIterator < 5; arrayIterator++) {
                            CoinHistoryJson iterationHistory = coinHistoryList.get(iteration + arrayIterator + 1);
                            //changes
                            changesArray[arrayIterator] = (iterationHistory.getClose().doubleValue() - currentCandle.getClose().doubleValue()) / currentCandle.getClose().doubleValue();
                            //tps
                            possibleTPs[arrayIterator] = (iterationHistory.getHigh().doubleValue() - currentCandle.getClose().doubleValue()) / (currentCandle.getClose().doubleValue());
                            possibleSLs[arrayIterator] = Doubles.max(possibleTPs);
                            //sls
                            possibleSLs[arrayIterator] = (iterationHistory.getLow().doubleValue() - currentCandle.getClose().doubleValue()) / currentCandle.getClose().doubleValue();
                            possibleSLs[arrayIterator] = Doubles.min(possibleSLs);
                        }
                        //set data
                        snapshotData.setCoinName(currentCandle.getCoinId());
                        snapshotData.setCandles(linearizedCandles);
                        snapshotData.setShape(generateShapeData(snapshotData));
                        snapshotData.setVolumeShape(generateVolumeShape(snapshotData));
                        snapshotData.setDate(currentCandle.getDate());
                        snapshotData.setChanges(changesArray);
                        snapshotData.setPossibleTPs(possibleTPs);
                        snapshotData.setPossibleSLs(possibleSLs);
                        snapshotData.setImgShape(imgShape);
                        data.add(snapshotData);
                    }
                }
            }
        }

        return data;
    }

    @Override
    public double[] generateShapeData(SnapshotData data){
        double[] closes = new double [data.getCandles().size()];
        for (int iteration = 0; iteration < data.getCandles().size(); iteration ++){
            closes[iteration] = data.getCandles().get(iteration).getClose();
        }
        return closes;
    }

    public double[] generateVolumeShape(SnapshotData data){
        double[] closes = new double [data.getCandles().size()];
        for (int iteration = 0; iteration < data.getCandles().size(); iteration ++){
            closes[iteration] = data.getCandles().get(iteration).getVolume();
        }
        return closes;
    }

    @Override
    public double[] getShape(List<CoinHistoryJson> history) {

        //var
        double[] shape = new double[history.size()], highs = new double[history.size()],lows = new double[history.size()],closes = new double[history.size()];

        //collect
        for (int iterator = 0; iterator < history.size(); iterator ++) {
            highs[iterator] = history.get(iterator).getHigh().doubleValue();
            closes[iterator] = history.get(iterator).getClose().doubleValue();
            lows[iterator] = history.get(iterator).getLow().doubleValue();
        }
        //high and lows
        double max = Doubles.max(highs), min = Doubles.min(lows);
        //fulfill
        for (int iterator = 0; iterator < history.size(); iterator ++) {
            shape[iterator] =  (int) Math.round((0 + (((closes[iterator]) - min) * 9) / (max - min)));
        }
        return shape;
    }

    public double[] getvShape(List<CoinHistoryJson> history) {

        //var
        double[] shape = new double[history.size()], vols = new double[history.size()];

        //collect
        for (int iterator = 0; iterator < history.size(); iterator ++) {
            vols[iterator] = history.get(iterator).getVolumeUSD24h().doubleValue();
        }
        //high and lows
        double max = Doubles.max(vols), min = Doubles.min(vols);
        //fulfill
        for (int iterator = 0; iterator < history.size(); iterator ++) {
            shape[iterator] =  (int) Math.round((0 + (((vols[iterator]) - min) * 9) / (max - min)));
        }
        return shape;
    }

    /*@Override
    public void generateAndSaveSnapshotImage(SnapshotData data, String imageName, int label) {

        temporary inutilized

        //image dimensions
        int width =  data.getCandles().size()*3;
        int height =  data.getCandles().size()*3;
        int bodySize =  3;
        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //file object
        File f = null;
        int currentX=width-1;


        //make it white just to get started
        makeItWhite(img, width, height);

        //list
        List<LinearizedCoinHistory> candles = data.getCandles();

        //create candle by candle
        for(LinearizedCoinHistory candle : candles){
            boolean isGreen = (candle.getClose()>candle.getOpen()) ? true : false;
            String color = isGreen ? "green" : "red";
            int yMaxBody = isGreen ? candle.getClose() : candle.getOpen();
            int yMinBody = isGreen ? candle.getOpen() : candle.getClose();
            int yMaxUpperShadow = candle.getHigh();
            int yMinUpperShadow = yMaxBody;
            int yMaxLowerShadow = yMinBody;
            int yMinLowerShadow = candle.getLow();
            int yVol = candle.getVolume();
            int yDistance = height/2;


            //iterate through candle body width
            for(int candleX=bodySize;candleX>0;candleX--){

                //set volume
                for(int volY=width-1;volY>width-1-yVol;volY--){
                    img.setRGB(currentX, volY, getColor(color));
                }
                //set body
                for(int bodyY=width-1-yMinBody;bodyY>width-1-yMaxBody;bodyY--){
                    img.setRGB(currentX, bodyY-yDistance, getColor(color));
                }

                //set shadows
                if(candleX==2){
                    //set upper shadow
                    for(int upperShadowY=width-1-yMinUpperShadow;upperShadowY>width-1-yMaxUpperShadow;upperShadowY--){
                        img.setRGB(currentX, upperShadowY-yDistance, getColor("black"));
                    }
                    //set lower shadow
                    for(int lowerShadowY=width-1-yMinLowerShadow;lowerShadowY>width-1-yMaxLowerShadow;lowerShadowY--){
                        img.setRGB(currentX, lowerShadowY-yDistance, getColor("black"));
                    }
                }
                currentX--;
            }

        }

        //write image
        try{
            //images
            File dir = new File("C:\\DeepLearning-Images\\clusters\\label"+label);
            dir.mkdirs();
            File file = new File(dir, imageName+".jpeg");

            ImageIO.write(img, "jpeg", file);

        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }*/

    @Override
    public void generateAndSaveSimplifiedSnapshotImage(double[] shapeForImg, String imageName, int label) {
        //image dimensions
        int width =  shapeForImg.length*4;
        int height =  shapeForImg.length*4;

        //create buffered image object img
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //file object
        File f = null;

        //make it white just to get started
        makeItWhite(img, width, height);
        //fullfill the points
        for (int x = 0; x < width; x ++){
            for (int y = 0; y < height; y ++){
                if (x%4 == 0 && y%4 == 0) {
                    if (shapeForImg[x/4] == y/4){
                        img.setRGB(x, y, getColor("black"));
                        if (x+2<width){
                            img.setRGB(x+2, y, getColor("black"));
                            if (y+2<height) img.setRGB(x+2, y+2, getColor("black"));
                            if (y+1<height) img.setRGB(x+2, y+1, getColor("black"));
                            if (y-1>0) img.setRGB(x+2, y-1, getColor("black"));
                            if (y-2>0) img.setRGB(x+2, y-2, getColor("black"));
                        }
                        if (x-2>0){
                            img.setRGB(x-2, y, getColor("black"));
                            if (y+2<height) img.setRGB(x-2, y+2, getColor("black"));
                            if (y+1<height) img.setRGB(x-2, y+1, getColor("black"));
                            if (y-2>0) img.setRGB(x-2, y-2, getColor("black"));
                            if (y-1>0) img.setRGB(x-2, y-1, getColor("black"));
                        }

                        if (x+1<width){
                            img.setRGB(x+1, y, getColor("black"));
                            if (y+2<height) img.setRGB(x+1, y+2, getColor("black"));
                            if (y+1<height) img.setRGB(x+1, y+1, getColor("black"));
                            if (y-1>0) img.setRGB(x+1, y-1, getColor("black"));
                            if (y-2>0) img.setRGB(x+1, y-2, getColor("black"));
                        }
                        if (x-1>0){
                            img.setRGB(x-1, y, getColor("black"));
                            if (y+2<height) img.setRGB(x-1, y+2, getColor("black"));
                            if (y+1<height) img.setRGB(x-1, y+1, getColor("black"));
                            if (y-2>0) img.setRGB(x-1, y-2, getColor("black"));
                            if (y-1>0) img.setRGB(x-1, y-1, getColor("black"));
                        }
                        if (y+2<height) {
                            img.setRGB(x, y+2, getColor("black"));
                            if (x-2>0)  img.setRGB(x-2, y+2, getColor("black"));
                        }
                        if (y-2>0) {
                            img.setRGB(x, y-2, getColor("black"));
                            if (x+2<height)  img.setRGB(x+2, y-2, getColor("black"));
                        }
                        if (y+1<height) {
                            img.setRGB(x, y+1, getColor("black"));
                            if (x+2<width)  img.setRGB(x+2, y+1, getColor("black"));
                            if (x-2>0)  img.setRGB(x-2, y+1, getColor("black"));
                        }
                        if (y-1>0) {
                            img.setRGB(x, y-1, getColor("black"));
                            if (x+2<width)  img.setRGB(x+2, y-1, getColor("black"));
                            if (x-2>0)  img.setRGB(x-2, y-1, getColor("black"));
                        }
                    }
                }
            }
        }
        //write image
        try{
            //images
            File dir = new File("C:\\DeepLearning-Images\\clusters\\label"+label);
            dir.mkdirs();
            File file = new File(dir, imageName+".jpeg");

            ImageIO.write(img, "jpeg", file);

        }catch(IOException e){
            System.out.println("Error: " + e);
        }
    }

    @Override
    public void makeItWhite(BufferedImage img, int width, int height) {
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                img.setRGB(x, y, getColor("white"));
            }
        }
    }

    @Override
    public int getColor(String colorName) {
        //black
        if(colorName.equals("black")){
            int r = 0;
            int g = 0;
            int b = 0;
            return  (r << 16) | (g << 8) | b;
        }
        //red
        if(colorName.equals("red")){
            int r = 252;
            int g = 45;
            int b = 45;
            return  (r << 16) | (g << 8) | b;
        }
        //green
        if(colorName.equals("green")){
            int r = 44;
            int g = 247;
            int b = 44;
            return  (r << 16) | (g << 8) | b;
        }
        //white
        if(colorName.equals("white")){
            int r = 255;
            int g = 255;
            int b = 255;
            return  (r << 16) | (g << 8) | b;
        }

        return 0;
    }
}
