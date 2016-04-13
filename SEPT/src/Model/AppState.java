package Model;

/**
 * Stores various attributes of app state. Singleton class.
 */
public final class AppState
{
    private static AppState instance;

    public int state;
    public String station;
    public String chart;

    public String v1;
    public String v2;
    public String v3;
    public String v4;
    public String v5;

    private AppState()
    {
    }

    /**
     * Singleton get instance method. Creates the instance for app state if it doesn't exist.
     *
     * @return the single app state instance
     */
    public static AppState getInstance()
    {
        if (instance == null)
        {
            instance = new AppState();
            instance.resetDefault();
        }

        return instance;
    }

    /**
     * Resets the app state to default values.
     */
    public final void resetDefault()
    {
        state = 0;
        station = "station";
        chart = "chart";

        v1 = "v1";
        v2 = "v2";
        v3 = "v3";
        v4 = "v4";
        v5 = "v5";
    }
}
