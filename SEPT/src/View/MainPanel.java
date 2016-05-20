package View;

import javax.swing.JPanel;

import Model.*;
import Utils.Log;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;

import com.alee.laf.button.WebButton;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

/**
 * Main Panel UI
 */
public final class MainPanel extends JPanel implements FavoriteCell.OnStationSelectListener, FavoriteCell.OnDataLoadListener
{
    private final JPanel pnMainContent;
    private final JPanel pnFavorites;
    private final WebButton wbtnAddStation;

    private final StationDetail stationDetail;
    private final StationChart stationChart;
    private final StationHistory stationHistory;
    private final StationForecast stationForecast;

    private OnActionListener _listenerAction;
    private OnViewForecastClickListener _listenerViewForecastClick;
    private FavoriteCell.OnStationSelectListener _listenerStationSelect;
    private FavoriteCell.OnDataLoadListener _listenerDataLoad;
    private JPanel pnForecast;

    /**
     * Create the panel.
     */
    public MainPanel()
    {
        setBackground(new Color(34, 139, 34));

        setLayout(new MigLayout("ins 0 0 0 0, gapy 0", "[grow][20%]", "[][grow][][160]"));

        pnFavorites = new JPanel();
        pnFavorites.setBackground(new Color(255, 255, 255));
        pnFavorites.setLayout(new MigLayout("ins 4 0 0 0", "[grow][grow]", ""));

        pnMainContent = new JPanel();
        pnMainContent.setBackground(Style.MAIN_PANEL_BACKGROUND);
        add(pnMainContent, "cell 0 1 2 1,grow");

        WebLabel wblblWeatherStations = new WebLabel();
        wblblWeatherStations.setForeground(new Color(255, 255, 255));
        wblblWeatherStations.setFont(Style.FONT_16);
        wblblWeatherStations.setText("Favourite Stations");
        add(wblblWeatherStations, "flowx,cell 0 3,gapx 15,gapy 5");

        wbtnAddStation = new WebButton("Add station");
        wbtnAddStation.setForeground(new Color(0, 100, 0));
        wbtnAddStation.setDrawShade(false);
        wbtnAddStation.setDefaultButtonShadeColor(new Color(154, 205, 50));
        wbtnAddStation.setBottomSelectedBgColor(new Color(50, 205, 50));
        wbtnAddStation.setBottomBgColor(new Color(240, 255, 240));
        wbtnAddStation.setFont(Style.FONT_BENDER_13);
        wbtnAddStation.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(wbtnAddStation, "cell 1 3,alignx right,gapx 0 15,gapy 5 0");

        WebScrollPane webScrollPane = new WebScrollPane(pnFavorites, false, true);
        webScrollPane.setDrawFocus(false);
        webScrollPane.setPreferredSize(new Dimension(0, 0));
        add(webScrollPane, "cell 0 4 2 1,hmin 160,grow");

        stationDetail = new StationDetail();
        stationChart = new StationChart();
        stationHistory = new StationHistory();
        stationForecast = new StationForecast();

        addListeners();
    }

    private void addListeners()
    {
        OnBackClickListener listener = new OnBackClickListener()
        {
            public final void onBackClick()
            {
                showPanel(PanelType.Detail);
            }
        };

        stationChart.setOnBackClickListener(listener);
        stationHistory.setOnBackClickListener(listener);

        stationDetail.setOnActionListener(new StationDetail.OnActionListener()
        {
            public final void onViewChartClick()
            {
                showPanel(PanelType.Chart);
            }
            public final void onViewForecastClick()
            {
                if (_listenerViewForecastClick != null)
                    _listenerViewForecastClick.onViewForecastClick();
            }
            public final void onViewHistoryClick()
            {
                showPanel(PanelType.History);
            }
        });

        wbtnAddStation.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listenerAction != null)
                    _listenerAction.onAddClick();
            }
        });
    }

    /**
     * Change panel shown.
     */
    public final void showPanel(PanelType type)
    {
        pnMainContent.removeAll();
        pnMainContent.setLayout(new MigLayout("ins 0", "[grow]", "[grow][160]"));

        AppState.getInstance().shownDetail = type.ordinal();
        Log.info(getClass(), "Panel changed to " + type.name());

        switch (type)
        {
            // VIEW_CHART
            case Chart:
                pnMainContent.add(stationChart, "cell 0 0, grow");
                stationChart.animate();
                break;

            // VIEW_HISTORY
            case History:
                pnMainContent.add(stationHistory, "cell 0 0, grow");
                break;

            // STATION_DETAIL
            default:
                pnMainContent.add(stationDetail, "cell 0 0, grow");
                pnMainContent.add(stationForecast, "cell 0 1, growx, hmin 160");
                break;
        }

        pnMainContent.validate();
        pnMainContent.repaint();
    }

    /**
     * Populate panel with favorites.
     */
    public final void setFavorites(Favorites favorites, States states, Station selectedStation)
    {
        pnFavorites.removeAll();

        int row = 0;
        int col = 0;

        for (Favorite fav : favorites)
        {
            FavoriteCell cell = new FavoriteCell(states.get(fav.state).getStation(fav.station));
            cell.updateSelected(selectedStation);
            cell.setOnStationSelectListener(this);
            cell.setOnDataLoadListener(this);

            if (col % 2 == 0)
                pnFavorites.add(cell, "cell 0 " + row + ", grow, gap 4");
            else
                pnFavorites.add(cell, "cell 1 " + (row++) + ", grow, gap 0 4");

            col++;
        }
    }
    public final void setStation(Station station, StationData data)
    {
        stationChart.setStation(station, data);
        stationHistory.setStation(station, data);
        stationDetail.setStation(station, data);
        stationForecast.setStation(station, data);

        // update selected favorite indicator
        for (Component cell : pnFavorites.getComponents())
            ((FavoriteCell)cell).updateSelected(station);

        pnMainContent.validate();
        pnMainContent.repaint();
    }


    public final void setOnActionListener(OnActionListener listener)
    {
        _listenerAction = listener;
    }
    public final void setOnViewForecastListener(OnViewForecastClickListener listener)
    {
        _listenerViewForecastClick = listener;
    }
    public final void setOnRemoveFromFavoritesClickListener(StationDetail.OnRemoveFavoriteClickListener listener)
    {
        stationDetail.setOnRemoveFavoriteClickListener(listener);
    }
    public final void setOnStationSelectListener(FavoriteCell.OnStationSelectListener listener)
    {
        _listenerStationSelect = listener;
    }
    public final void setOnDataLoadListener(FavoriteCell.OnDataLoadListener listener)
    {
        _listenerDataLoad = listener;
    }


    public interface OnActionListener
    {
        void onAddClick();
    }


    /* Event callbacks */

    public final void onStationSelect(Station station, StationData data)
    {
        if (_listenerStationSelect != null)
            _listenerStationSelect.onStationSelect(station, data);
    }

    public final void onDataLoadSuccess(Station station, StationData data)
    {
        if (_listenerDataLoad != null)
            _listenerDataLoad.onDataLoadSuccess(station, data);
    }
    public final void onDataLoadFail()
    {
        if (_listenerDataLoad != null)
            _listenerDataLoad.onDataLoadFail();
    }


    public interface OnViewForecastClickListener
    {
        void onViewForecastClick();
    }

    public enum PanelType
    {
        Detail, Chart, History
    }
}
