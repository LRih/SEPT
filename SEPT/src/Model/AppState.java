package Model;

import java.awt.*;

/**
 * Stores various attributes of app state.
 */
public final class AppState
{
    private final Rectangle windowRect;
    private final String state;
    private final String station;
    private final String chart;

    // TODO document
    public AppState(Rectangle windowRect, String state, String station, String chart)
    {
        this.windowRect = windowRect;
        this.state = state;
        this.station = station;
        this.chart = chart;
    }

    public final Rectangle getWindowRect()
    {
        return windowRect;
    }
    public final String getState()
    {
        return state;
    }
    public final String getStation()
    {
        return station;
    }
    public final String getChart()
    {
        return chart;
    }
}
