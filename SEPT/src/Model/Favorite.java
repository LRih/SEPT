package Model;

import java.io.Serializable;

public final class Favorite implements Serializable, Comparable<Favorite>
{
    public final String key;

    public final String state;
    public final String station;


    public Favorite(Station station)
    {
        this.key = station.getKey();

        this.state = station.getState().getName();
        this.station = station.getName();
    }


    public final int compareTo(Favorite fav)
    {
        // sort by key
        return key.compareTo(fav.key);
    }
}
