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

import com.bitagentur.renderer.JChartLibPanel;
import com.bitagentur.chart.JChartLibBaseChart;
import com.bitagentur.chart.JChartLibLineChart;
import com.bitagentur.data.JChartLibDataSet;
import com.bitagentur.data.JChartLibSerie;
import com.alee.laf.combobox.WebComboBox;

import javax.swing.DefaultComboBoxModel;

/**
 * Chart UI
 */
public final class StationChart extends JPanel
{
    private WebLabel wblblStation;
    private WebLabel wblblState;
    private JChartLibPanel chartLibPanel;
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

        chartLibPanel = new JChartLibPanel(null);
        panel.add(chartLibPanel, "cell 0 0, grow");
        chartLibPanel.setPreferredSize(new Dimension(600, 270));

        wcbChartType.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                AppState.getInstance().chartIndex = wcbChartType.getSelectedIndex();
                updateChart();
            }
        });
    }

    private JChartLibDataSet createDataset(ChartType type)
    {
        final JChartLibDataSet dataset = new JChartLibDataSet();

        if (data == null)
        {
            int[] tempData = {};
            dataset.addDataSerie("N/A", tempData);
            return dataset;
        }

        JChartLibSerie serie = null;

        switch (type)
        {
            case Chart9AM:
                serie = new JChartLibSerie("Daily 9am");
                for (HistoricalReading reading : data.getHistoricalReadings())
                    if (reading.temp9AM != null)
                        serie.addValue(reading.date.toDate(), reading.temp9AM);
                break;

            case Chart3PM:
                serie = new JChartLibSerie("Daily 3pm");
                for (HistoricalReading reading : data.getHistoricalReadings())
                    if (reading.temp3PM != null)
                        serie.addValue(reading.date.toDate(), reading.temp3PM);
                break;

            case Min:
                serie = new JChartLibSerie("Daily Min");
                for (HistoricalReading reading : data.getHistoricalReadings())
                    if (reading.minTemp != null)
                        serie.addValue(reading.date.toDate(), reading.minTemp);
                break;

            case Max:
                serie = new JChartLibSerie("Daily Max");
                for (HistoricalReading reading : data.getHistoricalReadings())
                    if (reading.maxTemp != null)
                        serie.addValue(reading.date.toDate(), reading.maxTemp);
                break;
        }

        dataset.addDataSerie(serie);

        return dataset;
    }

    private JChartLibBaseChart createChart(JChartLibDataSet dataset)
    {
        // create the chart with title and axis names
        return new JChartLibLineChart(
            "", // chart title
            "day", // x axis text
            "temperature (°C)", // y axis text
            dataset);
    }

    private void updateChart()
    {
        panel.removeAll();

        JChartLibDataSet chartLibDataSet = createDataset(ChartType.values()[wcbChartType.getSelectedIndex()]);
        JChartLibBaseChart baseChart = createChart(chartLibDataSet);
        chartLibPanel = new JChartLibPanel(baseChart);
        panel.add(chartLibPanel, "cell 0 0, grow");

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
