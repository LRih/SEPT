package Model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeMap;

// TODO document
public final class Favorites implements Serializable, Iterable<Favorite>
{
    private final TreeMap<String, Favorite> favorites;


    // TODO document
    public Favorites()
    {
        this.favorites = new TreeMap<>();
    }


    // TODO document
    public final boolean add(Station station)
    {
        return add(station.getKey(), station.getState().getName(), station.getName());
    }
    // TODO document
    public final boolean add(String key, String state, String station)
    {
        // only add if favorite does not already exist
        if (!favorites.containsKey(key))
        {
            favorites.put(key, new Favorite(key, state, station));
            return true;
        }

        return false;
    }
    // TODO document
    public final boolean delete(Station station)
    {
        if (favorites.containsKey(station.getKey()))
        {
            favorites.remove(station.getKey());
            return true;
        }

        return false;
    }

    // TODO document
    public final int size()
    {
        return favorites.size();
    }

    // TODO document
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
