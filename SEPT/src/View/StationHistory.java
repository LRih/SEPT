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

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Utils.AppDefine;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.scroll.WebScrollPane;
import java.awt.Component;
import com.alee.laf.table.WebTable;

public class StationHistory extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebLabel wblblMildura;
	private WebLabel wblblVictoria;
	private StationData data;
	private WebScrollPane webScrollPane;
	private WebTable webTable;
	private DateTimeFormatter dtfOut;

	/**
	 * Create the panel.
	 */
	public StationHistory(final MainPanel m) {

		dtfOut = DateTimeFormat.forPattern("HH:mm dd/MM");

		setBackground(new Color(240, 248, 255));
		setLayout(new MigLayout("", "[10%][][][grow]", "[][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setDrawShade(false);
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(AppDefine.STATION_DETAIL);
			}
		});
		wbtnBack.setText("Back");
		add(wbtnBack, "cell 0 0");

		wblblMildura = new WebLabel();
		wblblMildura.setText("-");
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));

		add(wblblMildura, "cell 1 0");

		wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("-");
		add(wblblVictoria, "cell 2 0");

		webTable = new WebTable(new SampleTableModel());

		webScrollPane = new WebScrollPane(webTable);
		webScrollPane.setDrawFocus(false);
		add(webScrollPane, "cell 0 1 4 2,grow");

		// Better column sizes
		initColumnSizes(webTable);

	}

	public void reloadTable() {
		webTable.setModel(new SampleTableModel());
		initColumnSizes(webTable);
	}

	public void setStation(Station station, StationData data) {
		this.data = data;

		reloadTable();

		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());
	}

	private void initColumnSizes(JTable table) {
		SampleTableModel model = (SampleTableModel) table.getModel();
		TableColumn column;
		Component comp;
		int headerWidth;
		int cellWidth;
		Object[] longValues = model.longValues;
		TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();

		for (int i = 0; i < model.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);

			comp = headerRenderer.getTableCellRendererComponent(null, column.getHeaderValue(), false, false, 0, 0);
			headerWidth = comp.getPreferredSize().width;

			comp = table.getDefaultRenderer(model.getColumnClass(i)).getTableCellRendererComponent(table, longValues[i],
					false, false, 0, i);
			cellWidth = comp.getPreferredSize().width;

			column.setPreferredWidth(Math.max(headerWidth, cellWidth));
		}
	}

	public class SampleTableModel extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames = {
			"Local Time", "Tmp", "App Tmp", "Dew Point", "Real Hum", "Delta-T", "Wind Dir",
            "Wind Spd km/h", "Wind Gust km/h", "Wind Spd kts", "Wind Gust kts", "Press QNH", "Press MSL", "Rain since 9am" };

		public final Object[] longValues = {
			"Local Time", "Tmp", "App Tmp", "Dew Point", "Real Hum", "Delta-T", "Wind Dir",
            "Wind Spd km/h", "Wind Gust km/h", "Wind Spd kts", "Wind Gust kts", "Press QNH", "Press MSL", "Rain since 9am" };

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public int getRowCount() {
			if (data == null)
				return 0;

			return data.getReadings().size();
		}

		@Override
		public String getColumnName(int col) {
			return columnNames[col];
		}

		@Override
		public Object getValueAt(int row, int col) {

			if (data == null)
				return "-";

			LatestReading reading = data.getReadings().get(row);
			switch (col) {
			case 0:
				return reading.getLocalDateTime() != null ? dtfOut.print(reading.getLocalDateTime()) : "-";
			case 1:
				return reading.getAirTemp() != null ? reading.getAirTemp() + "" : "-";
			case 2:
				return reading.getApparentTemp() != null ? reading.getApparentTemp() + "" : "-";
			case 3:
				return reading.getDewPt() != null ? reading.getDewPt() + "" : "-";
			case 4:
				return reading.getRelativeHumidity() != null ? reading.getRelativeHumidity() + "" : "-";
			case 5:
				return reading.getDeltaTemp() != null ? reading.getDeltaTemp() + "" : "-";
			case 6:
				return reading.getWindDir() != null ? reading.getWindDir() + "" : "-";
			case 7:
				return reading.getWindSpdKmH() != null ? reading.getWindSpdKmH() + "" : "-";
			case 8:
				return reading.getWindGustKmH() != null ? reading.getWindGustKmH() + "" : "-";
			case 9:
				return reading.getWindSpdKts() != null ? reading.getWindSpdKts() + "" : "-";
			case 10:
				return reading.getWindGustKts() != null ? reading.getWindGustKts() + "" : "-";
			case 11:
				return reading.getPressureQNH() != null ? reading.getPressureQNH() + "" : "-";
			case 12:
				return reading.getPressureMSL() != null ? reading.getPressureMSL() + "" : "-";
			case 13:
				return reading.getRainTrace() != null ? reading.getRainTrace() + "mm" : "-";
			default:
				return "-";
			}
		}

		@Override
		public Class getColumnClass(int c) {
			return longValues[c].getClass();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		// no need for this because we don't allow editing table
		@Override
		public void setValueAt(Object value, int row, int col) {
			fireTableCellUpdated(row, col);
		}
	}
}
