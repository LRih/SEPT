package Utils;

import Model.Favorites;

import java.io.*;

/*
    For handling the storage of favorites.
 */
public final class FavoritesManager
{
    private static String FILE_PATH = "favourites";


    private FavoritesManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /* loads serialized favorites class */
    public static Favorites load() throws IOException, ClassNotFoundException
    {
        Favorites favorites = new Favorites();

        // return empty list if file doesn't exist
        if (!new File(FILE_PATH).exists())
            return favorites;

        ObjectInputStream in = null;

        try
        {
            in = new ObjectInputStream(new FileInputStream(FILE_PATH));
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

    /* serializes and saves favorites class */
    public static void save(Favorites favorites) throws IOException
    {
        ObjectOutputStream out = null;

        try
        {
            out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
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
