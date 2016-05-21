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
	private static final int TEMP_COOL = 25;

	private final WebLabel wblblStation;
	private final WebLabel wblblHumid;
	private final WebLabel wblblState;
	private final WebLabel wblblWindSse;
	private final WebLabel wblblRainSinceam;
	private final WebLabel wblblc;
	private final WebLabel wblblPressQmh;
	private final WebLabel wblblPress;
	private final WebLabel wblblAirTemp;
	private final WebLabel wblblDewPoint;
	private final WebLabel wblblLastUpdate;

	private final WebButton wbtnViewChart;
	private final WebButton wbtnViewWeatherHistory;
	private final WebLabel wblblRemoveFromFavourites;

	private Station station;
	private StationData data;

	private final DateTimeFormatter dtFormatter;

	private OnActionListener _listenerAction;
	private OnRemoveFavoriteClickListener _listenerRemoveFavorite;

	/**
	 * Create the panel.
	 */
	public StationDetail() {
		setBackground(new Color(176, 196, 222));

		setLayout(new MigLayout("", "[30%,grow][grow][30%]", "[grow][][][][][][][][grow][]"));

		JPanel panelFiller1 = new JPanel();
		panelFiller1.setVisible(false);
		add(panelFiller1, "cell 0 0,grow");

		wblblStation = new WebLabel();
		wblblStation.setText("-");
		wblblStation.setForeground(new Color(255, 69, 0));
		wblblStation.setFont(Style.FONT_30);

		add(wblblStation, "cell 0 1 2 1");

		wblblHumid = new WebLabel();
		wblblHumid.setFont(Style.FONT_13);
		wblblHumid.setText("-");
		add(wblblHumid, "cell 2 1,aligny bottom");

		wblblState = new WebLabel();
		wblblState.setFont(Style.FONT_BENDER_16);
		wblblState.setText("-");
		add(wblblState, "cell 0 2 2 1");

		wblblWindSse = new WebLabel();
		wblblWindSse.setFont(Style.FONT_13);
		wblblWindSse.setText("-");
		add(wblblWindSse, "cell 2 2");

		wblblRainSinceam = new WebLabel();
		wblblRainSinceam.setFont(Style.FONT_13);
		wblblRainSinceam.setText("Rain since 9am: -");
		add(wblblRainSinceam, "cell 2 3");

		wblblc = new WebLabel();
		wblblc.setForeground(new Color(255, 255, 255));
		wblblc.setFont(Style.FONT_FUTURA_50);
		wblblc.setText("-°C");
		add(wblblc, "cell 1 3 1 3,alignx left,aligny top");

		wblblPressQmh = new WebLabel();
		wblblPressQmh.setFont(Style.FONT_13);
		wblblPressQmh.setText("Press QNH hPa: -");
		add(wblblPressQmh, "cell 2 4");

		wblblPress = new WebLabel();
		wblblPress.setFont(Style.FONT_13);
		wblblPress.setText("Press MSL hPa: -");
		add(wblblPress, "cell 2 5,aligny top");

		wblblAirTemp = new WebLabel();
		wblblAirTemp.setFont(Style.FONT_13);
		wblblAirTemp.setText("App temp: -°C");
		add(wblblAirTemp, "cell 1 6,aligny bottom");

		wbtnViewChart = new WebButton();
		wbtnViewChart.setFont(Style.FONT_13);
		wbtnViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewChart.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewChart.setDrawShade(false);
		wbtnViewChart.setText("View Chart");
		add(wbtnViewChart, "cell 2 6,alignx left,aligny bottom");

		wblblDewPoint = new WebLabel();
		wblblDewPoint.setFont(Style.FONT_13);
		wblblDewPoint.setText("Dew Point: -°C");
		add(wblblDewPoint, "cell 1 7");

		dtFormatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

		wbtnViewWeatherHistory = new WebButton();
		wbtnViewWeatherHistory.setText("View Weather History");
		wbtnViewWeatherHistory.setFont(Style.FONT_13);
		wbtnViewWeatherHistory.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewWeatherHistory.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewWeatherHistory.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewWeatherHistory.setDrawShade(false);
		add(wbtnViewWeatherHistory, "cell 2 7");

		JPanel panelFiller2 = new JPanel();
		panelFiller2.setVisible(false);
		add(panelFiller2, "cell 0 8,grow");

		wblblLastUpdate = new WebLabel();
		wblblLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		wblblLastUpdate.setText("Last update: -");
		add(wblblLastUpdate, "cell 0 9 2 1,aligny bottom");

		wblblRemoveFromFavourites = new WebLabel();
		wblblRemoveFromFavourites.setForeground(Color.RED);
		wblblRemoveFromFavourites.setFont(new Font("Century Gothic", Font.ITALIC, 13));
		wblblRemoveFromFavourites.setText("Remove from Favourites");

		add(wblblRemoveFromFavourites, "cell 2 9,alignx right,aligny bottom");

		addListeners();

		updateStation();
	}

	private void addListeners() {
		wbtnViewChart.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listenerAction != null)
					_listenerAction.onViewChartClick();
			}
		});

		wbtnViewWeatherHistory.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listenerAction != null)
					_listenerAction.onViewHistoryClick();
			}
		});

		wblblRemoveFromFavourites.addMouseListener(new MouseAdapter() {
			public final void mouseClicked(MouseEvent e) {
				if (_listenerRemoveFavorite != null
						&& wblblRemoveFromFavourites.isEnabled())
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
					setBackground(new Color(176, 196, 222));
					wblblc.setForeground(new Color(255, 255, 255));
				} else if (r.airTemp < TEMP_COOL) {
					setBackground(new Color(240, 248, 255));
					wblblc.setForeground(new Color(30, 144, 255));
				} else {
					setBackground(new Color(255, 248, 220));
					wblblc.setForeground(new Color(255, 99, 71));
				}
				wblblc.setText(r.airTemp + "°C");
			} else {
				setBackground(new Color(176, 196, 222));
				wblblc.setForeground(Color.black);
				wblblc.setText("-°C");
			}

			// set Text
			wblblHumid.setText("Humid: " + (r.relativeHumidity == null ? "-" : r.relativeHumidity) + "%");
			wblblStation.setText(station.getName());
			wblblState.setText(station.getState().getName());

			// set wind format
			String windDir = r.windDir == null ? "" : r.windDir;
			String windSpd = r.windSpdKmH == null || r.windGustKmH == null ? "" : r.windSpdKmH + "-" + r.windGustKmH;
			wblblWindSse.setText("Wind: " + windDir + " " + windSpd + " km/h");

			wblblRainSinceam.setText("Rain since 9am: " + (r.rainTrace == null ? "-" : r.rainTrace) + "mm");
			wblblPressQmh.setText("Press QNH hPa: " + (r.pressureQNH == null ? "-" : r.pressureQNH));
			wblblPress.setText("Press MSL hPa: " + (r.pressureMSL == null ? "-" : r.pressureMSL));
			wblblAirTemp.setText("App temp: " + (r.apparentTemp == null ? "-" : r.apparentTemp) + "°C");
			wblblDewPoint.setText("Dew Point: " + (r.dewPt == null ? "-" : r.dewPt) + "°C");
			wblblLastUpdate.setText("Last update: " + dtFormatter.print(r.localDateTime));
		} else {
			// set empty temp
			setBackground(new Color(176, 196, 222));
			wblblc.setForeground(Color.black);
			wblblc.setText("-°C");

			// set Text
			wblblHumid.setText("Humid: -%");
			wblblStation.setText(station.getName());
			wblblState.setText(station.getState().getName());
			wblblWindSse.setText("Wind: -km/h");
			wblblRainSinceam.setText("Rain since 9am: -mm");
			wblblPressQmh.setText("Press QNH hPa: -");
			wblblPress.setText("Press MSL hPa: -");
			wblblAirTemp.setText("App temp: -°C");
			wblblDewPoint.setText("Dew Point: -°C");
			wblblLastUpdate.setText("Last update: -");
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
		wbtnViewChart.setEnabled(isBlockUI);
		wbtnViewWeatherHistory.setEnabled(isBlockUI);
		wblblRemoveFromFavourites.setEnabled(isBlockUI);

	}
}
