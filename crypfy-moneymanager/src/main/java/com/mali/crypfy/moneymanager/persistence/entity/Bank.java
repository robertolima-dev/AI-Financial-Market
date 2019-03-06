package com.mali.crypfy.moneymanager.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Domain Entity That Represents a BANK
 */
@Entity
public class Bank implements Serializable {

    private Integer idbank;
    private String name;
    private Integer code;
    private String logo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idbank")
    public Integer getIdbank() {
        return idbank;
    }

    public void setIdbank(Integer idbank) {
        this.idbank = idbank;
    }

    @Column(name = "name",length = 150)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code")
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Column(name = "logo",length = 500)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
