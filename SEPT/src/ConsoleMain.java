import Utils.ForecastIOUtils;

import java.io.IOException;

/**
 * This class is for testing purposes only.
 */
public final class ConsoleMain
{
    public static void main(String[] args)
    {
        try
        {
            ForecastIOUtils.printForecast(-37.83, 144.98); // melbourne coords
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
