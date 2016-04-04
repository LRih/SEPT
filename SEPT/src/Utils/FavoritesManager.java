package Utils;

import Model.Favorite;
import Model.Favorites;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Handles the storage of favorites.
 */
public final class FavoritesManager
{
    private static String FILE_PATH = "favourites.json";
    private static int JSON_INDENT = 4;

    private static String KEY_KEY = "key";
    private static String KEY_STATE = "state";
    private static String KEY_STATION = "station";


    private FavoritesManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }

    /**
     * Loads favorites JSON from the filesystem.
     *
     * @return the deserialized favorites file
     * @throws IOException if there is an IO error of any sort
     * @throws JSONException if the JSON file is malformed
     */
    public static Favorites load() throws IOException, JSONException
    {
        Favorites favorites = new Favorites();

        // return empty list if file doesn't exist
        if (!new File(FILE_PATH).exists())
            return favorites;

        // process json file and load into favorites
        JSONArray json = new JSONArray(FileUtils.loadText(FILE_PATH));

        for (int i = 0; i < json.length(); i++)
        {
            JSONObject obj = json.getJSONObject(i);
            favorites.add(obj.getString(KEY_KEY), obj.getString(KEY_STATE), obj.getString(KEY_STATION));
        }

        return favorites;
    }

    /**
     * Saves favorites to the filesystem in JSON format.
     *
     * @param favorites the favorites instance to save
     * @throws IOException if there is an IO error of any sort
     * @throws JSONException if producing the JSON object produced an error
     */
    public static void save(Favorites favorites) throws IOException, JSONException
    {
        FileUtils.saveText(toJSON(favorites).toString(JSON_INDENT), FILE_PATH);
    }

    /**
     * Converts favorites to a JSON array for storage.
     *
     * @return the resultant JSON array
     */
    private static JSONArray toJSON(Favorites favorites)
    {
        JSONArray json = new JSONArray();

        for (Favorite favorite : favorites)
        {
            JSONObject obj = new JSONObject();
            obj.put(KEY_KEY, favorite.key);
            obj.put(KEY_STATE, favorite.state);
            obj.put(KEY_STATION, favorite.station);

            json.put(obj);
        }

        return json;
    }
}
