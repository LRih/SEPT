package View;

import Utils.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A component for drawing simple charts.
 */
public final class LineChart extends JPanel implements ActionListener
{
    // number of ticks to complete animation
    private static final int ANIMATION_TICKS = 30;
    private static final int ANIMATION_FPS = 60;

    private static final int PADDING = 60;
    private static final int PADDING_RIGHT = 0;
    private static final int LABEL_SIZE = 20;

    private static final int MINOR_AXIS_EXTRA = 6;

    private static final float AXIS_WIDTH = 1.5f;
    private static final float MINOR_AXIS_WIDTH = 1f;
    private static final float LINE_WIDTH = 2f;

    private static final Font FONT_TITLE = new Font("Century Gothic", Font.BOLD, 20);
    private static final Font FONT_AXIS = new Font("Century Gothic", Font.BOLD, 14);
    private static final Font FONT_MINOR_AXIS = new Font("Century Gothic", Font.PLAIN, 14);

    private static final Color COL_TEXT = new Color(70, 70, 70);
    private static final Color COL_AXIS = new Color(160, 160, 160);
    private static final Color COL_MINOR_AXIS = new Color(224, 224, 224);

    private String title = "";
    private String xAxisText = "";
    private String yAxisText = "";

    private String[] xValues = new String[] { };
    private int maxDataPoints;

    // linked hash map to preserve insertion order
    private final LinkedHashMap<String, double[]> datasets = new LinkedHashMap<>();
    private final HashMap<String, Color> colors = new HashMap<>();

    private double min = Float.MAX_VALUE;
    private double max = Float.MIN_VALUE;

    // used for animations
    private final HashMap<String, Integer> aniProgressList = new HashMap<>();
    private final Timer timer;


    public LineChart()
    {
        setBackground(Color.WHITE);

        timer = new Timer(1000 / ANIMATION_FPS, this);
    }


    /**
     * Restart animation for all datasets.
     */
    public final void animate()
    {
        for (String name : aniProgressList.keySet())
            aniProgressList.put(name, ANIMATION_TICKS);
    }


    public final void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawText(g);

        drawMinorAxis(g);
        drawAxis(g);

