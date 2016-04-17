package View;

import java.io.IOException;

import javax.swing.JFrame;

import Utils.DataManager;
import Utils.FavoritesManager;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alee.laf.optionpane.WebOptionPane;

import Model.AppState;
import Model.Favorites;
import Model.States;
import Model.Station;
import Model.StationData;

/**
 * initialises the front-end application
 */
public final class AppDefine {

	public static final int FIRST_RUN_PANEL = 0;
	public static final int ADD_STATION_PANEL = 1;
	public static final int MAIN_PANEL = 2;
	public static final int VIEW_CHART = 3;
	public static final int VIEW_HISTORY = 4;
	public static final int STATION_DETAIL = 5;
	public static final int TEMP_FREEZING = 0;
	public static final int TEMP_COOL = 25;
	public static final int NOTIFICATION_CLOSE_TIME_MILLIS = 5000;
	public static final int CHART_9AM = 0;
	public static final int CHART_3PM = 1;
	public static final int CHART_MAX = 2;
	public static final int CHART_MIN = 3;

	public static final boolean DEBUGGING = true;

    /**
     * Current data used by the view.
     */
	public static States states = null;
	public static Favorites favorites;
	public static Station currentStation;
	public static StationData currentStationData;

	public static DateTimeFormatter dtfOut;
	
	/**
	 * initialises ui elements
	 * @throws Exception e when invalid states format
     */
	public static void initApp(JFrame mainFrame) {

		dtfOut = DateTimeFormat.forPattern("HH:mm dd/MM");

		if (!AppState.getInstance().state.equals("") && !AppState.getInstance().station.equals(""))
			currentStation = AppDefine.states.get(AppState.getInstance().state)
					.getStation(AppState.getInstance().station);

		try {
			states = DataManager.loadStates();
		} catch (Exception e) {
			WebOptionPane.showMessageDialog(mainFrame, "Invalid States Format!", "ERROR", WebOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		try {
			favorites = FavoritesManager.load();
		} catch (IOException e) {
			// no problem
		}

	}

}
