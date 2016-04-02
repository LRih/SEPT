package Model;

import Utils.DataManager;
import org.joda.time.LocalDateTime;

public final class Reading
{
    private final LocalDateTime localDateTime;
    private final LocalDateTime utcDateTime;

    private final Float latitude;
    private final Float longitude;

    private final Float airTemp;
    private final Float apparentTemp;
    private final Float dewPt;
    private final Integer relativeHumidity;
    private final Float deltaTemp;

    private final String cloud;
    private final String cloudType;

    private final String windDir;
    private final Integer windSpdKmH;
    private final Integer windSpdKts;
    private final Integer windGustKmH;
    private final Integer windGustKts;

    private final Float pressureQNH;
    private final Float pressureMSL;

    private final Float rainTrace;


    public Reading(String localDateTime, String utcDateTime, Float latitude, Float longitude,
                   Float airTemp, Float apparentTemp, Float dewPt, Integer relativeHumidity, Float deltaTemp,
                   String cloud, String cloudType, String windDir, Integer windSpdKmH, Integer windSpdKts, Integer windGustKmH, Integer windGustKts,
                   Float pressureQNH, Float pressureMSL, Float rainTrace)
    {
        this.localDateTime = DataManager.toDateTime(localDateTime);
        this.utcDateTime = DataManager.toDateTime(utcDateTime);

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


    public final LocalDateTime getLocalDateTime()
    {
        return localDateTime;
    }
    public final LocalDateTime getUTCDateTime()
    {
        return utcDateTime;
    }

    public final Float getLatitude()
    {
        return latitude;
    }
    public final Float getLongitude()
    {
        return longitude;
    }

    public final Float getAirTemp()
    {
        return airTemp;
    }
    public final Float getApparentTemp()
    {
        return apparentTemp;
    }
    public final Float getDewPt()
    {
        return dewPt;
    }
    public final Integer getRelativeHumidity()
    {
        return relativeHumidity;
    }
    public final Float getDeltaTemp()
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
    public final Integer getWindSpdKmH()
    {
        return windSpdKmH;
    }
    public final Integer getWindSpdKts()
    {
        return windSpdKts;
    }
    public final Integer getWindGustKmH()
    {
        return windGustKmH;
    }
    public final Integer getWindGustKts()
    {
        return windGustKts;
    }

    public final Float getPressureQNH()
    {
        return pressureQNH;
    }
    public final Float getPressureMSL()
    {
        return pressureMSL;
    }

    public final Float getRainTrace()
    {
        return rainTrace;
    }
}
