package Utils;

import Model.Favorite;
import Model.Favorites;
import Model.States;
import Model.Station;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the storage of favorites.
 */
public final class FavoritesManager
{
    private static String FILE_PATH = "favourites.json";
    private static int JSON_INDENT = 4;

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
     */
    public static Favorites load() throws IOException
    {
        // return empty list if file doesn't exist
        if (!new File(FILE_PATH).exists())
            return new Favorites();

        return toFavorites(FileUtils.loadText(FILE_PATH));
    }

    /**
     * Saves favorites to the filesystem in JSON format.
     *
     * @param favorites the favorites instance to save
     * @throws IOException if there is an IO error of any sort
     */
    public static void save(Favorites favorites) throws IOException
    {
        FileUtils.saveText(toJSON(favorites).toString(JSON_INDENT), FILE_PATH);
    }

    /**
     * Remove any favorites not listed in states.
     *
     * @param favorites the favorites instance to save
     * @throws IOException if there is an IO error of any sort
     */
    public static void removeInvalidFavourites(Favorites favorites, States states)
    {
        List<String> invalidKeys = new ArrayList<>();

        // get list of invalid favorites
        for (Favorite fav : favorites)
            if (states.get(fav.state) == null || states.get(fav.state).getStation(fav.station) == null)
                invalidKeys.add(Station.getKey(fav.state, fav.station));

        // delete all collected invalid favorites
        for (String key : invalidKeys)
            favorites.delete(key);
    }


    /**
     * Converts JSON to favorites object. Ignores any invalid entries.
     *
     * @return the resultant favorites object
     */
    private static Favorites toFavorites(String json)
    {
        Favorites favorites = new Favorites();
        JSONArray array;

        // try to parse JSON
        try
        {
            array = new JSONArray(json);
        }
        catch (JSONException e)
        {
            // all entries are corrupt, return empty favorites
            e.printStackTrace();
            return favorites;
        }

        // get all favorites
        for (int i = 0; i < array.length(); i++)
        {
            try
            {
                JSONObject obj = array.getJSONObject(i);
                favorites.add(obj.getString(KEY_STATE), obj.getString(KEY_STATION));
            }
            catch (JSONException e)
            {
                // entry is corrupt so ignore
                e.printStackTrace();
            }
        }

        return favorites;
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
            try
            {
                JSONObject obj = new JSONObject();
                obj.put(KEY_STATE, favorite.state);
                obj.put(KEY_STATION, favorite.station);

                json.put(obj);
            }
            catch (JSONException e)
            {
                // ignore favorite when error occurred trying to save it
                e.printStackTrace();
            }
        }

        return json;
    }
}
