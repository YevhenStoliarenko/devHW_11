package servlet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeZone {

    public static String getLocalTime(String gtm) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(java.util.TimeZone.getTimeZone(gtm));
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Date date;
        date = localDateFormat.parse(simpleDateFormat.format(new Date()));
        return String.valueOf(date);
    }

}
