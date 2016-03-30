package Model;

public final class Station
{
    private final State state;
    private final String name;
    private final String url;


    public Station(State state, String name, String url)
    {
        this.state = state;

        this.name = name;
        this.url = url;
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


    public final String getKey()
    {
        return state.getName() + "-" + name;
    }
}
