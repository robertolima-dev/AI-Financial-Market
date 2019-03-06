package com.mali.persistence.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class PowerRate {

    private String idcoin;
    private String category;
    private double pRate;
    private double oRate;
    private double wRate;
    private double eRate;
    private double rRate;
    private double weight;
    private Date lastUpdate;

    @Id
    @Column(name = "idcoin")
    public String getIdcoin() {
        return idcoin;
    }

    public void setIdcoin(String idcoin) {
        this.idcoin = idcoin;
    }

    @Column(name = "category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Column(name = "weight")
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    @Column(name = "p_rate")
    public Double getpRate() {
        return pRate;
    }

    public void setpRate(Double pRate) {
        this.pRate = pRate;
    }

    @Column(name = "o_rate")
    public Double getoRate() {
        return oRate;
    }

    public void setoRate(Double oRate) {
        this.oRate = oRate;
    }

    @Column(name = "w_rate")
    public Double getwRate() {
        return wRate;
    }

    public void setwRate(Double wRate) {
        this.wRate = wRate;
    }

    @Column(name = "e_rate")
    public Double geteRate() {
        return eRate;
    }

    public void seteRate(Double eRate) {
        this.eRate = eRate;
    }

    @Column(name = "r_rate")
    public Double getrRate() {
        return rRate;
    }

    public void setrRate(Double rRate) {
        this.rRate = rRate;
    }

    @Column(name = "last_update")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
