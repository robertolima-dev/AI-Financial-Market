package com.mali.crypfy.gateway.services.moneymanager.json;

import java.io.Serializable;

public class BankJSON implements Serializable {

    private Integer idbank;
    private String name;
    private Integer code;
    private String logo;

    public Integer getIdbank() {
        return idbank;
    }

    public void setIdbank(Integer idbank) {
        this.idbank = idbank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
