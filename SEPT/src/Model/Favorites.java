package Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.TreeMap;

public final class Favorites implements Serializable
{
    private final TreeMap<String, Favorite> favorites;


    public Favorites()
    {
        this.favorites = new TreeMap<>();
    }


    public final boolean add(Station station)
    {
        // only add if favorite does not already exist
        if (!favorites.containsKey(station.getKey()))
        {
            favorites.put(station.getKey(), new Favorite(station));
            return true;
        }

        return false;
    }
    public final boolean delete(Station station)
    {
        if (favorites.containsKey(station.getKey()))
        {
            favorites.remove(station.getKey());
            return true;
        }

        return false;
    }

    public final Favorite[] getItems()
    {
        return favorites.values().toArray(new Favorite[favorites.size()]);
    }
    public final Favorite[] getSortedItems()
    {
        Favorite[] items = getItems();
        Arrays.sort(items);
        return items;
    }
}
