package Model;

public final class State
{
    private final String name;
    private final String abbr;


    public State(String name, String abbr)
    {
        this.name = name;
        this.abbr = abbr;
    }


    public String getName()
    {
        return name;
    }
    public String getAbbr()
    {
        return abbr;
    }
}
