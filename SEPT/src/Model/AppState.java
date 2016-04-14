package Model;

/**
 * Stores various attributes of app state. Singleton class.
 */
public final class AppState
{
    private static AppState instance;

    public int stateIndex;
    public String state;
    public String station;
    public int v1;
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
        stateIndex = -1;
        state = "";
        station = "";
        v1 = 0;
        v4 = "";
        v5 = "";
    }
}
