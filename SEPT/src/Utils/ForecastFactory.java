package Utils;

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
    // TODO document
    public static List<Forecast> getForecasts(double lat, double lon, Source src) throws IOException, JSONException
    {
        switch (src)
        {
            case ForecastIO:
                return ForecastIOUtils.getForecasts(lat, lon);
            case OpenWeatherMap:
                return OpenWeatherMapUtils.getForecasts(lat, lon);
            default:
                return new ArrayList<>();
        }
    }


    public enum Source
    {
        ForecastIO, OpenWeatherMap
    }
}
