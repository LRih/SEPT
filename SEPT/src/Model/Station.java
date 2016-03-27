package Model;

public final class Station
{
    private final String name;
    private final String url;

    private StationData data; // stores station data once it is loaded from the web


    public Station(String name, String url)
    {
        this.name = name;
        this.url = url;
    }


    public final String getName()
    {
        return name;
    }
    public final String getUrl()
    {
        return url;
    }

    public final boolean hasData()
    {
        return data != null;
    }
}
