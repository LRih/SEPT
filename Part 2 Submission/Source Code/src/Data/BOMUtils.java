package Data;

import Model.HistoricalReading;
import Model.LatestReading;
import Model.Station;
import Utils.Log;
import Utils.NetUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * BOM related functions.
 */
public final class BOMUtils
{
    private static final int JSON_INDENT = 4;

    /**
     * Number of past months to fetch for historical readings.
     */
    private static final int MONTHS_FETCH_COUNT = 3;


    private BOMUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /**
     * Obtain historical readings from BOM.
     */
    public static String getHistoricalReadings(Station station)
    {
        JSONArray json = new JSONArray();
        LocalDate date = LocalDate.now().minusMonths(MONTHS_FETCH_COUNT);

        // no historical data exists
        if (station.getHistoricalId().isEmpty())
            return json.toString(JSON_INDENT);

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
                Log.warn(BOMUtils.class, "[" + station.getName() + "] " + e.getMessage());
            }
        }

        return json.toString(JSON_INDENT);
    }

    private static String getHistoricalUrl(Station station, int year, int month)
    {
        String date = String.valueOf(year) + String.format("%02d", month);
        return String.format("http://www.bom.gov.au/climate/dwo/%1$s/text/%2$s.%1$s.csv", date, station.getHistoricalId());
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
            obj.put("minTemp", data[2].isEmpty() ? null : Double.parseDouble(data[2]));
            obj.put("maxTemp", data[3].isEmpty() ? null : Double.parseDouble(data[3]));

            obj.put("rainfall", data[4].isEmpty() ? null : Double.parseDouble(data[4]));
            obj.put("maxWindGustKmH", data[8].isEmpty() ? null : Double.parseDouble(data[8]));

            obj.put("temp9AM", data[10].isEmpty() ? null : Double.parseDouble(data[10]));
            obj.put("relHumidity9AM", data[11].isEmpty() ? null : Integer.parseInt(data[11]));

            if (!data[14].isEmpty())
                obj.put("windSpd9AM", data[14].toLowerCase().equals("calm") ? 0 : Integer.parseInt(data[14]));
            else
                obj.put("windSpd9AM", (Integer)null);

            obj.put("pressureMSL9AM", data[15].isEmpty() ? null : Double.parseDouble(data[15]));


            obj.put("temp3PM", data[16].isEmpty() ? null : Double.parseDouble(data[16]));
            obj.put("relHumidity3PM", data[17].isEmpty() ? null : Integer.parseInt(data[17]));

            if (!data[20].isEmpty())
                obj.put("windSpd3PM", data[20].toLowerCase().equals("calm") ? 0 : Integer.parseInt(data[20]));
            else
                obj.put("windSpd3PM", (Integer)null);

            obj.put("pressureMSL3PM", data[21].isEmpty() ? null : Double.parseDouble(data[21]));

            json.put(obj);
        }

        return json;
    }


    /**
     * Takes the jsonarray and grabs every value and orders it into the
     * arraylist 'readings'.
     *
     * @param arr is the json array of raw values
     * @return readings is the array list of all ordered values
     */
    public static List<LatestReading> createLatestReadings(JSONArray arr) throws JSONException
    {
        List<LatestReading> readings = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++)
            readings.add(createLatestReading(arr.getJSONObject(i)));

        return readings;
    }

    private static LatestReading createLatestReading(JSONObject obj) throws JSONException
    {
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

    public static List<HistoricalReading> createHistoricalReadings(JSONArray arr) throws JSONException
    {
        List<HistoricalReading> readings = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++)
            readings.add(createHistoricalReading(arr.getJSONObject(i)));

        return readings;
    }

    private static HistoricalReading createHistoricalReading(JSONObject obj) throws JSONException
    {
        return new HistoricalReading(
            obj.getString("date"),
            obj.isNull("minTemp") ? null : obj.getDouble("minTemp"),
            obj.isNull("maxTemp") ? null : obj.getDouble("maxTemp"),

            obj.isNull("rainfall") ? null : obj.getDouble("rainfall"),
            obj.isNull("maxWindGustKmH") ? null : obj.getDouble("maxWindGustKmH"),

            obj.isNull("temp9AM") ? null : obj.getDouble("temp9AM"),
            obj.isNull("relHumidity9AM") ? null : obj.getInt("relHumidity9AM"),
            obj.isNull("windSpd9AM") ? null : obj.getInt("windSpd9AM"),
            obj.isNull("pressureMSL9AM") ? null : obj.getDouble("pressureMSL9AM"),

            obj.isNull("temp3PM") ? null : obj.getDouble("temp3PM"),
            obj.isNull("relHumidity3PM") ? null : obj.getInt("relHumidity3PM"),
            obj.isNull("windSpd3PM") ? null : obj.getInt("windSpd3PM"),
            obj.isNull("pressureMSL3PM") ? null : obj.getDouble("pressureMSL3PM")
        );
    }


    /**
     * Convert BOM formatted date time to a form recognized by joda time.
     *
     * @param bomDt datetime string from BOM
     * @return datetime object of BOM date
     */
    public static LocalDateTime toDateTime(String bomDt)
    {
        String dt = bomDt.replaceAll("(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})", "$1-$2-$3T$4:$5:$6");
        return new LocalDateTime(dt);
    }
}
