package com.crypfy.api.json;

import java.util.List;

public class NCoinsBook {

    private List<NCoinsBookDetails> bid;
    private List<NCoinsBookDetails> ask;

    public List<NCoinsBookDetails> getBid() {
        return bid;
    }

    public void setBid(List<NCoinsBookDetails> bid) {
        this.bid = bid;
    }

    public List<NCoinsBookDetails> getAsk() {
        return ask;
    }

    public void setAsk(List<NCoinsBookDetails> ask) {
        this.ask = ask;
    }
}
