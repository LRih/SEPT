package Model;

import java.util.TreeMap;

public final class Area
{
    private final State state;
    private final String name;
    private final TreeMap<String, Station> stations = new TreeMap<>();


    public Area(State state, String name)
    {
        this.state = state;
        this.name = name;
    }


    public final boolean addStation(String name, String url)
    {
        // only add if station does not already exist
        if (!stations.containsKey(name))
        {
            stations.put(name, new Station(this, name, url));
            return true;
        }

        return false;
    }


    public final State getState()
    {
        return state;
    }
    public final String getName()
    {
        return name;
    }
    public final Station getStation(String name)
    {
        return stations.get(name);
    }
}
