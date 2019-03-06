package com.mali.crypfy.indexmanager.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class PlanOptions implements Serializable {

    private Integer idplanOptions;
    private Integer idplan;
    private Plan plan;
    private Integer duration;
    private BigDecimal perfomanceFee;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idplan_options")
    public Integer getIdplanOptions() {
        return idplanOptions;
    }

    public void setIdplanOptions(Integer idplanOptions) {
        this.idplanOptions = idplanOptions;
    }

    @Column(name = "idplan",insertable = false,updatable = false)
    public Integer getIdplan() {
        return idplan;
    }

    public void setIdplan(Integer idplan) {
        this.idplan = idplan;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idplan")
    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Column(name = "perfomance_fee",precision = 10, scale = 2)
    public BigDecimal getPerfomanceFee() {
        return perfomanceFee;
    }

    public void setPerfomanceFee(BigDecimal perfomanceFee) {
        this.perfomanceFee = perfomanceFee;
    }
}
