package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.HistoricalReading;
import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Utils.Log;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import com.alee.laf.combobox.WebComboBox;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import com.alee.laf.radiobutton.WebRadioButton;
import com.alee.utils.swing.UnselectableButtonGroup;
import com.alee.laf.checkbox.WebCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import com.alee.laf.slider.WebSlider;

/**
 * Chart UI
 */
public final class StationChart extends JPanel {
	// private final WebLabel wblblState;
	private final LineChart chartPanel;
	private final JPanel panel;
	// private final WebComboBox wcbChartType;

	private StationData data;

	private OnBackClickListener _listener;
	private JPanel pnSelectData;
	private WebRadioButton radioTemperature;
	private WebCheckBox checkboxTempMin;
	private WebCheckBox checkboxTempMax;
	private WebCheckBox checkboxTemp9am;
	private WebCheckBox checkboxTemp3pm;
	private WebRadioButton radioWind;
	private WebCheckBox checkboxWindGust;
	private WebCheckBox checkboxWind9am;
	private WebCheckBox checkboxWind3pm;
	private WebRadioButton radioPressure;
	private WebCheckBox checkboxPressure9am;
	private WebCheckBox checkboxPressure3pm;
	private WebRadioButton radioHumidity;
	private WebCheckBox checkboxHumid9am;
	private WebCheckBox checkboxHumid3pm;
	private WebRadioButton radioRainFall;
	private int current_color;
	private ChartGroup selected_group = ChartGroup.Temperature;

