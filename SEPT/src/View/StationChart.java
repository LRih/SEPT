package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.HistoricalReading;
import Model.Station;
import Model.StationData;
import Utils.Log;
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
    private final WebLabel wblblStation;
    private final WebLabel wblblState;
    private final LineChart chartPanel;
    private final JPanel panel;
    private final WebComboBox wcbChartType;

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
        wcbChartType.setModel(new DefaultComboBoxModel(new String[]
        {
            "Min temp.", "Max temp.", "Rainfall", "Max wind gust (km/h)",
            "Temp. 9AM", "Rel. humidity 9AM", "Wind spd 9AM", "Pressure 9AM",
            "Temp. 3PM", "Rel. humidity 3PM", "Wind spd 3PM", "Pressure 3PM"
        }));
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

    private void updateChart()
    {
        ChartType type = ChartType.values()[wcbChartType.getSelectedIndex()];

        chartPanel.setXValues(getXAxisValues());
        setDataset(type);

        Log.info(getClass(), "Chart changed to " + type.name());

        panel.validate();
        panel.repaint();
    }

    /**
     * Replay chart animation.
     */
    public final void animate()
    {
        chartPanel.animate();
    }


    private void setDataset(ChartType type)
    {
        chartPanel.clearDatasets();

        if (data == null)
            return;

        String name = (String)wcbChartType.getSelectedItem();
        double[] values = new double[data.getHistoricalReadings().size()];

        List<HistoricalReading> readings = data.getHistoricalReadings();

        switch (type)
        {
            case MinTemp:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).minTemp != null)
                        values[i] = readings.get(i).minTemp;
                break;

            case MaxTemp:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).maxTemp != null)
                        values[i] = readings.get(i).maxTemp;
                break;

            case Rainfall:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).rainfall != null)
                        values[i] = readings.get(i).rainfall;
                break;

            case MaxWindGustKmH:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).maxWindGustKmH != null)
                        values[i] = readings.get(i).maxWindGustKmH;
                break;

            case Temp9AM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).temp9AM != null)
                        values[i] = readings.get(i).temp9AM;
                break;
            case RelHumidity9AM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).relHumidity9AM != null)
                        values[i] = readings.get(i).relHumidity9AM;
                break;
            case WindSpd9AM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).windSpd9AM != null)
                        values[i] = readings.get(i).windSpd9AM;
                break;
            case PressureMSL9AM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).pressureMSL9AM != null)
                        values[i] = readings.get(i).pressureMSL9AM;
                break;

            case Temp3PM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).temp3PM != null)
                        values[i] = readings.get(i).temp3PM;
                break;
            case RelHumidity3PM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).relHumidity3PM != null)
                        values[i] = readings.get(i).relHumidity3PM;
                break;
            case WindSpd3PM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).windSpd3PM != null)
                        values[i] = readings.get(i).windSpd3PM;
                break;
            case PressureMSL3PM:
                for (int i = 0; i < values.length; i++)
                    if (readings.get(i).pressureMSL3PM != null)
                        values[i] = readings.get(i).pressureMSL3PM;
                break;

            default:
                Log.warn(getClass(), "Invalid chart type: " + type.name());
                return;
        }

        chartPanel.addDataset(name, values);
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
        MinTemp, MaxTemp, Rainfall, MaxWindGustKmH,
        Temp9AM, RelHumidity9AM, WindSpd9AM, PressureMSL9AM,
        Temp3PM, RelHumidity3PM, WindSpd3PM, PressureMSL3PM
    }
}
