package Model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

public final class Favorites implements Serializable, Iterable<Favorite>
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

    public final int size()
    {
        return favorites.size();
    }

    public final Iterator<Favorite> iterator()
    {
        return new FavouriteIterator();
    }


    /* provide a way to iterate through sorted favorites */
    private class FavouriteIterator implements Iterator<Favorite>
    {
        private Iterator<String> keyIterator = favorites.keySet().iterator();

        public final boolean hasNext()
        {
            return keyIterator.hasNext();
        }

        public final Favorite next()
        {
            return favorites.get(keyIterator.next());
        }

        public final void remove()
        {
            keyIterator.remove();
        }
    }
}
