package Model;

import java.io.Serializable;

// TODO document
public final class Favorite implements Serializable, Comparable<Favorite>
{
    // TODO document
    public final String key;

    // TODO document
    public final String state;
    // TODO document
    public final String station;


    // TODO document
    public Favorite(Station station)
    {
        this(station.getKey(), station.getState().getName(), station.getName());
    }
    // TODO document
    public Favorite(String key, String state, String station)
    {
        this.key = key;

        this.state = state;
        this.station = station;
    }


    // TODO document
    public final int compareTo(Favorite fav)
    {
        // sort by key
        return key.compareTo(fav.key);
    }
}
