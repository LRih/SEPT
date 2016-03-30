import View.LineChart;
import View.Main;

import javax.swing.*;
import java.awt.*;

public final class GraphMain
{
    private JFrame form;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public final void run()
            {
                try
                {
                    GraphMain window = new GraphMain();
                    window.form.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public GraphMain()
    {
        initialize();
    }

    private void initialize()
    {
        form = new JFrame();
        form.setTitle("Chart");
        form.setBounds(100, 100, 500, 400);
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LineChart chart = new LineChart();
        chart.setValues(new float[] { 23, 25, 27, 26.5f, 28, 24, 21, 18.9f, 22 });
        form.add(chart);
    }
}
