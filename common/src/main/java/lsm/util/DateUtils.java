package lsm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *  G 年代标志符
    y 年
    M 月
    d 日
    h 时 在上午或下午 (1~12)
    H 时 在一天中 (0~23)
    m 分
    s 秒
    S 毫秒
    E 星期
    D 一年中的第几天
    F 一月中第几个星期几
    w 一年中第几个星期
    W 一月中第几个星期
    a 上午 / 下午 标记符
    k 时 在一天中 (1~24)
    K 时 在上午或下午 (0~11)
    z 时区
    yyyy-MM-dd HH:mm:ss
 */
public class DateUtils {

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDD);

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);

    public static Date getDateAfterDay(Date date, int day){
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);

        return calendar.getTime();
    }

    public static Date getDateBeforeDay(Date date, int day){
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DATE, -day);

        return calendar.getTime();
    }

    public static Date getDateBeforeMinute(Date date, int minute){
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -minute);

        return calendar.getTime();
    }

    public static Date getDateAfterMinute(Date date, int minute){
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    public static String dateToString(Date date, String pattern){

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }

    /**
     * 获取指定日期的指定时间
     * @param date
     * @param hour
     * @return
     */
    public static Date getDateWithHour(Date date, int hour) {
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }


    public static Date getDateWithHourAndDay(Date date, int hour, int day) {
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static Date stringToDate(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getDayOfWeek(Date date){
        Calendar calendar =Calendar.getInstance();

        calendar.setTime(date);

        return calendar.get(Calendar.DAY_OF_WEEK) -1;
    }

    public static boolean isWeekend(Date date){
        if (getDayOfWeek(date) == 0 || getDayOfWeek(date) == 6) {
            return true;
        }
        return false;
    }

    /**
     * 时间是date2的0点到16点
     * @return
     */
    public static boolean isDate2Morning(Date date1, Date date2) {

        return date1.getTime() < getDateWithHour(date2, 16).getTime() && date1.getTime() > getDateWithHour(date2, 0).getTime();
    }

    /**
     * 时间是date2的16点到24点
     * @return
     */
    public static boolean isInDate2(Date date1, Date date2) {

        return date1.getTime() < getDateWithHourAndDay(date2, 0, 1).getTime() && date1.getTime() > getDateWithHour(date2, 0).getTime();
    }

    /**
     * 时间是date2的第二天0点到16点
     * @return
     */
    public static boolean isDateNextDayMorning(Date date1, Date date2) {

        return date1.getTime() < getDateWithHourAndDay(date2, 16, 1).getTime() && date1.getTime() > getDateWithHourAndDay(date2, 0, 1).getTime();
    }

    public static Date getStartOfDate(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfDate(Date date) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    public static Boolean isSameDay(Date fromTime, Date date) {

        if (Objects.equals(dateToString(fromTime, DateUtils.YYYYMMDD), dateToString(date, DateUtils.YYYYMMDD))) {
            return true;
        }

        return false;
    }

    public static Date getStartOfLastMonth(Date date) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getEndOfLastMonth(Date date) {

        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

}
