package Model;

import java.util.TreeMap;

// TODO document
public final class State
{
    private final String name;
    private final String abbr;
    private final TreeMap<String, Station> stations = new TreeMap<>();


    // TODO document
    public State(String name, String abbr)
    {
        this.name = name;
        this.abbr = abbr;
    }


    // TODO document
    public final Station addStation(String name, String url)
    {
        // only add station if it does not already exist
        if (!stations.containsKey(name))
        {
            Station station = new Station(this, name, url);
            stations.put(name, station);
            return station;
        }

        return null;
    }


    // TODO document
    public final String getName()
    {
        return name;
    }
    // TODO document
    public final String getAbbr()
    {
        return abbr;
    }
    // TODO document
    public final Station getStation(String name)
    {
        return stations.get(name);
    }
}
