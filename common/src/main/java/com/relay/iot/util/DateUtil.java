package com.relay.iot.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil
{
    private static final String OFFSET_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    private DateUtil()
    {
        
    }

    public static boolean isValid(String date)
    {
        return isValid(date, OFFSET_DATE_TIME_FORMAT);
    }

    public static boolean isValid(String date, String dateFormat)
    {
        boolean valid = true;
        try {
            OffsetDateTime.parse(date, DateTimeFormatter.ofPattern(dateFormat));
        }
        catch (Exception e)
        {
            valid = false;
        }
        return valid;
    }

    public static OffsetDateTime toOffsetDateTime(String date)
    {
        return toOffsetDateTime(date, OFFSET_DATE_TIME_FORMAT);
    }

    public static OffsetDateTime toOffsetDateTime(String date, String dateFormat)
    {
        LocalDateTime datetime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(dateFormat));
        ZonedDateTime zoned = datetime.atZone(ZoneId.systemDefault());
        return zoned.toOffsetDateTime();
    }
}
