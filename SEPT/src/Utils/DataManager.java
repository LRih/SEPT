package Utils;

import Model.*;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for BOM data management.
 */
public final class DataManager {
	private static String STATIONS_FILE_PATH = "stations.json";
    private static int JSON_INDENT = 4;

	/**
	 * Number of past months to fetch for historical readings.
	 */
    private static int MONTHS_FETCH_COUNT = 3;

	private DataManager() {
		// disallow instantiating
		throw new AssertionError();
	}

	/**
	 * Loads list of states with station URLs from locally stored json file.
	 *
	 * @return states loaded from file
	 * @throws IOException
	 *             if there is an IO error of any sort
	 */
	public static States loadStates() throws IOException {
		States states = new States();

		String json = FileUtils.loadText(DataManager.STATIONS_FILE_PATH);
		JSONArray jsonStates = new JSONArray(json);

		// iterate states
		for (int i = 0; i < jsonStates.length(); i++) {
			JSONObject jsonState = jsonStates.getJSONObject(i);

			State state = states.add(jsonState.getString("state"));

			// iterate stations
			JSONArray jsonStations = jsonState.getJSONArray("stations");
			for (int j = 0; j < jsonStations.length(); j++) {
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
	 * @return loaded station data
	 * @param station
	 *            the station for which to load data
	 */
	public static StationData getStationData(Station station) {
		// try to load weather data from web
		try {
			return getNetStationData(station);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// try to load weather data from cached file
		try {
			return getCachedStationData(station);
        } catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Loads the most recent data from the web. If it fails {@code null} is
	 * returned.
	 *
	 * @return loaded station data
	 * @param station the station for which to load data
	 * @throws IOException
	 */
	public static StationData getNetStationData(Station station) throws IOException {
        String jsonMain = NetUtils.get(station.getUrl());
        tryCacheLatestData(station, jsonMain); // cache data locally

        String jsonHistorical = getHistoricalReadings(station);
        tryCacheHistoricalData(station, jsonHistorical); // cache data locally

        return createStationData(jsonMain, jsonHistorical);
	}

    /**
     * Loads the cached version of station data locally. If it fails
     * {@code null} is returned.
     *
     * @return loaded station data
     * @param station
     *            the station for which to load data
     * @throws IOException
     */
    public static StationData getCachedStationData(Station station) throws IOException {
        return createStationData(loadLatestCacheData(station), loadHistoricalCacheData(station));
    }


    /**
     * Obtain historical readings from BOM.
     */
    private static String getHistoricalReadings(Station station)
    {
        JSONArray json = new JSONArray();
        LocalDate date = LocalDate.now().minusMonths(MONTHS_FETCH_COUNT);

        // fetch historical csv data
        for (int i = 0; i < MONTHS_FETCH_COUNT; i++)
        {
            date = date.plusMonths(1);

            // ignore any unobtainable historical reading
            try
            {
                String csv = NetUtils.get(getHistoricalUrl(station, date.getYear(), date.getMonthOfYear()));
                JSONArray jsonMonth = createJSON(csv);

                // add all month readings to result
                for (int j = 0; j < jsonMonth.length(); j++)
                    json.put(jsonMonth.getJSONObject(j));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return json.toString(JSON_INDENT);
    }

    /**
     * Create json format from historical csv data.
     */
    private static JSONArray createJSON(String historicalCSV)
    {
        JSONArray json = new JSONArray();

        String[] lines = historicalCSV.split("\n");
        int dataLineIndex = -1;

        // find where data starts based on reference point
        for (int i = 0; i < lines.length; i++)
        {
            if (lines[i].startsWith(",\"Date\""))
            {
                dataLineIndex = i + 1;
                break;
            }
        }

        // no data found, return empty array
        if (dataLineIndex == -1)
            return json;

        // convert data lines into json objects
        for (int i = dataLineIndex; i < lines.length; i++)
        {
            if (lines[i].trim().isEmpty())
                continue;

            String[] data = lines[i].split(",", -1); // -1 allows empty values
            JSONObject obj = new JSONObject();
            obj.put("date", data[1]);
            obj.put("min", data[2].isEmpty() ? null : Double.parseDouble(data[2]));
            obj.put("max", data[3].isEmpty() ? null : Double.parseDouble(data[3]));
            obj.put("temp9AM", data[10].isEmpty() ? null : Double.parseDouble(data[10]));
            obj.put("temp3PM", data[16].isEmpty() ? null : Double.parseDouble(data[16]));

            json.put(obj);
        }

        return json;
    }


	/**
	 * Takes the chosen json file, puts into workable jsonobject and returns
	 * readable stationdata.
	 *
	 * @return stationdata for the chosen station - taken from json
	 * @param jsonMain the json string being passed into a jsonobject
	 * @param jsonHistorical the historical json string being passed into a jsonobject
	 */
	private static StationData createStationData(String jsonMain, String jsonHistorical) {
		JSONObject obj = new JSONObject(jsonMain).getJSONObject("observations");
		JSONObject header = obj.getJSONArray("header").getJSONObject(0);

		return new StationData(
            header.getString("ID"),
            header.getString("main_ID"),
            header.getString("time_zone"),
            createLatestReadings(obj.getJSONArray("data")),
            createHistoricalReadings(new JSONArray(jsonHistorical))
        );
	}

	/**
	 * Takes the jsonarray and grabs every value and orders it into the
	 * arraylist 'readings'.
	 *
	 * @return readings is the array list of all ordered values
	 * @param arr is the json array of raw values
	 */
	private static List<LatestReading> createLatestReadings(JSONArray arr) {
		List<LatestReading> readings = new ArrayList<>();

		for (int i = 0; i < arr.length(); i++)
			readings.add(createLatestReading(arr.getJSONObject(i)));

		return readings;
	}
	private static LatestReading createLatestReading(JSONObject obj) {
		return new LatestReading(obj.getString("local_date_time_full"), obj.getString("aifstime_utc"),
				obj.isNull("lat") ? null : obj.getDouble("lat"), obj.isNull("lon") ? null : obj.getDouble("lon"),
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
				obj.isNull("rain_trace") ? null : obj.getString("rain_trace"));
	}

    private static List<HistoricalReading> createHistoricalReadings(JSONArray arr) {
        List<HistoricalReading> readings = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++)
            readings.add(createHistoricalReading(arr.getJSONObject(i)));

        return readings;
    }
    private static HistoricalReading createHistoricalReading(JSONObject obj) {

        return new HistoricalReading(
            obj.getString("date"),
            obj.isNull("min") ? null : obj.getDouble("min"),
            obj.isNull("max") ? null : obj.getDouble("max"),
            obj.isNull("temp9AM") ? null : obj.getDouble("temp9AM"),
            obj.isNull("temp3PM") ? null : obj.getDouble("temp3PM")
        );
    }


	private static String loadLatestCacheData(Station station) throws IOException {
		return FileUtils.loadText(getLatestCacheFilePath(station));
	}
    private static String loadHistoricalCacheData(Station station) throws IOException {
        return FileUtils.loadText(getHistoricalCacheFilePath(station));
    }

    private synchronized static void tryCacheLatestData(Station station, String json) {
        try {
            FileUtils.saveText(json, getLatestCacheFilePath(station));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private synchronized static void tryCacheHistoricalData(Station station, String json) {
		try {
			FileUtils.saveText(json, getHistoricalCacheFilePath(station));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getLatestCacheFilePath(Station station) {
		return "cache" + File.separator + station.getKey() + ".json";
	}
    private static String getHistoricalCacheFilePath(Station station) {
        return "cache" + File.separator + station.getKey() + "-historical.json";
    }

    private static String getHistoricalUrl(Station station, int year, int month)
    {
        String date = String.valueOf(year) + String.format("%02d", month);
        return String.format("http://www.bom.gov.au/climate/dwo/%1$s/text/%2$s.%1$s.csv", date, station.getHistoricalId());
    }


	/**
	 * Convert BOM formatted date time to a form recognized by joda time.
	 *
	 * @return datetime object of BOM date
	 * @param bomDt
	 *            datetime string from BOM
	 */
	public static LocalDateTime toDateTime(String bomDt) {
		String dt = bomDt.replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3T$4:$5:$6");
		return new LocalDateTime(dt);
	}
}
