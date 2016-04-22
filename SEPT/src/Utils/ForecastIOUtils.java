package Utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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


    public static void printForecast(double latitude, double longitude) throws IOException, JSONException
    {
        // TODO create object for forecast
        // TODO handle what happens after 1000 free requests per day

        /* JSON data values:
            time: long
            summary: string
            sunriseTime: long
            sunsetTime: long
            moonPhase: double

            precipIntensity: double
            precipIntensityMax: double
            precipIntensityMaxTime: long
            precipProbability (%): double
            precipType: string

            temperatureMin: double
            temperatureMinTime: long
            temperatureMax: double
            temperatureMaxTime: long

            apparentTemperatureMin: double
            apparentTemperatureMinTime: long
            apparentTemperatureMax: double
            apparentTemperatureMaxTime: long

            dewPoint: double
            humidity (%): double
            windSpeed (km/h): double
            windBering (north=0): int
            cloudCover (%): double
            pressure (hPa): double
            ozone (DU): int
        */

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
            System.out.println("[" + new LocalDate(day.getLong("time") * 1000) + "] Min: " + day.getDouble("temperatureMin") + ", Max: " + day.getDouble("temperatureMax"));
        }
    }
}
