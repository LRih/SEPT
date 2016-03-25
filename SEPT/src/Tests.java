import Model.Favorites;
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
        Favorites favorites = new Favorites();

        assertEquals(favorites.getItems().length, 0);

        favorites.add("abc", "Victoria");
        assertEquals(favorites.getItems().length, 1);

        favorites.add("abc", "Victoria");
        assertEquals(favorites.getItems().length, 1);

        favorites.delete("xyz");
        assertEquals(favorites.getItems().length, 1);

        favorites.delete("abc");
        assertEquals(favorites.getItems().length, 0);
    }
}
