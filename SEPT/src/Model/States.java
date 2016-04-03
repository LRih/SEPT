package Model;

import java.util.TreeMap;

// TODO document
public final class States
{
    private final TreeMap<String, State> states = new TreeMap<>();


    // TODO document
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


    // TODO document
    public final State get(String name)
    {
        return states.get(name);
    }
}
