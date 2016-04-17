package Utils;

import javax.swing.ImageIcon;

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

        System.err.println("Couldn't find file: " + path);
        return null;
    }
}
