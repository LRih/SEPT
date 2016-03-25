package Model;

import java.io.Serializable;

public final class Favorite implements Serializable, Comparable<Favorite>
{
    public final String id;
    public final String name;


    public Favorite(String id, String name)
    {
        this.id = id;
        this.name = name;
    }


    public final int compareTo(Favorite fav)
    {
        // sort alphabetically
        return name.compareTo(fav.name);
    }
}
