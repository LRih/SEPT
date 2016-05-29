package Tests;
import Model.*;
import Data.ForecastFactory;
import Utils.Log;
import Utils.SwingUtils;
import Utils.TextUtils;
import View.LineChart;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.*;

public final class Tests
{
    /**
     * Initialize loggers for tests.
     */
    @BeforeClass
    public static void preTest()
    {
        Log.initializeLoggers();
    }

    /**
     * Test added state properly added
     */
    @Test
    public void testAddState()
    {
        States states = new States();
        State state = states.add("Victoria");

        assertEquals(state.getName(), "Victoria");
    }

    /**
     * Test added station properly added
     */
    @Test
    public void testAddStation()
    {
        States states = new States();
        Station station = states.add("Victoria").addStation("Geelong", "http://www.example.com", "");

        assertEquals(station.getName(), "Geelong");
    }

    /**
     * Test add duplicate state
     */
    @Test
    public void testAddDuplicateState()
    {
        States states = new States();
        assertNotNull(states.add("Victoria"));
        assertNull(states.add("Victoria"));
    }

    /**
     * Test add duplicate station
     */
    @Test
    public void testAddDuplicateStation()
    {
        State state = new State("Victoria");
        assertNotNull(state.addStation("Melbourne", "http://www.example.com", ""));
        assertNull(state.addStation("Melbourne", "http://www.example.com", ""));
    }


    /**
     * Test get state
     */
    @Test
    public void testGetState()
    {
        States states = new States();
        states.add("Victoria");

        assertEquals(states.get("Victoria").getName(), "Victoria");
    }

    /**
     * Test get station
     */
    @Test
    public void testGetStation()
    {
        States states = new States();
        states.add("Victoria").addStation("Geelong", "http://www.example.com", "");

        Station station = states.get("Victoria").getStation("Geelong");
        assertEquals(station.getName(), "Geelong");
        assertEquals(station.getUrl(), "http://www.example.com");
    }


    /**
     * Test states sorted in alphabetical order
     */
    @Test
    public void testStatesSorted()
    {
        States states = new States();
        states.add("Victoria");
        states.add("New South Wales");
        states.add("Western Australia");

        String result = "";
        for (State state : states)
        {
            if (!result.isEmpty())
                result += ", ";

            result += state.getName();
        }

        assertEquals(result, "New South Wales, Victoria, Western Australia");
    }

    /**
     * Test stations sorted in alphabetical order
     */
    @Test
    public void testStationsSorted()
    {
        State state = new State("Victoria");
        state.addStation("Olympic Park", "http://www.example.com", "");
        state.addStation("Geelong", "http://www.example.com", "");
        state.addStation("Coburg", "http://www.example.com", "");

        String result = "";
        for (Station station : state)
        {
            if (!result.isEmpty())
                result += ", ";

            result += station.getName();
        }

        assertEquals(result, "Coburg, Geelong, Olympic Park");
    }

    /**
     * Test favorites sorted in alphabetical order
     */
    @Test
    public void testFavoritesSorted()
    {
        State state = new State("Victoria");
        Station station1 = state.addStation("Olympic Park", "http://www.example.com", "");
        Station station2 = state.addStation("Geelong", "http://www.example.com", "");
        Station station3 = state.addStation("Coburg", "http://www.example.com", "");

        Favorites favorites = new Favorites();
        favorites.add(station1);
        favorites.add(station2);
        favorites.add(station3);

        String result = "";
        for (Favorite favorite : favorites)
        {
            if (!result.isEmpty())
                result += ", ";

            result += Station.getKey(favorite.state, favorite.station);
        }

        assertEquals(result, "Victoria-Coburg, Victoria-Geelong, Victoria-Olympic Park");
    }


    /**
     * Test add duplicate favorite
     */
    @Test
    public void testAddDuplicateFavorite()
    {
        State state = new State("Victoria");
        state.addStation("Olympic Park", "http://www.example.com", "");
        state.addStation("Geelong", "http://www.example.com", "");

        Station station1 = state.getStation("Olympic Park");

        Favorites favorites = new Favorites();

        favorites.add(station1);
        favorites.add(station1);

        assertEquals(favorites.size(), 1);
    }

