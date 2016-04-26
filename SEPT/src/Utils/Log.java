package Utils;

import java.io.IOException;
import java.util.logging.*;

/**
 * Manages logging.
 */
public final class Log
{
    private static Logger _logger;


    private Log()
    {
        // prevent instantiating
        throw new AssertionError();
    }

    /**
     * Must be called before logging functions called.
     */
    public static boolean initializeLoggers()
    {
        // ensure multiple calls have no effect
        if (_logger != null)
            return false;

        try
        {
            _logger = initializeLogger("Main log", "main.log");
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private static Logger initializeLogger(String loggerName, String filename) throws IOException
    {
        FileHandler handler = new FileHandler(filename, true);
        handler.setFormatter(new SimpleFormatter());

        Logger logger = Logger.getLogger(loggerName);
        logger.addHandler(handler);

        return logger;
    }


    /**
     * Convenience function for logging info.
     */
    public static void info(Class cls, String msg)
    {
        log(Level.INFO, cls, msg);
    }

    /**
     * Convenience function for logging warn.
     */
    public static void warn(Class cls, String msg)
    {
        log(Level.WARNING, cls, msg);
    }

    private static void log(Level level, Class cls, String msg)
    {
        String text = String.format("[%s] %s", cls, msg);
        _logger.log(level, text);
    }
}
