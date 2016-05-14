package Data;

import Model.*;
import Utils.FileUtils;
import Utils.Log;
import Utils.NetUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

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
                state.addStation(jsonStation.getString("city"), jsonStation.getString("url"), jsonStation.getString("historicalId"));
            }
        }

        return states;
    }


    /**
     * Tries to load station data from various sources. First it tries to load
     * the most recent data from the web. If it fails it will try to load a
     * locally cached version. If this fails, {@code null} is returned.
     *
     * @param station the station for which to load data
     * @return loaded station data
     */
    public static StationData getStationData(Station station)
    {
        // try to load weather data from web
        try
        {
            return getNetStationData(station);
        }
        catch (Exception e)
        {
            Log.warn(DataManager.class, e.getMessage());
        }

        // try to load weather data from cached file
        return getCachedStationData(station);
    }

    /**
     * Loads the most recent data from the web. If it fails {@code null} is
     * returned.
     *
     * @param station the station for which to load data
     * @return loaded station data
     * @throws IOException an I/O error of any sort
     * @throws JSONException error parsing json
     */
    public static StationData getNetStationData(Station station) throws IOException, JSONException
    {
        String jsonMain = NetUtils.get(station.getUrl());
        tryCacheLatestBOMData(station, jsonMain); // cache data locally

        String jsonHistorical = BOMUtils.getHistoricalReadings(station);
        tryCacheHistoricalBOMData(station, jsonHistorical); // cache data locally

        return createStationData(jsonMain, jsonHistorical);
    }

    /**
     * Loads the cached version of station data locally. If it fails
     * {@code null} is returned.
     *
     * @param station the station for which to load data
     * @return loaded station data or {@code null} if failed
     */
    public static StationData getCachedStationData(Station station)
    {
        try
        {
            return createStationData(loadLatestBOMCacheData(station), loadHistoricalBOMCacheData(station));
        }
        catch (IOException|JSONException e)
        {
            Log.info(DataManager.class, e.getMessage());
        }

        return null;
    }

    /**
     * Takes the chosen json file, puts into workable jsonobject and returns
     * readable stationdata.
     *
     * @param jsonLatestBOM       the json string being passed into a jsonobject
     * @param jsonHistoricalBOM the historical json string being passed into a jsonobject
     * @return stationdata for the chosen station - taken from json
     */
    private static StationData createStationData(String jsonLatestBOM, String jsonHistoricalBOM) throws JSONException
    {
        JSONObject obj = new JSONObject(jsonLatestBOM).getJSONObject("observations");
        JSONObject header = obj.getJSONArray("header").getJSONObject(0);

        return new StationData(
            header.getString("ID"),
            header.getString("main_ID"),
            header.getString("time_zone"),
            BOMUtils.createLatestReadings(obj.getJSONArray("data")),
            BOMUtils.createHistoricalReadings(new JSONArray(jsonHistoricalBOM))
        );
    }


    private static String loadLatestBOMCacheData(Station station) throws IOException
    {
        return FileUtils.loadText(getLatestBOMCacheFilePath(station));
    }
    private static String loadHistoricalBOMCacheData(Station station) throws IOException
    {
        return FileUtils.loadText(getHistoricalBOMCacheFilePath(station));
    }

    private synchronized static void tryCacheLatestBOMData(Station station, String json)
    {
        try
        {
            FileUtils.saveText(json, getLatestBOMCacheFilePath(station));
        }
        catch (IOException e)
        {
            Log.warn(DataManager.class, e.getMessage());
        }
    }
    private synchronized static void tryCacheHistoricalBOMData(Station station, String json)
    {
        try
        {
            FileUtils.saveText(json, getHistoricalBOMCacheFilePath(station));
        }
        catch (IOException e)
        {
            Log.warn(DataManager.class, e.getMessage());
        }
    }

    private static String getLatestBOMCacheFilePath(Station station)
    {
        return "cache" + File.separator + station.getKey() + ".json";
    }
    private static String getHistoricalBOMCacheFilePath(Station station)
    {
        return "cache" + File.separator + station.getKey() + "-historical.json";
    }

}
