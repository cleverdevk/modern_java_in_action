package chapters;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Date;
import java.util.Locale;

public class ChapterTwelve {

    public static class NextWorkingDay implements TemporalAdjuster {

        @Override
        public Temporal adjustInto(Temporal temporal) {
            Temporal ret = temporal;
            while (ret.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SATURDAY.getValue() ||
                    ret.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
                ret = ret.plus(1, ChronoUnit.DAYS);
            }
            return ret;
        }
    }

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
        LocalTime timeFromString = LocalTime.parse("18:45:22");

        LocalDateTime ldt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
        LocalDateTime ldt2 = LocalDateTime.of(dateFromString, timeFromString);
        LocalDateTime ldt3 = date.atTime(13, 45, 20);
        LocalDateTime ldt4 = date.atTime(timeFromString);
        LocalDateTime ldt5 = timeFromString.atDate(date);

        Instant instant1 = Instant.now();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Instant instant2 = Instant.now();
        LocalDateTime dateTime = LocalDateTime.of(1994, Month.APRIL, 16, 15, 15);

        Duration duration = Duration.between(instant1, instant2);
        System.out.println(duration.getNano());

        Duration duration2 = Duration.between(ldt2, ldt1);
        System.out.println(duration2.getNano());

        Duration durationWithOfMinutes = Duration.ofMinutes(3);
        Duration durationWithChronoMinute = Duration.of(3, ChronoUnit.MINUTES);

        Period tenDays = Period.ofDays(10);
        Period twoYearsSixMonthsOneDay = Period.of(2, 6, 1);

        LocalDate date2 = date.withYear(2015);
        LocalDate date3 = date.withDayOfMonth(25);
        LocalDate date4 = date.with(ChronoField.MONTH_OF_YEAR, 2);

        LocalDate date5 = date.plus(6, ChronoUnit.MONTHS);

        LocalDate dateWithTemporalAdjusters = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));
        LocalDate dateWithTemporalAdjustersLastDay = date.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate dateWithCustomTemporalAdjusterWorkingDay = dateWithTemporalAdjusters.with(new NextWorkingDay());
        System.out.println(date);
        System.out.println(dateWithTemporalAdjusters);
        System.out.println(dateWithTemporalAdjustersLastDay);
        System.out.println(dateWithCustomTemporalAdjusterWorkingDay);

        String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        String s3 = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String s4WithLocale = date.format(DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN));

        DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(". ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.ITALIAN);

        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(s4WithLocale);
        System.out.println(date.format(italianFormatter));

        ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        ZonedDateTime zdt = date.atStartOfDay(seoulZone);
        ZonedDateTime zdtFromLdt = ldt1.atZone(seoulZone);
        ZonedDateTime zdtFromInstant = instant1.atZone(seoulZone);

        System.out.println(zdt);
        System.out.println(zdtFromLdt);
        System.out.println(zdtFromInstant);

        Date deprecatedDate = new Date();
        Instant insFromDate = deprecatedDate.toInstant();

        LocalDateTime dateTimeFromInstant = LocalDateTime.ofInstant(insFromDate, seoulZone);

        System.out.println(dateTimeFromInstant);
        // utc offset
        ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
        OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);
        System.out.println(dateTimeInNewYork);



    }


}
