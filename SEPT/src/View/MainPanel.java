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
import Model.Station;
import Model.StationData;

import com.alee.laf.button.WebButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Main Panel UI
 */
public final class MainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	protected Main frmMain = null;
	private JPanel pnMainContent;
	private StationDetail stationDetail;
	private StationChart stationChart;
	private StationHistory stationHistory;
	private Station selected;
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
		
		stationDetail = new StationDetail(this);
		stationChart = new StationChart(this);
		stationHistory = new StationHistory(this);

        // show first run panel if no valid favorites
        if (AppDefine.favorites.size() == 0)
            frmMain.showState(AppDefine.FIRST_RUN_PANEL, this.getClass().getName());
        else
        {
            // populate favorites list
            AppState as = AppState.getInstance();

            int row = 0;
            int col = 0;
            StationCell cell;

            if (selected == null) {
                if (AppDefine.states.get(as.state) != null && AppDefine.states.get(as.state).getStation(as.station) != null)
                    selected = AppDefine.states.get(as.state).getStation(as.station);
            }

            boolean flag;
            shown = false;
            for (Favorite fav : AppDefine.favorites) {
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
        }

		addListeners();
	}

	private void addListeners() {

		wbtnAddStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmMain.showState(AppDefine.ADD_STATION_PANEL, this.getClass().getName());
			}
		});

	}

	/**
     * Change App State
     */
	public void showState(int index, String from) {

		if (AppDefine.DEBUGGING)
			System.out.println("Class " + from + ": " + this.getClass().getName() + ".showState(" + index + ");");

		pnMainContent.removeAll();
		pnMainContent.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().shownDetail = index;

		switch (index) {
		// STATION_DETAIL
		default:
			if (AppDefine.currentStationData != null)
				stationDetail.setStation();
			pnMainContent.add(stationDetail, "cell 0 0, grow");
			break;
		// VIEW_CHART
		case AppDefine.VIEW_CHART:
			if (AppDefine.currentStationData != null)
				stationChart.setStation(AppDefine.currentStation);
			pnMainContent.add(stationChart, "cell 0 0, grow");
			break;
		// VIEW_HISTORY
		case AppDefine.VIEW_HISTORY:
			if (AppDefine.currentStationData != null)
				stationHistory.setStation();
			pnMainContent.add(stationHistory, "cell 0 0, grow");
			break;
		}
		pnMainContent.validate();
		pnMainContent.repaint();

	}

	/**
     * Set Current Station data
     */
	public void setStation(Station station, StationData data) {
		
		AppDefine.currentStation = station;
		AppDefine.currentStationData = data;

		switch (AppState.getInstance().shownDetail) {
            case AppDefine.VIEW_CHART:
                stationChart.setStation(station);
                showState(AppDefine.VIEW_CHART, this.getClass().getName());
                break;
            case AppDefine.VIEW_HISTORY:
                stationHistory.setStation();
                showState(AppDefine.VIEW_HISTORY, this.getClass().getName());
                break;
            default:
                stationDetail.setStation();
                showState(AppDefine.STATION_DETAIL, this.getClass().getName());
        }
	}

	/**
     * Show error notification when one of the station loaded failed.
     */
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
