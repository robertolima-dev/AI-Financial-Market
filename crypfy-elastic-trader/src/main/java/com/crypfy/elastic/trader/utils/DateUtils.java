package com.crypfy.elastic.trader.utils;

import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class DateUtils {

    public long diffBetweenDates(Date oldestDate, Date newestDate,TimeUnit timeUnit){
        long diffInMillies = newestDate.getTime() - oldestDate.getTime();
        return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public Date addDayFromDate(Date date,int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,days);
        return c.getTime();
    }

    public Date addHourFromDate(Date date,int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY,hours);
        return c.getTime();
    }

    public Date addMinuteFromDate(Date date,int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE,minutes);
        return c.getTime();
    }


    public  Date getFirstDayOfNextMonth(Date date) {
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

    public  Date getFirstDayOfCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE,calendar.getActualMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND,calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND,calendar.getActualMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }
}
