package View;

import net.miginfocom.swing.MigLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.*;
import com.alee.laf.WebLookAndFeel;

import Model.Favorites;
import Utils.AppStateManager;
import Utils.FavoritesManager;
import java.awt.Color;

public final class Main {
	private JFrame frmSept;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public final void run() {
				try {
					WebLookAndFeel.install();
					Main window = new Main();
					window.frmSept.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSept = new JFrame();
		frmSept.getContentPane().setBackground(Color.WHITE);
		frmSept.setBackground(Color.WHITE);
		frmSept.setTitle("Bom Weather");
		frmSept.setBounds(100, 100, 800, 500);
		frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		showMainScreen();

		// WeatherStations panel = new WeatherStations();
		// frmSept.getContentPane().add(panel, "cell 0 1 3 1,grow");
	}
	
	public void showMainScreen() {
		try {
			Favorites favs = FavoritesManager.load();
			if (favs.size() == 0)
				showState(0);
			else
				showState(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showState(int index) {
		frmSept.getContentPane().removeAll();
		frmSept.getContentPane().setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));
		
		AppStateManager.CURRENT_STATE = index;
		
		switch (index) {
		// FIRSTSCREEN
		case 0:
			FirstScreen firstScreen = new FirstScreen(this);
			frmSept.setTitle("Welcome to BOM Weather!");
			frmSept.getContentPane().add(firstScreen, "cell 0 0 1 1,grow");
			break;

		// ADD_STATION
		case 1:
			AddStation addStation = new AddStation(this);
			frmSept.setTitle("Bom Weather - Add Station");
			frmSept.getContentPane().add(addStation, "cell 0 0 1 1,grow");
			break;
			
		// MAIN_SCREEN
		case 2:
			MainPanel mainPanel = new MainPanel(this);
			frmSept.setTitle("Bom Weather - Weather Stations");
			frmSept.getContentPane().add(mainPanel, "cell 0 0 1 1,grow");
			break;

		}
		frmSept.getContentPane().validate();
		frmSept.getContentPane().repaint();
	}

}
