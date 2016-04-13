package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Model.Favorite;
import Model.States;
import Model.Station;
import Model.StationData;
import Utils.DataManager;
import Utils.StationDataWorker;
import Utils.StationDataWorker.OnTaskCompleteListener;

import java.awt.Font;
import java.io.IOException;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StationCell extends JPanel implements OnTaskCompleteListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Station station = null;
	private StationData data = null;
	private Boolean selected = false;
	private MainPanel main = null;
	private WebLabel wblblMildura;
	private WebLabel webLabel_2;
	private WebLabel wblblVictoria;
	
	
	/**
	 * Create the panel.
	 */
	public StationCell(final MainPanel m, Favorite fav, Boolean selected) {
		this.main = m;
		this.selected = selected;
		
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
			
			StationDataWorker dataWorker = new StationDataWorker(station);
			dataWorker.setOnTaskCompleteListener(this);
			dataWorker.execute();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setBorder(null);
		setBackground(new Color(248, 248, 255));
		setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));
		
		 wblblMildura = new WebLabel();
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblMildura.setText("-");
		add(wblblMildura, "cell 0 0 1 2,alignx left,grow");
		
		 webLabel_2 = new WebLabel();
		webLabel_2.setForeground(new Color(105, 105, 105));
		webLabel_2.setFont(new Font("Futura", Font.PLAIN, 20));
		webLabel_2.setText("-");
		add(webLabel_2, "cell 1 0 1 3,alignx center,aligny center");
		
		 wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 12));
		wblblVictoria.setText("-");
		add(wblblVictoria, "cell 0 2");

	}

	@Override
	public void onSuccess(StationData data) {

		if (selected)
			main.setStation(station, data);

		wblblMildura.setText(station.getName());
		webLabel_2.setText(data.getReadings().get(0).getAirTemp().toString());
		wblblVictoria.setText(station.getState().getName());
		
	}

	@Override
	public void onFail() {
		// TODO Auto-generated method stub
		
	}

}
