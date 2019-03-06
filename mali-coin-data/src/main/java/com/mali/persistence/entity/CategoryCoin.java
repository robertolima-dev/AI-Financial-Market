package com.mali.persistence.entity;


import javax.persistence.*;

@Entity
public class CategoryCoin {

    private Integer idcategoryCoin;
    private String name;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Integer getIdcategoryCoin() {
        return idcategoryCoin;
    }

    public void setIdcategoryCoin(Integer idcategoryCoin) {
        this.idcategoryCoin = idcategoryCoin;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
