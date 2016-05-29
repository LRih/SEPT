package Model;

import View.MainPanel;
import View.StationChart;

import java.awt.*;

import Data.ForecastFactory.Source;

/**
 * Stores various attributes of app state. Singleton class.
 */
public final class AppState
{
    private static AppState instance;

    public String windowRect;
    public int shownWindow;
    public int shownDetail;
    public int chartIndex;
    public String state;
    public String station;
    public Source forecastSource;

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
        windowRect = "100,100,1300,600";
        shownWindow = -1;
        shownDetail = -1;
        state = "";
        station = "";
        chartIndex = 0;
        forecastSource = Source.ForecastIO;
    }


    /**
     * Converts int encoded shown detail to enum.
     *
     * @return the enum object
     */
    public final MainPanel.PanelType getShownDetail()
    {
        if (shownDetail >= 0 && shownDetail < MainPanel.PanelType.values().length)
            return MainPanel.PanelType.values()[shownDetail];
        return MainPanel.PanelType.Detail;
    }

    /**
     * Converts int encoded chart type to enum.
     *
     * @return the enum object
     */
    public final StationChart.ChartType getChartType()
    {
        if (chartIndex >= 0 && chartIndex < StationChart.ChartType.values().length)
            return StationChart.ChartType.values()[chartIndex];
        return StationChart.ChartType.MaxTemp;
    }


    /**
     * Converts string encoded rectangle to object.
     *
     * @return the rectangle object
     */
    public final Rectangle getWindowRect()
    {
        try
        {
            String[] arr = windowRect.split(",");

            if (arr.length != 4)
                throw new Exception("Invalid AppState Format.");

            int x = (int)Math.max(Double.parseDouble(arr[0]), 0);
            int y = (int)Math.max(Double.parseDouble(arr[1]), 0);
            int width = (int)Double.parseDouble(arr[2]);
            int height = (int)Double.parseDouble(arr[3]);

            return new Rectangle(x, y, width, height);
        }
        catch (Exception e)
        {
            return new Rectangle(100, 100, 1300, 600);
        }
    }
    public final void setWindowRect(Point pos, Dimension size)
    {
        windowRect = pos.getX() + "," + pos.getY() + "," + size.getWidth() + "," + size.getHeight();
    }
}