    /**
     * Test delete non-existent favorite
     */
    @Test
    public void testDeleteNonExistentFavorite()
    {
        State state = new State("Victoria");
        state.addStation("Olympic Park", "http://www.example.com", "");
        state.addStation("Geelong", "http://www.example.com", "");

        Station station1 = state.getStation("Olympic Park");
        Station station2 = state.getStation("Geelong");

        Favorites favorites = new Favorites();

        favorites.add(station1);
        assertEquals(favorites.size(), 1);

        favorites.delete(station2);
        assertEquals(favorites.size(), 1);
    }

    /**
     * Test delete favorite
     */
    @Test
    public void testDeleteFavorite()
    {
        State state = new State("Victoria");
        state.addStation("Olympic Park", "http://www.example.com", "");
        state.addStation("Geelong", "http://www.example.com", "");

        Station station1 = state.getStation("Olympic Park");

        Favorites favorites = new Favorites();

        favorites.add(station1);
        assertEquals(favorites.size(), 1);

        favorites.delete(station1);
        assertEquals(favorites.size(), 0);
    }


    /**
     * Getting uncached Forecast IO forecast
     */
    @Test
    public void testGetUncachedForecastIO()
    {
        assertNull(ForecastFactory.getCachedForecasts(-1.337, -1.337, ForecastFactory.Source.ForecastIO));
    }

    /**
     * Getting uncached OpenWeatherMap forecast
     */
    @Test
    public void testGetUncachedOpenWeatherMap()
    {
        assertNull(ForecastFactory.getCachedForecasts(-1.337, -1.337, ForecastFactory.Source.OpenWeatherMap));
    }

    /**
     * Test load weather image
     */
    @Test
    public void testLoadWeatherImage()
    {
        assertNotNull(SwingUtils.createImage("/Images/clear.png"));
        assertNotNull(SwingUtils.createImage("/Images/cloudy.png"));
        assertNotNull(SwingUtils.createImage("/Images/fog.png"));
        assertNotNull(SwingUtils.createImage("/Images/partly_cloudy.png"));
        assertNotNull(SwingUtils.createImage("/Images/rain.png"));
        assertNotNull(SwingUtils.createImage("/Images/sleet.png"));
        assertNotNull(SwingUtils.createImage("/Images/snow.png"));
        assertNotNull(SwingUtils.createImage("/Images/windy.png"));
    }

    /**
     * Test MD5 hashing algorithm
     */
    @Test
    public void testMD5HashAlgorithm()
    {
        try
        {
            assertEquals(TextUtils.md5("ForecastIO,100,100"), "c319189aeeb10062c71f9f248f9d6ea0");
        }
        catch (NoSuchAlgorithmException e)
        {
            fail();
        }
    }

    /**
     * Test correct number of lines in line chart.
     */
    @Test
    public void testCorrectLineCount()
    {
        LineChart chart = new LineChart();

        chart.setXValues(new String[] { "1", "2" });
        chart.addDataset("D1", Color.BLUE, new double[] { 1, 2 });
        chart.addDataset("D2", Color.GREEN, new double[] { 1, 2 });

        assertEquals(chart.getLineCount(), 2);
    }

    /**
     * Test line chart correctly handles duplicates.
     */
    @Test
    public void testCorrectLineCountAfterDuplicate()
    {
        LineChart chart = new LineChart();

        chart.setXValues(new String[] { "1", "2" });
        chart.addDataset("D1", Color.BLUE, new double[] { 1, 2 });
        chart.addDataset("D1", Color.GREEN, new double[] { 1, 2 });

        assertEquals(chart.getLineCount(), 1);
    }

    /**
     * Test clearing line chart.
     */
    @Test
    public void testClearLineChart()
    {
        LineChart chart = new LineChart();

        chart.setXValues(new String[] { "1", "2" });
        chart.addDataset("D1", Color.BLUE, new double[] { 1, 2 });
        chart.addDataset("D2", Color.GREEN, new double[] { 1, 2 });

        chart.clearDatasets();

        assertEquals(chart.getLineCount(), 0);
    }
}
