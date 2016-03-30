package Model;

import Utils.BOMUtils;
import org.joda.time.LocalDateTime;

public final class Reading
{
    private final LocalDateTime dateTime;

    private final float latitude;
    private final float longitude;

    private final float airTemp;
    private final float apparentTemp;
    private final float dewPt;
    private final int relativeHumidity;
    private final float deltaTemp;

    private final String cloud;
    private final String cloudType;

    private final String windDir;
    private final int windSpdKmH;
    private final int windSpdKts;
    private final int windGustKmH;
    private final int windGustKts;

    private final float pressureQNH;
    private final float pressureMSL;

    private final float rainTrace;


    public Reading(String dateTime, float latitude, float longitude,
                   float airTemp, float apparentTemp, float dewPt, int relativeHumidity, float deltaTemp,
                   String cloud, String cloudType, String windDir, int windSpdKmH, int windSpdKts, int windGustKmH, int windGustKts,
                   float pressureQNH, float pressureMSL, float rainTrace)
    {
        this.dateTime = BOMUtils.toDateTime(dateTime);

        this.latitude = latitude;
        this.longitude = longitude;

        this.airTemp = airTemp;
        this.apparentTemp = apparentTemp;
        this.dewPt = dewPt;
        this.relativeHumidity = relativeHumidity;
        this.deltaTemp = deltaTemp;

        this.cloud = cloud;
        this.cloudType = cloudType;

        this.windDir = windDir;
        this.windSpdKmH = windSpdKmH;
        this.windSpdKts = windSpdKts;
        this.windGustKmH = windGustKmH;
        this.windGustKts = windGustKts;

        this.pressureQNH = pressureQNH;
        this.pressureMSL = pressureMSL;

        this.rainTrace = rainTrace;
    }


    public final LocalDateTime getDateTime()
    {
        return dateTime;
    }

    public final float getLatitude()
    {
        return latitude;
    }
    public final float getLongitude()
    {
        return longitude;
    }

    public final float getAirTemp()
    {
        return airTemp;
    }
    public final float getApparentTemp()
    {
        return apparentTemp;
    }
    public final float getDewPt()
    {
        return dewPt;
    }
    public final int getRelativeHumidity()
    {
        return relativeHumidity;
    }
    public final float getDeltaTemp()
    {
        return deltaTemp;
    }

    public final String getCloud()
    {
        return cloud;
    }
    public final String getCloudType()
    {
        return cloudType;
    }

    public final String getWindDir()
    {
        return windDir;
    }
    public final int getWindSpdKmH()
    {
        return windSpdKmH;
    }
    public final int getWindSpdKts()
    {
        return windSpdKts;
    }
    public final int getWindGustKmH()
    {
        return windGustKmH;
    }
    public final int getWindGustKts()
    {
        return windGustKts;
    }

    public final float getPressureQNH()
    {
        return pressureQNH;
    }
    public final float getPressureMSL()
    {
        return pressureMSL;
    }

    public final float getRainTrace()
    {
        return rainTrace;
    }
}
