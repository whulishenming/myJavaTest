package lsm.java8;

import java.time.*;

import static java.time.temporal.TemporalAdjusters.*;

/**
 * Created by za-lishenming on 2017/5/2.
 */
public class TestTime {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2017, 5, 2);
        int year = date.getYear();
        int monthValue = date.getMonthValue();
        int dayOfMonth = date.getDayOfMonth();
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        int dayOfYear = date.getDayOfYear();
        int lengthOfMonth = date.lengthOfMonth();
        LocalDate newdate = date.withDayOfMonth(15).plusDays(4).minusDays(2);


        System.out.println("year:" + year + "--month:" + monthValue + "--day:" + dayOfMonth);
        System.out.println("date:" + date.toString());
        System.out.println("dayOfWeek:" + dayOfWeek.toString());
        System.out.println("dayOfYear:" + dayOfYear);
        System.out.println("lengthOfMonth:" + lengthOfMonth);
        System.out.println("newDate:" + newdate.toString());
        System.out.println(date.with(firstDayOfMonth())); // 2017-05-01
        System.out.println(date.with(lastDayOfMonth())); // 2017-05-31
        System.out.println(date.with(firstDayOfNextMonth())); // 2017-06-01
        System.out.println(date.with(firstDayOfYear())); // 2017-01-01
        System.out.println(date.with(lastDayOfYear())); // 2017-12-31
        System.out.println(date.with(firstDayOfNextYear())); // 2018-01-01
        System.out.println(date.with(firstInMonth(DayOfWeek.MONDAY))); // 2017-05-01
        System.out.println(date.with(lastInMonth(DayOfWeek.MONDAY))); // 2017-05-29
        System.out.println(date.with(dayOfWeekInMonth(2, DayOfWeek.MONDAY))); // 2017-05-08
        System.out.println(date.with(next(DayOfWeek.MONDAY))); // 2017-05-08
        System.out.println(date.with(next(DayOfWeek.TUESDAY))); // 2017-05-09
        System.out.println(date.with(nextOrSame(DayOfWeek.TUESDAY))); // 2017-05-02
        System.out.println(date.with(previous(DayOfWeek.TUESDAY))); // 2017-04-25
        System.out.println(date.with(previousOrSame(DayOfWeek.TUESDAY))); // 2017-05-02
        System.out.println(LocalDate.now()); // 2017-05-02

        System.out.println(LocalTime.now()); // 17:14:21.444
        System.out.println(LocalTime.of(17, 30,26)); //17:30:26
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.of(2017, 5, 2, 16, 40, 3));

    }


}
