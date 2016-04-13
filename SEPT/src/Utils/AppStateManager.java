package Utils;

import Model.AppState;
import Model.Station;

import java.awt.*;
import java.io.IOException;

/**
 * Handles the storage of app state.
 */
public final class AppStateManager
{
	
	// current app state
	public static int CURRENT_STATE = 0;
	// extra datas to store
	public static String v1 = "";
	public static String v2 = "";
	public static String v3 = "";
	public static String v4 = "";
	public static String v5 = "";
	
    /**
     * Saves various app state attributes in JSON format.
     *
     * @return the app state of last session. Returns default app state if first run
     * @throws IOException if there is an IO error of any sort
     */
    public static AppState load() throws IOException
    {
        // TODO implement
        return null;
    }

    /**
     * Saves various app state attributes in JSON format.
     *
     * @param windowRect coordinates and dimensions of window
     * @param shownStation current station shown to the user. {@code null} if no station
     * @param selectedChart current chart of station shown to the user {@code null} if no chart or no station
     * @throws IOException if there is an IO error of any sort
     */
    public static void save(Rectangle windowRect, Station shownStation, String selectedChart) throws IOException
    {
        // TODO implement
        // TODO how can we store selectedChart?
    }
}
