package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class NetUtils
{
    private static final String CHARSET = "UTF-8";
    private static final int TIMEOUT = 10000;


    private NetUtils()
    {
        // disallow instantiating
        throw new AssertionError();
    }


    /* gets the source of a URL by GET request */
    public static String get(String url) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
        connection.setConnectTimeout(TIMEOUT);

        String source = getSource(connection);

        connection.disconnect();

        return source;
    }

    /* get source from connection object */
    private static String getSource(HttpURLConnection connection) throws IOException
    {
        String source = "";

        // return empty string when no content
        if (connection.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));

            String line;
            while ((line = reader.readLine()) != null)
                source += line;

            reader.close();
        }

        return source;
    }
}
