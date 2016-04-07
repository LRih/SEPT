package Model;

import java.util.Iterator;
import java.util.TreeMap;

/**
 * Data structure for representing a state.
 */
public final class State implements Iterable<Station>
{
    private final String name;
    private final TreeMap<String, Station> stations = new TreeMap<>();


    /**
     * Creates a new state instance.
     *
     * @param name name of state
     */
    public State(String name)
    {
        this.name = name;
    }


    /**
     * Adds a new station. Fails if it already exists.
     *
     * @return the added station. {@code null} if station already exists
     * @param name the name of the station
     * @param url URL of the JSON data from BOM
     */
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


    public final String getName()
    {
        return name;
    }

    /**
     * Gets a station contained in this state
     *
     * @return the station with name {@code name}
     * @param name the name of the station
     */
    public final Station getStation(String name)
    {
        return stations.get(name);
    }

    public final Iterator<Station> iterator()
    {
        return new KeySetIterator<>(stations);
    }
}
