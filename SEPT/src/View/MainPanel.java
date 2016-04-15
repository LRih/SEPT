package View;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;

import Model.AppState;
import Model.Favorite;
import Model.Favorites;
import Model.States;
import Model.Station;
import Model.StationData;
import Utils.AppStateManager;
import Utils.DataManager;
import Utils.FavoritesManager;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.time.ClockType;
import com.alee.extended.time.WebClock;
import com.alee.laf.button.WebButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Main main = null;
	private JPanel panel_1;
	StationDetail stationDetail;
	StationChart stationChart;
	StationHistory stationHistory;
	Station station;
	StationData stationData;
	Station selected;
	private Boolean shown;

	/**
	 * Create the panel.
	 */
	public MainPanel(Main m) {
		setBackground(new Color(255, 255, 255));
		main = m;

		setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow][20%]", "[][grow][][160]"));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new MigLayout("ins 4 0 0 0", "[grow][grow]", ""));

		panel_1 = new JPanel();
		panel_1.setBackground(new Color(240, 248, 255));
		add(panel_1, "cell 0 1 2 1,grow");

		WebLabel wblblWeatherStations = new WebLabel();
		wblblWeatherStations.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblWeatherStations.setText("Favourite Stations");
		add(wblblWeatherStations, "flowx,cell 0 2,gapx 15,gapy 5");

		WebButton webButton = new WebButton("Add station");
		webButton.setForeground(new Color(0, 100, 0));
		webButton.setDrawShade(false);
		webButton.setDefaultButtonShadeColor(new Color(154, 205, 50));
		webButton.setBottomSelectedBgColor(new Color(50, 205, 50));
		webButton.setBottomBgColor(new Color(240, 255, 240));
		webButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showState(1);
			}
		});
		webButton.setFont(new Font("Bender", Font.PLAIN, 13));
		webButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(webButton, "cell 1 2,alignx right, gapx 0 15, gapy 5 0");

		WebScrollPane webScrollPane = new WebScrollPane(panel, false, true);
		webScrollPane.setDrawFocus(false);
		webScrollPane.setPreferredSize(new Dimension(0, 0));
		add(webScrollPane, "cell 0 3 2 1,grow, hmin 160");

		try {

			Favorites favs = FavoritesManager.load();
			int row = 0;
			int col = 0;
			StationCell cell = null;

			if (selected == null)
				try {
					// TODO if DataManager.loadStates() fails, just best to
					// display
					// error message and close the app
					States states = DataManager.loadStates();
					if (!AppState.getInstance().state.equals("") && !AppState.getInstance().station.equals("")) {
						selected = states.get(AppState.getInstance().state).getStation(AppState.getInstance().station);
					}
				} catch (Exception e) {
					System.exit(0);
				}

			Boolean flag;
			shown = false;
			for (Favorite fav : favs) {
				flag = false;

				if (selected != null) {
					if (fav.state.equals(selected.getState().getName()) && fav.station.equals(selected.getName()))
						flag = true;
				} else {
					if (row == 0 && col == 0)
						flag = true;
				}

				cell = new StationCell(this, fav, flag);

				if (col % 2 == 0)
					panel.add(cell, "cell 0 " + row + ", grow, gap 4");
				else
					panel.add(cell, "cell 1 " + (row++) + ", grow, gap 0 4");

				col++;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void showState(int index) {
		panel_1.removeAll();
		panel_1.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().v1 = index;
		AppStateManager.trySave();

		switch (index) {
		// STATION DETAIL
		default:
			stationDetail = new StationDetail(this);
			if (stationData != null)
				stationDetail.setStation(station, stationData);
			panel_1.add(stationDetail, "cell 0 0, grow");
			break;

		// VIEW_CHART
		case 1:
			stationChart = new StationChart(this);
			if (stationData != null)
				stationChart.setStation(station, stationData);
			panel_1.add(stationChart, "cell 0 0, grow");
			break;
		// VIEW_HISTORY
		case 2:
			stationHistory = new StationHistory(this);
			if (stationData != null)
				stationHistory.setStation(station, stationData);
			panel_1.add(stationHistory, "cell 0 0, grow");
			break;
		}
		panel_1.validate();
		panel_1.repaint();

	}

	public void setStation(Station station, StationData data) {
		this.station = station;
		this.stationData = data;
		if (AppState.getInstance().v1 == 0) {
			stationDetail.setStation(station, data);
			showState(0);
		} else if (AppState.getInstance().v1 == 1) {
			stationChart.setStation(station, data);
			showState(1);
		} else if (AppState.getInstance().v1 == 2) {
			stationHistory.setStation(station, data);
			showState(2);
		}
	}

	public void loadFail() {
		if (!shown) {
			shown = true;
			final WebNotification notificationPopup = new WebNotification();
			notificationPopup.setIcon(NotificationIcon.clock);
			notificationPopup.setDisplayTime(5000);

			final WebClock clock = new WebClock();
			clock.setClockType(ClockType.timer);
			clock.setTimeLeft(6000);
			clock.setTimePattern("'No Internet Connection (' ss ')'");
			notificationPopup.setContent(new GroupPanel(clock));

			NotificationManager.showNotification(notificationPopup);
			clock.start();
		}
	}

}
