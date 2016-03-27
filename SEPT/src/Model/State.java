package Model;

import java.util.TreeMap;

public final class State
{
    private final String name;
    private final String abbr;
    private final TreeMap<String, Area> areas = new TreeMap<>();


    public State(String name, String abbr)
    {
        this.name = name;
        this.abbr = abbr;
    }


    public final boolean addArea(String name)
    {
        // only add if area does not already exist
        if (!areas.containsKey(name))
        {
            areas.put(name, new Area(this, name));
            return true;
        }

        return false;
    }


    public final String getName()
    {
        return name;
    }
    public final String getAbbr()
    {
        return abbr;
    }
    public final Area getArea(String name)
    {
        return areas.get(name);
    }
}
