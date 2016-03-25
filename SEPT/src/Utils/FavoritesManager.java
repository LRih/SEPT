package Utils;

import Model.Favorites;

import java.io.*;

/*
    For handling the storage of favorites
 */
public final class FavoritesManager
{
    private static String FILENAME = "favourites";


    private FavoritesManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    public static Favorites load() throws IOException, ClassNotFoundException
    {
        Favorites favorites = new Favorites();

        // return empty list if file doesn't exist
        if (!new File(FILENAME).exists())
            return favorites;

        ObjectInputStream in = null;

        try
        {
            in = new ObjectInputStream(new FileInputStream(FILENAME));
            favorites = (Favorites)in.readObject();
        }
        finally
        {
            // ensure ObjectInputStream is closed under any circumstances
            try
            {
                if (in != null)
                    in.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }

        return favorites;
    }

    public static void save(Favorites favorites) throws IOException
    {
        ObjectOutputStream out = null;

        try
        {
            out = new ObjectOutputStream(new FileOutputStream(FILENAME));
            out.writeObject(favorites);
        }
        finally
        {
            // ensure ObjectOutputStream is closed under any circumstances
            try
            {
                if (out != null)
                    out.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
