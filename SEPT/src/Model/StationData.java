package Model;

public final class StationData
{
    private final String id;
    private final String mainId;

    private final float latitude;
    private final float longitude;
    private final float height;


    public StationData(String id, String mainId, float latitude, float longitude, float height)
    {
        this.id = id;
        this.mainId = mainId;

        this.latitude = latitude;
        this.longitude = longitude;
        this.height = height;
    }


    public final String getId()
    {
        return id;
    }
    public final String getMainId()
    {
        return mainId;
    }

    public final float getLatitude()
    {
        return latitude;
    }
    public final float getLongitude()
    {
        return longitude;
    }
    public final float getHeight()
    {
        return height;
    }
}
