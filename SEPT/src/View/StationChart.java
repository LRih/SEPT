package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.HistoricalReading;
import Model.Station;
import Model.StationData;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import com.alee.laf.combobox.WebComboBox;

import javax.swing.DefaultComboBoxModel;

/**
 * Chart UI
 */
public final class StationChart extends JPanel
{
    private WebLabel wblblStation;
    private WebLabel wblblState;
    private LineChart chartPanel;
    private JPanel panel;
    private WebComboBox wcbChartType;

    private StationData data;

    private OnBackClickListener _listener;

    /**
     * Create the panel.
     */
    public StationChart()
    {
        setBackground(new Color(240, 248, 255));
        setLayout(new MigLayout("", "[10%][][][grow]", "[][grow]"));

        WebButton wbtnBack = new WebButton();
        wbtnBack.setFont(new Font("Bender", Font.PLAIN, 13));
        wbtnBack.setDrawShade(false);
        wbtnBack.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listener != null)
                    _listener.onBackClick();
            }
        });
        wbtnBack.setText("Back");
        add(wbtnBack, "cell 0 0,alignx left,aligny center");

        wblblStation = new WebLabel();
        wblblStation.setText("-");
        wblblStation.setForeground(new Color(255, 69, 0));
        wblblStation.setFont(new Font("Century Gothic", Font.PLAIN, 30));

        add(wblblStation, "cell 1 0");

        wblblState = new WebLabel();
        wblblState.setFont(new Font("Bender", Font.PLAIN, 16));
        wblblState.setText("-");
        add(wblblState, "cell 2 0,alignx trailing");

        WebLabel wblblSelectChart = new WebLabel();
        wblblSelectChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
        wblblSelectChart.setText("Select Chart");
        add(wblblSelectChart, "flowx,cell 3 0,alignx right,aligny center");

        wcbChartType = new WebComboBox();
        wcbChartType.setDrawFocus(false);
        wcbChartType.setFont(new Font("Bender", Font.PLAIN, 13));
        wcbChartType.setModel(
            new DefaultComboBoxModel(new String[] { "Daily 9AM", "Daily 3PM", "Daily Min", "Daily Max" }));
        add(wcbChartType, "cell 3 0,alignx right");

        if (AppState.getInstance().chartIndex >= 0 && AppState.getInstance().chartIndex < ChartType.values().length)
            wcbChartType.setSelectedIndex(AppState.getInstance().chartIndex);

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        add(panel, "cell 0 1 4 1,grow");
        panel.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

        chartPanel = new LineChart();
        chartPanel.setTitle("Historical");
        chartPanel.setXAxisText("Date");
        chartPanel.setYAxisText("Temperature (°C)");
        panel.add(chartPanel, "cell 0 0, grow");
        chartPanel.setPreferredSize(new Dimension(600, 270));

        wcbChartType.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                AppState.getInstance().chartIndex = wcbChartType.getSelectedIndex();
                updateChart();
            }
        });
    }

    private void setDataset(ChartType type)
    {
        chartPanel.clearDatasets();

        if (data == null)
            return;

        double[] values = new double[data.getHistoricalReadings().size()];

        List<HistoricalReading> readings = data.getHistoricalReadings();

        switch (type)
        {
            case Chart9AM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).temp9AM != null)
                        values[i] = readings.get(i).temp9AM;
                chartPanel.addDataset("Daily 9am", values);
                break;

            case Chart3PM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).temp3PM != null)
                        values[i] = readings.get(i).temp3PM;
                chartPanel.addDataset("Daily 3pm", values);
                break;

            case Min:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).minTemp != null)
                        values[i] = readings.get(i).minTemp;
                chartPanel.addDataset("Daily Min", values);
                break;

            case Max:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).maxTemp != null)
                        values[i] = readings.get(i).maxTemp;
                chartPanel.addDataset("Daily Max", values);
                break;
        }
    }

    private String[] getXAxisValues()
    {
        if (data == null)
            return new String[] { };

        String[] values = new String[data.getHistoricalReadings().size()];

        List<HistoricalReading> readings = data.getHistoricalReadings();

        for (int i = 0; i < values.length; i++)
            values[i] = readings.get(i).date.getDayOfMonth() + "/" + readings.get(i).date.getMonthOfYear();

        return values;
    }

    private void updateChart()
    {
        chartPanel.setXValues(getXAxisValues());
        setDataset(ChartType.values()[wcbChartType.getSelectedIndex()]);

        panel.validate();
        panel.repaint();
    }

    /**
     * Set station data for this panel.
     */
    public final void setStation(Station station, StationData data)
    {
        this.data = data;

        if (station != null)
        {
            wblblStation.setText(station.getName());
            wblblState.setText(station.getState().getName());
        }

        updateChart();
    }

    public final void setOnBackClickListener(OnBackClickListener listener)
    {
        _listener = listener;
    }


    public enum ChartType
    {
        Chart9AM, Chart3PM, Min, Max
    }
}
