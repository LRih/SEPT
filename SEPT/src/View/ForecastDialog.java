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
    private JPanel pnProgressBar = new JPanel();

    public ForecastDialog(Frame owner, Station station)
    {
        super(owner, "Forecast", true);

        setBounds(100, 100, 1000, 400);

        // initialize components
        chart.setTitle("Forecast IO: " + station.getName());
        chart.setYAxisText("Temperature (°C)");

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        // add components to dialog
        pnProgressBar.setBackground(Color.WHITE);
        pnProgressBar.setLayout(new GridBagLayout());
        pnProgressBar.add(progressBar);
        add(pnProgressBar);

        // load forecasts from the web
        ForecastWorker worker = new ForecastWorker(station, ForecastFactory.Source.ForecastIO);
        worker.setOnTaskCompleteListener(this);
        worker.execute();
    }

    /**
     * Hide progress bar and show chart.
     */
    private void showChart()
    {
        remove(pnProgressBar);
        add(chart);

        validate();
        repaint();
    }

    /**
     * Callbacks for forecast worker
     */
    public final void onTaskSuccess(List<Forecast> forecasts)
    {
        showChart();
        chart.setForecasts(forecasts);
    }
    public final void onTaskFail()
    {
        showChart();
    }
}
