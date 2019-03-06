package com.mali.crypfy.indexmanager.utils;

import com.mali.crypfy.indexmanager.core.impl.PlanServiceImpl;
import org.omg.CORBA.DATA_CONVERSION;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    final static Logger logger = LoggerFactory.getLogger(DateUtils.class);


    public static Date getFirstDayOfNextMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }


    public static Date getLastMomentOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getLastMomentOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int leftDays= Calendar.SATURDAY - currentDay;
        calendar.add(Calendar.DATE,leftDays);
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date setFirstDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    public static Date setLastDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    public static Date getLastSunday() {
        Calendar cal = Calendar.getInstance();
        int dayOfTheWeek = cal.get( Calendar.DAY_OF_WEEK );
        cal.add( Calendar.DAY_OF_WEEK, Calendar.SUNDAY - dayOfTheWeek );
        return cal.getTime();
    }

    public static Date getFirstDayOfCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getFirstDayOfCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static int getMonthAsInteger(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public static int getYearAsInteger(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public static boolean isFirstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (cal.get(Calendar.DAY_OF_MONTH) == 1) ? true : false;
    }

    public static boolean isLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return (currentDay == lastDay) ? true : false;
    }


    public static String getIntMonthAsString(int month) {
        switch (month) {
            case 0 :
                return "Janeiro";
            case 1 :
                return "Fevereiro";
            case 2 :
                return "Março";
            case 3 :
                return "Abril";
            case 4 :
                return "Maio";
            case 5 :
                return "Junho";
            case 6 :
                return "Julho";
            case 7 :
                return "Agosto";
            case 8 :
                return "Setembro";
            case 9 :
                return "Outubro";
            case 10 :
                return "Novembro";
            case 11 :
                return "Dezembro";
        }

        return "";
    }

    public static Date addMonthFromNow(int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH,months);
        return c.getTime();
    }

    public static Date addMonthFromDate(Date date,int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,months);
        return c.getTime();
    }

    public static Date addHoursFromDate(Date date,int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR,hours);
        return c.getTime();
    }

    public static Date addDaysFromDate(Date date,int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,days);
        return c.getTime();
    }

    public static Date setFirstDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date setLastDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getDateWithoutTime(Date date) {
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            logger.error("parse date error",e);
            return null;
        }
    }
}
