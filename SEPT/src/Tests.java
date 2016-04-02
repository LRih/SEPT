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
        assertNotNull(states.add("Victoria", "VIC"));
        assertNull(states.add("Victoria", "VIC"));
    }

    // test add duplicate station
    @Test
    public void testAddDuplicateStation()
    {
        State state = new State("Victoria", "VIC");
        assertNotNull(state.addStation("Melbourne", "http://www.example.com"));
        assertNull(state.addStation("Melbourne", "http://www.example.com"));
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

        assertEquals(favorites.size(), 0);

        favorites.add(station1);
        assertEquals(favorites.size(), 1);

        favorites.add(station1);
        assertEquals(favorites.size(), 1);

        favorites.delete(station2);
        assertEquals(favorites.size(), 1);

        favorites.delete(station1);
        assertEquals(favorites.size(), 0);
    }
}
