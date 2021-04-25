package com.lsm.utils;

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

    public static LocalDateTime parseToLocalDateTime(String time, DateTimeFormatter formatter) {
        return LocalDateTime.parse(time, formatter);
    }

    public static Long parseToTimeMillis(String time, DateTimeFormatter formatter) {
        LocalDateTime localDateTime = parseToLocalDateTime(time, formatter);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
