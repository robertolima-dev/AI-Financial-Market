package com.crypfy.api;

import com.crypfy.api.json.ArbitrageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArbitrageProcessor {

    @Autowired
    BookProcessor processor;

    public ArbitrageData getMultipliers(double brlAmount, double brlForLtc){

        //general order: mercado btc, foxbit, negocie coins e bitcointoyou
        //btc buy
        double buyMbtc = processor.getBuyPrice(processor.getMBtcBook().get(1),brlAmount);
        double buyFox = processor.getBuyPrice(processor.getFoxbitBook().get(1),brlAmount);
        double buyNCoins = processor.getBuyPrice(processor.getNcoinsBook().get(1),brlAmount);
        double buyBtcToYou = processor.getBuyPrice(processor.getBtcToYouBook().get(1),brlAmount);

        //ltc buy
        double buyLtcMbtc = processor.getBuyPrice(processor.getMBtcLTCBook().get(1),brlForLtc);
        double buyLtcNCoins = processor.getBuyPrice(processor.getNcoinsLTCBook().get(1),brlForLtc);

        //mean and amount
        double btcMeanBuyPrice = (buyMbtc+buyFox+buyNCoins+buyBtcToYou)/4;
        double btcAmount = brlAmount/btcMeanBuyPrice;
        double ltcMeanBuyPrice = (buyLtcMbtc + buyLtcNCoins)/2;
        double ltcAmount = brlForLtc/ltcMeanBuyPrice;

        //btc sell
        double sellMbtc = processor.getSellPrice(processor.getMBtcBook().get(0),btcAmount);
        double sellFox = processor.getSellPrice(processor.getFoxbitBook().get(0),btcAmount);
        double sellNCoins = processor.getSellPrice(processor.getNcoinsBook().get(0),btcAmount);
        double sellBtcToYou = processor.getSellPrice(processor.getBtcToYouBook().get(0),btcAmount);

        //ltc sell
        double sellLtcMbtc = processor.getSellPrice(processor.getMBtcLTCBook().get(0),ltcAmount);
        double sellLtcNCoins = processor.getSellPrice(processor.getNcoinsLTCBook().get(0),btcAmount);

        //mean
        double btcMeanSellPrice = (sellMbtc+sellFox+sellNCoins+sellBtcToYou)/4;
        double ltcMeanSellPrice = (sellLtcMbtc+sellLtcNCoins)/2;

        //fullfill it
        ArbitrageData data = new ArbitrageData();

        //mercado btc to foxbit - iteration 1
        data.setmBtcToFoxbit(((sellFox-buyMbtc)/buyMbtc)*100);
        //mercado btc to negocie coins - iteration 2
        data.setmBtcToNCoins(((sellNCoins-buyMbtc)/buyMbtc)*100);
        // mercado btc to btctoyou - iteration 3
        data.setmBtcToBTY(((sellBtcToYou-buyMbtc)/buyMbtc)*100);

        //fox to mercado btc - iteration 4
        data.setFoxbitToMBtc(((sellMbtc-buyFox)/buyFox)*100);
        //fox to negocie coins - iteration 5
        data.setFoxbitToNCoins(((sellNCoins-buyFox)/buyFox)*100);
        //fox to btctoyou - iteration 6
        data.setFoxbitToBTY(((sellBtcToYou-buyFox)/buyFox)*100);

        //negocie coins to mercado btc - iteration 7
        data.setnCoinsToMBtc(((sellMbtc-buyNCoins)/buyNCoins)*100);
        //negocie coins to fox - iteration 8
        data.setnCoinsToFoxbit(((sellFox-buyNCoins)/buyNCoins)*100);
        //negocie coins to btctoyou - iteration 9
        data.setnCoinsToBTY(((sellBtcToYou-buyNCoins)/buyNCoins)*100);

        //btctoyou to mercado btc - iteration 10
        data.setbTYouToMBtc(((sellMbtc-buyBtcToYou)/buyBtcToYou)*100);
        //btctoyou to fox - iteration 11
        data.setbTYouToFoxbit(((sellFox-buyBtcToYou)/buyBtcToYou)*100);
        //btctoyou yo negocie coins - iteration 12
        data.setbTYouToNCoins(((sellNCoins-buyBtcToYou)/buyBtcToYou)*100);

        //ltc interactions
        data.setLtcMBtcToNCoins(((sellLtcNCoins-buyLtcMbtc)/buyLtcMbtc)*100);
        data.setLtcNCoinsToMBtc(((sellLtcMbtc-buyLtcNCoins)/buyLtcNCoins)*100);

        //set buy prices
        data.setmBtcBuyPrice(buyMbtc);
        data.setFoxbitBuyPrice(buyFox);
        data.setnCoinsBuyPrice(buyNCoins);
        data.setbTYBuyPrice(buyBtcToYou);
        data.setmBtcLtcBuyPrice(buyLtcMbtc);
        data.setnCoinsLtcBuyPrice(buyLtcNCoins);
        //set sell prices
        data.setmBtcSellPrice(sellMbtc);
        data.setFoxbitSellPrice(sellFox);
        data.setnCoinsSellPrice(sellNCoins);
        data.setbTYSellPrice(sellBtcToYou);
        data.setmBtcLtcSellPrice(sellLtcMbtc);
        data.setnCoinsLtcSellPrice(sellLtcNCoins);
        //set means
        data.setBtcMeanBuyPrice(btcMeanBuyPrice);
        data.setBtcMeanSellPrice(btcMeanSellPrice);
        data.setLtcMeanBuyPrice(ltcMeanBuyPrice);
        data.setLtcMeanSellPrice(ltcMeanSellPrice);

        return data;
    }
}
