package Model;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

// TODO document
public final class Forecast
{
    public final LocalDateTime date;

    public final double min;
    public final double max;

    public final String summary;
    public final String description;

    public Forecast(LocalDateTime date, double min, double max, String summary, String description)
    {
        this.date = date;
        this.min = min;
        this.max = max;
        this.summary = summary;
        this.description = description;
    }
}
