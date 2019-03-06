package com.mali.entity;

import com.mali.enumerations.DealType;

public class TradeResult {

    private double price;
    private double volumeBaseCoin;
    private double volumeToCoin;
    private Long time;
    private boolean success;
    private DealType type;
    private String uuid;
    private String msg;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolumeBaseCoin() {
        return volumeBaseCoin;
    }

    public void setVolumeBaseCoin(double volumeBaseCoin) {
        this.volumeBaseCoin = volumeBaseCoin;
    }

    public double getVolumeToCoin() {
        return volumeToCoin;
    }

    public void setVolumeToCoin(double volumeToCoin) {
        this.volumeToCoin = volumeToCoin;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public DealType getType() {
        return type;
    }

    public void setType(DealType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
