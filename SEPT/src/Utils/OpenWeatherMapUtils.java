package Utils;

import Model.Forecast;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenWeatherMap related functions.
 */
public final class OpenWeatherMapUtils
{
    private OpenWeatherMapUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    public static List<Forecast> getForecasts(double latitude, double longitude) throws IOException, JSONException
    {
        List<Forecast> forecasts = new ArrayList<>();

        String apiKey = "223aadb56b14736c60c8c712df70858e";
        String units = "units=metric";

        String url = String.format("http://api.openweathermap.org/data/2.5/forecast?APPID=%s&lat=%f&lon=%f&%s",
            apiKey, latitude, longitude, units);

        JSONObject json = new JSONObject(NetUtils.get(url));
        JSONArray list = json.getJSONArray("list");

        for (int i = 0; i < list.length(); i++)
        {
            JSONObject entry = list.getJSONObject(i);
            JSONObject main = entry.getJSONObject("main");
            JSONArray weather = entry.getJSONArray("weather");

            forecasts.add(new Forecast(
                new LocalDateTime(entry.getLong("dt") * 1000),
                main.getDouble("temp_min"),
                main.getDouble("temp_max"),
                weather.length() > 0 ? weather.getJSONObject(0).getString("main") : "",
                weather.length() > 0 ? weather.getJSONObject(0).getString("description") : ""
            ));
        }

        return forecasts;
    }
}
