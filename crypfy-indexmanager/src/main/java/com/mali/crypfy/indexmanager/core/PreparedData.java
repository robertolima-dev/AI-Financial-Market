package com.mali.crypfy.indexmanager.core;

import java.util.Date;

public class PreparedData {

    private Date lastDayOfPastFourMonths;
    private Date lastDayOfLastMonth;
    private Date lastDayOfPastTwoMonths;
    private Date lastTwoDays;
    private Date yesterday;
    private Date lastDayOfLastWeek;
    private Date lastDayOfTwoPastWeeks;
    private Date today;

    public Date getLastDayOfPastFourMonths() {
        return lastDayOfPastFourMonths;
    }

    public void setLastDayOfPastFourMonths(Date lastDayOfPastFourMonths) {
        this.lastDayOfPastFourMonths = lastDayOfPastFourMonths;
    }

    public Date getLastDayOfLastMonth() {
        return lastDayOfLastMonth;
    }

    public void setLastDayOfLastMonth(Date lastDayOfLastMonth) {
        this.lastDayOfLastMonth = lastDayOfLastMonth;
    }

    public Date getLastDayOfPastTwoMonths() {
        return lastDayOfPastTwoMonths;
    }

    public void setLastDayOfPastTwoMonths(Date lastDayOfPastTwoMonths) {
        this.lastDayOfPastTwoMonths = lastDayOfPastTwoMonths;
    }

    public Date getLastTwoDays() {
        return lastTwoDays;
    }

    public void setLastTwoDays(Date lastTwoDays) {
        this.lastTwoDays = lastTwoDays;
    }

    public Date getYesterday() {
        return yesterday;
    }

    public void setYesterday(Date yesterday) {
        this.yesterday = yesterday;
    }

    public Date getLastDayOfLastWeek() {
        return lastDayOfLastWeek;
    }

    public void setLastDayOfLastWeek(Date lastDayOfLastWeek) {
        this.lastDayOfLastWeek = lastDayOfLastWeek;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public Date getLastDayOfTwoPastWeeks() {
        return lastDayOfTwoPastWeeks;
    }

    public void setLastDayOfTwoPastWeeks(Date lastDayOfTwoPastWeeks) {
        this.lastDayOfTwoPastWeeks = lastDayOfTwoPastWeeks;
    }
}
