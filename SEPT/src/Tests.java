import Model.Area;
import Model.Favorites;
import Model.State;
import Model.Station;
import org.junit.Test;

import static org.junit.Assert.*;

public final class Tests
{
    // tests that one equals one
    @Test
    public void testSample()
    {
        assertEquals(1, 1);
    }

    // test favorites operations
    @Test
    public void testAddFavorite()
    {
        State state = new State("Victoria", "VIC");
        state.addArea("Melbourne");

        Area area = state.getArea("Melbourne");
        area.addStation("Olympic Park", "http://www.example.com");
        area.addStation("Geelong", "http://www.example.com");

        Station station1 = area.getStation("Olympic Park");
        Station station2 = area.getStation("Geelong");

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
