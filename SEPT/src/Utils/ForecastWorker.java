package Utils;

import Model.Forecast;
import Model.LatestReading;
import Model.StationData;

import javax.swing.*;
import java.util.List;

/**
 * Used for downloading forecast data asynchronously.
 */
public final class ForecastWorker extends SwingWorker<List<Forecast>, Void>
{
    private StationData data;
    private ForecastFactory.Source source;
    private OnTaskCompleteListener listener;


    /**
     * Creates a new worker instance.
     *
     * @param data the station data for which to obtain forecast info
     */
    public ForecastWorker(StationData data, ForecastFactory.Source src)
    {
        this.data = data;
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
        if (data == null || data.getLatestReadings().isEmpty())
            throw new Exception("Could not extract location from station data");

        LatestReading reading = data.getLatestReadings().get(0);

        return ForecastFactory.getForecasts(reading.latitude, reading.longitude, source);
    }

    /**
     * Called when the worker completes. Notifies listeners of success/failure.
     */
    protected final void done()
    {
        // call success or fail depending on result
        try
        {
            if (listener != null)
                listener.onSuccess(get());
        }
        catch (Exception e)
        {
            Log.warn(ForecastWorker.class, e.getMessage());

            if (listener != null)
                listener.onFail();
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
        void onSuccess(List<Forecast> forecasts);

        /**
         * Called when forecasts could not be obtained.
         */
        void onFail();
    }
}
