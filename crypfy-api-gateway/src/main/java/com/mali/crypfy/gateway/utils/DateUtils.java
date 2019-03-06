package com.mali.crypfy.gateway.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;

public class DateUtils {

    private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat formatterFull = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static DateFormat formatterMonth = new SimpleDateFormat("/MM/yyyy");
    private static DateFormat formatterDayMonth = new SimpleDateFormat("dd/MM");

    public static Date getTodayDateWithoutTime() throws ParseException {
        Date today = new Date();
        Date todayWithZeroTime = formatter.parse(formatter.format(today));
        return todayWithZeroTime;
    }

    public static Date getDateWithoutDay(Date date) throws ParseException {
        return formatterMonth.parse(formatterMonth.format(date));
    }

    public static String getDateWithoutYearAsString(Date date) throws ParseException {
        return formatterDayMonth.format(date);
    }

    public static Date getDateWithoutHour(Date date) throws ParseException {
        String dateStr = formatter.format(date);
        Date date1 = formatter.parse(dateStr);
        return date1;
    }

    public static String dateWithoutDayAsString(Date date) {
        return formatterMonth.format(date);

    }

    public static String simpleDateToString(Date date){
        return formatter.format(date);
    }

    public static String dateToString(Date date){
        return formatterFull.format(date);
    }

    public static Date getFirstDayOfCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date addHoursFromNow(int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.HOUR_OF_DAY,hours);
        return c.getTime();
    }

    public static Date addSecondsFromNow(int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND,seconds);
        return c.getTime();
    }

    public static Date addHoursFromDate(int hours, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY,hours);
        return c.getTime();
    }

    public static Date addDaysFromDate(int days, Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,days);
        return c.getTime();
    }

    public static Date addMonthFromNow(int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH,months);
        return c.getTime();
    }

    public static Date addDaysFromNow(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH,days);
        return c.getTime();
    }

    public static int getCurrentYear(){
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c.get(Calendar.MONTH) + 1;
    }

    public static Date getLastDayOfCurrentMonth(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date setFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getLastDayOfMonth(int month,int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getFirstDayOfMonth(int month,int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getYesterdayDate() throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return formatter.parse(formatter.format(cal.getTime()));
    }

    public static Date getSundayOfWeek() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date monday = c.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(monday));
    }

    public static Date getSaturdayOfWeek() throws ParseException {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date sunday = c.getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return sunday = formatter.parse(formatter.format(sunday));
    }

    public static Date getFirstDayOfCurrentYear() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

}
