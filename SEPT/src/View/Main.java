package View;

import Model.*;
import Data.DataManager;
import Utils.FavoritesManager;
import Utils.Log;
import Utils.SwingUtils;

import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.notification.NotificationIcon;
import com.alee.managers.notification.NotificationManager;
import com.alee.managers.notification.WebNotification;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

import com.alee.laf.WebLookAndFeel;

import Utils.AppStateManager;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import com.alee.laf.label.WebLabel;
import com.alee.laf.button.WebButton;

import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.awt.Dimension;
import com.alee.extended.image.WebImage;

/**
 * App Main
 */
public final class Main
		implements AddFavoritePanel.OnAddFavoriteClickListener, StationDetail.OnRemoveFavoriteClickListener,
		FavoriteCell.OnStationSelectListener, FavoriteCell.OnDataLoadListener {
	private static final int NOTIFICATION_CLOSE_TIME_MILLIS = 5000;

	private JFrame frmMain;
	private JPanel pnMainBar;
	private JPanel pnContent;
	private WebButton wbtnRefreshData;

	private FirstRunPanel pnFirstRun;
	private AddFavoritePanel pnAddFavorite;
	private MainPanel pnMain;

	private States states;
	private Favorites favorites;

	private Station selectedStation;
	private StationData selectedStationData;
	private boolean notificationShown;
	private WebImage imageLoading;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		WebLookAndFeel.install();
		new Main().frmMain.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	private Main() {

		// 100, 100, 1300, 600

		// try to initialize logging
		if (!Log.initializeLoggers()) {
			WebOptionPane.showMessageDialog(frmMain, "Couldn't start loggin service.", "ERROR",
					WebOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		initializeData();
		initializeUI();

	}

	private void initializeData() {
		// load session state
		AppStateManager.tryLoad();

		try {
			states = DataManager.loadStates();
		} catch (Exception e) {
			WebOptionPane.showMessageDialog(frmMain, "\"stations.json\" missing or invalid, closing.", "ERROR",
					WebOptionPane.ERROR_MESSAGE);
			Log.severe(DataManager.class, "\"stations.json\" missing or invalid");
			System.exit(0);
		}

		// load favorites and delete invalid entries
		try {
			favorites = FavoritesManager.load();
			FavoritesManager.removeInvalidFavourites(favorites, states);
			FavoritesManager.save(favorites);
		} catch (IOException e) {
			Log.severe(FavoritesManager.class, "Error loading favorites");
		}

		// check for invalid station and state from previous session
		AppState as = AppState.getInstance();
		boolean isValid = states.get(as.state) != null && states.get(as.state).getStation(as.station) != null;
		if (isValid && favorites.exists(as.state, as.station)) {
			selectedStation = states.get(as.state).getStation(as.station);
			selectedStationData = DataManager.getCachedStationData(selectedStation);
		}
	}

	private void initializeUI() {
		
		// flash screen
		FrameLoading frameLoading = new FrameLoading();
		
		Rectangle screen = AppState.getInstance().getWindowRect();
		int flash_screen_width = 450;
		int flash_screen_height = 100;
		int screen_x = screen.x + (screen.width/2 - flash_screen_width/2) ;
		int screen_y = screen.y + (screen.height/2 - flash_screen_height/2);
		frameLoading.setBounds(screen_x, screen_y, 450, 100);
		frameLoading.setVisible(true);
		
		// main frame
		frmMain = new JFrame();
		frmMain.setMinimumSize(new Dimension(1000, 860));
		frmMain.setBackground(Color.WHITE);
		frmMain.setTitle("Bom Weather");
		frmMain.setBounds(AppState.getInstance().getWindowRect());

		frmMain.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frmMain.getContentPane().setLayout(new MigLayout("ins 0, gapy 0", "[grow]", "[60][grow]"));

		// Top Bar
		pnMainBar = new JPanel();
		pnMainBar.setBackground(Style.INTERNET_OFF_BACKGROUND);
		frmMain.getContentPane().add(pnMainBar, "cell 0 0,grow");
		pnMainBar.setLayout(new MigLayout("", "[grow][10%]", "[]"));

		Image img = SwingUtils.createImage("/Images/logo_small.png");
		imageLoading = new WebImage(img);
		imageLoading.setSize(62, 50);
		pnMainBar.add(imageLoading, "flowx,cell 0 0");

		WebLabel wblblBomWeather = new WebLabel();
		wblblBomWeather.setForeground(new Color(255, 255, 255));
		wblblBomWeather.setFont(Style.FONT_20);
		wblblBomWeather.setText("BOM Weather");
		pnMainBar.add(wblblBomWeather, "flowx,cell 0 0");

		wbtnRefreshData = new WebButton();
		wbtnRefreshData.setForeground(new Color(25, 25, 112));
		wbtnRefreshData.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnRefreshData.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnRefreshData.setBottomBgColor(new Color(240, 255, 255));
		wbtnRefreshData.setDrawShade(false);
		wbtnRefreshData.setFont(Style.FONT_BENDER_13);
		wbtnRefreshData.setText("Refresh Data");
		pnMainBar.add(wbtnRefreshData, "cell 1 0,aligny center, alignx right");

		pnContent = new JPanel();
		frmMain.getContentPane().add(pnContent, "cell 0 1,grow");

		pnFirstRun = new FirstRunPanel();
		pnAddFavorite = new AddFavoritePanel(states);
		pnMain = new MainPanel();

		// set initial data
		pnMain.setStation(selectedStation, selectedStationData);

		showPreviousSession();
		addListeners();
		
		frameLoading.setVisible(false);

	}

	private void addListeners() {
		// on close listener
		frmMain.addWindowListener(new WindowAdapter() {
			public final void windowClosing(WindowEvent e) {
				// store Current Window Location and Size
				AppState.getInstance().setWindowRect(frmMain.getLocationOnScreen(), frmMain.getSize());
				AppStateManager.trySave();

			}
		});

		// refresh button pressed
		wbtnRefreshData.addActionListener(new ActionListener() {
			public final void actionPerformed(ActionEvent e) {
				Log.info(getClass(), "Refresh started");

				notificationShown = false;

				// set Top Bar Background to Grey
				updateBackgroundColor(false);

				setBlockUI(true);

				// reload data
				showMainScreen();
			}
		});

		pnFirstRun.setOnAddListener(new FirstRunPanel.OnAddListener() {
			public final void onAddClick() {
				showPanel(PanelType.AddFavorite);
			}
		});

		pnAddFavorite.setOnBackClickListener(new OnBackClickListener() {
			public final void onBackClick() {
				showMainScreen();
			}
		});
		pnAddFavorite.setOnAddFavoriteClickListener(this);

		pnMain.setOnActionListener(new MainPanel.OnActionListener() {
			public final void onAddClick() {
				showPanel(PanelType.AddFavorite);
			}
		});
		pnMain.setOnRemoveFromFavoritesClickListener(this);
		pnMain.setOnStationSelectListener(this);
		pnMain.setOnDataLoadListener(this);
	}

	private void showPreviousSession() {
		// try restore previous session state
		if (AppState.getInstance().shownWindow == PanelType.AddFavorite.ordinal())
			showPanel(PanelType.AddFavorite);
		else
			showMainScreen();
	}

	/**
	 * Show appropriate main screen
	 */
	private void showMainScreen() {
		// if no favorites, show first screen
		showPanel(favorites.size() == 0 ? PanelType.FirstRun : PanelType.Main);
	}

	/**
	 * Change panel shown.
	 */
	private void showPanel(PanelType type) {
		AppState.getInstance().shownWindow = type.ordinal();
		Log.info(getClass(), "Panel changed to " + type.name());

		pnContent.removeAll();
		pnContent.setLayout(new MigLayout("ins 0", "[grow]", "[grow]"));

		switch (type) {
		case FirstRun:
			showFirstRunPanel();
			break;
		case AddFavorite:
			showAddFavoritePanel();
			break;
		case Main:
			showMainPanel();
			break;
		}

		pnContent.validate();
		pnContent.repaint();
	}

	private void showFirstRunPanel() {
		pnMainBar.setBackground(Style.INTERNET_ON_BACKGROUND);
		wbtnRefreshData.setVisible(false);
		pnContent.add(pnFirstRun, "cell 0 0 31 1,grow");
	}

	private void showAddFavoritePanel() {
		// pnMainBar.setBackground(Style.INTERNET_ON_BACKGROUND);
		wbtnRefreshData.setVisible(false);
		pnContent.add(pnAddFavorite, "cell 0 0 1 1,grow");
	}

	private void showMainPanel() {
		pnContent.add(pnMain, "cell 0 0 1 1,grow");
		wbtnRefreshData.setVisible(true);

		// always show detail if no station selected
		if (selectedStation != null)
			pnMain.showPanel(AppState.getInstance().getShownDetail());
		else
			pnMain.showPanel(MainPanel.PanelType.Detail);

		pnMain.setFavorites(favorites, states, selectedStation);
	}

	/**
	 * Show error notification when one of the station loaded failed.
	 */
	private void showNoInternetNotification() {
		if (!notificationShown) {
			notificationShown = true;
			WebNotification notificationPopup = new WebNotification();
			notificationPopup.setIcon(NotificationIcon.error);
			notificationPopup.setDisplayTime(NOTIFICATION_CLOSE_TIME_MILLIS);
			notificationPopup.setContent("No Internet Connection");

			NotificationManager.showNotification(notificationPopup);
		}
	}

	/**
	 * Change Background when user has Internet/No Internet
	 */
	private void updateBackgroundColor(boolean hasInternetConnection) {
		pnMain.updateBackgroundColor(hasInternetConnection);
		if (hasInternetConnection)
			pnMainBar.setBackground(Style.INTERNET_ON_BACKGROUND);
		else
			pnMainBar.setBackground(Style.INTERNET_OFF_BACKGROUND);
	}

	private void setStation(Station station, StationData data) {
		selectedStation = station;

		// try use cached data when data is null
		if (station != null && data == null)
			selectedStationData = DataManager.getCachedStationData(station);
		else
			selectedStationData = data;

		// store selected station state
		if (station == null) {
			AppState.getInstance().state = "";
			AppState.getInstance().station = "";

			Log.info(getClass(), "Station deselected");
		} else {
			AppState.getInstance().state = station.getState().getName();
			AppState.getInstance().station = station.getName();

			Log.info(getClass(),
					"Station selected: " + station.getName() + (data == null ? " (no data)" : " (data available)"));
		}

		// update main panel
		pnMain.setStation(station, data);
	}

	/* Event callbacks */
	public final void onAddFavoriteClick(Station station) {
		// when favorite already exists
		if (!favorites.add(station)) {
			WebOptionPane.showMessageDialog(frmMain, "You already have this station in your Favorites.", "",
					WebOptionPane.WARNING_MESSAGE);
			return;
		}

		// failed to save favorites
		try {
			FavoritesManager.save(favorites);

			Log.info(FavoritesManager.class, "Favorite added: " + station.getName());
		} catch (IOException e) {
			WebOptionPane.showMessageDialog(frmMain, "Failed to save favorites.", "", WebOptionPane.WARNING_MESSAGE);
			return;
		}

		setStation(station, null);
		showMainScreen();
	}

	public final void onRemoveFavoriteClick(Station station) {
		favorites.delete(station);

		// failed to save favorites
		try {
			FavoritesManager.save(favorites);

			Log.info(FavoritesManager.class, "Favorite removed: " + station.getName());
		} catch (IOException e) {
			WebOptionPane.showMessageDialog(frmMain, "Failed to save favorites.", "", WebOptionPane.WARNING_MESSAGE);
			return;
		}

		setStation(null, null);
		showMainScreen();
	}

	public final void onStationSelect(Station station, StationData data) {
		setStation(station, data);
	}

	private void setBlockUI(boolean isBlocked) {
		pnMain.setBlockUI(isBlocked);
		wbtnRefreshData.setEnabled(!isBlocked);
	}

	public final void onDataLoadSuccess(Station station, StationData data) {
		// update main panel data selected station matches downloaded data
		if (station == selectedStation)
			setStation(station, data);

		updateBackgroundColor(true);
		setBlockUI(false);

	}

	public final void onDataLoadFail() {
		showNoInternetNotification();
		updateBackgroundColor(false);
		setBlockUI(false);
	}

	private enum PanelType {
		FirstRun, Main, AddFavorite
	}
}