        drawLines(g);
    }

    private void drawText(Graphics2D g)
    {
        g.setColor(COL_TEXT);

        drawTitle(g);
        drawAxisText(g);
//        drawDatasetNames(g);
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
    private void drawDatasetNames(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_MINOR_AXIS);

        g.setFont(FONT_MINOR_AXIS);

        int index = 0;
        for (String name : datasets.keySet())
        {
            Rectangle2D rect = metrics.getStringBounds(name, g);
            float x = getWidth() - PADDING_RIGHT + MINOR_AXIS_EXTRA;
            float y = PADDING + index * (LABEL_SIZE + MINOR_AXIS_EXTRA);

            g.setColor(colors.get(name));
            g.fillRect((int)x, (int)y, LABEL_SIZE, LABEL_SIZE);

            g.setColor(COL_TEXT);
            g.drawString(name, x + LABEL_SIZE + MINOR_AXIS_EXTRA, y + LABEL_SIZE / 2f - (float)rect.getCenterY());

            index++;
        }
    }
    private void drawEmptyText(Graphics2D g)
    {
        if (!datasets.isEmpty())
            return;

        String text = "No data";
        FontMetrics metrics = g.getFontMetrics(FONT_MINOR_AXIS);
        Rectangle2D rect = metrics.getStringBounds(text, g);

        g.setFont(FONT_MINOR_AXIS);
        g.setColor(COL_AXIS);
        g.drawString(text, getWidth() / 2f - (float)rect.getCenterX(), getHeight() / 2f - (float)rect.getCenterY());
    }

    private void drawMinorAxis(Graphics2D g)
    {
        // don't draw minor axis if panel too small
        if (getWidth() >= PADDING * 4)
            drawMinorXAxis(g);

        // don't draw minor axis if panel too small
        if (getHeight() >= PADDING * 4)
            drawMinorYAxis(g);
    }
    private void drawMinorXAxis(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_MINOR_AXIS);
        g.setFont(FONT_MINOR_AXIS);
        g.setStroke(new BasicStroke(MINOR_AXIS_WIDTH));

        int interval = (int)(maxDataPoints / (getWidth() / 100f));

        if (interval == 0)
            interval = 1;

        if (datasets.isEmpty())
            return;

        for (int i = getStartIndex(); i < xValues.length; i += interval)
        {
            Rectangle2D rect = metrics.getStringBounds(xValues[i], g);
            int x = (int)getX(i);
            int y2 = getHeight() - PADDING + MINOR_AXIS_EXTRA;

            g.setColor(COL_MINOR_AXIS);
            g.drawLine(x, PADDING, x, y2);

            g.setColor(COL_TEXT);
            g.drawString(xValues[i], x - (float)rect.getCenterX(), y2 + (float)rect.getHeight());
        }
    }
    private void drawMinorYAxis(Graphics2D g)
    {
        FontMetrics metrics = g.getFontMetrics(FONT_MINOR_AXIS);
        double median = (max + min) / 2;
        int interval = (int)Math.abs((max - min) / (getHeight() / 100f));

        if (interval == 0)
            interval = 1;

        if (datasets.isEmpty())
            return;

        // draw above mean
        int value = (int)median;
        int y = (int)getY(value);

        while (y >= PADDING)
        {
            String text = String.valueOf(value);
            Rectangle2D rect = metrics.getStringBounds(text, g);
            int x = PADDING - MINOR_AXIS_EXTRA;

            g.setColor(COL_MINOR_AXIS);
            g.drawLine(x, y, getWidth() - PADDING_RIGHT, y);

            g.setColor(COL_TEXT);
            g.drawString(text, x - (float)rect.getWidth(), y - (float)rect.getCenterY());

            value += interval;
            y = (int)getY(value);
        }

        // draw below mean
        value = (int)median - interval;
        y = (int)getY(value);

        while (y <= getHeight() - PADDING)
        {
            String text = String.valueOf(value);
            Rectangle2D rect = metrics.getStringBounds(text, g);
            int x = PADDING - MINOR_AXIS_EXTRA;

            g.setColor(COL_MINOR_AXIS);
            g.drawLine(x, y, getWidth() - PADDING_RIGHT, y);

            g.setColor(COL_TEXT);
            g.drawString(text, x - (float)rect.getWidth(), y - (float)rect.getCenterY());

            value -= interval;
            y = (int)getY(value);
        }
    }

    private void drawAxis(Graphics2D g)
    {
        g.setColor(COL_AXIS);
        g.setStroke(new BasicStroke(AXIS_WIDTH));

        g.drawLine(PADDING, PADDING, PADDING, getHeight() - PADDING); // vertical axis

        // constrain axis to chart bounds
        int y = (int)getY(0);
        y = Math.min(Math.max(y, PADDING), getHeight() - PADDING);

        // set axis to bottom when no datasets
        if(datasets.isEmpty())
            y = getHeight() - PADDING;

        g.drawLine(PADDING, y, getWidth() - PADDING_RIGHT, y); // horizontal axis
    }

    private void drawLines(Graphics2D g)
    {
        // set clip so line does not draw outside the axis
        g.setClip(PADDING, PADDING, getWidth() - PADDING - PADDING_RIGHT, getHeight() - PADDING * 2);

        int index = 0;
        for (String name : datasets.keySet())
        {
            g.setColor(colors.get(name));
            g.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(getLinePath(datasets.get(name), name));

            index++;
        }

        g.setClip(null);
    }


    private Path2D getLinePath(double[] values, String name)
    {
        double median = (min + max) / 2;
        double aniLeftPercent = aniProgressList.get(name) / (float)ANIMATION_TICKS;

        int start = getStartIndex();

        Path2D path = new Path2D.Float();
        path.moveTo(getX(start), getY(values[start] - (values[start] - median) * aniLeftPercent));

        for (int i = start + 1; i < values.length; i++)
            path.lineTo(getX(i), getY(values[i] - (values[i] - median) * aniLeftPercent));

        return path;
    }

    /**
     * Get x-coord of value based on index of value.
     */
    private float getX(int index)
    {
        int width = getWidth() - PADDING - PADDING_RIGHT;
        int left = PADDING;

        if (xValues.length == 1)
            return left;

        return left + width / ((float)maxDataPoints - 1) * (index - getStartIndex());
    }

    /**
     * Get y-coord of value based on min/max values.
     */
    private double getY(double value)
    {
        int height = getHeight() - PADDING * 2;
        float top = PADDING + height / 8f;
        float bottom = getHeight() - PADDING - height / 8f;

        return bottom - (bottom - top) * (value - min) / (max - min);
    }

    /**
     * Based on max data points.
     */
    private int getStartIndex()
    {
        return xValues.length - maxDataPoints;
    }


    public final void setXValues(String[] values)
    {
        xValues = values;
        maxDataPoints = values.length;

        repaint();
    }

    public final void addDataset(String name, Color color, double[] values)
    {
        if (datasets.containsKey(name))
        {
            Log.warn(getClass(), "Dataset " + name + " already exists");
            return;
        }

        if (values.length == 0)
            return;

        datasets.put(name, values);
        colors.put(name, color);
        aniProgressList.put(name, ANIMATION_TICKS);

        // calculate new min
        for (double value : values)
            if (value < min)
                min = value;

        // calculate new max
        for (double value : values)
            if (value > max)
                max = value;

        repaint();
        timer.start();

        Log.info(getClass(), "New range, min: " + min + ", max: " + max);
    }
    public final void removeDataset(String name)
    {
        if (!datasets.containsKey(name))
        {
            Log.warn(getClass(), "Dataset " + name + " doesn't exist");
            return;
        }

        datasets.remove(name);
        colors.remove(name);
        aniProgressList.remove(name);
    }
    
    public final void resetChart() {
    	colors.clear();
        aniProgressList.clear();

        min = Float.MAX_VALUE;
        max = Float.MIN_VALUE;

        repaint();
    }

    public final void clearDatasets()
    {
        datasets.clear();
        
        resetChart();
        
    }
    public final void clearValues()
    {
        xValues = new String[] { };
        clearDatasets();

        repaint();
    }

    public final int getLineCount()
    {
        return datasets.keySet().size();
    }

    public final void setTitle(String title)
    {
        this.title = title;
        repaint();
    }
    public final void setXAxisText(String text)
    {
        this.xAxisText = text;
        repaint();
    }
    public final void setYAxisText(String text)
    {
        this.yAxisText = text;
        repaint();
    }
    
    public final String[] getXValues() {
    	return xValues;
    }

    public final void setMaxDataPoints(int max)
    {
        maxDataPoints = Math.min(max, xValues.length);

        repaint();
    }

    /**
     * Timer callback.
     */
    public final void actionPerformed(ActionEvent e)
    {
        boolean isAnimationDone = true;

        for (String name : aniProgressList.keySet())
        {
            int tick = aniProgressList.get(name);

            if (tick > 1)
                isAnimationDone = false;

            if (tick > 0)
                aniProgressList.put(name, tick - 1);
        }

        repaint();

        // if animation is not done, repeat timer
        if (!isAnimationDone)
            timer.start();
    }
}