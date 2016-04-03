package Model;

// TODO document
public final class Station
{
    private final State state;
    private final String name;
    private final String url;


    // TODO document
    public Station(State state, String name, String url)
    {
        this.state = state;

        this.name = name;
        this.url = url;
    }


    // TODO document
    public final State getState()
    {
        return state;
    }
    // TODO document
    public final String getName()
    {
        return name;
    }
    // TODO document
    public final String getUrl()
    {
        return url;
    }


    // TODO document
    public final String getKey()
    {
        return state.getName() + "-" + name;
    }
}
