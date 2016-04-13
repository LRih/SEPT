package View;

import javax.swing.JPanel;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;

import Model.Favorite;
import Model.Favorites;
import Model.States;
import Model.Station;
import Model.StationData;
import Utils.DataManager;
import Utils.FavoritesManager;

import com.alee.laf.button.WebButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;
	private Station station = null;
	private StationData data = null;
	private WebLabel wblblMildura;
	private WebLabel wblblHumid;
	private WebLabel wblblVictoria;
	private WebLabel wblblWindSse;
	private WebLabel wblblRainSinceam;
	private WebLabel wblblc;
	private WebLabel wblblPressQmh;
	private WebLabel wblblPress;
	private WebLabel wblblAirTemp;
	private WebLabel wblblDewPoint;
	private WebLabel wblblLastUpdate;
	private DateTimeFormatter dtfOut;

	/**
	 * Create the panel.
	 */
	public MainPanel(Main m) {
		setBackground(new Color(255, 255, 255));
		main = m;

		setLayout(new MigLayout("ins 0 0 0 0", "[grow][20%]", "[][grow][][160]"));

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new MigLayout("ins 4 0 0 0", "[grow][grow]", ""));

		try {

			Favorites favs = FavoritesManager.load();
			States states = DataManager.loadStates();
			int row = 0;
			int col = 0;
			StationCell cell = null;
			for (Favorite fav : favs) {

				if (row == 0 && col == 0)
					station = states.get(fav.state).getStation(fav.station);

				cell = new StationCell(this, fav);
				if (col % 2 == 0)
					panel.add(cell, "cell 0 " + row + ", grow, gap 4");
				else
					panel.add(cell, "cell 1 " + (row++) + ", grow, gap 0 4");

				col++;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(240, 248, 255));
		add(panel_1, "cell 0 1 2 1,grow");
		panel_1.setLayout(new MigLayout("", "[30%][grow][30%]", "[][][][][][][][grow]"));

		wblblMildura = new WebLabel();
		wblblMildura.setText("-");
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));

		panel_1.add(wblblMildura, "cell 0 0 2 1");

		wblblHumid = new WebLabel();
		wblblHumid.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblHumid.setText("-");
		panel_1.add(wblblHumid, "cell 2 0,aligny bottom");

		wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("-");
		panel_1.add(wblblVictoria, "cell 0 1 2 1");

		wblblWindSse = new WebLabel();
		wblblWindSse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblWindSse.setText("-");
		panel_1.add(wblblWindSse, "cell 2 1");

		wblblRainSinceam = new WebLabel();
		wblblRainSinceam.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblRainSinceam.setText("Rain since 9am: -");
		panel_1.add(wblblRainSinceam, "cell 2 2");

		wblblc = new WebLabel();
		wblblc.setForeground(new Color(60, 179, 113));
		wblblc.setFont(new Font("Futura", Font.PLAIN, 50));
		wblblc.setText("-°C");
		panel_1.add(wblblc, "cell 1 2 1 3,alignx left,aligny top");

		wblblPressQmh = new WebLabel();
		wblblPressQmh.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPressQmh.setText("Press QNH hPa: -");
		panel_1.add(wblblPressQmh, "cell 2 3");

		wblblPress = new WebLabel();
		wblblPress.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPress.setText("Press MSL hPa: -");
		panel_1.add(wblblPress, "cell 2 4,aligny top");

		wblblAirTemp = new WebLabel();
		wblblAirTemp.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblAirTemp.setText("App temp: -°C");
		panel_1.add(wblblAirTemp, "cell 1 5,aligny bottom");

		WebButton wbtnViewChart = new WebButton();
		wbtnViewChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wbtnViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewChart.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewChart.setDrawShade(false);
		wbtnViewChart.setText("View Chart");
		panel_1.add(wbtnViewChart, "cell 2 5,alignx left,aligny bottom");

		wblblDewPoint = new WebLabel();
		wblblDewPoint.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblDewPoint.setText("Dew Point: -°C");
		panel_1.add(wblblDewPoint, "cell 1 6");

		 dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

		wblblLastUpdate = new WebLabel();
		wblblLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		wblblLastUpdate.setText("Last update: -");
		panel_1.add(wblblLastUpdate, "cell 0 7 2 1,aligny bottom");

		WebLabel wblblWeatherStations = new WebLabel();
		wblblWeatherStations.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblWeatherStations.setText("Favourite Stations");
		add(wblblWeatherStations, "flowx,cell 0 2,gapx 15,gapy 5");

		WebButton webButton = new WebButton("Add station");
		webButton.setForeground(new Color(0, 100, 0));
		webButton.setDrawShade(false);
		webButton.setDefaultButtonShadeColor(new Color(154, 205, 50));
		webButton.setBottomSelectedBgColor(new Color(50, 205, 50));
		webButton.setBottomBgColor(new Color(240, 255, 240));
		webButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showState(1);
			}
		});
		webButton.setFont(new Font("Bender", Font.PLAIN, 13));
		add(webButton, "cell 1 2,alignx right, gapx 0 15, gapy 5 0");

		WebScrollPane webScrollPane = new WebScrollPane(panel, false, true);
		webScrollPane.setPreferredSize(new Dimension(0, 0));
		add(webScrollPane, "cell 0 3 2 1,grow");
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		data = DataManager.getStationData(station);

		wblblMildura.setText(station.getName());
		wblblHumid.setText("Humid: " + data.getReadings().get(0).getRelativeHumidity() + "%");
		wblblVictoria.setText(station.getState().getName());
		wblblWindSse.setText(
				"Wind: " + data.getReadings().get(0).getWindDir() + " " + data.getReadings().get(0).getWindSpdKmH()
						+ "-" + data.getReadings().get(0).getWindGustKmH() + " km/h");
		wblblRainSinceam.setText("Rain since 9am: " + data.getReadings().get(0).getRainTrace() + "mm");
		wblblc.setText(data.getReadings().get(0).getAirTemp() + "°C");
		wblblPressQmh.setText("Press QNH hPa: " + data.getReadings().get(0).getPressureQNH());
		wblblPress.setText("Press MSL hPa: " + data.getReadings().get(0).getPressureMSL());
		wblblAirTemp.setText("App temp: " + data.getReadings().get(0).getApparentTemp() + "°C");
		wblblDewPoint.setText("Dew Point: " + data.getReadings().get(0).getDewPt() + "°C");
		wblblLastUpdate.setText("Last update: " + dtfOut.print(data.getReadings().get(0).getLocalDateTime()));

	}

}
