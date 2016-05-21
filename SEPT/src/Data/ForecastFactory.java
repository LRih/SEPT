package Data;

import Model.Forecast;
import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Utils.Log;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Factory method for getting forecast data.
 */
public class ForecastFactory
{
    /**
     * Get forecasts from the web from specified source.
     */
    public static List<Forecast> getNetForecasts(double lat, double lon, Source src) throws IOException, JSONException
    {
        switch (src)
        {
            case ForecastIO:
                return ForecastIOUtils.getNetForecasts(lat, lon);
            case OpenWeatherMap:
                return OpenWeatherMapUtils.getNetForecasts(lat, lon);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get cached forecasts from specified source.
     */
    public static List<Forecast> getCachedForecasts(Station station, Source src) throws JSONException
    {
        Log.info(ForecastFactory.class, "Loading cached forecast for " + station.getName());

        // first try to load cached data (we only need lat/lon info)
        StationData data = DataManager.getCachedStationData(station);

        // return fail if cached station data or lat/lon info. does not exist
        if (data == null || data.getLatestReadings().isEmpty())
        {
            Log.info(ForecastFactory.class, "Unable to load cached forecast for " + station.getName());
            return null;
        }

        LatestReading reading = data.getLatestReadings().get(0);
        List<Forecast> forecasts = getCachedForecasts(reading.latitude, reading.longitude, src);

        Log.info(DataManager.class, "Cached data successfully loaded for " + station.getName());

        return forecasts;
    }
    public static List<Forecast> getCachedForecasts(double lat, double lon, Source src) throws JSONException
    {
        switch (src)
        {
            case ForecastIO:
                return ForecastIOUtils.getCachedForecasts(lat, lon);
            case OpenWeatherMap:
                return OpenWeatherMapUtils.getCachedForecasts(lat, lon);
            default:
                return new ArrayList<>();
        }
    }


    public enum Source
    {
        ForecastIO, OpenWeatherMap
    }
}
