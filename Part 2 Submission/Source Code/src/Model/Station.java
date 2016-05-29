package Model;

/**
 * Data structure for representing a station.
 */
public final class Station
{
    private final State state;
    private final String name;
    private final String url;

    /**
     * Used for fetching historical data.
     */
    private final String historicalId;


    /**
     * Creates a new station.
     *
     * @param state state in which station is contained
     * @param name name of station
     * @param url URL of JSON data from BOM
     * @param historicalId used for fetching historical from BOM
     */
    public Station(State state, String name, String url, String historicalId)
    {
        this.state = state;

        this.name = name;
        this.url = url;
        this.historicalId = historicalId;
    }


    public final State getState()
    {
        return state;
    }
    public final String getName()
    {
        return name;
    }
    public final String getUrl()
    {
        return url;
    }
    public final String getHistoricalId()
    {
        return historicalId;
    }

    public final String getKey()
    {
        return getKey(state.getName(), name);
    }

    /**
     * Key that uniquely identifies a station.
     *
     * @return the key
     * @param state name of state
     * @param station name of station
     */
    public static String getKey(String state, String station)
    {
        return state + "-" + station;
    }
}
