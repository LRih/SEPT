package View;

import javax.swing.JPanel;

import Model.AppState;
import Model.HistoricalReading;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;

import Model.Station;

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
public final class StationChart extends JPanel {

	private static final long serialVersionUID = 1L;
	private WebLabel wblblStation;
	private WebLabel wblblState;
	private JChartLibPanel chartLibPanel;
	private JPanel panel;
	private WebComboBox wcbChartType;
	private WebLabel wblblSelectChart;
	private MainPanel mainPanel;

	private JChartLibDataSet createDataset(int serieType) {

		final JChartLibDataSet dataset = new JChartLibDataSet();

		if (AppDefine.currentStationData == null) {
			int[] tempData = {};
			dataset.addDataSerie("N/A", tempData);
			return dataset;
		}

		JChartLibSerie serie = null;

		switch (serieType) {
		case AppDefine.CHART_9AM:
			serie = new JChartLibSerie("Daily 9am");
			for (HistoricalReading reading : AppDefine.currentStationData.getHistoricalReadings()) {
				if (reading.temp9AM != null) {
					serie.addValue(reading.date.toDate(), reading.temp9AM);
				}
			}
			break;
		case AppDefine.CHART_3PM:
			serie = new JChartLibSerie("Daily 3pm");
			for (HistoricalReading reading : AppDefine.currentStationData.getHistoricalReadings()) {
				if (reading.temp3PM != null) {
					serie.addValue(reading.date.toDate(), reading.temp3PM);
				}
			}
			break;
		case AppDefine.CHART_MAX:
			serie = new JChartLibSerie("Daily Max");
			for (HistoricalReading reading : AppDefine.currentStationData.getHistoricalReadings()) {
				if (reading.max != null) {
					serie.addValue(reading.date.toDate(), reading.max);
				}
			}
			break;
		case AppDefine.CHART_MIN:
			serie = new JChartLibSerie("Daily Min");
			for (HistoricalReading reading : AppDefine.currentStationData.getHistoricalReadings()) {
				if (reading.min != null) {
					serie.addValue(reading.date.toDate(), reading.min);
				}
			}
			break;
		}

		dataset.addDataSerie(serie);

		return dataset;
	}

	private JChartLibBaseChart createChart(final JChartLibDataSet dataset) {

		// create the chart with title and axis names
		final JChartLibLineChart chart = new JChartLibLineChart("", // chart
																	// title
				"day", // x axis text
				"temperature (°C)", // y axis text
				dataset);

		return chart;
	}

	/**
	 * Create the panel.
	 */
	public StationChart(final MainPanel m) {

		mainPanel = m;

		setBackground(new Color(240, 248, 255));
		setLayout(new MigLayout("", "[10%][][][grow]", "[][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnBack.setDrawShade(false);
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(AppDefine.STATION_DETAIL, this.getClass().getName());
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

		wblblSelectChart = new WebLabel();
		wblblSelectChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblSelectChart.setText("Select Chart");
		add(wblblSelectChart, "flowx,cell 3 0,alignx right,aligny center");

		wcbChartType = new WebComboBox();
		wcbChartType.setDrawFocus(false);
		wcbChartType.setFont(new Font("Bender", Font.PLAIN, 13));
		wcbChartType.setModel(
				new DefaultComboBoxModel(new String[] { "Daily 9AM", "Daily 3PM", "Daily Max", "Daily Min" }));
		add(wcbChartType, "cell 3 0,alignx right");

        if (AppState.getInstance().chartIndex >= AppDefine.CHART_9AM && AppState.getInstance().chartIndex <= AppDefine.CHART_MIN)
            wcbChartType.setSelectedIndex(AppState.getInstance().chartIndex);

		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 0 1 4 1,grow");
		panel.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		chartLibPanel = new JChartLibPanel(null);
		panel.add(chartLibPanel, "cell 0 0, grow");
		chartLibPanel.setPreferredSize(new Dimension(600, 270));

		wcbChartType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateChart(wcbChartType.getSelectedIndex());
			}
		});
	}

	private void updateChart(int type) {

        AppState.getInstance().chartIndex = type;

		panel.removeAll();

		JChartLibDataSet chartLibDataSet = createDataset(type);
		JChartLibBaseChart baseChart = createChart(chartLibDataSet);
		chartLibPanel = new JChartLibPanel(baseChart);
		panel.add(chartLibPanel, "cell 0 0, grow");

		panel.validate();
		panel.repaint();

		if (chartLibDataSet.getSeries().get(0).getValues().size() == 0)
			WebOptionPane.showMessageDialog(mainPanel,
                "No data found for " + chartLibDataSet.getSeries().get(0).getTitle(), "Chart",
                WebOptionPane.ERROR_MESSAGE);

	}
	/**
	 * Set station data for this panel
	 */
	public void setStation(Station station) {

		wblblStation.setText(station.getName());
		wblblState.setText(station.getState().getName());

		updateChart(wcbChartType.getSelectedIndex());
	}

}
