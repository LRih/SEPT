package Model;

import java.util.ArrayList;
import java.util.List;

public final class Area
{
    private final String name;
    private final List<Area> stations = new ArrayList<>();


    public Area(String name)
    {
        this.name = name;
    }


    public final String getName()
    {
        return name;
    }
    public final List<Area> getStations()
    {
        return stations;
    }
}
