import Model.Forecast;
import Data.ForecastFactory;

import java.io.IOException;

/**
 * This class is for testing purposes only.
 */
public final class ConsoleMain
{
    public static void main(String[] args)
    {
        System.out.println("Forecast IO");
        try
        {
            for (Forecast f : ForecastFactory.getNetForecasts(-37.83, 144.98, ForecastFactory.Source.ForecastIO))
            {
                System.out.println("[" + f.date + "] Min: " + f.min + ", Max: " + f.max);
                System.out.println("Summary: " + f.summary + ", Desc: " + f.description);
            }
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }

        System.out.println();
        System.out.println("OpenWeatherMap");
        try
        {
            for (Forecast f : ForecastFactory.getNetForecasts(-37.83, 144.98, ForecastFactory.Source.OpenWeatherMap))
            {
                System.out.println("[" + f.date + "] Min: " + f.min + ", Max: " + f.max);
                System.out.println("Summary: " + f.summary + ", Desc: " + f.description);
            }
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
