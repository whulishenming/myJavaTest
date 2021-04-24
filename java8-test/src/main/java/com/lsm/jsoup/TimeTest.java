package com.lsm.jsoup;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static java.time.temporal.TemporalAdjusters.*;

@Slf4j
public class TimeTest {

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);

    /**
     * LocalDate代表一个IOS格式(yyyy-MM-dd)的日期
     */
    @Test
    public void testLocalDate() {

        /**
         * LocalDate 提供了三种创建实例的方法
         *  1. LocalDate date = LocalDate.of(2017, 5, 2);
         *  2. LocalDate date = LocalDate.parse("2017-05-02");
         *  3. LocalDate date = LocalDate.now();
          */
        LocalDate date = LocalDate.of(2020, 3, 9);

        int year = date.getYear();
        int monthValue = date.getMonthValue();
        int dayOfMonth = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfYear = date.getDayOfYear();
        int lengthOfMonth = date.lengthOfMonth();
        LocalDate newDate = date.withDayOfMonth(15).plusDays(4).minusDays(2);

        long start = LocalDate.of(2020, 1, 6).getLong(ChronoField.EPOCH_DAY);
        long epochDay = date.getLong(ChronoField.EPOCH_DAY);

        log.info("year:" + year + "--month:" + monthValue + "--day:" + dayOfMonth);
        log.info("date:" + date.toString());
        log.info("dayOfWeek:" + dayOfWeek.toString());
        log.info("dayOfYear:" + dayOfYear);
        log.info("lengthOfMonth:" + lengthOfMonth);
        log.info("newDate:" + newDate.toString());
        log.info("firstDayOfMonth:{}", date.with(firstDayOfMonth())); // 2017-05-01
        log.info("lastDayOfMonth:{}",date.with(lastDayOfMonth())); // 2017-05-31
        log.info("firstDayOfNextMonth:{}",date.with(firstDayOfNextMonth())); // 2017-06-01
        log.info("firstDayOfYear:{}",date.with(firstDayOfYear())); // 2017-01-01
        log.info("lastDayOfYear:{}",date.with(lastDayOfYear())); // 2017-12-31
        log.info("firstDayOfNextYear:{}",date.with(firstDayOfNextYear())); // 2018-01-01
        log.info("firstInMonth:{}",date.with(firstInMonth(DayOfWeek.MONDAY))); // 2017-05-01
        TemporalField fieldISO = WeekFields.of(Locale.FRANCE).dayOfWeek();
        log.info("firstInWeek:{}",date.with(fieldISO, 1)); //
        log.info("lastInMonth:{}",date.with(lastInMonth(DayOfWeek.MONDAY))); // 2017-05-29
        log.info("dayOfWeekInMonth:{}",date.with(dayOfWeekInMonth(2, DayOfWeek.MONDAY))); // 2017-05-08
        log.info("nextMONDAY:{}",date.with(next(DayOfWeek.MONDAY))); // 2017-05-08
        log.info("nextTUESDAY:{}",date.with(next(DayOfWeek.TUESDAY))); // 2017-05-09
        log.info("nextOrSameTUESDAY:{}",date.with(nextOrSame(DayOfWeek.TUESDAY))); // 2017-05-02
        log.info("previousTUESDAY:{}",date.with(previous(DayOfWeek.TUESDAY))); // 2017-04-25
        log.info("previousOrSameTUESDAY:{}",date.with(previousOrSame(DayOfWeek.TUESDAY))); // 2017-05-02
    }

    @Test
    public void testLocalTime() {
        /**
         * LocalTime 提供了三种创建实例的方法
         *  1. LocalTime localTime = LocalTime.now();
         *  2. LocalTime localTime2 = LocalTime.of(21, 30, 59, 11001);
         *  3. LocalTime localTime = LocalTime.parse("21:30:23");
          */
        LocalTime localTime = LocalTime.parse("06:23");
        LocalTime localTime2 = LocalTime.parse("6:23", DateTimeFormatter.ofPattern("H:mm"));
        /**
         * 通过这些方法访问其时、分、秒、纳秒
         */
        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        int second = localTime.getSecond();
        int nano = localTime.getNano();

        log.info("localTime:{}, hour:{}, minute:{}, second:{}, nano:{}", localTime, hour, minute, second, nano);

        /**
         * LocalTime的计算
         *  1. plusHours()
         *  2. plusMinutes()
         *  3. plusSeconds()
         *  4. plusNanos()
         *  5. minusHours()
         *  6. minusMinutes()
         *  7. minusSeconds()
         *  8. minusNanos()
         */
        LocalTime localTimeLater   = localTime.plusHours(3);

        int secondOfDay = localTime.toSecondOfDay();

        log.info("localTimeLater:{}", localTimeLater);
    }

    @Test
    public void testLocalDateTime() {
        /**
         *  LocalDateTime 提供了三种创建实例的方法
         *      1. LocalDateTime localDateTime = LocalDateTime.of(2017, 8, 9, 9, 22, 30, 555);
         *      2. LocalDateTime localDateTime = LocalDateTime.now();
         *      3. LocalDateTime localDateTime = LocalDateTime.parse("2017-08-09T09:22:30.555");
         */
        LocalDateTime localDateTime = LocalDateTime.parse("2017-08-09T09:22:30.555");
        LocalDateTime localDateTime2 = LocalDateTime.parse("2017-08-09 09:22:30", YYYYMMDDHHMMSS_FORMATTER);
        String s = localDateTime.toString();

        log.info("localDateTime:{}", localDateTime);
    }

    @Test
    public void toTime(){
        LocalDateTime now = LocalDateTime.now();
        Long second = now.toEpochSecond(ZoneOffset.of("+8"));

        log.info("second:{}", second);
        //获取毫秒数
        Long milliSecond = now.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        log.info("milliSecond:{}", milliSecond);

        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(milliSecond / 1000, (int) (milliSecond % 1000) * 1000, ZoneOffset.of("+8"));

        System.out.println(localDateTime);

    }

    /**
     * Date和Instant, LocalDateTime, LocalDate, LocalTime互相转换
     */
    @Test
    public void testToDate() {
        Date date = new Date();

        ZoneId zone = ZoneId.systemDefault();

        Instant instant = date.toInstant();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();

        log.info("instant:{}, localDateTime:{}, localDate:{}, localTime:{}", instant, localDateTime, localDate, localTime);

        Date fromInstant = Date.from(instant);
        Date fromLocalDateTime =  Date.from(localDateTime.atZone(zone).toInstant());
        Date fromLocalDate =  Date.from(localDate.atStartOfDay().atZone(zone).toInstant());
        Date fromLocalTime =  Date.from(localTime.atDate(LocalDate.now()).atZone(zone).toInstant());

        log.info("fromInstant:{}, fromLocalDateTime:{}, fromLocalDate:{}, fromLocalTime:{}", fromInstant, fromLocalDateTime, fromLocalDate, fromLocalTime);

        for (int i = 0; i < 100; i++) {
            System.out.println(new Random().nextInt(10));
        }

    }



}
