package Utils;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Operations related to Swing.
 */
public final class SwingUtils
{
    /**
     * Get jar embedded image.
     *
     * @return an ImageIcon, or null if the path was invalid.
     */
    public static ImageIcon createImageIcon(String path, String description)
    {
        URL imgURL = SwingUtils.class.getResource(path);

        if (imgURL != null)
            return new ImageIcon(imgURL, description);

        Log.warn(SwingUtils.class, "Couldn't find file: " + path);
        return null;
    }

    /**
     * Get jar embedded image.
     *
     * @return an Image, or null if the path was invalid.
     */
    public static Image createImage(String path)
    {
        URL imgURL = SwingUtils.class.getResource(path);

        try
        {
            if (imgURL != null)
                return ImageIO.read(imgURL);
        }
        catch (IOException e)
        {
            Log.warn(SwingUtils.class, "Couldn't load file: " + path);
            return null;
        }

        Log.warn(SwingUtils.class, "Couldn't find file: " + path);
        return null;
    }

    /**
     * Take dimensions values and scale it to the required values maintaining aspect ratio.
     */
    public static Size scale(float width, float height, float reqWidth, float reqHeight)
    {
        float newWidth = reqWidth;
        float newHeight = newWidth / width * height;

        if (newHeight > reqHeight)
        {
            newWidth *= reqHeight / newHeight;
            newHeight *= reqHeight / newHeight;
        }

        return new Size(newWidth, newHeight);
    }

    public static class Size
    {
        public final float width;
        public final float height;

        public Size(float width, float height)
        {
            this.width = width;
            this.height = height;
        }
    }
}
