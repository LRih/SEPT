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

    private final List<LatestReading> latestReadings;
    private final List<HistoricalReading> historicalReadings;


    /**
     * Creates a station data instance.
     *
     * @param id BOM id of station
     * @param mainId BOM main id of station
     * @param timezone timezone of station
     * @param latestReadings list of latest readings for station
     * @param historicalReadings list of historical readings for station
     */
    public StationData(String id, String mainId, String timezone, List<LatestReading> latestReadings, List<HistoricalReading> historicalReadings)
    {
        this.id = id;
        this.mainId = mainId;

        this.timezone = timezone;

        this.latestReadings = Collections.unmodifiableList(latestReadings);
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

    public final List<LatestReading> getLatestReadings()
    {
        return latestReadings;
    }
    public final List<HistoricalReading> getHistoricalReadings()
    {
        return historicalReadings;
    }
}
