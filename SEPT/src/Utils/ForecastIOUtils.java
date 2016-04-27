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
 * Forecast.io related functions.
 */
public final class ForecastIOUtils
{
    private ForecastIOUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    public static List<Forecast> getForecasts(double latitude, double longitude) throws IOException, JSONException
    {
        List<Forecast> forecasts = new ArrayList<>();

        // TODO cache
        // TODO handle what happens after 1000 free requests per day
        String apiKey = "7a2f97dff65c6b5301016808a0a9692a";
        String units = "units=ca";
        String exclude = "exclude=currently,minutely,hourly,alerts,flags";

        String url = String.format("https://api.forecast.io/forecast/%s/%f,%f?%s&%s",
            apiKey, latitude, longitude, units, exclude);

        JSONObject json = new JSONObject(NetUtils.get(url));
        JSONArray daily = json.getJSONObject("daily").getJSONArray("data");

        for (int i = 0; i < daily.length(); i++)
        {
            JSONObject day = daily.getJSONObject(i);

            forecasts.add(new Forecast(
                new LocalDateTime(day.getLong("time") * 1000),
                day.getDouble("temperatureMin"),
                day.getDouble("temperatureMax"),
                day.getString("icon"),
                day.getString("summary")
            ));
        }

        return forecasts;
    }
}
