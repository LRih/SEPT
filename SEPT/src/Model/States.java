package Model;

import java.util.TreeMap;

public final class States
{
    private final TreeMap<String, State> states = new TreeMap<>();


    public final boolean add(String name, String abbr)
    {
        // only add state if it does not already exist
        if (!states.containsKey(name))
        {
            states.put(name, new State(name, abbr));
            return true;
        }

        return false;
    }


    public final State get(String name)
    {
        return states.get(name);
    }
}
