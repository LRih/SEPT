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
        this.key = station.getKey();

        this.state = station.getState().getName();
        this.station = station.getName();
    }


    // TODO document
    public final int compareTo(Favorite fav)
    {
        // sort by key
        return key.compareTo(fav.key);
    }
}
