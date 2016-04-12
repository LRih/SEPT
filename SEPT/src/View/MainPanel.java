package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;

import Model.Favorite;
import Model.Favorites;
import Utils.FavoritesManager;
import Utils.JavaUtils;

import com.alee.laf.button.WebButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.ScrollPaneConstants;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;

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
			int row = 0;
			int col = 0;
			StationCell cell = null;
			for (Favorite fav : favs) {

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

		WebLabel wblblMildura = new WebLabel();
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));
		wblblMildura.setText("Mildura");
		panel_1.add(wblblMildura, "cell 0 0 2 1");

		WebLabel wblblHumid = new WebLabel();
		wblblHumid.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblHumid.setText("Humid: 79%");
		panel_1.add(wblblHumid, "cell 2 0,aligny bottom");

		WebLabel wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("Victoria");
		panel_1.add(wblblVictoria, "cell 0 1 2 1");

		WebLabel wblblWindSse = new WebLabel();
		wblblWindSse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblWindSse.setText("Wind: SSE, 11-17 km/h");
		panel_1.add(wblblWindSse, "cell 2 1");

		WebLabel wblblRainSinceam = new WebLabel();
		wblblRainSinceam.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblRainSinceam.setText("Rain since 9am: 0.2mm");
		panel_1.add(wblblRainSinceam, "cell 2 2");

		WebLabel wblblc = new WebLabel();
		wblblc.setForeground(new Color(60, 179, 113));
		wblblc.setFont(new Font("Futura", Font.PLAIN, 50));
		wblblc.setText("10.9°C");
		panel_1.add(wblblc, "cell 1 2 1 3,alignx left,aligny top");

		WebLabel wblblPressQmh = new WebLabel();
		wblblPressQmh.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPressQmh.setText("Press QNH hPa: 1025.2");
		panel_1.add(wblblPressQmh, "cell 2 3");

		WebLabel wblblPress = new WebLabel();
		wblblPress.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPress.setText("Press MSL hPa: 1025.2");
		panel_1.add(wblblPress, "cell 2 4,aligny top");

		WebLabel wblblAirTemp = new WebLabel();
		wblblAirTemp.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblAirTemp.setText("App temp: 12.6°C");
		panel_1.add(wblblAirTemp, "cell 1 5,aligny bottom");
		
		WebButton wbtnViewChart = new WebButton();
		wbtnViewChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wbtnViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewChart.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewChart.setDrawShade(false);
		wbtnViewChart.setText("View Chart");
		panel_1.add(wbtnViewChart, "cell 2 5,alignx left,aligny bottom");

		WebLabel wblblDewPoint = new WebLabel();
		wblblDewPoint.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblDewPoint.setText("Dew Point: 10.3°C");
		panel_1.add(wblblDewPoint, "cell 1 6");

		WebLabel wblblLastUpdate = new WebLabel();
		wblblLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		wblblLastUpdate.setText("Last update: 12/04/2016 09:00");
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

}
