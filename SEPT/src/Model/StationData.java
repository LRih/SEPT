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

    private List<Reading> readings;


    // TODO document
    public StationData(String id, String mainId, String timezone, List<Reading> readings)
    {
        this.id = id;
        this.mainId = mainId;

        this.timezone = timezone;

        this.readings = Collections.unmodifiableList(readings);
    }


    // TODO document
    public final String getId()
    {
        return id;
    }
    // TODO document
    public final String getMainId()
    {
        return mainId;
    }

    // TODO document
    public final String getTimezone()
    {
        return timezone;
    }

    // TODO document
    public final List<Reading> getReadings()
    {
        return readings;
    }
}
