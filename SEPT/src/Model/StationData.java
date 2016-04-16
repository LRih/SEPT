package Model;

import Utils.DateUtils;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Object for storing information about a station obtained from BOM.
 */
public final class StationData
{
    private final String id;
    private final String mainId;

    private final String timezone;

    private final List<LatestReading> readings;
    private final List<HistoricalReading> historicalReadings;


    /**
     * Creates a station data instance.
     *
     * @param id BOM id of station
     * @param mainId BOM main id of station
     * @param timezone timezone of station
     * @param readings list of readings for station
     */
    public StationData(String id, String mainId, String timezone, List<LatestReading> readings, List<HistoricalReading> historicalReadings)
    {
        this.id = id;
        this.mainId = mainId;

        this.timezone = timezone;

        this.readings = Collections.unmodifiableList(readings);
        this.historicalReadings = Collections.unmodifiableList(historicalReadings);
    }


    public final String getId()
    {
        return id;
    }
    public final String getMainId()
    {
        return mainId;
    }

    public final String getTimezone()
    {
        return timezone;
    }

    public final List<LatestReading> getReadings()
    {
        return readings;
    }
    public final List<HistoricalReading> getHistoricalReadings()
    {
        return historicalReadings;
    }

    /**
     * Gets a list of the daily min temp readings.
     *
     * @return the list of filtered readings
     */
    @Deprecated
    public final List<LatestReading> getMinReadings()
    {
        List<LatestReading> results = new ArrayList<>();

        // no readings
        if (readings.size() == 0)
            return results;

        LatestReading min = null;

        for (LatestReading reading : readings)
        {
            // ignore readings with no temp data
            if (reading.getAirTemp() == null)
                continue;

            if (min == null)
            {
                min = reading;
                continue;
            }

            // new day so add current min and start finding min of next day
            if (DateUtils.isDifferentDay(min.getLocalDateTime(), reading.getLocalDateTime()))
            {
                results.add(min);
                min = reading;
            }
            else if (reading.getAirTemp() < min.getAirTemp())
                min = reading;
        }

        // add min of last day checked
        results.add(min);

        return results;
    }

    /**
     * Gets a list of the daily max temp readings.
     *
     * @return the list of filtered readings
     */
    @Deprecated
    public final List<LatestReading> getMaxReadings()
    {
        List<LatestReading> results = new ArrayList<>();

        // no readings
        if (readings.size() == 0)
            return results;

        LatestReading max = null;

        for (LatestReading reading : readings)
        {
            // ignore readings with no temp data
            if (reading.getAirTemp() == null)
                continue;

            if (max == null)
            {
                max = reading;
                continue;
            }

            // new day so add current min and start finding min of next day
            if (DateUtils.isDifferentDay(max.getLocalDateTime(), reading.getLocalDateTime()))
            {
                results.add(max);
                max = reading;
            }
            else if (reading.getAirTemp() > max.getAirTemp())
                max = reading;
        }

        // add min of last day checked
        results.add(max);

        return results;
    }

    /**
     * Gets a list of 9 AM readings.
     *
     * @return the list of filtered readings
     */
    @Deprecated
    public final List<LatestReading> get9AMReadings()
    {
        List<LatestReading> results = new ArrayList<>();

        for (LatestReading reading : readings)
        {
            LocalDateTime dt = reading.getLocalDateTime();
            if (dt.getHourOfDay() == 9 && dt.getMinuteOfHour() == 0)
                results.add(reading);
        }

        return results;
    }

    /**
     * Gets a list of 3 PM readings.
     *
     * @return the list of filtered readings
     */
    @Deprecated
    public final List<LatestReading> get3PMReadings()
    {
        List<LatestReading> results = new ArrayList<>();

        for (LatestReading reading : readings)
        {
            LocalDateTime dt = reading.getLocalDateTime();
            if (dt.getHourOfDay() == 15 && dt.getMinuteOfHour() == 0)
                results.add(reading);
        }

        return results;
    }
}
