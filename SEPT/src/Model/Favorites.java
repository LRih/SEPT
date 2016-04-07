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
        return add(station.getKey(), station.getState().getName(), station.getName());
    }

    /**
     * Adds a new favorite. Fails if it already exists.
     *
     * @return success of failure of the add
     * @param key unique station identifier
     * @param state name of state
     * @param station name of station
     */
    public final boolean add(String key, String state, String station)
    {
        // only add if favorite does not already exist
        if (!favorites.containsKey(key))
        {
            favorites.put(key, new Favorite(key, state, station));
            return true;
        }

        return false;
    }

    /**
     * Deletes a station. Fails if it already exists.
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
