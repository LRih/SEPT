package Utils;

import javax.swing.ImageIcon;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

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
	
	public static String timeAgo(DateTime time) {
		DateTime now = new DateTime();
		Period period = new Period(time, now);

		PeriodFormatter formatter = new PeriodFormatterBuilder()
		    .appendSeconds().appendSuffix(" seconds")
		    .appendMinutes().appendSuffix(" minutes")
		    .appendHours().appendSuffix(" hours")
		    .appendDays().appendSuffix(" days")
		    .appendWeeks().appendSuffix(" weeks")
		    .appendMonths().appendSuffix(" months")
		    .appendYears().appendSuffix(" years")
		    .printZeroNever()
		    .toFormatter();

		return formatter.print(period)+" ago";
	}

}
