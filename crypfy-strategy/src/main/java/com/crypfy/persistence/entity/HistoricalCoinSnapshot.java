package com.crypfy.persistence.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class HistoricalCoinSnapshot {
    private Integer id;
    private String date;
    private Integer weakId;
    private Integer rank;
    private String name;
    private double cap;
    private double price;
    private double supply;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "date")
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Column(name = "weak_id")
    public Integer getWeakId() {
        return weakId;
    }

    public void setWeakId(Integer weakId) {
        this.weakId = weakId;
    }

    @Column(name = "rank")
    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "cap")
    public double getCap() {
        return cap;
    }

    public void setCap(double cap) {
        this.cap = cap;
    }

    @Column(name = "price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name = "supply")
    public double getSupply() {
        return supply;
    }

    public void setSupply(double supply) {
        this.supply = supply;
    }

}
