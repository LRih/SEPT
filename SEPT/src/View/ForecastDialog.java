package View;

import Model.Forecast;
import Model.Station;
import Utils.ForecastFactory;
import Utils.ForecastWorker;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Window for showing the forecast.
 */
public final class ForecastDialog extends JDialog implements ForecastWorker.OnTaskCompleteListener
{
    private ForecastChart chart = new ForecastChart();

    public ForecastDialog(Window owner, Station station)
    {
        super(owner, "Forecast");

        setBounds(100, 100, 1000, 400);

        chart.setTitle("Forecast IO");
        chart.setYAxisText("Temperature (°C)");
        add(chart);

        setVisible(true);

        // load forecasts from the web
        ForecastWorker worker = new ForecastWorker(station, ForecastFactory.Source.ForecastIO);
        worker.setOnTaskCompleteListener(this);
        worker.execute();
    }

    /**
     * Callbacks for forecast worker
     */
    public final void onTaskSuccess(List<Forecast> forecasts)
    {
        chart.setForecasts(forecasts);
    }
    public final void onTaskFail()
    {
        // ignored
    }
}
