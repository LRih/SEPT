package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Model.Favorite;
import Model.States;
import Model.Station;
import Model.StationData;
import Utils.DataManager;

import java.awt.Font;
import java.io.IOException;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StationCell extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Station station = null;
	private StationData data = null;
	
	/**
	 * Create the panel.
	 */
	public StationCell(MainPanel m, Favorite fav, Boolean selected) {

		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				m.setStation(station, data);
			}
		});
		States states = null;
		try {
			states = DataManager.loadStates();
			station = states.get(fav.state).getStation(fav.station);
			data = DataManager.getStationData(station);
			
			if (selected)
				m.setStation(station, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setBorder(null);
		setBackground(new Color(248, 248, 255));
		setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));
		
		WebLabel wblblMildura = new WebLabel();
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblMildura.setText(station.getName());
		add(wblblMildura, "cell 0 0 1 2,alignx left,grow");
		
		WebLabel webLabel_2 = new WebLabel();
		webLabel_2.setForeground(new Color(105, 105, 105));
		webLabel_2.setFont(new Font("Futura", Font.PLAIN, 20));
		webLabel_2.setText(data.getReadings().get(0).getAirTemp().toString());
		add(webLabel_2, "cell 1 0 1 3,alignx center,aligny center");
		
		WebLabel wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 12));
		wblblVictoria.setText(station.getState().getName());
		add(wblblVictoria, "cell 0 2");

	}

}
