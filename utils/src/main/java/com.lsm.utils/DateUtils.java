package com.lsm.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author lishenming
 * @version 1.0
 * @date 2021/4/24 13:50
 **/

public class DateUtils {

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);

    public static final String YYYYMMDDHHMMSS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final DateTimeFormatter YYYYMMDDHHMMSS_SSS_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS_SSS);

    public static LocalDateTime parseToLocalDateTime(String time, DateTimeFormatter formatter) {
        return LocalDateTime.parse(time, formatter);
    }

    public static Long parseToTimeMillis(String time, DateTimeFormatter formatter) {
        LocalDateTime localDateTime = parseToLocalDateTime(time, formatter);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static LocalDateTime parseToLocalDateTime(long timestamp) {
        Timestamp time = Timestamp.from(Instant.ofEpochMilli(timestamp));

        return time.toLocalDateTime();
    }

    public static String parseToString(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime.format(formatter);
    }

    public static String parseToString(long timestamp, DateTimeFormatter formatter) {
        return parseToLocalDateTime(timestamp).format(formatter);
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = parseToLocalDateTime(System.currentTimeMillis());

        System.out.println(localDateTime);
    }
}
