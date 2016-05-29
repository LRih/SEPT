package Utils;

import Data.DataManager;
import Data.ForecastFactory;
import Model.Forecast;
import Model.LatestReading;
import Model.Station;
import Model.StationData;

import javax.swing.*;
import java.util.List;

/**
 * Used for downloading forecast data asynchronously.
 */
public final class ForecastWorker extends SwingWorker<List<Forecast>, Void>
{
    private final Station station;
    private final ForecastFactory.Source source;
    private OnTaskCompleteListener listener;


    /**
     * Creates a new worker instance.
     *
     * @param station the station for which to obtain forecast info
     */
    public ForecastWorker(Station station, ForecastFactory.Source src)
    {
        this.station = station;
        this.source = src;
    }


    /**
     * Run in a background thread when {@code execute()} is run.
     *
     * @return the data associated to {@code station}
     * @throws Exception if data is null meaning it could not be obtained
     */
    protected final List<Forecast> doInBackground() throws Exception
    {
        Log.info(getClass(), "Starting to get forecast info for " + station.getName() + ", source: " + source.name());

        // first try to load cached data (we only need lat/lon info)
        StationData data = DataManager.getCachedStationData(station);

        // failing that, load data from the net
        if (data == null)
            data = DataManager.getNetStationData(station);

        if (data.getLatestReadings().isEmpty())
            throw new Exception("Could not extract location from station data");

        LatestReading reading = data.getLatestReadings().get(0);

        return ForecastFactory.getNetForecasts(reading.latitude, reading.longitude, source);
    }

    /**
     * Called when the worker completes. Notifies listeners of success/failure.
     */
    protected final void done()
    {
        // call success or fail depending on result
        try
        {
            Log.info(getClass(), station.getName() + " forecast from " + source.name() + " downloaded");

            if (listener != null)
                listener.onTaskSuccess(get());
        }
        catch (Exception e)
        {
            Log.warn(getClass(), e.getMessage());

            if (listener != null)
                listener.onTaskFail();
        }
    }


    /**
     * Set the success/fail listener.
     *
     * @param listener the listener to set
     */
    public final void setOnTaskCompleteListener(OnTaskCompleteListener listener)
    {
        this.listener = listener;
    }


    /**
     * The success/fail listener interface.
     */
    public interface OnTaskCompleteListener
    {
        /**
         * Called when data has be obtained successfully.
         *
         * @param forecasts the obtained forecasts
         */
        void onTaskSuccess(List<Forecast> forecasts);

        /**
         * Called when forecasts could not be obtained.
         */
        void onTaskFail();
    }
}
