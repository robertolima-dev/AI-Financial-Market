package com.mali.exchanger.communication.impl.okex.sub.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderHistory {

    private boolean result;
    private int total;
    private int currentPage;
    private List<OrderDetails> orders;
    private int pageLenght;
    private long errorCode;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @JsonProperty("currency_page")
    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<OrderDetails> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetails> orders) {
        this.orders = orders;
    }

    @JsonProperty("page_length")
    public int getPageLenght() {
        return pageLenght;
    }

    public void setPageLenght(int pageLenght) {
        this.pageLenght = pageLenght;
    }

    @JsonProperty("error_code")
    public long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }
}
