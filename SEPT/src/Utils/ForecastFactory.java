package Utils;

import Data.ForecastIOUtils;
import Data.OpenWeatherMapUtils;
import Model.Forecast;
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
