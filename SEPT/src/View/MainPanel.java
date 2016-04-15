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
import Utils.AppDefine;
import Utils.AppStateManager;
import Utils.DataManager;
import Utils.FavoritesManager;

import com.alee.laf.button.WebButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Main frmMain = null;
	private JPanel pnMainContent;
	StationDetail stationDetail;
	StationChart stationChart;
	StationHistory stationHistory;
	Station station;
	StationData stationData;
	Station selected;
	private Boolean shown;
	private WebButton wbtnAddStation;

	/**
	 * Create the panel.
	 */
	public MainPanel(Main m) {
		setBackground(new Color(255, 255, 255));
		frmMain = m;

		setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow][20%]", "[][grow][][160]"));

		JPanel pnFavorites = new JPanel();
		pnFavorites.setBackground(Color.WHITE);
		pnFavorites.setLayout(new MigLayout("ins 4 0 0 0", "[grow][grow]", ""));

		pnMainContent = new JPanel();
		pnMainContent.setBackground(new Color(240, 248, 255));
		add(pnMainContent, "cell 0 1 2 1,grow");

		WebLabel wblblWeatherStations = new WebLabel();
		wblblWeatherStations.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblWeatherStations.setText("Favourite Stations");
		add(wblblWeatherStations, "flowx,cell 0 2,gapx 15,gapy 5");

		wbtnAddStation = new WebButton("Add station");
		wbtnAddStation.setForeground(new Color(0, 100, 0));
		wbtnAddStation.setDrawShade(false);
		wbtnAddStation.setDefaultButtonShadeColor(new Color(154, 205, 50));
		wbtnAddStation.setBottomSelectedBgColor(new Color(50, 205, 50));
		wbtnAddStation.setBottomBgColor(new Color(240, 255, 240));
		wbtnAddStation.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnAddStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		add(wbtnAddStation, "cell 1 2,alignx right, gapx 0 15, gapy 5 0");

		WebScrollPane webScrollPane = new WebScrollPane(pnFavorites, false, true);
		webScrollPane.setDrawFocus(false);
		webScrollPane.setPreferredSize(new Dimension(0, 0));
		add(webScrollPane, "cell 0 3 2 1,grow, hmin 160");

		try {

			Favorites favs = FavoritesManager.load();
			if (favs.size() == 0)
				throw new Exception("NO_FAVORITE_STATION");
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
					pnFavorites.add(cell, "cell 0 " + row + ", grow, gap 4");
				else
					pnFavorites.add(cell, "cell 1 " + (row++) + ", grow, gap 0 4");

				col++;
			}
		} catch (Exception e1) {
			frmMain.showState(AppDefine.FIRST_SCREEN);
		}

		addListeners();
	}

	private void addListeners() {

		wbtnAddStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMain.showState(1);
			}
		});

	}

	public void showState(int index) {
		pnMainContent.removeAll();
		pnMainContent.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().shownWindow = index;
		AppStateManager.trySave();

		switch (index) {
		// STATION_DETAIL
		default:
			stationDetail = new StationDetail(this);
			if (stationData != null)
				stationDetail.setStation(station, stationData);
			pnMainContent.add(stationDetail, "cell 0 0, grow");
			break;

		// VIEW_CHART
		case AppDefine.VIEW_CHART:
			stationChart = new StationChart(this);
			if (stationData != null)
				stationChart.setStation(station, stationData);
			pnMainContent.add(stationChart, "cell 0 0, grow");
			break;
		// VIEW_HISTORY
		case AppDefine.VIEW_HISTORY:
			stationHistory = new StationHistory(this);
			if (stationData != null)
				stationHistory.setStation(station, stationData);
			pnMainContent.add(stationHistory, "cell 0 0, grow");
			break;
		}
		pnMainContent.validate();
		pnMainContent.repaint();

	}

	public void setStation(Station station, StationData data) {
		this.station = station;
		this.stationData = data;
		if (AppState.getInstance().shownWindow == AppDefine.STATION_DETAIL) {
			stationDetail.setStation(station, data);
			showState(AppDefine.STATION_DETAIL);
		} else if (AppState.getInstance().shownWindow == AppDefine.VIEW_CHART) {
			stationChart.setStation(station, data);
			showState(AppDefine.VIEW_CHART);
		} else if (AppState.getInstance().shownWindow == AppDefine.VIEW_HISTORY) {
			stationHistory.setStation(station, data);
			showState(AppDefine.VIEW_HISTORY);
		}
	}

	public void loadFail() {
		if (!shown) {
			shown = true;
			final WebNotification notificationPopup = new WebNotification();
			notificationPopup.setIcon(NotificationIcon.error);
			notificationPopup.setDisplayTime(AppDefine.NOTIFICATION_CLOSE_TIME_MILLIS);
			notificationPopup.setContent("No Internet Connection");

			NotificationManager.showNotification(notificationPopup);
		}
	}

}
