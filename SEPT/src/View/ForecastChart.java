package View;

import Model.Forecast;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A component for drawing bar charts for forecasts.
 */
public final class ForecastChart extends JPanel
{
    private static final int PADDING = 60;

    private static final float AXIS_WIDTH = 1.5f;

    private static final Font FONT_TITLE = new Font("Century Gothic", Font.BOLD, 20);
    private static final Font FONT_AXIS = new Font("Century Gothic", Font.BOLD, 14);
    private static final Font FONT_FORECAST = new Font("Century Gothic", Font.PLAIN, 14);

    private static final Color COL_TEXT = new Color(70, 70, 70);
    private static final Color COL_AXIS = new Color(160, 160, 160);

    private static final Color COL_MIN = new Color(36, 162, 32);
    private static final Color COL_MAX = new Color(255, 127, 0);

    private String title = "";
    private String xAxisText = "";
    private String yAxisText = "";

    private List<Forecast> forecasts = new ArrayList<>();

    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;


    public ForecastChart()
    {
        setBackground(Color.WHITE);
    }


    public final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawText(g);
        drawAxis(g);
        drawBars(g);
    }

    private void drawText(Graphics2D g)
    {
        g.setColor(COL_TEXT);

        drawTitle(g);
        drawAxisText(g);
        drawEmptyText(g);
    }

    private void drawTitle(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_TITLE);
        Rectangle2D rect = metrics.getStringBounds(title, g);

        g.setFont(FONT_TITLE);

        float x = getWidth() / 2f - (float)rect.getCenterX();
        float y = PADDING / 2f - (float)rect.getCenterY();

        g.drawString(title, x, y);
    }
    private void drawAxisText(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_AXIS);
        g.setFont(FONT_AXIS);

        // x-axis
        Rectangle2D rect = metrics.getStringBounds(xAxisText, g);

        float x = getWidth() / 2f - (float)rect.getCenterX();
        float y = getHeight() - PADDING / 3f - (float)rect.getCenterY();

        g.drawString(xAxisText, x, y);

        // y-axis
        rect = metrics.getStringBounds(yAxisText, g);

        float rad = 90 * (float)Math.PI /180;

        x = PADDING / 3f;
        y = getHeight() / 2f;

        g.rotate(-rad, x, y);
        g.drawString(yAxisText, PADDING / 3f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
        g.rotate(rad, x, y);
    }
    private void drawEmptyText(Graphics2D g)
    {
        if (!forecasts.isEmpty())
            return;

        String text = "No data";
        FontMetrics metrics = g.getFontMetrics(FONT_FORECAST);
        Rectangle2D rect = metrics.getStringBounds(text, g);

        g.setFont(FONT_FORECAST);
        g.setColor(COL_AXIS);
        g.drawString(text, getWidth() / 2f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
    }

    private void drawAxis(Graphics2D g)
    {
        g.setColor(COL_AXIS);
        g.setStroke(new BasicStroke(AXIS_WIDTH));

        // constrain axis to chart bounds
        int y = (int)getY(0);
        y = Math.min(Math.max(y, PADDING), getHeight() - PADDING);

        // set axis to bottom when no forecasts
        if(forecasts.isEmpty())
            y = getHeight() - PADDING;

        g.drawLine(PADDING, y, getWidth() - PADDING, y); // horizontal axis
    }

    private void drawBars(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_AXIS);
        g.setFont(FONT_AXIS);

        // calculate bar width
        float width = getWidth() - PADDING * 2;

        float barSep = width / 80;
        float forecastSep = width / 30;
        float bar = (width - forecastSep * (forecasts.size() + 1) - barSep * forecasts.size()) / (forecasts.size() * 2);

        for (int i = 0; i < forecasts.size(); i++)
        {
            // min value
            String temp = String.valueOf(forecasts.get(i).min);
            Rectangle2D rect = metrics.getStringBounds(temp, g);

            g.setColor(COL_MIN);

            int x1 = (int)(PADDING + forecastSep + i * (barSep + bar * 2 + forecastSep));
            int y1 = (int)getY(forecasts.get(i).min);
            int y2 = (int)getY(0);

            g.fillRect(x1, y1 < y2 ? y1 : y2, (int)bar, Math.abs(y2 - y1));

            // don't draw text if width too small
            if (getWidth() >= PADDING * 6)
                g.drawString(temp, x1 + bar / 2 - (float)rect.getCenterX(), (y1 < y2 ? y1 : y2) + (float)rect.getCenterY());

            // max value
            temp = String.valueOf(forecasts.get(i).max);
            rect = metrics.getStringBounds(temp, g);

            g.setColor(COL_MAX);

            x1 = (int)(PADDING + forecastSep + bar + barSep + i * (barSep + bar * 2 + forecastSep));
            y1 = (int)getY(forecasts.get(i).max);
            y2 = (int)getY(0);

            g.fillRect(x1, y1 < y2 ? y1 : y2, (int)bar, Math.abs(y2 - y1));

            // don't draw text if width too small
            if (getWidth() >= PADDING * 6)
                g.drawString(temp, x1 + bar / 2 - (float)rect.getCenterX(), (y1 < y2 ? y1 : y2) + (float)rect.getCenterY());
        }

        // draw the text below forecast
        metrics = g.getFontMetrics(FONT_FORECAST);
        g.setFont(FONT_FORECAST);
        g.setColor(COL_TEXT);

        for (int i = 0; i < forecasts.size(); i++)
        {
            float x = PADDING + forecastSep + i * (barSep + bar * 2 + forecastSep) + bar + barSep / 2;
            float y = getHeight() - PADDING;

            // don't draw text if width too small
            if (getWidth() >= PADDING * 6)
            {
                String date = forecasts.get(i).date.getDayOfMonth() + "/" + forecasts.get(i).date.getMonthOfYear();
                Rectangle2D rect = metrics.getStringBounds(date, g);
                g.drawString(date, x - (float)rect.getCenterX(), y + (float)rect.getHeight());
            }

            // don't draw text if width too small
            Rectangle2D rect = metrics.getStringBounds(forecasts.get(i).summary, g);
            if (getWidth() >= PADDING * 8)
                g.drawString(forecasts.get(i).summary, x - (float)rect.getCenterX(), y + (float)rect.getHeight() * 2);
            else if (getWidth() >= PADDING * 6)
                g.drawString(forecasts.get(i).summary, x - (float)rect.getCenterX(), y + (float)rect.getHeight() * (i % 2 == 0 ? 2 : 3));
        }
    }

    /**
     * Get y-coord of value based on min/max values.
     */
    private double getY(double value)
    {
        int height = getHeight() - PADDING * 2;

        if (min < 0 && max > 0) // both positive and negative values, draw axis in the middle
        {
            double top = PADDING + height / 8f;
            double bottom = getHeight() - PADDING - height / 8f;

            return bottom - (bottom - top) * (value - min) / (max - min);
        }
        else if (min >= 0) // all positive
        {
            double top = PADDING + height / 8f;
            double bottom = getHeight() - PADDING;

            return bottom - (bottom - top) * value / max;
        }
        else if (max <= 0) // all negative
        {
            double top = PADDING;
            double bottom = getHeight() - PADDING - height / 8f;

            return bottom - (bottom - top) * (value - min) / -min;
        }

        return 0;
    }


    public final void setForecasts(List<Forecast> forecasts)
    {
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

        invalidate();
    }

    public final void setTitle(String title)
    {
        this.title = title;
        invalidate();
    }
    public final void setXAxisText(String text)
    {
        this.xAxisText = text;
        invalidate();
    }
    public final void setYAxisText(String text)
    {
        this.yAxisText = text;
        invalidate();
    }
}