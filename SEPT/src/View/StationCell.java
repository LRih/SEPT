package View;

import javax.swing.*;

import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Model.AppState;
import Model.Favorite;
import Model.Station;
import Model.StationData;
import Utils.AppStateManager;
import Utils.DataManager;
import Utils.StationDataWorker;
import Utils.StationDataWorker.OnTaskCompleteListener;

import java.awt.*;
import java.io.IOException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A Station Cell for repeating in favorite list.
 */
public final class StationCell extends JPanel implements OnTaskCompleteListener {

	private static final long serialVersionUID = 1L;
	private Station station = null;
	private StationData data = null;
	private Boolean selected = false;
	private MainPanel main = null;
	private WebLabel wblblStation;
	private WebLabel wblblTemp;
	private WebLabel wblblState;
	private StationDataWorker dataWorker;

	/**
	 * Create the panel.
	 */
	public StationCell(final MainPanel m, final Favorite fav, Boolean selected) {
		this.main = m;
		this.selected = selected;
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				m.setStation(station, data);
				AppState.getInstance().state = fav.state;
				AppState.getInstance().station = fav.station;
			}

			// hover effect
			public final void mouseEntered(MouseEvent e) {
				setBackground(new Color(240, 248, 255));
			}

			public final void mouseExited(MouseEvent e) {
				setBackground(new Color(248, 248, 255));
			}
		});

		this.station = AppDefine.states.get(fav.state).getStation(fav.station);
		
		try {
			this.data = DataManager.getCachedStationData(station);
		} catch (Exception e1) {
            e1.printStackTrace();
			// no problem, this is normal.
			this.data = null;
		}

		dataWorker = new StationDataWorker(station);
		dataWorker.setOnTaskCompleteListener(this);
		dataWorker.execute();

		setBorder(null);
		setBackground(new Color(248, 248, 255));
		setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));

		wblblStation = new WebLabel();
		wblblStation.setForeground(new Color(255, 69, 0));
		wblblStation.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblStation.setText(fav.station);
		add(wblblStation, "cell 0 0 1 2,alignx left,grow");

		wblblTemp = new WebLabel();
		wblblTemp.setForeground(new Color(155, 155, 155));
		wblblTemp.setFont(new Font("Futura", Font.PLAIN, 20));

		if (this.data != null) {
			try {
				if (selected)
					m.setStation(this.station, this.data);
			} catch (Exception e) {
				e.printStackTrace();
			}

            if(data.getLatestReadings().size() > 0 && data.getLatestReadings().get(0).getAirTemp() != null)
			    wblblTemp.setText(data.getLatestReadings().get(0).getAirTemp().toString());
		} else
			wblblTemp.setText("-");
		
		add(wblblTemp, "cell 1 0 1 3,alignx center,aligny center");

		wblblState = new WebLabel();
		wblblState.setFont(new Font("Bender", Font.PLAIN, 12));
		wblblState.setText(fav.state);
		add(wblblState, "cell 0 2");

	}

	/**
	 * On success call back for StationWorker
	 */
	@Override
	public void onSuccess(StationData data) {

		this.data = data;
		if (selected) {
			main.setStation(station, data);
		}

        main.frmMain.setMainBg(true);

        wblblStation.setText(station.getName());
		wblblState.setText(station.getState().getName());

        if(data.getLatestReadings().size() > 0 && data.getLatestReadings().get(0).getAirTemp() != null)
            wblblTemp.setText(data.getLatestReadings().get(0).getAirTemp().toString());
        else
            wblblTemp.setText("-");

		wblblTemp.setForeground(new Color(34, 139, 34));

	}

	/**
	 * On fail call back for StationWorker
	 */
	@Override
	public void onFail(Exception e) {
        e.printStackTrace();
		main.loadFail();
		main.frmMain.setMainBg(false);
		main.showState(AppDefine.STATION_DETAIL, this.getClass().getName());
	}
}
