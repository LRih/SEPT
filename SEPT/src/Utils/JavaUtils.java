package Utils;

import javax.swing.ImageIcon;

import View.FirstScreen;

public class JavaUtils {

	// Returns an ImageIcon, or null if the path was invalid.
	public static ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = FirstScreen.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

}
