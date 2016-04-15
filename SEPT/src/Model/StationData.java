package Model;

import Utils.DateUtils;
import org.joda.time.Days;
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

    private final List<Reading> readings;


    /**
     * Creates a station data instance.
     *
     * @param id BOM id of station
     * @param mainId BOM main id of station
     * @param timezone timezone of station
     * @param readings list of readings for station
     */
    public StationData(String id, String mainId, String timezone, List<Reading> readings)
    {
        this.id = id;
        this.mainId = mainId;

        this.timezone = timezone;

        this.readings = Collections.unmodifiableList(readings);
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

    public final List<Reading> getReadings()
    {
        return readings;
    }

    /**
     * Gets a list of the daily min temp readings.
     *
     * @return the list of filtered readings
     */
    public final List<Reading> getMinReadings()
    {
        List<Reading> results = new ArrayList<>();

        // no readings
        if (readings.size() == 0)
            return results;

        Reading min = null;

        for (Reading reading : readings)
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
    public final List<Reading> getMaxReadings()
    {
        List<Reading> results = new ArrayList<>();

        // no readings
        if (readings.size() == 0)
            return results;

        Reading max = null;

        for (Reading reading : readings)
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
    public final List<Reading> get9AMReadings()
    {
        List<Reading> results = new ArrayList<>();

        for (Reading reading : readings)
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
    public final List<Reading> get3PMReadings()
    {
        List<Reading> results = new ArrayList<>();

        for (Reading reading : readings)
        {
            LocalDateTime dt = reading.getLocalDateTime();
            if (dt.getHourOfDay() == 15 && dt.getMinuteOfHour() == 0)
                results.add(reading);
        }

        return results;
    }
}
