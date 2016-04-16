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
import Utils.AppDefine;
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
	private WebButton wbtnBack;
	private WebButton wbtnAddToMy;

	/**
	 * Create the panel.
	 */
	public AddStation(Main m) {
		setBackground(Color.WHITE);
		main = m;

		setLayout(new MigLayout("", "[20%][grow]", "[][grow]"));

		wbtnBack = new WebButton();
		wbtnBack.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnBack.setText("< Back");
		add(wbtnBack, "cell 0 0");

		JPanel pnContent = new JPanel();
		pnContent.setBackground(Color.WHITE);
		add(pnContent, "cell 0 1 2 1,grow");
		pnContent.setLayout(new MigLayout("", "[20%][grow][20%]", "[grow][grow][grow]"));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		pnContent.add(panel_2, "cell 0 1,grow");

		JPanel pnAddStation = new JPanel();
		pnAddStation.setBackground(Color.WHITE);
		pnContent.add(pnAddStation, "cell 1 1,grow");
		pnAddStation.setLayout(new MigLayout("", "[grow][70%,grow]", "[][][][]"));

		WebLabel wblblAddStation = new WebLabel();
		pnAddStation.add(wblblAddStation, "cell 1 0");
		wblblAddStation.setForeground(new Color(0, 0, 0));
		wblblAddStation.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblAddStation.setText("Find your station");

		WebLabel wblblSelectState = new WebLabel();
		wblblSelectState.setText("Select State");
		pnAddStation.add(wblblSelectState, "cell 0 1,alignx trailing");

		wcbStates = new WebComboBox();
		wcbStates.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wcbStates.setDrawFocus(false);
		wcbStates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadStationsByState(wcbStates.getSelectedItem().toString());
			}
		});
		pnAddStation.add(wcbStates, "cell 1 1,growx");

		WebLabel wblblSelectStation = new WebLabel();
		wblblSelectStation.setText("Select Station");
		pnAddStation.add(wblblSelectStation, "cell 0 2,alignx trailing");

		wcbStations = new WebComboBox();
		wcbStations.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wcbStations.setDrawFocus(false);
		pnAddStation.add(wcbStations, "cell 1 2,growx");

		wbtnAddToMy = new WebButton();
		wbtnAddToMy.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnAddToMy.setForeground(new Color(0, 128, 0));
		wbtnAddToMy.setDefaultButtonShadeColor(new Color(240, 255, 240));
		wbtnAddToMy.setBottomSelectedBgColor(new Color(154, 205, 50));
		wbtnAddToMy.setBottomBgColor(new Color(240, 255, 240));
		wbtnAddToMy.setDrawShade(false);
		wbtnAddToMy.setText("Add to My Favorites");
		pnAddStation.add(wbtnAddToMy, "cell 1 3");

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.WHITE);
		pnContent.add(panel_3, "cell 2 1,grow");

		loadData();
		addListeners();
	}

	private void addListeners() {

		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showMainScreen();
			}
		});

		wbtnAddToMy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Station station = states.get(wcbStates.getSelectedItem().toString())
						.getStation(wcbStations.getSelectedItem().toString());
				
				AppDefine.currentStation = station;
				AppDefine.favorites.add(station);

				try {
					FavoritesManager.save(AppDefine.favorites);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				AppState.getInstance().state = station.getState().getName();
				AppState.getInstance().station = station.getName();
				AppStateManager.trySave();

				main.showState(AppDefine.MAIN_SCREEN, this.getClass().getName());
			}
		});

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
