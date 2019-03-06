package com.mali.crypfy.gateway.services.indexmanager.json;

import java.math.BigDecimal;

public class PlanJSON {

    private Integer idplan;
    private String name;
    private BigDecimal fee6;
    private BigDecimal fee12;
    private BigDecimal inputFee;
    private String logo;
    private String logo2;

    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFee6() {
        return fee6;
    }

    public void setFee6(BigDecimal fee6) {
        this.fee6 = fee6;
    }

    public BigDecimal getFee12() {
        return fee12;
    }

    public void setFee12(BigDecimal fee12) {
        this.fee12 = fee12;
    }

    public BigDecimal getInputFee() {
        return inputFee;
    }

    public void setInputFee(BigDecimal inputFee) {
        this.inputFee = inputFee;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }
}
