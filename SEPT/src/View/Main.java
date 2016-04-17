package View;

import Model.AppState;
import net.miginfocom.swing.MigLayout;
import java.awt.EventQueue;

import javax.swing.*;

import com.alee.laf.WebLookAndFeel;

import Utils.AppStateManager;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import com.alee.laf.label.WebLabel;
import com.alee.laf.button.WebButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * App Main
 */
public final class Main {
	private JFrame frmMain;
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
					window.frmMain.setVisible(true);
				} catch (Exception e) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {

		AppDefine.initApp(frmMain);
		initialize();
	}

	private void initialize() {
		frmMain = new JFrame();
		frmMain.getContentPane().setBackground(Color.WHITE);
		frmMain.getContentPane().setLayout(new MigLayout("ins 0, gapy 0", "[grow]", "[60][grow]"));

		// Top Bar
		pnMainBar = new JPanel();
		pnMainBar.setBackground(new Color(169, 169, 169));
		frmMain.getContentPane().add(pnMainBar, "cell 0 0,grow");
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
		frmMain.getContentPane().add(pnMain, "cell 0 1,grow");
		frmMain.setBackground(Color.WHITE);
		frmMain.setTitle("Bom Weather");

		AppStateManager.tryLoad();
		frmMain.setBounds(AppState.getInstance().getWindowRect());

		frmMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		showPreviousSession();
		addListeners();
	}

	private void addListeners() {
		// on close listener
		frmMain.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// store Current Window Location and Size
				AppState.getInstance().setWindowRect(frmMain.getLocationOnScreen(), frmMain.getSize());
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

    public void showPreviousSession()
    {
        // try restore previous session state
        if (AppState.getInstance().shownWindow == AppDefine.ADD_STATION_PANEL) {
            showState(AppDefine.ADD_STATION_PANEL, this.getClass().getName());
        } else {
            showMainScreen();
        }
    }

	/**
	 * Show appropriate main screen
	 */
	public void showMainScreen() {
        // if no favorites, show first screen
        showState(AppDefine.favorites.size() == 0 ? AppDefine.FIRST_RUN_PANEL : AppDefine.MAIN_PANEL, this.getClass().getName());
	}

	/**
	 * Change App State
	 */
	public void showState(int index, String from) {

		if (AppDefine.DEBUGGING)
			System.out.println("Class " + from + ": " + this.getClass().getName() + ".showState(" + index + ");");

		pnMain.removeAll();
		pnMain.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		AppState.getInstance().shownWindow = index;

		switch (index) {
            // FIRSTSCREEN
            case AppDefine.FIRST_RUN_PANEL:
                FirstRunPanel firstRunPanel = new FirstRunPanel(this);
                pnMainBar.setBackground(new Color(255, 140, 0));
                wbtnRefreshData.setVisible(false);
                pnMain.add(firstRunPanel, "cell 0 0 31 1,grow");
                break;

            // ADD_STATION
            case AppDefine.ADD_STATION_PANEL:
                AddStationPanel addStationPanel = new AddStationPanel(this);
                pnMainBar.setBackground(new Color(255, 140, 0));
                wbtnRefreshData.setVisible(false);
                pnMain.add(addStationPanel, "cell 0 0 1 1,grow");
                break;

            // MAIN_SCREEN
            case AppDefine.MAIN_PANEL:
                MainPanel mainPanel = new MainPanel(this);
                pnMain.add(mainPanel, "cell 0 0 1 1,grow");
                wbtnRefreshData.setVisible(true);
                mainPanel.showState(AppState.getInstance().shownDetail, this.getClass().getName());

                break;

		}
		pnMain.validate();
		pnMain.repaint();
	}

	/**
	 * Change Background when user has Internet/No Internet
	 */
	public void setMainBg(Boolean hasInternetConnection) {
		if (hasInternetConnection) {
			pnMainBar.setBackground(new Color(255, 140, 0));
		} else {
			pnMainBar.setBackground(new Color(169, 169, 169));
		}
	}

}
