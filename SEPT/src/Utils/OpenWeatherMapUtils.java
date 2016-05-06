package Utils;

import Model.Forecast;
import org.joda.time.LocalDate;
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
        // TODO cache
        List<Forecast> forecasts = new ArrayList<>();

        String apiKey = "223aadb56b14736c60c8c712df70858e";
        String units = "units=metric";

        String url = String.format("http://api.openweathermap.org/data/2.5/forecast?APPID=%s&lat=%f&lon=%f&%s",
            apiKey, latitude, longitude, units);

        JSONObject json = new JSONObject(NetUtils.get(url));
        JSONArray list = json.getJSONArray("list");

        LocalDate curDate = null;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_NORMAL;
        String summary = "";
        String description = "";

        // traverse readings
        for (int i = 0; i < list.length(); i++)
        {
            JSONObject entry = list.getJSONObject(i);
            JSONObject main = entry.getJSONObject("main");
            JSONArray weather = entry.getJSONArray("weather");

            LocalDate date = new LocalDate(entry.getLong("dt") * 1000);

            // if dates don't match, means next day
            if (curDate != null && date.getDayOfYear() != curDate.getDayOfYear())
            {
                forecasts.add(new Forecast(curDate, min, max, summary, description));
                min = Double.MAX_VALUE;
                max = Double.MIN_VALUE;
                summary = "";
                description = "";
            }

            curDate = date;

            // update min/max
            min = Math.min(min, main.getDouble("temp_min"));
            max = Math.max(max, main.getDouble("temp_max"));

            // get summary/descriptions
            if (summary.equals("") && weather.length() > 0)
                summary = weather.getJSONObject(0).getString("main");

            if (description.equals("") && weather.length() > 0)
                description = weather.getJSONObject(0).getString("description");
        }

        // add last day
        forecasts.add(new Forecast(curDate, min, max, summary, description));

        return forecasts;
    }
}
