package com.panda.zh.erp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 *
 * @author Panda.Z
 */
@SuppressWarnings({"UnusedDeclaration", "SpellCheckingInspection"})
public class TimeUtil {

    public final static String TIME_FORMAT = "HH:mm:ss";
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public final static String ISO_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * @return yyyy-MM-dd'T'HH:mm:ssZ
     */
    public static String getISOTime(Date date) {
        return new SimpleDateFormat(ISO_TIME_FORMAT).format(date);
    }

    /**
     * @param time yyyy-MM-dd'T'HH:mm:ssZ
     * @throws ParseException
     */
    public static Date getISOTime(String time) throws ParseException {
        return new SimpleDateFormat(ISO_TIME_FORMAT).parse(time);
    }

    /**
     * @return HH:mm:ss
     */
    public static String getTime(Date date) {
        return new SimpleDateFormat(TIME_FORMAT).format(date);
    }

    /**
     * @param time HH:mm:ss
     * @throws ParseException
     */
    public static Date getTime(String time) throws ParseException {
        return new SimpleDateFormat(TIME_FORMAT).parse(time);
    }

    /**
     * @return yyyy-MM-dd
     */
    public static String getDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    /**
     * @param date yyyy-MM-dd
     * @throws ParseException
     */
    public static Date getDate(String date) throws ParseException {
        return new SimpleDateFormat(DATE_FORMAT).parse(date);
    }

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getDateTime(Date date) {
        return new SimpleDateFormat(DATE_TIME_FORMAT).format(date);
    }


    /**
     * @param dateTime yyyy-MM-dd HH:mm:ss
     * @throws ParseException
     */
    public static Date getDateTime(String dateTime) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_FORMAT).parse(dateTime);
    }
}
