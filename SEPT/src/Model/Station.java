package Model;

public final class Station
{
    private final String id;
    private final String mainId;
    private final String name;
    private final String url;

    private final float latitude;
    private final float longitude;
    private final float height;


    public Station(String id, String mainId, String name, String url, float latitude, float longitude, float height)
    {
        this.id = id;
        this.mainId = mainId;
        this.name = name;
        this.url = url;

        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }


    public String getId()
    {
        return id;
    }
    public String getMainId()
    {
        return mainId;
    }
    public String getName()
    {
        return name;
    }
    public String getUrl()
    {
        return url;
    }

    public float getLatitude()
    {
        return latitude;
    }
    public float getLongitude()
    {
        return longitude;
    }
    public float getHeight()
    {
        return height;
    }
}
