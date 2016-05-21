package View;

import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Data.DataManager;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Utils.StationDataWorker;
import Utils.StationDataWorker.OnTaskCompleteListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.border.LineBorder;

/**
 * A Station Cell for repeating in favorite list.
 */
public final class FavoriteCell extends JPanel implements OnTaskCompleteListener {
	private final WebLabel labelTemp;

	private final Station station;
	private StationData data;

	private boolean isSelected = false;

	private OnStationSelectListener _listenerSelect;
	private OnDataLoadListener _listenerDataLoad;

	/**
	 * Create the panel.
	 */
	public FavoriteCell(Station station) {
		this.station = station;

		StationDataWorker dataWorker = new StationDataWorker(station);
		dataWorker.setOnTaskCompleteListener(this);
		dataWorker.execute();

		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter() {
			public final void mouseClicked(MouseEvent e) {
				if (_listenerSelect != null)
					_listenerSelect.onStationSelect(FavoriteCell.this.station, data);
			}

			// hover effect
			public final void mouseEntered(MouseEvent e) {
				setBorder(new LineBorder(Style.CELL_SELECTED));
			}

			public final void mouseExited(MouseEvent e) {
				setBorder(new LineBorder(isSelected ? Style.CELL_SELECTED : Style.CELL_NORMAL));
			}
		});

		setBorder(new LineBorder(Style.CELL_NORMAL));
		setBackground(Style.CELL_BACKGROUND);
		setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));

		WebLabel labelStation = new WebLabel();
		labelStation.setForeground(Style.CELL_STATION_LABEL);
		labelStation.setFont(Style.FONT_16);
		labelStation.setText(station.getName());
		add(labelStation, "cell 0 0 1 2,alignx left,grow");

		labelTemp = new WebLabel();
		labelTemp.setFont(Style.FONT_FUTURA_20);
		labelTemp.setForeground(new Color(34, 139, 34));

		add(labelTemp, "cell 1 0 1 3,alignx center,aligny center");

		WebLabel labelState = new WebLabel();
		labelState.setFont(Style.FONT_BENDER_12);
		labelState.setText(station.getState().getName());
		add(labelState, "cell 0 2");

		setStationData(DataManager.getCachedStationData(station));
	}

	public final void updateSelected(Station selectedStation) {
		isSelected = station == selectedStation;
		setBorder(new LineBorder(isSelected ? Style.CELL_SELECTED : Style.CELL_NORMAL));
	}

	private void setStationData(StationData data) {
		this.data = data;
		labelTemp.setText("-");

		if (data != null) {
			List<LatestReading> readings = data.getLatestReadings();

			if (readings.size() > 0 && readings.get(0).airTemp != null)
				labelTemp.setText(readings.get(0).airTemp.toString());
			labelTemp.setForeground(Style.INTERNET_ON_BACKGROUND);
		} else
			labelTemp.setForeground(new Color(155, 155, 155));
	}

	public final void setOnStationSelectListener(OnStationSelectListener listener) {
		_listenerSelect = listener;
	}

	public final void setOnDataLoadListener(OnDataLoadListener listener) {
		_listenerDataLoad = listener;
	}

	/**
	 * On success callback for StationWorker
	 */
	public final void onTaskSuccess(StationData data) {
		if (_listenerDataLoad != null)
			_listenerDataLoad.onDataLoadSuccess(station, data);

		setStationData(data);
		labelTemp.setForeground(Style.INTERNET_ON_BACKGROUND);
	}

	/**
	 * On fail call back for StationWorker
	 */
	public final void onTaskFail() {
		if (_listenerDataLoad != null)
			_listenerDataLoad.onDataLoadFail();
		labelTemp.setForeground(Style.INTERNET_OFF_BACKGROUND);
	}

	public interface OnStationSelectListener {
		void onStationSelect(Station station, StationData data);
	}

	public interface OnDataLoadListener {
		void onDataLoadSuccess(Station station, StationData data);

		void onDataLoadFail();
	}
}
