package Model;

// TODO document
public final class Station
{
    private final State state;
    private final String name;
    private final String url;


    /**
     * Creates a new station.
     *
     * @param state state in which station is contained
     * @param name name of station
     * @param url URL of JSON data from BOM
     */
    public Station(State state, String name, String url)
    {
        this.state = state;

        this.name = name;
        this.url = url;
    }


    /**
     * Getter for state.
     */
    public final State getState()
    {
        return state;
    }
    /**
     * Getter for name.
     */
    public final String getName()
    {
        return name;
    }
    /**
     * Getter for URL.
     */
    public final String getUrl()
    {
        return url;
    }


    /**
     * Key that uniquely identifies this station.
     *
     * @return the key
     */
    public final String getKey()
    {
        return state.getName() + "-" + name;
    }
}
