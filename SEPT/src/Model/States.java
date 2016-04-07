package Model;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Data structure for representing a collection of states.
 */
public final class States implements Iterable<State>
{
    private final TreeMap<String, State> states = new TreeMap<>();


    /**
     * Adds a new state. Fails if it already exists.
     *
     * @return the added state. {@code null} if state already exists
     * @param name the name of the state
     */
    public final State add(String name)
    {
        // only add state if it does not already exist
        if (!states.containsKey(name))
        {
            State state = new State(name);
            states.put(name, state);
            return state;
        }

        return null;
    }


    /**
     * Gets a state contained in states
     *
     * @return the state with name {@code name}
     * @param name the name of the state
     */
    public final State get(String name)
    {
        return states.get(name);
    }

    public final Iterator<State> iterator()
    {
        return new KeySetIterator<>(states);
    }
}
