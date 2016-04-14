package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Model.AppState;
import Model.Favorite;
import Model.Favorites;
import Model.States;
import Model.Station;
import Model.StationData;
import Utils.DataManager;
import Utils.FavoritesManager;
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
	public StationCell(final MainPanel m, final Favorite fav, Boolean selected) {
		this.main = m;
		this.selected = selected;

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				m.setStation(station, data);
				AppState.getInstance().v2 = fav.state;
				AppState.getInstance().v3 = fav.station;
			}
		});
		States states = null;
		try {
			states = DataManager.loadStates();
			station = states.get(fav.state).getStation(fav.station);
		} catch (IOException e) {
		}

		StationDataWorker dataWorker = new StationDataWorker(station);
		dataWorker.setOnTaskCompleteListener(this);
		dataWorker.execute();

		setBorder(null);
		setBackground(new Color(248, 248, 255));
		setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));

		wblblMildura = new WebLabel();
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblMildura.setText(fav.station);
		add(wblblMildura, "cell 0 0 1 2,alignx left,grow");

		webLabel_2 = new WebLabel();
		webLabel_2.setForeground(new Color(105, 105, 105));
		webLabel_2.setFont(new Font("Futura", Font.PLAIN, 20));
		webLabel_2.setText("-");
		add(webLabel_2, "cell 1 0 1 3,alignx center,aligny center");

		wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 12));
		wblblVictoria.setText(fav.state);
		add(wblblVictoria, "cell 0 2");

	}

	@Override
	public void onSuccess(StationData data) {

		this.data = data;
		if (selected) {
//			setBackground(new Color(230, 230, 250));
			main.setStation(station, data);
		}

		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());
		webLabel_2.setText(data.getReadings().get(0).getAirTemp().toString());

	}
	
	public void setSelected(Boolean selected) {
		if (selected)
			setBackground(new Color(230, 230, 250));
		else
			setBackground(new Color(248, 248, 255));
	}

	@Override
	public void onFail() {
		// TODO don't delete favourite here. It could fail when no network connection and not cached.
		try {
			Favorites favs = FavoritesManager.load();
			favs.delete(station);
			 FavoritesManager.save(favs);
		} catch (IOException e) {
		}
		main.showState(0);

	}

}
