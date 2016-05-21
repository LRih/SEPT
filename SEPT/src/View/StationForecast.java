package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.Forecast;
import Model.Station;
import Model.StationData;
import Utils.ForecastWorker;
import Utils.ForecastFactory.Source;

import java.awt.Color;
import java.awt.Panel;
import java.util.List;
import net.miginfocom.swing.MigLayout;

import com.alee.extended.panel.WebButtonGroup;
import com.alee.laf.button.WebToggleButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StationForecast extends JPanel implements ForecastWorker.OnTaskCompleteListener {

	private final ForecastChart forecastChart;
	private WebComboBox cbForecastSource;
	private WebLabel labelForecastSource;
	private WebButtonGroup groupForcast;

	private Source forecastSource;
	private Panel pnChart;

	private Station station;
	private StationData data;
	private WebLabel wblblForecast;

	/**
	 * Create the panel.
	 */
	public StationForecast() {
		setBackground(new Color(169, 169, 169));
		setLayout(new MigLayout("ins 0 0 0 0", "[grow][150]", "[15][grow]"));
		
		wblblForecast = new WebLabel();
		wblblForecast.setForeground(new Color(255, 255, 255));
		wblblForecast.setFont(Style.FONT_16);
		wblblForecast.setText("Forecast");
		add(wblblForecast, "cell 0 0, gapx 15 0");

		pnChart = new Panel();
		add(pnChart, "cell 0 1 2 1,grow");
		pnChart.setLayout(new MigLayout("ins 0 0 0 0", "[grow]", "[grow]"));

		forecastChart = new ForecastChart();
		pnChart.add(forecastChart, "cell 0 0 1 1, grow");

		WebToggleButton radioForecastIO = new WebToggleButton("Forecast.io");
		radioForecastIO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forecastSource = Source.ForecastIO;
				updateStation();
			}
		});
		radioForecastIO.setForeground(Color.BLACK);
		radioForecastIO.setSelectedForeground(new Color(0, 100, 0));
		radioForecastIO.setBottomSelectedBgColor(Color.WHITE);
		radioForecastIO.setBottomBgColor(Color.WHITE);
		radioForecastIO.setFont(Style.FONT_BENDER_12);

		WebToggleButton radioOpenWeatherMap = new WebToggleButton("OpenWeatherMap");
		radioOpenWeatherMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forecastSource = Source.OpenWeatherMap;
				updateStation();
			}
		});
		radioOpenWeatherMap.setForeground(Color.BLACK);
		radioOpenWeatherMap.setSelectedForeground(new Color(0, 100, 0));
		radioOpenWeatherMap.setBottomSelectedBgColor(new Color(240, 255, 240));
		radioOpenWeatherMap.setBottomBgColor(Color.WHITE);
		radioOpenWeatherMap.setFont(Style.FONT_BENDER_12);

		// save to appstate and restore when open
		if (AppState.getInstance().forecastSource == Source.ForecastIO)
			radioForecastIO.doClick();
		else
			radioOpenWeatherMap.doClick();

		groupForcast = new WebButtonGroup(true, radioForecastIO, radioOpenWeatherMap);
		groupForcast.setButtonsDrawFocus(false);
		groupForcast.setShadeWidth(0);
		add(groupForcast, "cell 1 0,alignx right,gapx 0 15,gapy 7 3");

	}
	
	public void updateBackgroundColor(boolean hasInternetConnection) {
		if (hasInternetConnection)
			setBackground(Style.INTERNET_ON_BACKGROUND);
		else
			setBackground(Style.INTERNET_OFF_BACKGROUND);
	}

	private void updateStation() {
		
		AppState.getInstance().forecastSource = forecastSource;
		
		// no station select so hide
		if (station == null) {
			setVisible(false);
			return;
		} else
			setVisible(true);

		if (data != null && !data.getLatestReadings().isEmpty()) {

			// load forecasts from the web
			ForecastWorker worker = new ForecastWorker(station, forecastSource);
			worker.setOnTaskCompleteListener(this);
			worker.execute();

			// block UI while loading
			groupForcast.setEnabled(false);
			
		} 

		// clear forecast
		forecastChart.setStatus(ForecastChart.LOADING_DATA);
		forecastChart.clearForecast();
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
	 * show chart.
	 */
	private void showChart() {

		// realease UI
		groupForcast.setEnabled(true);

		pnChart.removeAll();
		pnChart.setLayout(new MigLayout("ins 0 0 0 0", "[grow]", "[grow]"));

		// initialize components
		pnChart.add(forecastChart, "cell 0 0 1 1, grow");

		pnChart.validate();
		pnChart.repaint();
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

	public void setBlockUI(boolean isBlockUI) {
		groupForcast.setEnabled(isBlockUI);
	}

}
