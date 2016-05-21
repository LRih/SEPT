package View;

import javax.swing.JPanel;

import Model.*;
import Utils.Log;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;

import com.alee.laf.button.WebButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

/**
 * Main Panel UI
 */
public final class MainPanel extends JPanel
		implements FavoriteCell.OnStationSelectListener, FavoriteCell.OnDataLoadListener {
	private final JPanel panelMainContent;
	private final JPanel panelFavorites;
	private final WebButton buttonAddStation;

	private final StationDetail stationDetail;
	private final StationChart stationChart;
	private final StationHistory stationHistory;
	private final StationForecast stationForecast;

	private OnActionListener _listenerAction;
	private FavoriteCell.OnStationSelectListener _listenerStationSelect;
	private FavoriteCell.OnDataLoadListener _listenerDataLoad;

	private JPanel pnStationList;

	/**
	 * Create the panel.
	 */
	public MainPanel() {
		setBackground(new Color(34, 139, 34));

		setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow][20%]", "[][grow][199][]"));

		panelFavorites = new JPanel();
		panelFavorites.setBackground(new Color(255, 255, 255));
		panelFavorites.setLayout(new MigLayout("ins 4 0 0 0", "[grow][grow]", ""));

		panelMainContent = new JPanel();
		panelMainContent.setBackground(Style.MAIN_PANEL_BACKGROUND);
		add(panelMainContent, "cell 0 1 2 1,grow");
		
		stationForecast = new StationForecast();
		add(stationForecast, "cell 0 2 2 1, growx, hmin 200");

		pnStationList = new JPanel();
		add(pnStationList, "cell 0 3 2 1,grow");
		pnStationList.setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow][20%]", "[28][grow]"));
		pnStationList.setBackground(new Color(169, 169, 169));

		WebLabel labelWeatherStations = new WebLabel();
		pnStationList.add(labelWeatherStations, "cell 0 0, gapx 15 0, gapy 6 8");
		labelWeatherStations.setForeground(new Color(255, 255, 255));
		labelWeatherStations.setFont(Style.FONT_16);
		labelWeatherStations.setText("Favourite Stations");

		buttonAddStation = new WebButton("Add station");
		pnStationList.add(buttonAddStation, "cell 1 0,alignx right, gapx 0 15");
		buttonAddStation.setForeground(new Color(0, 100, 0));
		buttonAddStation.setDrawShade(false);
		buttonAddStation.setDefaultButtonShadeColor(new Color(154, 205, 50));
		buttonAddStation.setBottomSelectedBgColor(new Color(50, 205, 50));
		buttonAddStation.setBottomBgColor(new Color(240, 255, 240));
		buttonAddStation.setFont(Style.FONT_BENDER_13);
		buttonAddStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		WebScrollPane webScrollPane = new WebScrollPane(panelFavorites, false, true);
		webScrollPane.setDrawFocus(false);
		webScrollPane.setPreferredSize(new Dimension(0, 0));
		pnStationList.add(webScrollPane, "cell 0 1 2 1,hmin 158,grow");

		stationDetail = new StationDetail();
		stationChart = new StationChart();
		stationHistory = new StationHistory();
		

		addListeners();
	}

	private void addListeners() {
		OnBackClickListener listener = new OnBackClickListener() {
			public final void onBackClick() {
				showPanel(PanelType.Detail);
			}
		};

		stationChart.setOnBackClickListener(listener);
		stationHistory.setOnBackClickListener(listener);

		stationDetail.setOnActionListener(new StationDetail.OnActionListener() {
			public final void onViewChartClick() {
				showPanel(PanelType.Chart);
			}

			public final void onViewHistoryClick() {
				showPanel(PanelType.History);
			}
		});

		buttonAddStation.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				if (_listenerAction != null)
					_listenerAction.onAddClick();
			}
		});
	}
	
	public void setBlockUI(boolean isEnable) {
		stationChart.setBlockUI(!isEnable);
		stationHistory.setBlockUI(!isEnable);
		stationDetail.setBlockUI(!isEnable);
		stationForecast.setBlockUI(!isEnable);
		
		buttonAddStation.setEnabled(!isEnable);
	}

	/**
	 * Change panel shown.
	 */
	public final void showPanel(PanelType type) {
		panelMainContent.removeAll();
		panelMainContent.setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow]", "[grow]"));

		AppState.getInstance().shownDetail = type.ordinal();
		Log.info(getClass(), "Panel changed to " + type.name());

		switch (type) {
		// VIEW_CHART
		case Chart:
			panelMainContent.add(stationChart, "cell 0 0 1 1, grow");
			stationChart.animate();
			break;

		// VIEW_HISTORY
		case History:
			panelMainContent.add(stationHistory, "cell 0 0 1 1, grow");
			break;

		// STATION_DETAIL
		default:
			panelMainContent.add(stationDetail, "cell 0 0 1 1, grow");
			break;
		}

		panelMainContent.validate();
		panelMainContent.repaint();
	}

	public void updateBackgroundColor(boolean hasInternetConnection) {
		stationForecast.updateBackgroundColor(hasInternetConnection);
		if (hasInternetConnection)
			pnStationList.setBackground(Style.INTERNET_ON_BACKGROUND);
		else
			pnStationList.setBackground(Style.INTERNET_OFF_BACKGROUND);
	}

	/**
	 * Populate panel with favorites.
	 */
	public final void setFavorites(Favorites favorites, States states, Station selectedStation) {
		panelFavorites.removeAll();
		panelFavorites.setLayout(new MigLayout("ins 4 0 0 0", "[][grow]", "[]"));

		int row = 0;
		int col = 0;

		for (Favorite fav : favorites) {
			FavoriteCell cell = new FavoriteCell(states.get(fav.state).getStation(fav.station));
			cell.updateSelected(selectedStation);
			cell.setOnStationSelectListener(this);
			cell.setOnDataLoadListener(this);

			if (col % 2 == 0)
				panelFavorites.add(cell, "cell 0 " + row + ", width max(50%), gap 4");
			else
				panelFavorites.add(cell, "cell 1 " + (row++) + ", growx, gap 0 4");

			col++;
		}

	}

	public final void setStation(Station station, StationData data) {
		stationChart.setStation(station, data);
		stationHistory.setStation(station, data);
		stationDetail.setStation(station, data);
		stationForecast.setStation(station);

		// update selected favorite indicator
		for (Component cell : panelFavorites.getComponents())
			((FavoriteCell) cell).updateSelected(station);

		panelMainContent.validate();
		panelMainContent.repaint();
	}

	public final void setOnActionListener(OnActionListener listener) {
		_listenerAction = listener;
	}

	public final void setOnRemoveFromFavoritesClickListener(StationDetail.OnRemoveFavoriteClickListener listener) {
		stationDetail.setOnRemoveFavoriteClickListener(listener);
	}

	public final void setOnStationSelectListener(FavoriteCell.OnStationSelectListener listener) {
		_listenerStationSelect = listener;
	}

	public final void setOnDataLoadListener(FavoriteCell.OnDataLoadListener listener) {
		_listenerDataLoad = listener;
	}

	public interface OnActionListener {
		void onAddClick();
	}

	/* Event callbacks */

	public final void onStationSelect(Station station, StationData data) {
		if (_listenerStationSelect != null)
			_listenerStationSelect.onStationSelect(station, data);
	}

	public final void onDataLoadSuccess(Station station, StationData data) {
		if (_listenerDataLoad != null)
			_listenerDataLoad.onDataLoadSuccess(station, data);
	}

	public final void onDataLoadFail() {
		if (_listenerDataLoad != null)
			_listenerDataLoad.onDataLoadFail();
	}

	public enum PanelType {
		Detail, Chart, History
	}
}
