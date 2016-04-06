package Utils;

import Model.*;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for data management.
 */
public final class DataManager
{
    private static String STATIONS_FILE_PATH = "stations.json";


    private DataManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /**
     * Loads list of states with station URLs from locally stored json file.
     *
     * @return states loaded from file
     * @throws IOException if there is an IO error of any sort
     */
    public static States loadStates() throws IOException
    {
        States states = new States();

        String json = FileUtils.loadText(DataManager.STATIONS_FILE_PATH);
        JSONArray jsonStates = new JSONArray(json);

        // iterate states
        for (int i = 0; i < jsonStates.length(); i++)
        {
            JSONObject jsonState = jsonStates.getJSONObject(i);

            State state = states.add(jsonState.getString("state"));

            // iterate stations
            JSONArray jsonStations = jsonState.getJSONArray("stations");
            for (int j = 0; j < jsonStations.length(); j++)
            {
                JSONObject jsonStation = jsonStations.getJSONObject(j);
                state.addStation(jsonStation.getString("city"), jsonStation.getString("url"));
            }
        }

        return states;
    }

    /**
     * Tries to load station data from various sources. First it tries to load
     * the most recent data from the web. If it fails it will try to load a
     * locally cached version. If this fails, {@code null} is returned.
     *
     * @return loaded station data
     * @param station the station for which to load data
     */
    public static StationData getStationData(Station station)
    {
        // try to load weather data from web
        try
        {
            String json = NetUtils.get(station.getUrl());
            trySaveCache(station, json); // cache data locally
            return createStationData(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // try to load weather data from cached file
        try
        {
            return createStationData(loadCache(station));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
    private static StationData createStationData(String json)
    {
        JSONObject obj = new JSONObject(json).getJSONObject("observations");
        JSONObject header = obj.getJSONArray("header").getJSONObject(0);

        return new StationData(header.getString("ID"), header.getString("main_ID"), header.getString("time_zone"), createReadings(obj.getJSONArray("data")));
    }

    private static List<Reading> createReadings(JSONArray arr)
    {
        List<Reading> readings = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++)
            readings.add(createReading(arr.getJSONObject(i)));

        return readings;
    }
    private static Reading createReading(JSONObject obj)
    {
        return new Reading(
            obj.getString("local_date_time_full"),
            obj.getString("aifstime_utc"),
            obj.isNull("lat") ? null : obj.getDouble("lat"),
            obj.isNull("lon") ? null : obj.getDouble("lon"),
            obj.isNull("air_temp") ? null : obj.getDouble("air_temp"),
            obj.isNull("apparent_t") ? null : obj.getDouble("apparent_t"),
            obj.isNull("dewpt") ? null : obj.getDouble("dewpt"),
            obj.isNull("rel_hum") ? null : obj.getInt("rel_hum"),
            obj.isNull("delta_t") ? null : obj.getDouble("delta_t"),
            obj.isNull("cloud") ? null : obj.getString("cloud"),
            obj.isNull("cloud_type") ? null : obj.getString("cloud_type"),
            obj.isNull("wind_dir") ? null : obj.getString("wind_dir"),
            obj.isNull("wind_spd_kmh") ? null : obj.getInt("wind_spd_kmh"),
            obj.isNull("wind_spd_kt") ? null : obj.getInt("wind_spd_kt"),
            obj.isNull("gust_kmh") ? null : obj.getInt("gust_kmh"),
            obj.isNull("gust_kt") ? null : obj.getInt("gust_kt"),
            obj.isNull("press_qnh") ? null : obj.getDouble("press_qnh"),
            obj.isNull("press_msl") ? null : obj.getDouble("press_msl"),
            obj.isNull("rain_trace") ? null : obj.getString("rain_trace")
        );
    }

    private static String loadCache(Station station) throws IOException
    {
        return FileUtils.loadText(getCacheFilePath(station));
    }
    private static void trySaveCache(Station station, String json)
    {
        try
        {
            FileUtils.saveText(json, getCacheFilePath(station));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String getCacheFilePath(Station station)
    {
        return "cache" + File.separator + station.getKey() + ".json";
    }

    /**
     * Convert BOM formatted date time to a form recognized by joda time.
     *
     * @return datetime object of BOM date
     * @param bomDt datetime string from BOM
     */
    public static LocalDateTime toDateTime(String bomDt)
    {
        String dt = bomDt.replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3T$4:$5:$6");
        return new LocalDateTime(dt);
    }
}
