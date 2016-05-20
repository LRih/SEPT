package View;

import Model.LatestReading;
import Model.Station;
import Model.StationData;
import Data.DataManager;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Utils.StationDataWorker;
import Utils.StationDataWorker.OnTaskCompleteListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.border.LineBorder;

/**
 * A Station Cell for repeating in favorite list.
 */
public final class FavoriteCell extends JPanel implements OnTaskCompleteListener
{
    private final WebLabel wblblTemp;

    private final Station station;
    private StationData data;

    private boolean isSelected = false;

    private OnStationSelectListener _listenerSelect;
    private OnDataLoadListener _listenerDataLoad;

    /**
     * Create the panel.
     */
    public FavoriteCell(Station station)
    {
        this.station = station;

        StationDataWorker dataWorker = new StationDataWorker(station);
        dataWorker.setOnTaskCompleteListener(this);
        dataWorker.execute();

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter()
        {
            public final void mouseClicked(MouseEvent e)
            {
                if (_listenerSelect != null)
                    _listenerSelect.onStationSelect(FavoriteCell.this.station, data);
            }

            // hover effect
            public final void mouseEntered(MouseEvent e)
            {
            	setBorder(new LineBorder(isSelected ? Style.CELL_SELECTED : new Color(160, 160, 160)));
            }
            public final void mouseExited(MouseEvent e)
            {
            	setBorder(new LineBorder(isSelected ? Style.CELL_SELECTED : Style.CELL_NORMAL));
            }
        });

        setBorder(new LineBorder(new Color(245, 245, 245)));
        setBackground(new Color(250, 250, 250));
        setLayout(new MigLayout("", "[grow][][5%][]", "[][][]"));

        WebLabel wblblStation = new WebLabel();
        wblblStation.setForeground(new Color(255, 69, 0));
        wblblStation.setFont(Style.FONT_16);
        wblblStation.setText(station.getName());
        add(wblblStation, "cell 0 0 1 2,alignx left,grow");

        wblblTemp = new WebLabel();
        wblblTemp.setFont(Style.FONT_FUTURA_20);
        wblblTemp.setForeground(new Color(34, 139, 34));

        add(wblblTemp, "cell 1 0 1 3,alignx center,aligny center");

        WebLabel wblblState = new WebLabel();
        wblblState.setFont(Style.FONT_BENDER_12);
        wblblState.setText(station.getState().getName());
        add(wblblState, "cell 0 2");

        setStationData(DataManager.getCachedStationData(station));
    }

    public final void updateSelected(Station selectedStation)
    {
        isSelected = station == selectedStation;
        setBorder(new LineBorder(isSelected ? Style.CELL_SELECTED : Style.CELL_NORMAL));
    }

    private void setStationData(StationData data)
    {
        this.data = data;
        wblblTemp.setText("-");

        if (data != null)
        {
            List<LatestReading> readings = data.getLatestReadings();

            if (readings.size() > 0 && readings.get(0).airTemp != null)
                wblblTemp.setText(readings.get(0).airTemp.toString());
            wblblTemp.setForeground(new Color(34, 139, 34));
        }
        else
            wblblTemp.setForeground(new Color(155, 155, 155));
    }


    public final void setOnStationSelectListener(OnStationSelectListener listener)
    {
        _listenerSelect = listener;
    }
    public final void setOnDataLoadListener(OnDataLoadListener listener)
    {
        _listenerDataLoad = listener;
    }


    /**
     * On success callback for StationWorker
     */
    public final void onTaskSuccess(StationData data)
    {
        if (_listenerDataLoad != null)
            _listenerDataLoad.onDataLoadSuccess(station, data);

        setStationData(data);
    }

    /**
     * On fail call back for StationWorker
     */
    public final void onTaskFail()
    {
        if (_listenerDataLoad != null)
            _listenerDataLoad.onDataLoadFail();
    }


    public interface OnStationSelectListener
    {
        void onStationSelect(Station station, StationData data);
    }
    public interface OnDataLoadListener
    {
        void onDataLoadSuccess(Station station, StationData data);
        void onDataLoadFail();
    }
}
