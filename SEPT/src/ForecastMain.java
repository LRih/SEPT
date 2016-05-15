import Model.Forecast;
import Utils.ForecastFactory;
import View.ForecastChart;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public final class ForecastMain
{
    public static void main(String[] args) throws IOException
    {
        new ForecastMain();
    }

    public ForecastMain() throws IOException
    {
        JFrame form = new JFrame();
        form.setTitle("Chart");
        form.setBounds(100, 100, 500, 400);
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        ForecastChart chart = new ForecastChart();
        List<Forecast> forecasts = ForecastFactory.getNetForecasts(-37.83, 144.98, ForecastFactory.Source.ForecastIO);
//        List<Forecast> forecasts = ForecastFactory.getNetForecasts(-37.83, 144.98, ForecastFactory.Source.OpenWeatherMap);

        chart.setTitle("Forecast");
        chart.setYAxisText("Temperature (°C)");
        chart.setForecasts(forecasts);

        form.add(chart);

        form.setVisible(true);
    }
}
