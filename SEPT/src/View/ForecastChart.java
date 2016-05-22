package View;

import Model.Forecast;
import Utils.Log;
import Utils.SwingUtils;
import org.joda.time.LocalTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A component for drawing bar charts for forecasts.
 */
public final class ForecastChart extends JPanel implements ActionListener {
	// number of ticks to complete animation
	private static final int ANIMATION_TICKS = 30;
	private static final int ANIMATION_FPS = 60;

	private static final int PADDING = 25;
	private static final int PADDING_BOTTOM = 90;
	private static final int DATE_PADDING_TOP = 15;
	private static final int IMG_PADDING = 4;

	private static final float AXIS_WIDTH = 1.5f;

	private static final Font FONT_TITLE = new Font("Century Gothic", Font.BOLD, 20);
	private static final Font FONT_AXIS = new Font("Century Gothic", Font.BOLD, 14);
	private static final Font FONT_FORECAST = new Font("Century Gothic", Font.PLAIN, 14);

	private static final Color COL_TEXT = new Color(70, 70, 70);
	private static final Color COL_AXIS = new Color(160, 160, 160);

	private static final Color COL_MIN = new Color(23, 118, 182);
	private static final Color COL_MAX = new Color(244, 81, 30);

	public static final int NO_DATA = 1;
	public static final int LOADING_DATA = 2;
	public static final int NO_INTERNET = 3;

	// 0 no data 1 loading 2 no internet
	private int status = NO_DATA;

	private String title = "";
	private String xAxisText = "";
	private String yAxisText = "";

	private List<Forecast> oldForecasts = null; // for animation purposes
	private List<Forecast> forecasts = new ArrayList<>();

	private double oldMin, oldMax;
	private double min = Float.MAX_VALUE;
	private double max = Float.MIN_VALUE;

	// format for displaying temperature
	private DecimalFormat df = new DecimalFormat("#0.00");

	private Image[] images;

	// used for animations
	private int aniProgress;
	private final Timer timer;

	public ForecastChart() {
		setBackground(Color.WHITE);
		initializeImages();

		timer = new Timer(1000 / ANIMATION_FPS, this);
	}

	private void initializeImages() {

		LocalTime now = LocalTime.now();
		String night = "";
		if (now.getHourOfDay() >= 18 || now.getHourOfDay() <= 6)
			night = "_night";

		images = new Image[] { SwingUtils.createImage("/Images/clear" + night + ".png"),
				SwingUtils.createImage("/Images/partly_cloudy" + night + ".png"),
				SwingUtils.createImage("/Images/cloudy" + night + ".png"),
				SwingUtils.createImage("/Images/rain" + night + ".png"),
				SwingUtils.createImage("/Images/hail" + night + ".png"),
				SwingUtils.createImage("/Images/storm" + night + ".png"),
				SwingUtils.createImage("/Images/windy" + night + ".png"),
				SwingUtils.createImage("/Images/fog" + night + ".png"),
				SwingUtils.createImage("/Images/sleet" + night + ".png"),
				SwingUtils.createImage("/Images/snow" + night + ".png"),
				SwingUtils.createImage("/Images/dunno" + night + ".png"), };
	}

