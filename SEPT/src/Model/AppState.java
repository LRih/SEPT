package Model;

import java.awt.*;

/**
 * Stores various attributes of app state. Singleton class.
 */
public final class AppState
{
    private static AppState instance;

    public String windowRect;
    public int shownWindow;
    public int shownDetail;
    public String state;
    public String station;
    public int chartIndex;

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

            int x = (int)Double.parseDouble(arr[0]);
            int y = (int)Double.parseDouble(arr[1]);
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
