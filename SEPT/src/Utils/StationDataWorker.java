package Utils;

import Model.Station;
import Model.StationData;

import javax.swing.*;

/*
    Used for downloading BOM station data asynchronously.
 */
public final class StationDataWorker extends SwingWorker<StationData, Void>
{
    private Station station;
    private OnTaskCompleteListener listener;


    public StationDataWorker(Station station)
    {
        this.station = station;
    }


    /* called when execute is called */
    protected final StationData doInBackground() throws Exception
    {
        StationData data = DataManager.getStationData(station);

        // fail when data is null
        if (data == null)
            throw new Exception("Could not get station data");

        return data;
    }

    /* called when background task finishes */
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
            if (listener != null)
                listener.onFail();
        }
    }


    public final void setOnTaskCompleteListener(OnTaskCompleteListener listener)
    {
        this.listener = listener;
    }


    public interface OnTaskCompleteListener
    {
        void onSuccess(StationData data);
        void onFail();
    }
}
