package Model;

import java.util.HashMap;

public final class StationData
{
    private final String id;
    private final String mainId;

    private final String timezone;

    HashMap<Long, Reading> readings; // hashed by datetime


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
}
