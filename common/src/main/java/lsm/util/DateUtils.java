package lsm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    /**
     * 获取起始时间
     * @param date
     * @return
     */
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

    /**
     * 获取结束时间
     * @param date
     * @return
     */
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

    /**
     * stringToDate
     * @param dateString
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date stringToDate(String dateString, String pattern) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.parse(dateString);
    }

    /**
     * dateToString
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern){

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        return sdf.format(date);
    }
}
