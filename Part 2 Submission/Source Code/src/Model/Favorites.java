package Model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * Data structure for representing a collection of favorites.
 */
public final class Favorites implements Serializable, Iterable<Favorite>
{
    private final TreeMap<String, Favorite> favorites;


    /**
     * Creates a new favorites instance.
     */
    public Favorites()
    {
        this.favorites = new TreeMap<>();
    }


    /**
     * Adds a new favorite. Fails if it already exists.
     *
     * @return success of failure of the add
     * @param station station for which to add a favorite
     */
    public final boolean add(Station station)
    {
        return add(station.getState().getName(), station.getName());
    }

    /**
     * Adds a new favorite. Fails if it already exists.
     *
     * @return success of failure of the add
     * @param state name of state
     * @param station name of station
     */
    public final boolean add(String state, String station)
    {
        String key = Station.getKey(state, station);

        // only add if favorite does not already exist
        if (!favorites.containsKey(key))
        {
            favorites.put(key, new Favorite(state, station));
            return true;
        }

        return false;
    }

    /**
     * Deletes a favorite. Fails if it doesn't exist.
     *
     * @return success of failure of the deletion
     * @param station station for which to delete favorite
     */
    public final boolean delete(Station station)
    {
        if (favorites.containsKey(station.getKey()))
        {
            favorites.remove(station.getKey());
            return true;
        }

        return false;
    }

    /**
     * Deletes a favorite. Fails if it doesn't exist.
     *
     * @return success of failure of the deletion
     * @param key key of favorite
     */
    public final boolean delete(String key)
    {
        if (favorites.containsKey(key))
        {
            favorites.remove(key);
            return true;
        }

        return false;
    }


    /**
     * Checks if favorite exists.
     */
    public final boolean exists(String state, String station)
    {
        return favorites.containsKey(Station.getKey(state, station));
    }

    /**
     * Returns the number of favorites.
     */
    public final int size()
    {
        return favorites.size();
    }

    public final Iterator<Favorite> iterator()
    {
        return new KeySetIterator<>(favorites);
    }
}
