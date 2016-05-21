package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import Model.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import net.miginfocom.swing.MigLayout;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Detail information UI in Main Screen
 */
public final class StationDetail extends JPanel {
	private static final int TEMP_FREEZING = 0;
	private static final int TEMP_COOL = 20;

	private final WebLabel labelStation;
	private final WebLabel labelHumid;
	private final WebLabel labelState;
	private final WebLabel labelWindSse;
	private final WebLabel labelRainSinceam;
	private final WebLabel labelc;
	private final WebLabel labelPressQmh;
	private final WebLabel labelPress;
	private final WebLabel labelAirTemp;
	private final WebLabel labelDewPoint;
	private final WebLabel labelLastUpdate;

	private final WebButton buttonViewChart;
	private final WebButton buttonViewWeatherHistory;
	private final WebLabel labelRemoveFromFavourites;

	private Station station;
	private StationData data;

	private final DateTimeFormatter dtFormatter;

	private OnActionListener _listenerAction;
	private OnRemoveFavoriteClickListener _listenerRemoveFavorite;

	/**
	 * Create the panel.
	 */
	public StationDetail() {
		setBackground(Style.STATION_DETAIL_BACKGROUND);

		setLayout(new MigLayout("", "[30%,grow][grow][30%]", "[grow][][][][][][][][grow][]"));

		JPanel panelFiller1 = new JPanel();
		panelFiller1.setVisible(false);
		add(panelFiller1, "cell 0 0,grow");

		labelStation = new WebLabel();
		labelStation.setText("-");
		labelStation.setForeground(Style.CELL_STATION_LABEL);
		labelStation.setFont(Style.FONT_30);

		add(labelStation, "cell 0 1 2 1");

		labelHumid = new WebLabel();
		labelHumid.setFont(Style.FONT_13);
		labelHumid.setText("-");
		add(labelHumid, "cell 2 1,aligny bottom");

		labelState = new WebLabel();
		labelState.setFont(Style.FONT_BENDER_16);
		labelState.setText("-");
		add(labelState, "cell 0 2 2 1");

		labelWindSse = new WebLabel();
		labelWindSse.setFont(Style.FONT_13);
		labelWindSse.setText("-");
		add(labelWindSse, "cell 2 2");

		labelRainSinceam = new WebLabel();
		labelRainSinceam.setFont(Style.FONT_13);
		labelRainSinceam.setText("Rain since 9am: -");
		add(labelRainSinceam, "cell 2 3");

		labelc = new WebLabel();
		labelc.setForeground(Color.white);
		labelc.setFont(Style.FONT_FUTURA_50);
		labelc.setText("-°C");
		add(labelc, "cell 1 3 1 3,alignx left,aligny top");

		labelPressQmh = new WebLabel();
		labelPressQmh.setFont(Style.FONT_13);
		labelPressQmh.setText("Press QNH hPa: -");
		add(labelPressQmh, "cell 2 4");

		labelPress = new WebLabel();
		labelPress.setFont(Style.FONT_13);
		labelPress.setText("Press MSL hPa: -");
		add(labelPress, "cell 2 5,aligny top");

		labelAirTemp = new WebLabel();
		labelAirTemp.setFont(Style.FONT_13);
		labelAirTemp.setText("App temp: -°C");
		add(labelAirTemp, "cell 1 6,aligny bottom");

		buttonViewChart = new WebButton();
		buttonViewChart.setFont(Style.FONT_13);
		buttonViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		buttonViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		buttonViewChart.setBottomBgColor(new Color(240, 248, 255));
		buttonViewChart.setDrawShade(false);
		buttonViewChart.setText("View Chart");
		add(buttonViewChart, "cell 2 6,alignx left,aligny bottom");

		labelDewPoint = new WebLabel();
		labelDewPoint.setFont(Style.FONT_13);
		labelDewPoint.setText("Dew Point: -°C");
		add(labelDewPoint, "cell 1 7");

		dtFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

		buttonViewWeatherHistory = new WebButton();
		buttonViewWeatherHistory.setText("View Weather History");
		buttonViewWeatherHistory.setFont(Style.FONT_13);
		buttonViewWeatherHistory.setDefaultButtonShadeColor(new Color(240, 255, 255));
		buttonViewWeatherHistory.setBottomSelectedBgColor(new Color(224, 255, 255));
		buttonViewWeatherHistory.setBottomBgColor(new Color(240, 248, 255));
		buttonViewWeatherHistory.setDrawShade(false);
		add(buttonViewWeatherHistory, "cell 2 7");

		JPanel panelFiller2 = new JPanel();
		panelFiller2.setVisible(false);
		add(panelFiller2, "cell 0 8,grow");

		labelLastUpdate = new WebLabel();
		labelLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		labelLastUpdate.setText("Last update: -");
		add(labelLastUpdate, "cell 0 9 2 1,aligny bottom");

		labelRemoveFromFavourites = new WebLabel();
		labelRemoveFromFavourites.setForeground(Color.RED);
		labelRemoveFromFavourites.setFont(new Font("Century Gothic", Font.ITALIC, 13));
		labelRemoveFromFavourites.setText("Remove from Favourites");

		add(labelRemoveFromFavourites, "cell 2 9,alignx right,aligny bottom");

		addListeners();

		updateStation();
	}

