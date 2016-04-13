package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.Station;
import Model.StationData;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import com.bitagentur.renderer.JChartLibPanel;
import com.bitagentur.chart.JChartLibAreachart;
import com.bitagentur.chart.JChartLibBaseChart;
import com.bitagentur.chart.JChartLibLineChart;
import com.bitagentur.data.JChartLibDataSet;
import com.bitagentur.data.JChartLibSerie;

public class StationChart extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebLabel wblblMildura;
	private WebLabel wblblVictoria;
	private StationData data;
	private JChartLibPanel chartLibPanel;

	private JChartLibDataSet createDataset() {
	    //Dataseries can be added with int arrays
	    int[] values1 = new int[5];
	    values1[0] = 1;
	    values1[1] = 3;
	    values1[2] = 4;
	    values1[3] = 7;
	    values1[4] = 2;

	    //or by generating a Dataserie object
	    JChartLibSerie values2 = new JChartLibSerie("Banana");
	    values2.addValue(5);
	    values2.addValue(4);
	    values2.addValue(2);
	    values2.addValue(6);
	    values2.addValue(2);

	    final JChartLibDataSet dataset = new JChartLibDataSet();
	    dataset.addDataSerie("Apple", values1);   //adds the apples
	    dataset.addDataSerie(values2);            //adds the bananas

	    return dataset;
	}
	
	private JChartLibBaseChart createChart(final JChartLibDataSet dataset) {

	    // create the chart with title and axis names
	    final JChartLibLineChart chart = new JChartLibLineChart(
	            "", // chart title
	            "time", // x axis text
	            "temperature", // y axis text
	            dataset
	            );

	    return chart;
	}
	
	/**
	 * Create the panel.
	 */
	public StationChart(final MainPanel m) {
		setBackground(new Color(240, 248, 255));
		setLayout(new MigLayout("", "[grow][][][]", "[][grow][][][][][][]"));

		setLayout(new MigLayout("", "[10%][][][grow]", "[][][][][][][][grow]"));

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
		

		final JChartLibDataSet dataset = createDataset();
	    final JChartLibBaseChart chart = createChart(dataset);
//	    chartLibPanel = new JChartLibPanel(null);
		chartLibPanel = new JChartLibPanel(chart);
		chartLibPanel.setPreferredSize(new Dimension(600, 270));
		chartLibPanel.setBackground(new Color(240, 248, 255));
		add(chartLibPanel, "cell 0 1 4 1,growx");
		
	}

	public void setStation(Station station, StationData data) {
		this.data = data;
		
		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());
	}
	

}
