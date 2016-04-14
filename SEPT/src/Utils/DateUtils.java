package Utils;

import org.joda.time.DateTime;
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

    // TODO document
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
