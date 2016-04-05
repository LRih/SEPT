package Model;

import java.io.Serializable;

/**
 * Data structure for representing a favorite.
 */
public final class Favorite implements Serializable, Comparable<Favorite>
{
    /**
     * For identifying the favorite. All favorites have a unique key.
     */
    public final String key;

    /**
     * Name of state.
     */
    public final String state;

    /**
     * Name of station.
     */
    public final String station;


    /**
     * Creates a new favourite.
     *
     * @param station the station to for which to create a favorite
     */
    public Favorite(Station station)
    {
        this(station.getKey(), station.getState().getName(), station.getName());
    }

    /**
     * Creates a new favourite.
     *
     * @param key key of station
     * @param state name of state
     * @param station name of station
     */
    public Favorite(String key, String state, String station)
    {
        this.key = key;

        this.state = state;
        this.station = station;
    }


    /**
     * Favorites are sorted alphabetically by key.
     *
     * @return the sort order compared to {@code fav}
     * @param fav comparison target
     */
    public final int compareTo(Favorite fav)
    {
        // sort by key
        return key.compareTo(fav.key);
    }
}
