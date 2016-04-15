package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.AppState;
import Model.Favorites;
import Model.Station;
import Model.StationData;
import Utils.AppStateManager;
import Utils.FavoritesManager;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StationDetail extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	private WebButton wbtnViewWeatherHistory;
	private WebLabel wblblRemoveFromFavourites;
	private JPanel panel;
	private JPanel panel_1;
	
	/**
	 * Create the panel.
	 */
	public StationDetail(final MainPanel m) {
		setBackground(new Color(176, 196, 222));
		
		setLayout(new MigLayout("", "[30%,grow][grow][30%]", "[grow][][][][][][][][grow][]"));
		
		panel = new JPanel();
		panel.setVisible(false);
		add(panel, "cell 0 0,grow");
		
		wblblMildura = new WebLabel();
		wblblMildura.setText("-");
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));

		add(wblblMildura, "cell 0 1 2 1");

		wblblHumid = new WebLabel();
		wblblHumid.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblHumid.setText("-");
		add(wblblHumid, "cell 2 1,aligny bottom");

		wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("-");
		add(wblblVictoria, "cell 0 2 2 1");

		wblblWindSse = new WebLabel();
		wblblWindSse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblWindSse.setText("-");
		add(wblblWindSse, "cell 2 2");

		wblblRainSinceam = new WebLabel();
		wblblRainSinceam.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblRainSinceam.setText("Rain since 9am: -");
		add(wblblRainSinceam, "cell 2 3");

		wblblc = new WebLabel();
		wblblc.setForeground(new Color(255, 255, 255));
		wblblc.setFont(new Font("Futura", Font.PLAIN, 50));
		wblblc.setText("-°C");
		add(wblblc, "cell 1 3 1 3,alignx left,aligny top");

		wblblPressQmh = new WebLabel();
		wblblPressQmh.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPressQmh.setText("Press QNH hPa: -");
		add(wblblPressQmh, "cell 2 4");

		wblblPress = new WebLabel();
		wblblPress.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPress.setText("Press MSL hPa: -");
		add(wblblPress, "cell 2 5,aligny top");

		wblblAirTemp = new WebLabel();
		wblblAirTemp.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblAirTemp.setText("App temp: -°C");
		add(wblblAirTemp, "cell 1 6,aligny bottom");

		WebButton wbtnViewChart = new WebButton();
		wbtnViewChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(1);
			}
		});
		wbtnViewChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wbtnViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewChart.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewChart.setDrawShade(false);
		wbtnViewChart.setText("View Chart");
		add(wbtnViewChart, "cell 2 6,alignx left,aligny bottom");

		wblblDewPoint = new WebLabel();
		wblblDewPoint.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblDewPoint.setText("Dew Point: -°C");
		add(wblblDewPoint, "cell 1 7");

		 dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");
		
		wbtnViewWeatherHistory = new WebButton();
		wbtnViewWeatherHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(2);
			}
		});
		wbtnViewWeatherHistory.setText("View Weather History");
		wbtnViewWeatherHistory.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wbtnViewWeatherHistory.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewWeatherHistory.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewWeatherHistory.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewWeatherHistory.setDrawShade(false);
		add(wbtnViewWeatherHistory, "cell 2 7");
		
		panel_1 = new JPanel();
		panel_1.setVisible(false);
		add(panel_1, "cell 0 8,grow");

		wblblLastUpdate = new WebLabel();
		wblblLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		wblblLastUpdate.setText("Last update: -");
		add(wblblLastUpdate, "cell 0 9 2 1,aligny bottom");
		

		wblblRemoveFromFavourites = new WebLabel();
		wblblRemoveFromFavourites.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				try {
					Favorites favs = FavoritesManager.load();
					favs.delete(m.station);
					FavoritesManager.save(favs);
					AppState.getInstance().state = "";
					AppState.getInstance().station = "";
					AppStateManager.trySave();
				} catch (IOException e1) {
				}
				m.main.showMainScreen();
			}
		});
		wblblRemoveFromFavourites.setForeground(Color.RED);
		wblblRemoveFromFavourites.setFont(new Font("Century Gothic", Font.ITALIC, 13));
		wblblRemoveFromFavourites.setText("Remove from Favourites");
		
		add(wblblRemoveFromFavourites, "cell 2 9,alignx right,aligny bottom");
	}
	
	public void setStation(Station station, StationData data) {

		if (data.getReadings().get(0).getAirTemp() < 0) {
			setBackground(new Color(176, 196, 222));
			wblblc.setForeground(new Color(255, 255, 255));
		} else if (data.getReadings().get(0).getAirTemp() < 25) {
			setBackground(new Color(240, 248, 255));
			wblblc.setForeground(new Color(30, 144, 255));
		} else {
			setBackground(new Color(255, 248, 220));
			wblblc.setForeground(new Color(255, 99, 71));
		}
		
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
