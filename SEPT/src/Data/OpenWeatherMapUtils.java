package Data;

import Model.Forecast;
import Utils.FileUtils;
import Utils.Log;
import Utils.NetUtils;
import Utils.TextUtils;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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


    /**
     * Loads forecasts directly from the web.
     *
     * @param latitude the latitude of location
     * @param longitude the longitude of location
     * @return loaded forecasts
     * @throws IOException if any IO error occurs
     * @throws JSONException error parsing the data
     */
    public static List<Forecast> getNetForecasts(double latitude, double longitude) throws IOException, JSONException
    {
        String apiKey = "223aadb56b14736c60c8c712df70858e";
        String units = "units=metric";

        String url = String.format("http://api.openweathermap.org/data/2.5/forecast?APPID=%s&lat=%f&lon=%f&%s",
            apiKey, latitude, longitude, units);

        String src = NetUtils.get(url);
        tryCache(latitude, longitude, src);

        return createForecasts(src);
    }

    /**
     * Loads the cached version of forecasts locally. If it fails
     * {@code null} is returned.
     *
     * @param latitude the latitude of location
     * @param longitude the longitude of location
     * @return loaded forecasts or {@code null} if failed
     */
    public static List<Forecast> getCachedForecasts(double latitude, double longitude)
    {
        try
        {
            return createForecasts(loadCache(latitude, longitude));
        }
        catch (IOException|JSONException|NoSuchAlgorithmException e)
        {
            Log.info(OpenWeatherMapUtils.class, e.getMessage());
        }

        return null;
    }

    private static List<Forecast> createForecasts(String json) throws JSONException
    {
        List<Forecast> forecasts = new ArrayList<>();

        JSONObject obj = new JSONObject(json);
        JSONArray list = obj.getJSONArray("list");

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


    private static String loadCache(double latitude, double longitude) throws IOException, NoSuchAlgorithmException
    {
        return FileUtils.loadText(getCacheFilePath(latitude, longitude));
    }

    private synchronized static void tryCache(double latitude, double longitude, String json)
    {
        try
        {
            FileUtils.saveText(json, getCacheFilePath(latitude, longitude));
        }
        catch (IOException e)
        {
            Log.warn(OpenWeatherMapUtils.class, e.getMessage());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.severe(OpenWeatherMapUtils.class, e.getMessage());
        }
    }

    /**
     * The file path is a hash of the location info.
     */
    private static String getCacheFilePath(double latitude, double longitude) throws NoSuchAlgorithmException
    {
        String md5 = TextUtils.md5("OpenWeatherMap" + latitude + longitude);
        return "cache" + File.separator + md5 + ".json";
    }
}
