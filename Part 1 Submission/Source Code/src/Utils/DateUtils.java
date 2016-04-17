package Utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Handles date based operations.
 */
public final class DateUtils
{
    private DateUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }

    /**
     * Checks if two dates are different days.
     *
     * @return different day if true
     * @param dt1 first date to check
     * @param dt2 second date to check
     */
    public static boolean isDifferentDay(LocalDateTime dt1, LocalDateTime dt2)
    {
        return dt1.getYear() != dt2.getYear() || dt1.getMonthOfYear() != dt2.getMonthOfYear() || dt1.getDayOfMonth() != dt2.getDayOfMonth();
    }

    /**
     * Converts date to ago string
     *
     * @return the string
     * @param time the date to convert
     */
    public static String toTimeAgo(DateTime time)
    {
        DateTime now = new DateTime();
        Period period = new Period(time, now);

        PeriodFormatter formatter = new PeriodFormatterBuilder()
            .appendSeconds().appendSuffix(" seconds")
            .appendMinutes().appendSuffix(" minutes")
            .appendHours().appendSuffix(" hours")
            .appendDays().appendSuffix(" days")
            .appendWeeks().appendSuffix(" weeks")
            .appendMonths().appendSuffix(" months")
            .appendYears().appendSuffix(" years")
            .printZeroNever()
            .toFormatter();

        return formatter.print(period) + " ago";
    }
}
