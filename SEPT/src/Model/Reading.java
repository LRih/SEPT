package Model;

public final class Reading
{
    private final long dateTime;

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


    public Reading(long dateTime, float airTemp, float apparentTemp, float dewPt, int relativeHumidity, float deltaTemp,
                   String cloud, String cloudType, String windDir, int windSpdKmH, int windSpdKts, int windGustKmH, int windGustKts,
                   float pressureQNH, float pressureMSL, float rainTrace)
    {
        this.dateTime = dateTime;

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
}
