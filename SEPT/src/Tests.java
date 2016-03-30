import Model.Favorites;
import Model.State;
import Model.States;
import Model.Station;
import org.junit.Test;

import static org.junit.Assert.*;

public final class Tests
{
    // test add duplicate state
    @Test
    public void testAddDuplicateState()
    {
        States states = new States();
        assertEquals(states.add("Victoria", "VIC"), true);
        assertEquals(states.add("Victoria", "VIC"), false);
    }

    // test add duplicate station
    @Test
    public void testAddDuplicateStation()
    {
        State state = new State("Victoria", "VIC");
        assertEquals(state.addStation("Melbourne", "http://www.example.com"), true);
        assertEquals(state.addStation("Melbourne", "http://www.example.com"), false);
    }

    // test favorites operations
    @Test
    public void testFavorites()
    {
        State state = new State("Victoria", "VIC");
        state.addStation("Olympic Park", "http://www.example.com");
        state.addStation("Geelong", "http://www.example.com");

        Station station1 = state.getStation("Olympic Park");
        Station station2 = state.getStation("Geelong");

        Favorites favorites = new Favorites();

        assertEquals(favorites.getItems().length, 0);

        favorites.add(station1);
        assertEquals(favorites.getItems().length, 1);

        favorites.add(station1);
        assertEquals(favorites.getItems().length, 1);

        favorites.delete(station2);
        assertEquals(favorites.getItems().length, 1);

        favorites.delete(station1);
        assertEquals(favorites.getItems().length, 0);
    }
}
