package Model;

public final class Station
{
    private final Area area;
    private final String name;
    private final String url;

    private StationData data; // stores station data once it is loaded from the web


    public Station(Area area, String name, String url)
    {
        this.area = area;

        this.name = name;
        this.url = url;
    }


    public final Area getArea()
    {
        return area;
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


    public final String getKey()
    {
        return area.getState().getName() + "-" + area.getName() + "-" + name;
    }
}
