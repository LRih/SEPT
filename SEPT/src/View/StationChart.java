package View;

import javax.swing.JPanel;

import Model.HistoricalReading;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Utils.AppDefine;

import java.awt.event.ActionListener;
import java.util.List;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import com.bitagentur.renderer.JChartLibPanel;
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
	private StationData data = null;
	private JChartLibPanel chartLibPanel;
	private JPanel panel;

	private JChartLibDataSet createDataset() {

		final JChartLibDataSet dataset = new JChartLibDataSet();

		if (this.data == null) {
			int[] tempData = {};
			dataset.addDataSerie("N/A", tempData);
			return dataset;
		}

        // TODO check this Quan
//		JChartLibSerie serie3PM = getSerie("3PM Temp", data.get3PMReadings());
//		JChartLibSerie serie9AM = getSerie("9AM Temp", data.get9AMReadings());
//		JChartLibSerie serieMax = getSerie("Max Temp", data.getMaxReadings());
//		JChartLibSerie serieMin = getSerie("Min Temp", data.getMinReadings());
//
//		dataset.addDataSerie(serie3PM);
//		dataset.addDataSerie(serie9AM);
//		dataset.addDataSerie(serieMax);
//		dataset.addDataSerie(serieMin);

        JChartLibSerie serieMax = new JChartLibSerie("Max Temp");
        for (HistoricalReading reading : data.getHistoricalReadings()) {
            if (reading.max != null) {
                serieMax.addValue(reading.max);
            }
        }
		dataset.addDataSerie(serieMax);

		return dataset;
	}

	private JChartLibSerie getSerie(String name, List<LatestReading> list) {
		JChartLibSerie serie = new JChartLibSerie(name);
		for (LatestReading reading : list) {
			serie.addValue(reading.getAirTemp());
		}

		return serie;
	}

	private JChartLibBaseChart createChart(final JChartLibDataSet dataset) {

		// create the chart with title and axis names
		final JChartLibLineChart chart = new JChartLibLineChart("", // chart
																	// title
				"time", // x axis text
				"temperature", // y axis text
				dataset);

		return chart;
	}

	/**
	 * Create the panel.
	 */
	public StationChart(final MainPanel m) {
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
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 0 1 4 1,grow");
		panel.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		chartLibPanel = new JChartLibPanel(null);
		panel.add(chartLibPanel, "cell 0 0, grow");
		chartLibPanel.setPreferredSize(new Dimension(600, 270));

	}

	public void updateChart() {
		
		panel.removeAll();
		
		JChartLibDataSet chartLibDataSet = createDataset();
		JChartLibBaseChart baseChart = createChart(chartLibDataSet);
		chartLibPanel = new JChartLibPanel(baseChart);
		panel.add(chartLibPanel, "cell 0 0, grow");
		
		panel.validate();
		panel.repaint();
	}

	public void setStation(Station station, StationData data) {
		this.data = data;

		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());

		updateChart();
	}

}
