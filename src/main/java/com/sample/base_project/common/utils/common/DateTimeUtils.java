package com.sample.base_project.common.utils.common;

import com.sample.base_project.common.exception.ErrorMessageUtils;
import org.springframework.lang.Nullable;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeUtils {

    public static final String DATE_TIME_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_STRING_FORMAT = "yyyy-MM-dd";
    public static final String HOUR_STRING_FORMAT = "HH";
    public static final String HOUR_MINUTE_STRING_FORMAT = "HH:mm";
    public static final String HOUR_MINUTE_SECOND_STRING_FORMAT = "HH:mm:ss";

    public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat(DATE_TIME_STRING_FORMAT);
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat(DATE_STRING_FORMAT);
    public static final DateFormat HOUR_FORMAT = new SimpleDateFormat(HOUR_STRING_FORMAT);
    public static final DateFormat HOUR_MINUTE_FORMAT = new SimpleDateFormat(HOUR_MINUTE_STRING_FORMAT);
    public static final DateFormat HOUR_MINUTE_SECOND_FORMAT = new SimpleDateFormat(HOUR_MINUTE_SECOND_STRING_FORMAT);

    static {
        // Set the time zone to UTC for DATE_FORMAT
        DATE_TIME_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        HOUR_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        HOUR_MINUTE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        HOUR_MINUTE_SECOND_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static DateFormat getCustomDateFormat(String format, ZoneId zoneId) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone(zoneId));
        return dateFormat;
    }

    public static DateFormat getCustomDateFormat(String format) {
        return getCustomDateFormat(format, ZoneId.of("UTC"));
    }

    public static DateFormat getDateTimeFormat(ZoneId zoneId) {
        return getCustomDateFormat(DATE_TIME_STRING_FORMAT, zoneId);
    }

    public static DateFormat getDateFormat(ZoneId zoneId) {
        return getCustomDateFormat(DATE_STRING_FORMAT, zoneId);
    }

    public static DateFormat getHourFormat(ZoneId zoneId) {
        return getCustomDateFormat(HOUR_STRING_FORMAT, zoneId);
    }

    public static DateFormat getHourMinuteFormat(ZoneId zoneId) {
        return getCustomDateFormat(HOUR_MINUTE_STRING_FORMAT, zoneId);
    }

    public static DateFormat getHourMinuteSecondFormat(ZoneId zoneId) {
        return getCustomDateFormat(HOUR_MINUTE_SECOND_STRING_FORMAT, zoneId);
    }

    public static @Nullable Timestamp convertToTimestamp(@Nullable String date) throws ParseException {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        return new Timestamp(convertToDate(date).getTime());
    }

    public static @Nullable Timestamp convertToTimestamp(@Nullable String date, String fieldErrorName) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        try {
            return new Timestamp(convertToDate(date).getTime());
        } catch (ParseException e) {
            throw ErrorMessageUtils.invalid(fieldErrorName);
        }
    }

    public static Date convertToDate(String date) throws ParseException {
        return DATE_TIME_FORMAT.parse(date);
    }

    public static String convertDateToDateTimeString(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }

    public static String convertDateToDateString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Long convertDateToHourLong(Date date) {
        return Long.parseLong(HOUR_FORMAT.format(date));
    }

    public static String convertDateToHourMinuteString(Date date) {
        return HOUR_MINUTE_FORMAT.format(date);
    }

    public static String convertDateToHourMinuteSecondString(Date date) {
        return HOUR_MINUTE_SECOND_FORMAT.format(date);
    }

    public static boolean isNewDay(Timestamp date) {
        ZoneId utc = ZoneId.of("UTC");
        return isNewDay(date, utc);
    }

    public static boolean isNewDay(Timestamp date, ZoneId zoneId) {
        if (date != null) {
            LocalDate currentDate = LocalDate.ofInstant(Instant.now(), zoneId);
            LocalDate checkInDate = date.toInstant().atZone(zoneId).toLocalDate();
            return checkInDate.isBefore(currentDate);
        } else {
            return true;
        }
    }
}
