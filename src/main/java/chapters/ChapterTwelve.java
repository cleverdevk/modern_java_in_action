package chapters;

import java.time.*;
import java.time.temporal.ChronoField;

public class ChapterTwelve {
    public static void run() {
        LocalDate date = LocalDate.of(2021, 5, 13);
        int year = date.getYear();
        Month month = date.getMonth();
        int day = date.getDayOfMonth();
        DayOfWeek dow = date.getDayOfWeek();
        int len = date.lengthOfMonth();
        boolean leap = date.isLeapYear();

        LocalDate now = LocalDate.now();

        System.out.println(now);
        int cyear = now.get(ChronoField.YEAR);
        int cmonth = now.get(ChronoField.MONTH_OF_YEAR);
        int cday = now.get(ChronoField.DAY_OF_MONTH);

        System.out.println(String.format("%s %s %S", cyear, cmonth, cday));

        LocalDate dateFromString = LocalDate.parse("2017-09-21");
        LocalTime timeFromString = LocalTime.parse("13:45:22");

        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
        LocalDateTime ldt2 = LocalDateTime.of(date, timeFromString);
        LocalDateTime ldt3 = date.atTime(13, 45, 20);
        LocalDateTime ldt4 = date.atTime(timeFromString);
        LocalDateTime ldt5 = timeFromString.atDate(date);


    }


}
