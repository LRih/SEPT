package Model;

import java.util.TreeMap;

/**
 * Data structure for representing a collection of states.
 */
public final class States
{
    private final TreeMap<String, State> states = new TreeMap<>();


    /**
     * Adds a new state. Fails if it already exists.
     *
     * @return the added state. {@code null} if state already exists
     * @param name the name of the state
     * @param abbr abbreviation of name
     */
    public final State add(String name, String abbr)
    {
        // only add state if it does not already exist
        if (!states.containsKey(name))
        {
            State state = new State(name, abbr);
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
}
