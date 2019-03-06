package com.mali.persistence.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Portfolio {

    private Integer idportfolio;
    private String name;
    private Date created;
    private List<PowerRate> coins;
    private String type;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getIdportfolio() {
        return idportfolio;
    }

    public void setIdportfolio(Integer idportfolio) {
        this.idportfolio = idportfolio;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "created")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "portfolio_power_rate", joinColumns = @JoinColumn(name = "idportfolio", referencedColumnName = "idportfolio"),
            inverseJoinColumns = @JoinColumn(name = "idcoin", referencedColumnName = "idcoin"))
    public List<PowerRate> getCoins() {
        return coins;
    }

    public void setCoins(List<PowerRate> coins) {
        this.coins = coins;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
