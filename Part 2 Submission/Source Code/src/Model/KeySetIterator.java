package Model;

import java.util.Iterator;
import java.util.Map;

/**
 * Provide a way to iterate through sorted key sets. Returns values of key.
 */
public final class KeySetIterator<T> implements Iterator<T>
{
    private final Map<String, T> map;
    private final Iterator<String> keyIterator;

    /**
     * Creates a new KeySetIterator.
     *
     * @param map the map to iterate
     */
    public KeySetIterator(Map<String, T> map)
    {
        this.map = map;
        keyIterator = map.keySet().iterator();
    }

    public final boolean hasNext()
    {
        return keyIterator.hasNext();
    }

    public final T next()
    {
        return map.get(keyIterator.next());
    }

    public final void remove()
    {
        keyIterator.remove();
    }
}
