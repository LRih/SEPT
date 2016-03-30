package View;

import java.awt.*;
import java.awt.geom.Path2D;

public final class LineChart extends Canvas
{
    private static final int PADDING = 40;
    private static final int AXIS_WIDTH = 2;
    private static final int LINE_WIDTH = 3;

    private static final Color COL_AXIS = new Color(160, 160, 160);
    private static final Color COL_LINE = new Color(39, 130, 160);
    private static final Color COL_LINE_FILL = new Color(152, 207, 210);

    private float[] values;

    public LineChart()
    {
        setBackground(Color.WHITE);
    }

    public final void paint(Graphics graphics)
    {
        Graphics2D g = (Graphics2D)graphics;

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        drawLineFill(g);
        drawLine(g);
        drawAxis(g);
    }

    public final void drawAxis(Graphics2D g)
    {
        g.setColor(COL_AXIS);
        g.setStroke(new BasicStroke(AXIS_WIDTH));
        g.drawLine(PADDING, PADDING, PADDING, getHeight() - PADDING); // vertical axis
        g.drawLine(PADDING, getHeight() - PADDING, getWidth() - PADDING, getHeight() - PADDING); // horizontal axis
    }

    public final void drawLine(Graphics2D g)
    {
        // no values, don't draw
        if (values == null || values.length == 0)
            return;

        g.setColor(COL_LINE);
        g.setStroke(new BasicStroke(LINE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));

        g.draw(getLinePath());
    }
    public final void drawLineFill(Graphics2D g)
    {
        // no values, don't draw
        if (values == null || values.length == 0)
            return;

        g.setColor(COL_LINE_FILL);

        Path2D path = getLinePath();

        // close path to fill
        path.lineTo(getWidth() - PADDING, getHeight() - PADDING);
        path.lineTo(PADDING, getHeight() - PADDING);
        path.closePath();

        g.fill(path);
    }


    private Path2D getLinePath()
    {
        float min = getMinValue();
        float max = getMaxValue();

        Path2D path = new Path2D.Float();
        path.moveTo(getX(0), getY(values[0], min, max));

        for (int i = 1; i < values.length; i++)
            path.lineTo(getX(i), getY(values[i], min, max));

        return path;
    }

    /* get x coord of value based on index of value */
    private float getX(int index)
    {
        int width = getWidth() - PADDING * 2;
        int left = PADDING;

        if (values.length == 1)
            return left;

        return left + width / ((float)values.length - 1) * index;
    }

    /* get y coord of value based on min/max values */
    private float getY(float value, float min, float max)
    {
        int height = getHeight() - PADDING * 2;
        float top = PADDING + height / 8f;
        float bottom = getHeight() - PADDING - height / 8f;

        return bottom - (bottom - top) * (value - min) / (max - min);
    }

    private float getMinValue()
    {
        if (values == null || values.length == 0)
            return 0;

        float min = values[0];

        // iterate array to find min
        for (int i = 1; i < values.length; i++)
            min = Math.min(min, values[i]);

        return min;
    }
    private float getMaxValue()
    {
        if (values == null || values.length == 0)
            return 0;

        float max = values[0];

        // iterate array to find max
        for (int i = 1; i < values.length; i++)
            max = Math.max(max, values[i]);

        return max;
    }

    public final void clearValues()
    {
        values = null;
        invalidate();
    }

    public final void setValues(int[] values)
    {
        this.values = new float[values.length];

        // copy source array to values
        for (int i = 0; i < values.length; i++)
            this.values[i] = values[i];

        invalidate();
    }
    public final void setValues(float[] values)
    {
        this.values = values;
        invalidate();
    }
}