package View;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.awt.event.ActionEvent;
import com.alee.laf.label.WebLabel;

import Model.AppState;
import Model.Favorites;
import Model.State;
import Model.States;
import Model.Station;
import Utils.AppStateManager;
import Utils.DataManager;
import Utils.FavoritesManager;

import java.awt.Font;
import com.alee.laf.combobox.WebComboBox;
import java.awt.Color;

public class AddStation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;
	private WebComboBox wcbStates;
	private WebComboBox wcbStations;
	private States states;

	/**
	 * Create the panel.
	 */
	public AddStation(Main m) {
		setBackground(Color.WHITE);
		main = m;

		setLayout(new MigLayout("", "[grow][60%][grow]", "[][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showMainScreen();
			}
		});
		wbtnBack.setText("< Back");
		add(wbtnBack, "cell 0 0");

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 0 1 2 1,grow");
		panel.setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow]"));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		panel.add(panel_2, "cell 0 1,grow");

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		panel.add(panel_1, "cell 1 1,grow");
		panel_1.setLayout(new MigLayout("", "[30%][70%,grow]", "[][][][]"));

		WebLabel wblblAddStation = new WebLabel();
		panel_1.add(wblblAddStation, "cell 1 0");
		wblblAddStation.setForeground(new Color(0, 0, 0));
		wblblAddStation.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblAddStation.setText("Find your station");

		WebLabel wblblSelectState = new WebLabel();
		wblblSelectState.setText("Select State");
		panel_1.add(wblblSelectState, "cell 0 1,alignx trailing");

		wcbStates = new WebComboBox();
		wcbStates.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wcbStates.setDrawFocus(false);
		wcbStates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadStationsByState(wcbStates.getSelectedItem().toString());
			}
		});
		panel_1.add(wcbStates, "cell 1 1,growx");

		WebLabel wblblSelectStation = new WebLabel();
		wblblSelectStation.setText("Select Station");
		panel_1.add(wblblSelectStation, "cell 0 2,alignx trailing");

		wcbStations = new WebComboBox();
		wcbStations.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wcbStations.setDrawFocus(false);
		panel_1.add(wcbStations, "cell 1 2,growx");

		WebButton wbtnAddToMy = new WebButton();
		wbtnAddToMy.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnAddToMy.setForeground(new Color(0, 128, 0));
		wbtnAddToMy.setDefaultButtonShadeColor(new Color(240, 255, 240));
		wbtnAddToMy.setBottomSelectedBgColor(new Color(154, 205, 50));
		wbtnAddToMy.setBottomBgColor(new Color(240, 255, 240));
		wbtnAddToMy.setDrawShade(false);
		wbtnAddToMy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Station station = states.get(wcbStates.getSelectedItem().toString()).getStation(wcbStations.getSelectedItem().toString());
				
				Favorites favs;
				try {
					favs = FavoritesManager.load();
		            favs.add(station);
		            FavoritesManager.save(favs);
		            
		            AppState.getInstance().state = station.getState().getName();
					AppState.getInstance().station = station.getName();
					AppStateManager.trySave();
				} catch (IOException e1) {
					// TODO: what to do?
				}
				
				main.showMainScreen();
			}
		});
		wbtnAddToMy.setText("Add to My Favorites");
		panel_1.add(wbtnAddToMy, "cell 1 3");

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		panel.add(panel_3, "cell 2 1,grow");

		loadData();
	}

	private void loadData() {
		loadStates();
		if (wcbStates.getItemCount() > 0)
			wcbStates.setSelectedIndex(0);
	}

	private void loadStationsByState(String name) {
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		Iterator<Station> stations = states.get(name).iterator();
		while (stations.hasNext())
			model.addElement(stations.next().getName());
		 wcbStations.setModel(model);
	}

	private void loadStates() {
		try {
			states = DataManager.loadStates();
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
			for (State state : states) {

				model.addElement(state.getName());
			}
			 wcbStates.setModel(model);
		} catch (IOException e) {
			// TODO what to do?
		}

	}

}
