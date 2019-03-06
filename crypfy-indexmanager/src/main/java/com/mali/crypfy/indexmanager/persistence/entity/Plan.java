package com.mali.crypfy.indexmanager.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Plan implements Serializable {

    private Integer idplan;
    private String name;
    private BigDecimal fee6;
    private BigDecimal fee12;
    private BigDecimal inputFee;
    private String logo;
    private String logo2;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplan")
    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    @Column(name = "name",length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "fee6",precision = 10,scale = 2)
    public BigDecimal getFee6() {
        return fee6;
    }

    public void setFee6(BigDecimal fee6) {
        this.fee6 = fee6;
    }

    @Column(name = "fee12",precision = 10,scale = 2)
    public BigDecimal getFee12() {
        return fee12;
    }

    public void setFee12(BigDecimal fee12) {
        this.fee12 = fee12;
    }

    @Column(name = "input_fee",precision = 10,scale = 2)
    public BigDecimal getInputFee() {
        return inputFee;
    }

    public void setInputFee(BigDecimal inputFee) {
        this.inputFee = inputFee;
    }

    @Column(name = "logo",length = 500)
    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Column(name = "logo2",length = 500)
    public String getLogo2() {
        return logo2;
    }

    public void setLogo2(String logo2) {
        this.logo2 = logo2;
    }
}
