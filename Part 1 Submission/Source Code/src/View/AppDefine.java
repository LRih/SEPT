package View;

import java.io.IOException;

import javax.swing.JFrame;

import Utils.AppStateManager;
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
	public static final int MAIN_PANEL = 1;
	public static final int ADD_STATION_PANEL = 2;

	public static final int STATION_DETAIL = 0;
	public static final int VIEW_CHART = 1;
	public static final int VIEW_HISTORY = 2;

	public static final int TEMP_FREEZING = 0;
	public static final int TEMP_COOL = 25;

	public static final int NOTIFICATION_CLOSE_TIME_MILLIS = 5000;

	public static final int CHART_9AM = 0;
	public static final int CHART_3PM = 1;
	public static final int CHART_MAX = 2;
	public static final int CHART_MIN = 3;

	public static final boolean DEBUGGING = false;

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
     */
	public static void initApp(JFrame mainFrame) {

        // load session state
        AppStateManager.tryLoad();

		dtfOut = DateTimeFormat.forPattern("HH:mm dd/MM");

		try {
			states = DataManager.loadStates();
		} catch (Exception e) {
			WebOptionPane.showMessageDialog(mainFrame, "\"stations.json\" missing or invalid, closing.", "ERROR", WebOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		// load favorites and delete invalid entries
		try {
			favorites = FavoritesManager.load();
            FavoritesManager.removeInvalidFavourites(favorites, states);
            FavoritesManager.save(favorites);
		} catch (IOException e) {
			// no problem
		}

        // check for invalid station and state from previous session
        AppState as = AppState.getInstance();
        boolean isValid = states.get(as.state) != null && states.get(as.state).getStation(as.station) != null;
        if (isValid && favorites.exists(as.state, as.station))
            currentStation = states.get(as.state).getStation(as.station);

	}
}
