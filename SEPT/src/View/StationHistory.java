package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.Station;
import Model.StationData;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.scroll.WebScrollPane;
import java.awt.Component;
import com.alee.laf.table.WebTable;
import com.alee.laf.table.renderers.WebTableCellRenderer;
import com.alee.utils.swing.WebDefaultCellEditor;

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

	/**
	 * Create the panel.
	 */
	public StationHistory(MainPanel m) {
		setBackground(new Color(240, 248, 255));
		setLayout(new MigLayout("", "[10%][][][grow]", "[][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setDrawShade(false);
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(0);
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

		webTable = new WebTable(new ExampleTableModel());
		
		webScrollPane = new WebScrollPane(webTable);
		webScrollPane.setDrawFocus(false);
		add(webScrollPane, "cell 0 1 4 2,grow");
		
		// Custom column
        TableColumn column = webTable.getColumnModel ().getColumn ( 1 );

        // Custom renderer
        WebTableCellRenderer renderer = new WebTableCellRenderer ();
        renderer.setToolTipText ( "Click for combo box" );
        column.setCellRenderer ( renderer );

        // Custom editor
        JComboBox<String> comboBox = new JComboBox<String> ();
        comboBox.addItem ( "Snowboarding" );
        comboBox.addItem ( "Rowing" );
        comboBox.addItem ( "Knitting" );
        comboBox.addItem ( "Speed reading" );
        comboBox.addItem ( "Pool" );
        comboBox.addItem ( "None of the above" );
        column.setCellEditor ( new WebDefaultCellEditor ( comboBox ) );

        // Better column sizes
        initColumnSizes ( webTable );

	}

	public void setStation(Station station, StationData data) {
		this.data = data;

		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());
	}
	
	private void initColumnSizes ( JTable table )
    {
        ExampleTableModel model = ( ExampleTableModel ) table.getModel ();
        TableColumn column;
        Component comp;
        int headerWidth;
        int cellWidth;
        Object[] longValues = model.longValues;
        TableCellRenderer headerRenderer = table.getTableHeader ().getDefaultRenderer ();

        for ( int i = 0; i < model.getColumnCount (); i++ )
        {
            column = table.getColumnModel ().getColumn ( i );

            comp = headerRenderer.getTableCellRendererComponent ( null, column.getHeaderValue (), false, false, 0, 0 );
            headerWidth = comp.getPreferredSize ().width;

            comp = table.getDefaultRenderer ( model.getColumnClass ( i ) ).
                    getTableCellRendererComponent ( table, longValues[ i ], false, false, 0, i );
            cellWidth = comp.getPreferredSize ().width;

            column.setPreferredWidth ( Math.max ( headerWidth, cellWidth ) );
        }
    }

	
	public class ExampleTableModel extends AbstractTableModel
    {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String[] columnNames = { "Name", "Sport", "# of Years", "Vegetarian" };
        private Object[][] data = { { "Kathy", "Snowboarding", 5, false }, { "John", "Rowing", 3, true }, { "Sue", "Knitting", 2, false },
                { "Jane", "Speed reading", 20, true }, { "Joe", "Pool", 10, false } };

        public final Object[] longValues = { "Jane", "None of the above", 20, Boolean.TRUE };

        @Override
        public int getColumnCount ()
        {
            return columnNames.length;
        }

        @Override
        public int getRowCount ()
        {
            return data.length;
        }

        @Override
        public String getColumnName ( int col )
        {
            return columnNames[ col ];
        }

        @Override
        public Object getValueAt ( int row, int col )
        {
            return data[ row ][ col ];
        }

        @Override
        public Class getColumnClass ( int c )
        {
            return longValues[ c ].getClass ();
        }

        @Override
        public boolean isCellEditable ( int row, int col )
        {
            return col >= 1;
        }

        @Override
        public void setValueAt ( Object value, int row, int col )
        {
            data[ row ][ col ] = value;
            fireTableCellUpdated ( row, col );
        }
    }
}

