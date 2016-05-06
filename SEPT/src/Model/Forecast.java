package Model;

import org.joda.time.LocalDate;

// TODO document
public final class Forecast
{
    public final LocalDate date;

    public final double min;
    public final double max;

    public final String summary;
    public final String description;

    public Forecast(LocalDate date, double min, double max, String summary, String description)
    {
        this.date = date;
        this.min = min;
        this.max = max;
        this.summary = summary;
        this.description = description;
    }
}
