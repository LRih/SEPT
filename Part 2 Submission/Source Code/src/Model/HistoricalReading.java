package Model;

import org.joda.time.LocalDate;

/**
 * Data structure for storing historical BOM daily station readings.
 */
public final class HistoricalReading
{
    public final LocalDate date;

    public final Double minTemp;
    public final Double maxTemp;

    public final Double rainfall;

    public final Double maxWindGustKmH;

    public final Double temp9AM;
    public final Integer relHumidity9AM;
    public final Integer windSpd9AM;
    public final Double pressureMSL9AM;

    public final Double temp3PM;
    public final Integer relHumidity3PM;
    public final Integer windSpd3PM;
    public final Double pressureMSL3PM;

    public HistoricalReading(String date, Double minTemp, Double maxTemp, Double rainfall, Double maxWindGustKmH,
                             Double temp9AM, Integer relHumidity9AM, Integer windSpd9AM, Double pressureMSL9AM,
                             Double temp3PM, Integer relHumidity3PM, Integer windSpd3PM, Double pressureMSL3PM)
    {
        this.date = new LocalDate(date);

        this.minTemp = minTemp;
        this.maxTemp = maxTemp;

        this.rainfall = rainfall;

        this.maxWindGustKmH = maxWindGustKmH;

        this.temp9AM = temp9AM;
        this.relHumidity9AM = relHumidity9AM;
        this.windSpd9AM = windSpd9AM;
        this.pressureMSL9AM = pressureMSL9AM;

        this.temp3PM = temp3PM;
        this.relHumidity3PM = relHumidity3PM;
        this.windSpd3PM = windSpd3PM;
        this.pressureMSL3PM = pressureMSL3PM;
    }
}
