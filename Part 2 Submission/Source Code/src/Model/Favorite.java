package Model;

import java.io.Serializable;

/**
 * Data structure for representing a favorite.
 */
public final class Favorite implements Serializable, Comparable<Favorite>
{
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
        this(station.getState().getName(), station.getName());
    }

    /**
     * Creates a new favourite.
     *
     * @param state name of state
     * @param station name of station
     */
    public Favorite(String state, String station)
    {
        this.state = state;
        this.station = station;
    }


    /**
     * Favorites are sorted alphabetically by state then station.
     *
     * @return the sort order compared to {@code fav}
     * @param fav comparison target
     */
    public final int compareTo(Favorite fav)
    {
        int stateCmp = state.compareTo(fav.state);
        if (stateCmp != 0)
            return stateCmp;

        return station.compareTo(fav.station);
    }
}
