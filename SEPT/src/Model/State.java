package Model;

import java.util.ArrayList;
import java.util.List;

public final class State
{
    private final String name;
    private final String abbr;
    private final List<Area> areas = new ArrayList<>();


    public State(String name, String abbr)
    {
        this.name = name;
        this.abbr = abbr;
    }


    public final String getName()
    {
        return name;
    }
    public final String getAbbr()
    {
        return abbr;
    }
    public final List<Area> getAreas()
    {
        return areas;
    }
}
