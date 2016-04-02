package Utils;

import Model.*;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/*
    Helper class for data management.
 */
public final class DataManager
{
    private static String STATIONS_FILE_PATH = "stations.json";


    private DataManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /* loads list of states with station URLs from locally stored json file */
    public static States loadStates() throws IOException
    {
        States states = new States();

        String json = FileUtils.loadText(DataManager.STATIONS_FILE_PATH);
        JSONArray jsonStates = new JSONArray(json);

        // iterate states
        for (int i = 0; i < jsonStates.length(); i++)
        {
            JSONObject jsonState = jsonStates.getJSONObject(i);

            State state = states.add(jsonState.getString("state"), jsonState.getString("state"));

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
        // TODO parse json into station data
        return new StationData(json, null, null, new ArrayList<Reading>());
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

    /* convert BOM formatted date time to a form recognized by joda time */
    public static LocalDateTime toDateTime(String bomDt)
    {
        String dt = bomDt.replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3T$4:$5:$6");
        return new LocalDateTime(dt);
    }
}
