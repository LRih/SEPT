package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Favorites implements Serializable
{
    private final List<Favorite> favorites;


    public Favorites()
    {
        this.favorites = new ArrayList<>();
    }


    public final void add(String id, String name)
    {
        // only add if id does not already exist
        if (!isExist(id))
            favorites.add(new Favorite(id, name));
    }
    public final void delete(String id)
    {
        for (int i = 0; i < favorites.size(); i++)
        {
            if (id.equals(favorites.get(i).id))
            {
                favorites.remove(i);
                return;
            }
        }
    }
    public final void sort()
    {
        Collections.sort(favorites);
    }

    public final Favorite[] getItems()
    {
        return favorites.toArray(new Favorite[favorites.size()]);
    }

    private boolean isExist(String id)
    {
        // check if id already exists
        for (Favorite favorite : favorites)
            if (id.equals(favorite.id))
                return true;

        return false;
    }
}
