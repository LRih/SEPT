package View;

import Model.AppState;
import net.miginfocom.swing.MigLayout;
import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.*;

import com.alee.extended.progress.WebProgressOverlay;
import com.alee.laf.WebLookAndFeel;

import Model.Favorites;
import Utils.AppStateManager;
import Utils.FavoritesManager;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.alee.laf.label.WebLabel;
import com.alee.laf.button.WebButton;
import java.awt.Font;

public final class Main {
	private JFrame frmSept;
	private Favorites favs;
	private JPanel pnMain;

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
		frmSept.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				AppStateManager.trySave();
			}
		});
		frmSept.getContentPane().setBackground(Color.WHITE);
		frmSept.getContentPane().setLayout(new MigLayout("ins 0, gapy 0", "[grow]", "[60][grow]"));

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 140, 0));
		frmSept.getContentPane().add(panel, "cell 0 0,grow");
		panel.setLayout(new MigLayout("", "[grow][10%]", "[]"));

		WebLabel wblblBomWeather = new WebLabel();
		wblblBomWeather.setBackground(new Color(25, 25, 112));
		wblblBomWeather.setForeground(new Color(255, 255, 255));
		wblblBomWeather.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		wblblBomWeather.setText("BOM Weather");
		panel.add(wblblBomWeather, "cell 0 0");

		WebButton wbtnRefreshData = new WebButton();
		wbtnRefreshData.setForeground(new Color(25, 25, 112));
		wbtnRefreshData.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnRefreshData.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnRefreshData.setBottomBgColor(new Color(240, 255, 255));
		wbtnRefreshData.setDrawShade(false);
		wbtnRefreshData.setText("Refresh Data");
		panel.add(wbtnRefreshData, "cell 1 0,aligny center, alignx right");

		pnMain = new JPanel();
		frmSept.getContentPane().add(pnMain, "cell 0 1,grow");
		frmSept.setBackground(Color.WHITE);
		frmSept.setTitle("Bom Weather");
		frmSept.setBounds(100, 100, 800, 600);
		frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		AppStateManager.tryLoad();
		if (AppState.getInstance().stateIndex > -1) {
			showState(AppState.getInstance().stateIndex);
		} else
			showMainScreen();

	}

	public void showMainScreen() {
		try {
			favs = FavoritesManager.load();

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
		pnMain.removeAll();
		pnMain.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().stateIndex = index;

		switch (index) {
		// FIRSTSCREEN
		case 0:
			FirstScreen firstScreen = new FirstScreen(this);
			pnMain.add(firstScreen, "cell 0 0 31 1,grow");
			break;

		// ADD_STATION
		case 1:
			AddStation addStation = new AddStation(this);
			pnMain.add(addStation, "cell 0 0 1 1,grow");
			break;

		// MAIN_SCREEN
		case 2:
			MainPanel mainPanel = new MainPanel(this);
			pnMain.add(mainPanel, "cell 0 0 1 1,grow");

			mainPanel.showState(AppState.getInstance().v1);

			break;

		}
		pnMain.validate();
		pnMain.repaint();
	}

}
