package Model;

import Data.BOMUtils;
import org.joda.time.LocalDateTime;

/**
 * Data structure for storing recent BOM station readings.
 */
public final class LatestReading
{
    public final LocalDateTime localDateTime;
    public final LocalDateTime utcDateTime;

    public final Double latitude;
    public final Double longitude;

    public final Double airTemp;
    public final Double apparentTemp;
    public final Double dewPt;
    public final Integer relativeHumidity;
    public final Double deltaTemp;

    public final String cloud;
    public final String cloudType;

    public final String windDir;
    public final Integer windSpdKmH;
    public final Integer windSpdKts;
    public final Integer windGustKmH;
    public final Integer windGustKts;

    public final Double pressureQNH;
    public final Double pressureMSL;

    public final String rainTrace;

    public LatestReading(String localDateTime, String utcDateTime, Double latitude, Double longitude, Double airTemp,
                         Double apparentTemp, Double dewPt, Integer relativeHumidity, Double deltaTemp, String cloud,
                         String cloudType, String windDir, Integer windSpdKmH, Integer windSpdKts, Integer windGustKmH,
                         Integer windGustKts, Double pressureQNH, Double pressureMSL, String rainTrace)
    {
        this.localDateTime = BOMUtils.toDateTime(localDateTime);
        this.utcDateTime = BOMUtils.toDateTime(utcDateTime);

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

    public final String toString()
    {
        return this.latitude + "," + this.longitude + "-" + this.cloudType + "-" + this.windDir + "-"
            + this.windSpdKmH + "-" + this.rainTrace;
    }
}
