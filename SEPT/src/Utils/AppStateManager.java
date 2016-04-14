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

    private static String KEY_STATE = "state";
    private static String KEY_STATION = "station";
    private static String KEY_CHART = "chart";
    private static String KEY_V1 = "v1";
    private static String KEY_V2 = "v2";
    private static String KEY_V4 = "v4";
    private static String KEY_V5 = "v5";

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

            state.state = json.getInt(KEY_STATE);
            state.station = json.getString(KEY_STATION);
            state.v1 = json.getInt(KEY_V1);
            state.v2 = json.getString(KEY_V2);
            state.v4 = json.getString(KEY_V4);
            state.v5 = json.getString(KEY_V5);
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
            json.put(KEY_STATE, appState.state);
            json.put(KEY_STATION, appState.station);
            json.put(KEY_V1, appState.v1);
            json.put(KEY_V2, appState.v2);
            json.put(KEY_V4, appState.v4);
            json.put(KEY_V5, appState.v5);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json;
    }
}