	public final void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		Graphics2D g = (Graphics2D) graphics;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		drawText(g);
		drawAxis(g);
		drawBars(g);
	}

	private void drawText(Graphics2D g) {
		g.setColor(COL_TEXT);

		drawTitle(g);
		drawAxisText(g);
		drawEmptyText(g);
	}

	private void drawTitle(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(FONT_TITLE);
		Rectangle2D rect = metrics.getStringBounds(title, g);

		g.setFont(FONT_TITLE);

		float x = getWidth() / 2f - (float) rect.getCenterX();
		float y = PADDING / 2f - (float) rect.getCenterY();

		g.drawString(title, x, y);
	}

	private void drawAxisText(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(FONT_AXIS);
		g.setFont(FONT_AXIS);

		// x-axis
		Rectangle2D rect = metrics.getStringBounds(xAxisText, g);

		float x = getWidth() / 2f - (float) rect.getCenterX();
		float y = getHeight() - PADDING / 3f - (float) rect.getCenterY();

		g.drawString(xAxisText, x, y);

		// y-axis
		rect = metrics.getStringBounds(yAxisText, g);

		float rad = 90 * (float) Math.PI / 180;

		x = PADDING / 3f;
		y = getHeight() / 2f;

		g.rotate(-rad, x, y);
		g.drawString(yAxisText, PADDING / 3f - (float) rect.getCenterX(), getHeight() / 2f - (float) rect.getCenterY());
		g.rotate(rad, x, y);
	}

	public void setStatus(int status) {
		this.status = status;
	}

	private void drawEmptyText(Graphics2D g) {
		if (!forecasts.isEmpty())
			return;

		String text = "no data";
		if (status == LOADING_DATA)
			text = "loading data..";
		else if (status == NO_INTERNET)
			text = "no internet connection";

		FontMetrics metrics = g.getFontMetrics(FONT_FORECAST);
		Rectangle2D rect = metrics.getStringBounds(text, g);

		// draw text in middle of chart when no data loaded
		g.setFont(FONT_FORECAST);
		g.setColor(COL_AXIS);
		g.drawString(text, getWidth() / 2f - (float) rect.getCenterX(), getHeight() / 2f - (float) rect.getCenterY());
	}

	private void drawAxis(Graphics2D g) {
		g.setColor(COL_AXIS);
		g.setStroke(new BasicStroke(AXIS_WIDTH));

		// constrain axis to chart bounds
		int y = (int) getY(0);
		y = Math.min(Math.max(y, PADDING), getHeight() - PADDING_BOTTOM);

		// set axis to bottom when no forecasts
		if (forecasts.isEmpty())
			y = getHeight() - PADDING_BOTTOM;

		g.drawLine(PADDING, y, getWidth() - PADDING, y); // horizontal axis
	}

	private void drawBars(Graphics2D g) {
		FontMetrics metrics = g.getFontMetrics(FONT_AXIS);
		g.setFont(FONT_AXIS);

		// calculate bar width
		float width = getWidth() - PADDING * 2;

		float barSep = 0;
		float forecastSep = width / 30;
		float bar = (width - forecastSep * (forecasts.size() + 1) - barSep * forecasts.size()) / (forecasts.size() * 2);

		for (int i = 0; i < forecasts.size(); i++) {
			// min value
			String temp = df.format(forecasts.get(i).min);
			Rectangle2D rect = metrics.getStringBounds(temp, g);

			g.setColor(
					new Color(COL_MIN.getRed(), COL_MIN.getGreen(), COL_MIN.getBlue(), getAlpha(forecasts.get(i).min)));

			int x1 = (int) (PADDING + forecastSep + i * (barSep + bar * 2 + forecastSep));

			int y1;
            if (oldForecasts == null)
                y1 = (int) getY(forecasts.get(i).min * aniProgress / (float) ANIMATION_TICKS);
            else
                y1 = (int) getY((forecasts.get(i).min - oldForecasts.get(i).min) * aniProgress / (float) ANIMATION_TICKS + oldForecasts.get(i).min);

			int y2 = (int) getY(0);

			g.fillRect(x1, y1 < y2 ? y1 : y2, (int) bar, Math.abs(y2 - y1));

			// don't draw text if width too small
			if (getWidth() >= PADDING * 6) {
				float yText = y1 + (float) (y1 < y2 ? rect.getCenterY() : rect.getHeight());
				g.drawString(temp, x1 + bar / 2 - (float) rect.getCenterX(), yText);
			}

			// max value
			temp = df.format(forecasts.get(i).max);
			rect = metrics.getStringBounds(temp, g);

			g.setColor(
					new Color(COL_MAX.getRed(), COL_MAX.getGreen(), COL_MAX.getBlue(), getAlpha(forecasts.get(i).max)));

			x1 = (int) (PADDING + forecastSep + bar + barSep + i * (barSep + bar * 2 + forecastSep));

            if (oldForecasts == null)
                y1 = (int) getY(forecasts.get(i).max * aniProgress / (float) ANIMATION_TICKS);
            else
                y1 = (int) getY((forecasts.get(i).max - oldForecasts.get(i).max) * aniProgress / (float) ANIMATION_TICKS + oldForecasts.get(i).max);

			y2 = (int) getY(0);

			g.fillRect(x1, y1 < y2 ? y1 : y2, (int) bar, Math.abs(y2 - y1));

			// don't draw text if width too small
			if (getWidth() >= PADDING * 6) {
				float yText = y1 + (float) (y1 < y2 ? rect.getCenterY() : rect.getHeight());
				g.drawString(temp, x1 + bar / 2 - (float) rect.getCenterX(), yText);
			}
		}

		// draw the text and image below forecast
		metrics = g.getFontMetrics(FONT_FORECAST);
		g.setFont(FONT_FORECAST);

		for (int i = 0; i < forecasts.size(); i++) {
			Forecast forecast = forecasts.get(i);

			float x = PADDING + forecastSep + i * (barSep + bar * 2 + forecastSep) + bar + barSep / 2;
			float y = getHeight() - PADDING_BOTTOM;

			String date = forecast.date.getDayOfMonth() + "/" + forecast.date.getMonthOfYear();
			Rectangle2D rect = metrics.getStringBounds(date, g);

			// draw summary image
			Image img = getImage(forecast.summary);
			if (img != null) {
				float totalImgHeight = PADDING_BOTTOM - (float) rect.getHeight() - IMG_PADDING * 2 - DATE_PADDING_TOP;
				int imgY = (int) (getHeight() - totalImgHeight - IMG_PADDING);

				// scale image to fit
				int imgWidth = (int) bar;
				int imgHeight = (int) (imgWidth / (float) img.getWidth(null) * img.getHeight(null)); // maintain
																										// aspect
																										// ratio

				if (imgHeight > totalImgHeight) {
					imgWidth *= totalImgHeight / (float) imgHeight;
					imgHeight *= totalImgHeight / (float) imgHeight;
				}

				g.drawImage(img, (int) (x - imgWidth / 2), (int) (imgY + (totalImgHeight - imgHeight) / 2), imgWidth,
						imgHeight, null);
			}

			// don't draw text if width too small
			if (getWidth() >= PADDING * 6) {
				g.setColor(COL_TEXT);
				g.drawString(date, x - (float) rect.getCenterX(), y + (float) rect.getHeight() + DATE_PADDING_TOP);
			}
		}
	}

	/**
	 * Get y-coord of value based on min/max values.
	 */
	private double getY(double value) {
		int height = getHeight() - PADDING - PADDING_BOTTOM;
        double aniMin = (min - oldMin) * aniProgress / (float) ANIMATION_TICKS + oldMin;
        double aniMax = (max - oldMax) * aniProgress / (float) ANIMATION_TICKS + oldMax;

        double top = PADDING;
        double bottom = getHeight() - PADDING_BOTTOM;

		if (aniMin < 0 && aniMax > 0) // both positive and negative values, draw axis in the middle
			return bottom - (bottom - top) * (value - aniMin) / (aniMax - aniMin);
		else if (aniMin >= 0) // all positive
			return bottom - (bottom - top) * value / aniMax;
		else if (aniMax <= 0) // all negative
			return bottom - (bottom - top) * (value - aniMin) / -aniMin;

		return 0;
	}

	/**
	 * Get alpha value for drawing bars.
	 */
	private int getAlpha(double value) {
		double divisor = value >= 0 ? max : min;
		int alpha = (int) (75 * Math.abs(value / divisor) + 180);
		return Math.min(Math.max(alpha, 0), 255);
	}

	private Image getImage(String summary) {
		// normalize to lower case
		summary = summary.toLowerCase();

		if (summary.contains("clear"))
			return images[0];
		else if (summary.contains("partly") && summary.contains("cloud"))
			return images[1];
		else if (summary.contains("cloud"))
			return images[2];
		else if (summary.contains("rain"))
			return images[3];
		else if (summary.contains("hail"))
			return images[4];
		else if (summary.contains("storm"))
			return images[5];
		else if (summary.contains("wind"))
			return images[6];
		else if (summary.contains("fog"))
			return images[7];
		else if (summary.contains("sleet"))
			return images[8];
		else if (summary.contains("snow"))
			return images[9];

		Log.warn(getClass(), "Unsupported summary detected: " + summary);
		return images[10];
	}

	public final void setForecasts(List<Forecast> forecasts) {
		// only animate from old values if the number of data items is the same
		if (this.forecasts.size() == forecasts.size())
        {
			oldForecasts = this.forecasts;
            oldMin = min;
            oldMax = max;
        }
		else
			oldForecasts = null;

		this.forecasts = forecasts;

		min = Float.MAX_VALUE;
		max = Float.MIN_VALUE;

		// calculate new min
		for (Forecast forecast : forecasts)
			if (forecast.min < min)
				min = forecast.min;

		// calculate new max
		for (Forecast forecast : forecasts)
			if (forecast.max > max)
				max = forecast.max;

        // don't animate axis if previous forecast doesn't exist
        if (oldForecasts == null)
        {
            oldMin = min;
            oldMax = max;
        }

		repaint();

        timer.start();
        aniProgress = 0;

		Log.info(getClass(), "New range, min: " + min + ", max: " + max);
	}

	public final void clearForecast() {
		setForecasts(new ArrayList<Forecast>());
	}

	public final void setTitle(String title) {
		this.title = title;
		repaint();
	}

	public final void setXAxisText(String text) {
		this.xAxisText = text;
		repaint();
	}

	public final void setYAxisText(String text) {
		this.yAxisText = text;
		repaint();
	}

	/**
	 * Timer callback.
	 */
	public final void actionPerformed(ActionEvent e) {
		if (aniProgress < ANIMATION_TICKS)
			aniProgress++;

		repaint();

		// if animation is not done, repeat timer
		if (aniProgress < ANIMATION_TICKS)
			timer.start();
	}
}