	/**
	 * Create the panel.
	 */
	public StationChart() {
		setBackground(Color.WHITE);
		setLayout(new MigLayout("ins 5 5 5 5, gapy 0", "[50][][][grow][200]", "[][grow]"));

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 0 0 4 2,grow");
		panel.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		chartPanel = new LineChart();
		chartPanel.setTitle("Historical");
		chartPanel.setXAxisText("Date");
		chartPanel.setYAxisText("Temperature (°C)");
		panel.add(chartPanel, "cell 0 0, grow");
		chartPanel.setPreferredSize(new Dimension(600, 270));
		chartPanel.setLayout(new MigLayout("", "[46px]", "[25px]"));

		WebButton wbtnBack = new WebButton();
		chartPanel.add(wbtnBack, "cell 0 0,alignx left,aligny top");
		wbtnBack.setFont(Style.FONT_BENDER_13);
		wbtnBack.setDrawShade(false);
		wbtnBack.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listener != null)
					_listener.onBackClick();
			}
		});
		wbtnBack.setText("Back");

		pnSelectData = new JPanel();
		pnSelectData.setBackground(new Color(255, 255, 255));
		add(pnSelectData, "cell 4 0 1 2,grow");
		pnSelectData.setLayout(new MigLayout("", "[14%][50%][36%]", "[][][][][][][][][][][][][][][][][][][]"));

		radioTemperature = new WebRadioButton();
		radioTemperature.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				radioChanged(ChartGroup.Temperature, radioTemperature.isSelected());
			}
		});
		radioTemperature.setRolloverDarkBorderOnly(true);
		radioTemperature.setText("Temperature (°C)");
		pnSelectData.add(radioTemperature, "cell 0 0 3 2,gapy 5 0");

		checkboxTemp9am = new WebCheckBox();
		checkboxTemp9am.setAnimated(false);
		checkboxTemp9am.setFocusable(false);
		checkboxTemp9am.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxTemp9am, ChartType.Temp9AM);
			}
		});
		checkboxTemp9am.setRolloverDarkBorderOnly(true);
		checkboxTemp9am.setText("9am");
		pnSelectData.add(checkboxTemp9am, "cell 1 2");

		checkboxTemp3pm = new WebCheckBox();
		checkboxTemp3pm.setAnimated(false);
		checkboxTemp3pm.setFocusable(false);
		checkboxTemp3pm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxTemp3pm, ChartType.Temp3PM);
			}
		});
		checkboxTemp3pm.setRolloverDarkBorderOnly(true);
		checkboxTemp3pm.setText("3pm");
		pnSelectData.add(checkboxTemp3pm, "cell 2 2");

		checkboxTempMin = new WebCheckBox();
		checkboxTempMin.setAnimated(false);
		checkboxTempMin.setFocusable(false);
		checkboxTempMin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxTempMin, ChartType.MinTemp);
			}
		});
		checkboxTempMin.setRolloverDarkBorderOnly(true);
		checkboxTempMin.setText("Min");
		pnSelectData.add(checkboxTempMin, "cell 1 3");

		checkboxTempMax = new WebCheckBox();
		checkboxTempMax.setAnimated(false);
		checkboxTempMax.setFocusable(false);
		checkboxTempMax.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxTempMax, ChartType.MaxTemp);
			}
		});
		checkboxTempMax.setRolloverDarkBorderOnly(true);
		checkboxTempMax.setText("Max");
		pnSelectData.add(checkboxTempMax, "cell 2 3");

		radioWind = new WebRadioButton();
		radioWind.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				radioChanged(ChartGroup.WindSpeed, radioWind.isSelected());
			}
		});
		radioWind.setRolloverDarkBorderOnly(true);
		radioWind.setText("Wind Speed (km/h)");
		pnSelectData.add(radioWind, "cell 0 4 3 1, gapy 5 0");

		checkboxWind9am = new WebCheckBox();
		checkboxWind9am.setAnimated(false);
		checkboxWind9am.setFocusable(false);
		checkboxWind9am.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxWind9am, ChartType.WindSpd9AM);
			}
		});
		checkboxWind9am.setRolloverDarkBorderOnly(true);
		checkboxWind9am.setText("9am");
		pnSelectData.add(checkboxWind9am, "cell 1 5");

		checkboxWind3pm = new WebCheckBox();
		checkboxWind3pm.setAnimated(false);
		checkboxWind3pm.setFocusable(false);
		checkboxWind3pm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxWind3pm, ChartType.WindSpd3PM);
			}
		});
		checkboxWind3pm.setRolloverDarkBorderOnly(true);
		checkboxWind3pm.setText("3pm");
		pnSelectData.add(checkboxWind3pm, "cell 2 5");

		checkboxWindGust = new WebCheckBox();
		checkboxWindGust.setAnimated(false);
		checkboxWindGust.setFocusable(false);
		checkboxWindGust.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxWindGust, ChartType.MaxWindGustKmH);
			}
		});
		checkboxWindGust.setRolloverDarkBorderOnly(true);
		checkboxWindGust.setText("Max Gust");
		pnSelectData.add(checkboxWindGust, "cell 1 6");

		radioPressure = new WebRadioButton();
		radioPressure.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				radioChanged(ChartGroup.Pressure, radioPressure.isSelected());
			}
		});
		radioPressure.setRolloverDarkBorderOnly(true);
		radioPressure.setText("Pressure (hPa)");
		pnSelectData.add(radioPressure, "cell 0 7 3 1, gapy 5 0");

		checkboxPressure9am = new WebCheckBox();
		checkboxPressure9am.setAnimated(false);
		checkboxPressure9am.setFocusable(false);
		checkboxPressure9am.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxPressure9am, ChartType.PressureMSL9AM);
			}
		});
		checkboxPressure9am.setRolloverDarkBorderOnly(true);
		checkboxPressure9am.setText("9am");
		pnSelectData.add(checkboxPressure9am, "cell 1 8");

		checkboxPressure3pm = new WebCheckBox();
		checkboxPressure3pm.setAnimated(false);
		checkboxPressure3pm.setFocusable(false);
		checkboxPressure3pm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxPressure3pm, ChartType.PressureMSL3PM);
			}
		});
		checkboxPressure3pm.setRolloverDarkBorderOnly(true);
		checkboxPressure3pm.setText("3pm");
		pnSelectData.add(checkboxPressure3pm, "cell 2 8");

		radioHumidity = new WebRadioButton();
		radioHumidity.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				radioChanged(ChartGroup.Humidity, radioHumidity.isSelected());
			}
		});
		radioHumidity.setRolloverDarkBorderOnly(true);
		radioHumidity.setText("Humidity (%)");
		pnSelectData.add(radioHumidity, "cell 0 9 3 1, gapy 5 0");

		checkboxHumid9am = new WebCheckBox();
		checkboxHumid9am.setAnimated(false);
		checkboxHumid9am.setFocusable(false);
		checkboxHumid9am.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxHumid9am, ChartType.RelHumidity9AM);
			}
		});
		checkboxHumid9am.setRolloverDarkBorderOnly(true);
		checkboxHumid9am.setText("9am");
		pnSelectData.add(checkboxHumid9am, "cell 1 10");

		checkboxHumid3pm = new WebCheckBox();
		checkboxHumid3pm.setAnimated(false);
		checkboxHumid3pm.setFocusable(false);
		checkboxHumid3pm.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				checkboxChanged(checkboxHumid3pm, ChartType.RelHumidity3PM);
			}
		});
		checkboxHumid3pm.setRolloverDarkBorderOnly(true);
		checkboxHumid3pm.setText("3pm");
		pnSelectData.add(checkboxHumid3pm, "cell 2 10");

		radioRainFall = new WebRadioButton();
		radioRainFall.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				radioChanged(ChartGroup.RainFall, radioRainFall.isSelected());
			}
		});
		radioRainFall.setRolloverDarkBorderOnly(true);
		radioRainFall.setText("Rain Fall (mm)");
		pnSelectData.add(radioRainFall, "cell 0 11 3 1, gapy 5 0");

		UnselectableButtonGroup.group(radioHumidity, radioPressure, radioRainFall, radioTemperature, radioWind);

		wblblZoom = new WebLabel();
		wblblZoom.setText("Zoom");
		pnSelectData.add(wblblZoom, "cell 0 12 3 1, gapy 15");

		sliderZoom = new WebSlider();
		sliderZoom.setFocusable(false);
		sliderZoom.setMinimum(3);
		sliderZoom.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				chartPanel.setMaxDataPoints(sliderZoom.getValue());
				wblblZoom.setText("View: Last " + sliderZoom.getValue() + " day(s).");
			}
		});
		pnSelectData.add(sliderZoom, "cell 0 13 3 1");

	}

	protected void checkboxChanged(WebCheckBox checkbox, ChartType type) {
		if (checkbox.isSelected()) {
			// set legend color
			checkbox.setForeground(Style.LINE_COLORS[current_color % Style.LINE_COLORS.length]);
			checkbox.setBorderColor(Style.LINE_COLORS[current_color % Style.LINE_COLORS.length]);
			setDataset(type);
		} else {
			// remove legend color
			checkbox.setForeground(Color.black);
			checkbox.setBorderColor(Color.gray);
			removeDataset(type);
		}
	}

	protected void radioChanged(ChartGroup group, boolean isSelected) {

		// System.out.println(chartPanel.getXValues().length);

		// reset color index
		current_color = 0;

		// clear data set, reset max, min
		chartPanel.clearDatasets();

		// disable all other checkboxes
		disableCheckboxes(group);

		// update group of checkboxes
		updateGroup(group, isSelected);

		sliderZoom.setMaximum(chartPanel.getXValues().length);

		chartPanel.setMaxDataPoints(sliderZoom.getValue());

	}

	private void updateGroup(ChartGroup group, boolean isEnabled) {

		if (isEnabled)
			selected_group = group;

		switch (group) {
		case Temperature:
			chartPanel.setYAxisText("Temperature (°C)");
			updateCheckbox(checkboxTemp9am, isEnabled);
			updateCheckbox(checkboxTemp3pm, isEnabled);
			updateCheckbox(checkboxTempMax, isEnabled);
			updateCheckbox(checkboxTempMin, isEnabled);
			break;
		case WindSpeed:
			chartPanel.setYAxisText("Wind Speed (km/h)");
			chartPanel.setXValues(getXAxisValues());
			updateCheckbox(checkboxWind9am, isEnabled);
			updateCheckbox(checkboxWind3pm, isEnabled);
			updateCheckbox(checkboxWindGust, isEnabled);
			break;
		case Pressure:
			chartPanel.setYAxisText("Pressure (hPa)");
			chartPanel.setXValues(getXAxisValues());
			updateCheckbox(checkboxPressure9am, isEnabled);
			updateCheckbox(checkboxPressure3pm, isEnabled);
			break;
		case Humidity:
			chartPanel.setYAxisText("Humidity (%)");
			chartPanel.setXValues(getXAxisValues());
			updateCheckbox(checkboxHumid9am, isEnabled);
			updateCheckbox(checkboxHumid3pm, isEnabled);
			break;
		case RainFall:
			chartPanel.setYAxisText("Rain fall (mm)");
			if (isEnabled) {
				radioRainFall.setForeground(Style.LINE_COLORS[current_color % Style.LINE_COLORS.length]);
				setDataset(ChartType.Rainfall);
			} else {
				radioRainFall.setForeground(Color.black);
				removeDataset(ChartType.Rainfall);
			}
			break;

		}
	}

	private void updateCheckbox(WebCheckBox checkbox, boolean isEnabled) {
		checkbox.setSelected(isEnabled);
		checkbox.setEnabled(isEnabled);
	}

	private void disableCheckboxes(ChartGroup group) {
		if (group != ChartGroup.Temperature)
			updateGroup(ChartGroup.Temperature, false);

		if (group != ChartGroup.WindSpeed)
			updateGroup(ChartGroup.WindSpeed, false);

		if (group != ChartGroup.Pressure)
			updateGroup(ChartGroup.Pressure, false);

		if (group != ChartGroup.Humidity)
			updateGroup(ChartGroup.Humidity, false);
	}

	private void updateChart() {

		// reset color
		current_color = 0;

		// reset max min
		chartPanel.clearDatasets();

		// re-select selected checkboxes
		// to reload data for new station
		reselectCheckboxes();

	}

	boolean selectedYet = false;
	private WebLabel wblblZoom;
	private WebSlider sliderZoom;

	private void reselectCheckboxes() {

		if (radioRainFall.isSelected()) {
			radioRainFall.setSelected(false);
			radioRainFall.setSelected(true);
			selectedYet = true;
		} else {
			reselectCheckbox(checkboxTemp9am);
			reselectCheckbox(checkboxTemp3pm);
			reselectCheckbox(checkboxTempMax);
			reselectCheckbox(checkboxTempMin);
			reselectCheckbox(checkboxWind9am);
			reselectCheckbox(checkboxWind3pm);
			reselectCheckbox(checkboxWindGust);
			reselectCheckbox(checkboxPressure9am);
			reselectCheckbox(checkboxPressure3pm);
			reselectCheckbox(checkboxHumid9am);
			reselectCheckbox(checkboxHumid3pm);
		}

		// if first time open panel
		// show Temperature by default
		if (!selectedYet)
			radioTemperature.setSelected(true);

	}

	private void reselectCheckbox(WebCheckBox checkbox) {
		if (checkbox.isSelected()) {
			checkbox.setSelected(false);
			checkbox.setSelected(true);
			selectedYet = true;
		}
	}

	/**
	 * Replay chart animation.
	 */
	public final void animate() {
		chartPanel.animate();
	}

	private void removeDataset(ChartType type) {
		chartPanel.removeDataset(type.toString());
	}

	private void setDataset(ChartType type) {

		if (data == null)
			return;

		String name = type.toString();

		double[] values;
		List<HistoricalReading> readings = data.getHistoricalReadings();

		if (sliderZoom.getValue() == 2) {
			values = new double[data.getLatestReadings().size()];
//			List<LatestReading> latest_readings = data.getLatestReadings();
			// switch to latest reading
		} else {
			values = new double[data.getHistoricalReadings().size()];
		}

		switch (type) {
		case MinTemp:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).minTemp != null)
					values[i] = readings.get(i).minTemp;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		case MaxTemp:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).maxTemp != null)
					values[i] = readings.get(i).maxTemp;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		case Rainfall:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).rainfall != null)
					values[i] = readings.get(i).rainfall;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		case MaxWindGustKmH:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).maxWindGustKmH != null)
					values[i] = readings.get(i).maxWindGustKmH;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		case Temp9AM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).temp9AM != null)
					values[i] = readings.get(i).temp9AM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case RelHumidity9AM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).relHumidity9AM != null)
					values[i] = readings.get(i).relHumidity9AM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case WindSpd9AM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).windSpd9AM != null)
					values[i] = readings.get(i).windSpd9AM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case PressureMSL9AM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).pressureMSL9AM != null)
					values[i] = readings.get(i).pressureMSL9AM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		case Temp3PM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).temp3PM != null)
					values[i] = readings.get(i).temp3PM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case RelHumidity3PM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).relHumidity3PM != null)
					values[i] = readings.get(i).relHumidity3PM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case WindSpd3PM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).windSpd3PM != null)
					values[i] = readings.get(i).windSpd3PM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;
		case PressureMSL3PM:
			for (int i = 0; i < values.length; i++)
				if (readings.get(i).pressureMSL3PM != null)
					values[i] = readings.get(i).pressureMSL3PM;
				else if (i > 0)
					values[i] = values[i - 1];
			break;

		default:
			Log.warn(getClass(), "Invalid chart type: " + type.name());
			return;
		}

		Color col = Style.LINE_COLORS[current_color % Style.LINE_COLORS.length];
		current_color++;
		chartPanel.addDataset(name, col, values);
	}

	private String[] getXAxisValues() {
		if (data == null)
			return new String[] {};

		String[] values = new String[data.getHistoricalReadings().size()];

		List<HistoricalReading> readings = data.getHistoricalReadings();

		for (int i = 0; i < values.length; i++)
			values[i] = readings.get(i).date.getDayOfMonth() + "/" + readings.get(i).date.getMonthOfYear();

		return values;
	}

	/**
	 * Set station data for this panel.
	 */
	public final void setStation(Station station, StationData data) {
		this.data = data;

		if (station != null) {
			// wblblStation.setText(station.getName());
			chartPanel.setTitle(station.getName());
			// wblblState.setText(station.getState().getName());
		}

		updateChart();
	}

	public final void setOnBackClickListener(OnBackClickListener listener) {
		_listener = listener;
	}

	public enum ChartType {
		MinTemp, MaxTemp, Rainfall, MaxWindGustKmH, Temp9AM, RelHumidity9AM, WindSpd9AM, PressureMSL9AM, Temp3PM, RelHumidity3PM, WindSpd3PM, PressureMSL3PM
	}

	public enum ChartGroup {
		Temperature, WindSpeed, Pressure, Humidity, RainFall
	}

	public void setBlockUI(boolean isBlockUI) {
		// wcbChartType.setEnabled(isBlockUI);
	}
}
