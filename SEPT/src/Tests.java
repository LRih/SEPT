import Model.Favorites;
import Model.State;
import Model.States;
import Model.Station;
import org.junit.Test;

import static org.junit.Assert.*;

public final class Tests
{
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
        assertNotNull(state.addStation("Melbourne", "http://www.example.com"));
        assertNull(state.addStation("Melbourne", "http://www.example.com"));
    }

    /**
     * Test add duplicate favorite
     */
    @Test
    public void testAddDuplicateFavorite()
    {
        State state = new State("Victoria");
        state.addStation("Olympic Park", "http://www.example.com");
        state.addStation("Geelong", "http://www.example.com");

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
        state.addStation("Olympic Park", "http://www.example.com");
        state.addStation("Geelong", "http://www.example.com");

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
        state.addStation("Olympic Park", "http://www.example.com");
        state.addStation("Geelong", "http://www.example.com");

        Station station1 = state.getStation("Olympic Park");
        Station station2 = state.getStation("Geelong");

        Favorites favorites = new Favorites();

        favorites.add(station1);
        assertEquals(favorites.size(), 1);

        favorites.delete(station1);
        assertEquals(favorites.size(), 0);
    }
}
