package Model;

import java.util.List;

public final class StationData
{
    private final String id;
    private final String mainId;

    private final String timezone;

    private List<Reading> readings;


    public StationData(String id, String mainId, String timezone)
    {
        this.id = id;
        this.mainId = mainId;

        this.timezone = timezone;
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
    public final void setReading(List<Reading> readings)
    {
        this.readings = readings;
    }
}