	private void addListeners() {
		buttonViewChart.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listenerAction != null)
					_listenerAction.onViewChartClick();
			}
		});

		buttonViewWeatherHistory.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listenerAction != null)
					_listenerAction.onViewHistoryClick();
			}
		});

		labelRemoveFromFavourites.addMouseListener(new MouseAdapter() {
			public final void mouseClicked(MouseEvent e) {
				if (_listenerRemoveFavorite != null
						&& labelRemoveFromFavourites.isEnabled())
					_listenerRemoveFavorite.onRemoveFavoriteClick(station);
			}
		});
	}

	private void updateStation() {
		// no station select so hide
		if (station == null) {
			setVisible(false);
			return;
		} else
			setVisible(true);

		if (data != null && !data.getLatestReadings().isEmpty()) {
			LatestReading r = data.getLatestReadings().get(0);

			// change colours by Temperature
			if (r.airTemp != null) {
				if (r.airTemp < TEMP_FREEZING) {
					setBackground(Style.STATION_DETAIL_BACKGROUND);
					labelc.setForeground(new Color(2, 136, 209));
				} else if (r.airTemp < TEMP_COOL) {
					setBackground(Style.WEATHER_COOL_BACKGROUND);
					labelc.setForeground(Style.WEATHER_COOL_LABEL);
				} else {
					setBackground(Style.WEATHER_HOT_BACKGROUND);
					labelc.setForeground(Style.WEATHER_HOT_LABEL);
				}
				labelc.setText(r.airTemp + "°C");
			} else {
				setBackground(new Color(176, 196, 222));
				labelc.setForeground(Color.black);
				labelc.setText("-°C");
			}

			// set Text
			labelHumid.setText("Humid: " + (r.relativeHumidity == null ? "-" : r.relativeHumidity) + "%");
			labelStation.setText(station.getName());
			labelState.setText(station.getState().getName());

			// set wind format
			String windDir = r.windDir == null ? "" : r.windDir;
			String windSpd = r.windSpdKmH == null || r.windGustKmH == null ? "" : r.windSpdKmH + "-" + r.windGustKmH;
			labelWindSse.setText("Wind: " + windDir + " " + windSpd + " km/h");

			labelRainSinceam.setText("Rain since 9am: " + (r.rainTrace == null ? "-" : r.rainTrace) + "mm");
			labelPressQmh.setText("Press QNH hPa: " + (r.pressureQNH == null ? "-" : r.pressureQNH));
			labelPress.setText("Press MSL hPa: " + (r.pressureMSL == null ? "-" : r.pressureMSL));
			labelAirTemp.setText("App temp: " + (r.apparentTemp == null ? "-" : r.apparentTemp) + "°C");
			labelDewPoint.setText("Dew Point: " + (r.dewPt == null ? "-" : r.dewPt) + "°C");
			labelLastUpdate.setText("Last update: " + dtFormatter.print(r.localDateTime));
		} else {
			// set empty temp
			setBackground(new Color(176, 196, 222));
			labelc.setForeground(Color.black);
			labelc.setText("-°C");

			// set Text
			labelHumid.setText("Humid: -%");
			labelStation.setText(station.getName());
			labelState.setText(station.getState().getName());
			labelWindSse.setText("Wind: -km/h");
			labelRainSinceam.setText("Rain since 9am: -mm");
			labelPressQmh.setText("Press QNH hPa: -");
			labelPress.setText("Press MSL hPa: -");
			labelAirTemp.setText("App temp: -°C");
			labelDewPoint.setText("Dew Point: -°C");
			labelLastUpdate.setText("Last update: -");
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

	public final void setOnActionListener(OnActionListener listener) {
		_listenerAction = listener;
	}

	public final void setOnRemoveFavoriteClickListener(OnRemoveFavoriteClickListener listener) {
		_listenerRemoveFavorite = listener;
	}

	public interface OnActionListener {
		void onViewChartClick();
		void onViewHistoryClick();
	}

	public interface OnRemoveFavoriteClickListener {
		void onRemoveFavoriteClick(Station station);
	}

	public void setBlockUI(boolean isBlockUI) {
		buttonViewChart.setEnabled(isBlockUI);
		buttonViewWeatherHistory.setEnabled(isBlockUI);
		labelRemoveFromFavourites.setEnabled(isBlockUI);

	}
}
