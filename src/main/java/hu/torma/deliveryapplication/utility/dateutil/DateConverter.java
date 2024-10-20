package hu.torma.deliveryapplication.utility.dateutil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateConverter {
    public static String toDottedDate(java.util.Date dt) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+01"));

        return dt == null ? "0000.01.01" : sdf.format(dt);
    }
}
