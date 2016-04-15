package View;

import Model.AppState;
import net.miginfocom.swing.MigLayout;
import java.awt.EventQueue;

import javax.swing.*;

import com.alee.laf.WebLookAndFeel;

import Model.Favorites;
import Utils.AppDefine;
import Utils.AppStateManager;
import Utils.FavoritesManager;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.alee.laf.label.WebLabel;
import com.alee.laf.button.WebButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public final class Main {
	private JFrame frmSept;
	Favorites favs;
	private JPanel pnMain;
	private JPanel pnMainBar;
	private WebButton wbtnRefreshData;

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
		frmSept.getContentPane().setLayout(new MigLayout("ins 0, gapy 0", "[grow]", "[60][grow]"));

		// Top Bar
		pnMainBar = new JPanel();
		pnMainBar.setBackground(new Color(169, 169, 169));
		frmSept.getContentPane().add(pnMainBar, "cell 0 0,grow");
		pnMainBar.setLayout(new MigLayout("", "[grow][10%]", "[]"));

		WebLabel wblblBomWeather = new WebLabel();
		wblblBomWeather.setForeground(new Color(255, 255, 255));
		wblblBomWeather.setFont(new Font("Century Gothic", Font.PLAIN, 20));
		wblblBomWeather.setText("BOM Weather");
		pnMainBar.add(wblblBomWeather, "cell 0 0");

		wbtnRefreshData = new WebButton();
		wbtnRefreshData.setForeground(new Color(25, 25, 112));
		wbtnRefreshData.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnRefreshData.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnRefreshData.setBottomBgColor(new Color(240, 255, 255));
		wbtnRefreshData.setDrawShade(false);
		wbtnRefreshData.setText("Refresh Data");
		pnMainBar.add(wbtnRefreshData, "cell 1 0,aligny center, alignx right");

		pnMain = new JPanel();
		frmSept.getContentPane().add(pnMain, "cell 0 1,grow");
		frmSept.setBackground(Color.WHITE);
		frmSept.setTitle("Bom Weather");

		AppStateManager.tryLoad();

		try {
			String[] arr = AppState.getInstance().v4.split(",");
			if (arr.length != 4)
				throw new Exception("Invalid AppState Format.");

			int x = (int) Double.parseDouble(arr[0]);
			int y = (int) Double.parseDouble(arr[1]);
			int width = (int) Double.parseDouble(arr[2]);
			int height = (int) Double.parseDouble(arr[3]);
			frmSept.setBounds(x, y, width, height);
		} catch (Exception e) {
			frmSept.setBounds(100, 100, 1300, 600);
		}

		frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// re-open last AppState or show MainScreen
		if (AppState.getInstance().stateIndex > -1) {
			showState(AppState.getInstance().stateIndex);
		} else
			showMainScreen();
		
		addListeners();
	}
	
	private void addListeners() {
		// on close listener
		frmSept.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				// store Current Window Location and Size
				double x = frmSept.getLocationOnScreen().getX();
				double y = frmSept.getLocationOnScreen().getY();
				double width = frmSept.getSize().getWidth();
				double height = frmSept.getSize().getHeight();

				AppState.getInstance().v4 = x + "," + y + "," + width + "," + height;
				AppStateManager.trySave();

			}
		});

		// refresh button pressed
		wbtnRefreshData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// set Top Bar Background to Grey
				setMainBg(false);
				
				// reload data
				showMainScreen();
			}
		});
	}

	public void showMainScreen() {
		try {
			favs = FavoritesManager.load();
			// if there is no favorited station, show first screen
			if (favs.size() == 0)
				throw new Exception("NO_FAVORITE_STATION");
			else
				showState(AppDefine.MAIN_SCREEN);
		} catch (Exception e) {
			showState(AppDefine.FIRST_SCREEN);
		}
	}

	public void showState(int index) {
		pnMain.removeAll();
		pnMain.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().stateIndex = index;
		AppStateManager.trySave();

		switch (index) {
		// FIRSTSCREEN
		case AppDefine.FIRST_SCREEN:
			FirstScreen firstScreen = new FirstScreen(this);
			pnMainBar.setBackground(new Color(255, 140, 0));
			wbtnRefreshData.setVisible(false);
			pnMain.add(firstScreen, "cell 0 0 31 1,grow");
			break;

		// ADD_STATION
		case AppDefine.ADD_STATION:
			AddStation addStation = new AddStation(this);
			wbtnRefreshData.setVisible(false);
			pnMain.add(addStation, "cell 0 0 1 1,grow");
			break;

		// MAIN_SCREEN
		case AppDefine.MAIN_SCREEN:
			MainPanel mainPanel = new MainPanel(this);
			pnMain.add(mainPanel, "cell 0 0 1 1,grow");
			wbtnRefreshData.setVisible(true);
			mainPanel.showState(AppState.getInstance().v1);

			break;

		}
		pnMain.validate();
		pnMain.repaint();
	}

	public void setMainBg(Boolean hasInternetConnection) {
		if (hasInternetConnection) {
			pnMainBar.setBackground(new Color(255, 140, 0));
		} else {
			pnMainBar.setBackground(new Color(169, 169, 169));
		}
	}

}
