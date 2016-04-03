package Utils;

import java.io.*;

/**
 * Handles file system based operations.
 */
public final class FileUtils
{
    private static final String CHARSET = "UTF-8";


    private FileUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /**
     * Load a text based file.
     *
     * @param path the path of the file to load
     * @return the data contained in file at path {@code path}
     * @throws IOException if there is an IO error of any sort
     */
    public static String loadText(String path) throws IOException
    {
        StringBuilder data = new StringBuilder();
        InputStreamReader in = null;

        try
        {
            in = new InputStreamReader(new FileInputStream(path), CHARSET);

            int c;
            while ((c = in.read()) != -1)
                data.append((char)c);
        }
        catch (IOException ex)
        {
            throw new IOException("Unable to load " + path);
        }
        finally
        {
            // ensure InputStreamReader is closed under any circumstances
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

        return data.toString();
    }

    /**
     * Save a text based file.
     *
     * @param text the text data to save to file
     * @param path the path of the save location
     * @throws IOException if there is an IO error of any sort
     */
    public static void saveText(String text, String path) throws IOException
    {
        OutputStreamWriter out = null;

        // make the directories if non-existent
        String directory = new File(path).getParent();
        new File(directory).mkdirs();

        try
        {
            out = new OutputStreamWriter(new FileOutputStream(path), CHARSET);
            out.write(text);
        }
        catch (IOException ex)
        {
            throw new IOException("Unable to save " + path);
        }
        finally
        {
            // ensure OutputStreamWriter is closed under any circumstances
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
