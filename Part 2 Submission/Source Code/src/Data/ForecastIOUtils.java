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
 * Forecast.io related functions.
 */
public final class ForecastIOUtils
{
    private ForecastIOUtils()
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
        String apiKey = "7a2f97dff65c6b5301016808a0a9692a";
        String units = "units=ca";
        String exclude = "exclude=currently,minutely,hourly,alerts,flags";

        String url = String.format("https://api.forecast.io/forecast/%s/%f,%f?%s&%s",
            apiKey, latitude, longitude, units, exclude);

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
            Log.info(ForecastIOUtils.class, e.getMessage());
        }

        return null;
    }

    private static List<Forecast> createForecasts(String json) throws JSONException
    {
        List<Forecast> forecasts = new ArrayList<>();

        JSONObject obj = new JSONObject(json);
        JSONArray daily = obj.getJSONObject("daily").getJSONArray("data");

        // loop through days array and create forecast obj. for each
        for (int i = 0; i < daily.length(); i++)
        {
            JSONObject day = daily.getJSONObject(i);

            forecasts.add(new Forecast(
                new LocalDate(day.getLong("time") * 1000),
                day.getDouble("temperatureMin"),
                day.getDouble("temperatureMax"),
                day.getString("icon"),
                day.getString("summary")
            ));
        }

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
            Log.warn(ForecastIOUtils.class, e.getMessage());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.severe(ForecastIOUtils.class, e.getMessage());
        }
    }

    /**
     * The file path is a hash of the location info.
     */
    private static String getCacheFilePath(double latitude, double longitude) throws NoSuchAlgorithmException
    {
        String md5 = TextUtils.md5("ForecastIO" + latitude + longitude);
        return "cache" + File.separator + md5 + ".json";
    }
}
