package Model;

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
}
