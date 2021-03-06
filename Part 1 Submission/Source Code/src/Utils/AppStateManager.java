package Utils;

import Model.AppState;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Handles the storage of app state.
 */
public final class AppStateManager
{
    private static String FILE_PATH = "appstate.json";
    private static int JSON_INDENT = 4;

    private static String KEY_WINDOW_RECT = "windowRect";
    private static String KEY_SHOWN_WINDOW = "shownWindow";
    private static String KEY_SHOWN_DETAIL = "shownDetail";
    private static String KEY_STATE = "state";
    private static String KEY_STATION = "station";
    private static String KEY_CHART_INDEX = "chartIndex";

    /**
     * Tries to load the app state from the last session.
     */
    public static void tryLoad()
    {
        try
        {
            load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads the app state from the last session.
     *
     * @throws IOException if there is an IO error of any sort
     */
    public static void load() throws IOException
    {
        // don't do anything if app state doesn't exist
        if (!new File(FILE_PATH).exists())
            return;

        String data = FileUtils.loadText(FILE_PATH);
        AppState state = AppState.getInstance();

        // try to parse JSON
        try
        {
            JSONObject json = new JSONObject(data);

            state.windowRect = json.getString(KEY_WINDOW_RECT);
            state.shownWindow = json.getInt(KEY_SHOWN_WINDOW);
            state.shownDetail = json.getInt(KEY_SHOWN_DETAIL);
            state.state = json.getString(KEY_STATE);
            state.station = json.getString(KEY_STATION);
            state.chartIndex = json.getInt(KEY_CHART_INDEX);
        }
        catch (JSONException e)
        {
            // app state is corrupt, set app state to defaults
            e.printStackTrace();
            state.resetDefault();
        }
    }

    /**
     * Tries to save the current instance of app state.
     */
    public static void trySave()
    {
        try
        {
            save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current instance of app state.
     *
     * @throws IOException if there is an IO error of any sort
     */
    public static void save() throws IOException
    {
        FileUtils.saveText(toJSON(AppState.getInstance()).toString(JSON_INDENT), FILE_PATH);
    }

    /**
     * Converts app state to a JSON object for storage.
     *
     * @return the resultant JSON array
     */
    private static JSONObject toJSON(AppState appState)
    {
        JSONObject json = new JSONObject();

        try
        {
            json.put(KEY_WINDOW_RECT, appState.windowRect);
            json.put(KEY_SHOWN_WINDOW, appState.shownWindow);
            json.put(KEY_SHOWN_DETAIL, appState.shownDetail);
            json.put(KEY_STATE, appState.state);
            json.put(KEY_STATION, appState.station);
            json.put(KEY_CHART_INDEX, appState.chartIndex);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }
}
