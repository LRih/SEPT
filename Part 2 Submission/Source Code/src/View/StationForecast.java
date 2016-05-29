package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.Forecast;
import Model.Station;
import Model.StationData;
import Utils.ForecastWorker;
import Data.ForecastFactory;
import Data.ForecastFactory.Source;

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
	private WebButtonGroup groupForcast;

	private Source forecastSource;
	private Panel panelChart;

	private Station station;
	private WebLabel labelForecast;

	private ForecastWorker worker;

	/**
	 * Create the panel.
	 */
	public StationForecast() {
		setBackground(Style.INTERNET_OFF_BACKGROUND);
		setLayout(new MigLayout("ins 0 0 0 0", "[grow][150]", "[15][grow]"));

		labelForecast = new WebLabel();
		labelForecast.setForeground(Color.white);
		labelForecast.setFont(Style.FONT_16);
		labelForecast.setText("Weather Forecast");
		add(labelForecast, "cell 0 0, gapx 15 0");

		panelChart = new Panel();
		add(panelChart, "cell 0 1 2 1,grow");
		panelChart.setLayout(new MigLayout("ins 0 0 0 0", "[grow]", "[grow]"));

		forecastChart = new ForecastChart();
		panelChart.add(forecastChart, "cell 0 0 1 1, grow");

		WebToggleButton radioForecastIO = new WebToggleButton("Forecast.io");
		radioForecastIO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forecastSource = Source.ForecastIO;
				updateStation();
			}
		});
		radioForecastIO.setForeground(Color.BLACK);
		radioForecastIO.setSelectedForeground(Style.ADD_STATION_BUTTON);
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
		radioOpenWeatherMap.setSelectedForeground(Style.ADD_STATION_BUTTON);
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
			setBackground(Style.MAIN_PANEL_BACKGROUND);
			setVisible(false);
			return;
		} else
			setVisible(true);

		if (station != null) {
			// get cached data if exist
			List<Forecast> cachedForecast = ForecastFactory.getCachedForecasts(station, forecastSource);
			if (cachedForecast != null) {
				forecastChart.setForecasts(cachedForecast);
				labelForecast.setText("Next " + (cachedForecast.size() - 1) + " days forecast");
			} else {
				// clear forecast
				forecastChart.setStatus(ForecastChart.LOADING_DATA);
				forecastChart.clearForecast();
			}

			if (worker != null)
				worker.cancel(true);

			// load forecasts from the web
			worker = new ForecastWorker(station, forecastSource);
			worker.setOnTaskCompleteListener(this);
			worker.execute();

			// block UI while loading
			groupForcast.setEnabled(false);
		}
	}

	/**
	 * set station information to this Panel
	 */
	public final void setStation(Station station) {

		this.station = station;

		updateStation();
	}

	/**
	 * Callbacks for forecast worker
	 */
	public final void onTaskSuccess(List<Forecast> forecasts) {
		groupForcast.setEnabled(true);
		forecastChart.setForecasts(forecasts);
		labelForecast.setText("Next " + (forecasts.size() - 1) + " days forecast");
	}

	public final void onTaskFail() {
		groupForcast.setEnabled(true);
		forecastChart.setStatus(ForecastChart.NO_DATA);
	}

	public void setBlockUI(boolean isBlockUI) {
		groupForcast.setEnabled(isBlockUI);
	}

}
