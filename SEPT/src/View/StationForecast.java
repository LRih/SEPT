package View;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import Model.Forecast;
import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Utils.ForecastFactory;
import Utils.ForecastWorker;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.List;
import net.miginfocom.swing.MigLayout;

public class StationForecast extends JPanel implements ForecastWorker.OnTaskCompleteListener {

	private final ForecastChart forecastChart;

	private Station station;
	private StationData data;

	/**
	 * Create the panel.
	 */
	public StationForecast() {
		setBackground(Color.WHITE);
		setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow]", "[grow]"));
		
		forecastChart = new ForecastChart();
		add(forecastChart, "cell 0 0 1 1,grow");

	}

	private void updateStation() {
		// no station select so hide
		if (station == null) {
			setVisible(false);
			return;
		} else
			setVisible(true);

		if (data != null && !data.getLatestReadings().isEmpty()) {

			// load forecasts from the web
			ForecastWorker worker = new ForecastWorker(station, ForecastFactory.Source.ForecastIO);
			worker.setOnTaskCompleteListener(this);
			worker.execute();
			
		} else {
			// set empty temp

		}

	}

	/**
	 * set station information to this Panel
	 */
	public final void setStation(Station station, StationData data) {
		this.station = station;
		this.data = data;

		updateStation();
	}

	/**
	 * Hide progress bar and show chart.
	 */
	private void showChart() {
		
		// initialize components
		forecastChart.setTitle("Forecast IO: " + station.getName());
		forecastChart.setYAxisText("Temperature (°C)");
		add(forecastChart, "cell 0 0 1 1,grow");
		
		validate();
		repaint();
	}

	/**
	 * Callbacks for forecast worker
	 */
	public final void onTaskSuccess(List<Forecast> forecasts) {
		showChart();
		forecastChart.setForecasts(forecasts);
	}

	public final void onTaskFail() {
		showChart();
	}

}
