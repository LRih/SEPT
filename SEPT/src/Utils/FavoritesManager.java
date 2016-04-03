package Utils;

import Model.Favorites;

import java.io.*;

/**
 * Handles the storage of favorites.
 */
public final class FavoritesManager
{
    private static String FILE_PATH = "favourites";


    private FavoritesManager()
    {
        // disallow instantiating
        throw new AssertionError();
    }

    /**
     * Loads serialized favorites class from the filesystem.
     *
     * @return the deserialized favorites file
     * @throws IOException if there is an IO error of any sort
     * @throws ClassNotFoundException if the favorites class is not defined
     */
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

    /**
     * Serializes and saves favorites class to the filesystem.
     *
     * @param favorites the favorites instance to save
     * @throws IOException if there is an IO error of any sort
     */
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
