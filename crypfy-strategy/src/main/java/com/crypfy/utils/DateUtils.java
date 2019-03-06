package com.crypfy.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DateUtils {

    public List<DateTime> getSundays(String start,String end){

        //smooth as fuck
        DateTimeFormatter pattern = DateTimeFormat.forPattern("dd/MM/yyyy");
        DateTime startDate = pattern.parseDateTime(start);
        DateTime endDate = pattern.parseDateTime(end);

        List<DateTime> sundays = new ArrayList<>();

        while (startDate.isBefore(endDate)){
            if ( startDate.getDayOfWeek() == DateTimeConstants.SUNDAY ){
                sundays.add(startDate);
            }
            startDate = startDate.plusDays(1);
        }
        return sundays;
    }

    public String datetimeToStringCoinMarketCap(DateTime datetime){

        String year = ""+datetime.year().get();
        String month = datetime.monthOfYear().get()>9 ? ""+datetime.monthOfYear().get() : "0"+datetime.monthOfYear().get();
        String day = datetime.dayOfMonth().get()>9 ? ""+datetime.dayOfMonth().get() : "0"+datetime.dayOfMonth().get();
        return year+month+day;
    }
}
