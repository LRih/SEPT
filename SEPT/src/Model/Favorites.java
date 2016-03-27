package Model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public final class Favorites implements Serializable
{
    // TODO cannot hash by id since ID and WMO can be duplicate
    private final HashMap<String, Favorite> favorites;


    public Favorites()
    {
        this.favorites = new HashMap<>();
    }


    public final boolean add(String id, String name)
    {
        // only add if id does not already exist
        if (!favorites.containsKey(id))
        {
            favorites.put(id, new Favorite(id, name));
            return true;
        }

        return false;
    }
    public final boolean delete(String id)
    {
        if (favorites.containsKey(id))
        {
            favorites.remove(id);
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
