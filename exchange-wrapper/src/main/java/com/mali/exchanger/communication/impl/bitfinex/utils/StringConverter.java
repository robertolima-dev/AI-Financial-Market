package com.mali.exchanger.communication.impl.bitfinex.utils;

public class StringConverter {

    //bitfinex deposit methods
    public static String coinSymbolToMethod(String coinSymbol){

        //check through possible bitfinex deposit methods
        if(coinSymbol.equals("BTC")) return "bitcoin";
        if(coinSymbol.equals("LTC")) return "litecoin";
        if(coinSymbol.equals("ETH")) return "ethereum";
        if(coinSymbol.equals("USDT")) return "tetheruso";
        if(coinSymbol.equals("ETC")) return "ethereumc";
        if(coinSymbol.equals("ZEC")) return "zcash";
        if(coinSymbol.equals("XMR")) return "monero";
        if(coinSymbol.equals("IOTA")) return "iota";
        if(coinSymbol.equals("BCH")) return "bcash";

        return null;
    }

    //bitfinex withdraw_type
    public static String coinSymbolToWithdrawType(String coinSymbol){

        //check through possible bitfinex deposit methods
        if(coinSymbol.equals("BTC")) return "bitcoin";
        if(coinSymbol.equals("LTC")) return "litecoin";
        if(coinSymbol.equals("ETH")) return "ethereum";
        if(coinSymbol.equals("ETC")) return "ethereumc";
        if(coinSymbol.equals("ZEC")) return "zcash";
        if(coinSymbol.equals("XMR")) return "monero";
        if(coinSymbol.equals("DASH")) return "dash";
        if(coinSymbol.equals("XRP")) return "ripple";
        if(coinSymbol.equals("EOS")) return "eos";
        if(coinSymbol.equals("NEO")) return "neo";
        if(coinSymbol.equals("AVT")) return "aventus";
        if(coinSymbol.equals("QTUM")) return "qtum";
        if(coinSymbol.equals("EDO")) return "eidoo";

        return null;
    }

}
