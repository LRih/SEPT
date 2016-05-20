package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import Model.Station;
import Model.StationData;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.LatestReading;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.scroll.WebScrollPane;

import java.awt.Component;

import com.alee.laf.table.WebTable;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Show all weather history table UI
 */
public final class StationHistory extends JPanel
{
    private static final DateTimeFormatter DT_FORMATTER = DateTimeFormat.forPattern("HH:mm dd/MM");

    private final WebLabel wblblStation;
    private final WebLabel wblblState;
    private final WebTable webTable;

    private StationData data;

    private OnBackClickListener _listener;

    /**
     * Create the panel.
     */
    public StationHistory()
    {
    	setBackground(Color.WHITE);
        setLayout(new MigLayout("", "[10%][][][grow]", "[][grow]"));

        WebButton wbtnBack = new WebButton();
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
        add(wbtnBack, "cell 0 0");

        wblblStation = new WebLabel();
        wblblStation.setText("-");
        wblblStation.setForeground(new Color(255, 69, 0));
        wblblStation.setFont(Style.FONT_30);

        add(wblblStation, "cell 1 0");

        wblblState = new WebLabel();
        wblblState.setFont(Style.FONT_BENDER_16);
        wblblState.setText("-");
        add(wblblState, "cell 2 0");

        webTable = new WebTable(new SampleTableModel());

        WebScrollPane webScrollPane = new WebScrollPane(webTable);
        webScrollPane.setDrawFocus(false);
        add(webScrollPane, "cell 0 1 4 2,grow");

        // Better column sizes
        initColumnSizes(webTable);

    }

    private void reloadTable()
    {
        webTable.setModel(new SampleTableModel());
        initColumnSizes(webTable);
    }

    /**
     * Set station information to this Panel
     */
    public final void setStation(Station station, StationData data)
    {
        this.data = data;

        reloadTable();

        if (station != null)
        {
            wblblStation.setText(station.getName());
            wblblState.setText(station.getState().getName());
        }
    }

    private void initColumnSizes(JTable table)
    {
        SampleTableModel model = (SampleTableModel)table.getModel();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

        for (int i = 0; i < model.getColumnCount(); i++)
        {
            column = table.getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
            headerWidth = comp.getPreferredSize().width;

            comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, longValues[i],
                false, false, 0, i);
            cellWidth = comp.getPreferredSize().width;

            column.setPreferredWidth(Math.max(headerWidth, cellWidth));
        }
    }

    public final void setOnBackClickListener(OnBackClickListener listener)
    {
        _listener = listener;
    }


    /**
     * Sample Table Model for the UI
     */
    public class SampleTableModel extends AbstractTableModel
    {
        private static final long serialVersionUID = 1L;
        private final String[] columnNames = {
            "Local Time", "Tmp", "App Tmp", "Dew Point", "Real Hum", "Delta-T", "Wind Dir",
            "Wind Spd km/h", "Wind Gust km/h", "Wind Spd kts", "Wind Gust kts", "Press QNH", "Press MSL", "Rain since 9am" };

        private final Object[] longValues = {
            "Local Time", "Tmp", "App Tmp", "Dew Point", "Real Hum", "Delta-T", "Wind Dir",
            "Wind Spd km/h", "Wind Gust km/h", "Wind Spd kts", "Wind Gust kts", "Press QNH", "Press MSL", "Rain since 9am" };

        @Override
        public int getColumnCount()
        {
            return columnNames.length;
        }

        @Override
        public int getRowCount()
        {
            if (data == null)
                return 0;

            return data.getLatestReadings().size();
        }

        @Override
        public String getColumnName(int col)
        {
            return columnNames[col];
        }

        @Override
        public Object getValueAt(int row, int col)
        {
            if (data == null)
                return "-";

            LatestReading reading = data.getLatestReadings().get(row);
            switch (col)
            {
                case 0:
                    return reading.localDateTime != null ? DT_FORMATTER.print(reading.localDateTime) : "-";
                case 1:
                    return reading.airTemp != null ? reading.airTemp + "" : "-";
                case 2:
                    return reading.apparentTemp != null ? reading.apparentTemp + "" : "-";
                case 3:
                    return reading.dewPt != null ? reading.dewPt + "" : "-";
                case 4:
                    return reading.relativeHumidity != null ? reading.relativeHumidity + "" : "-";
                case 5:
                    return reading.deltaTemp != null ? reading.deltaTemp + "" : "-";
                case 6:
                    return reading.windDir != null ? reading.windDir + "" : "-";
                case 7:
                    return reading.windSpdKmH != null ? reading.windSpdKmH + "" : "-";
                case 8:
                    return reading.windGustKmH != null ? reading.windGustKmH + "" : "-";
                case 9:
                    return reading.windSpdKts != null ? reading.windSpdKts + "" : "-";
                case 10:
                    return reading.windGustKts != null ? reading.windGustKts + "" : "-";
                case 11:
                    return reading.pressureQNH != null ? reading.pressureQNH + "" : "-";
                case 12:
                    return reading.pressureMSL != null ? reading.pressureMSL + "" : "-";
                case 13:
                    return reading.rainTrace != null ? reading.rainTrace + "mm" : "-";
                default:
                    return "-";
            }
        }

        @Override
        public Class getColumnClass(int c)
        {
            return longValues[c].getClass();
        }

        @Override
        public boolean isCellEditable(int row, int col)
        {
            return false;
        }

        @Override
        public void setValueAt(Object value, int row, int col)
        {
            fireTableCellUpdated(row, col);
        }
    }

	public void setBlockUI(boolean isBlockUI) {
		
	}
}
