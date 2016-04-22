package View;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;

import Model.States;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.alee.laf.label.WebLabel;

import Model.State;
import Model.Station;

import java.awt.Font;

import com.alee.laf.combobox.WebComboBox;

import java.awt.Color;

/**
 * Add Station UI Panel
 */
public final class AddFavoritePanel extends JPanel
{
    private final WebComboBox wcbStates;
    private final WebComboBox wcbStations;
    private final WebButton wbtnBack;
    private final WebButton wbtnAddToMyFavorites;

    private final States states;

    private OnBackClickListener _listenerBack;
    private OnAddFavoriteClickListener _listenerAddFavorite;

    /**
     * Create the panel.
     */
    public AddFavoritePanel(States states)
    {
        this.states = states;

        setBackground(Color.WHITE);

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
        pnAddStation.add(wcbStates, "cell 1 1,growx");

        WebLabel wblblSelectStation = new WebLabel();
        wblblSelectStation.setText("Select Station");
        pnAddStation.add(wblblSelectStation, "cell 0 2,alignx trailing");

        wcbStations = new WebComboBox();
        wcbStations.setFont(new Font("Century Gothic", Font.PLAIN, 13));
        wcbStations.setDrawFocus(false);
        pnAddStation.add(wcbStations, "cell 1 2,growx");

        wbtnAddToMyFavorites = new WebButton();
        wbtnAddToMyFavorites.setFont(new Font("Bender", Font.PLAIN, 13));
        wbtnAddToMyFavorites.setForeground(new Color(0, 128, 0));
        wbtnAddToMyFavorites.setDefaultButtonShadeColor(new Color(240, 255, 240));
        wbtnAddToMyFavorites.setBottomSelectedBgColor(new Color(154, 205, 50));
        wbtnAddToMyFavorites.setBottomBgColor(new Color(240, 255, 240));
        wbtnAddToMyFavorites.setDrawShade(false);
        wbtnAddToMyFavorites.setText("Add to My Favorites");
        pnAddStation.add(wbtnAddToMyFavorites, "cell 1 3");

        JPanel panelFiller = new JPanel();
        panelFiller.setBackground(Color.WHITE);
        pnContent.add(panelFiller, "cell 2 1,grow");

        addListeners();

        loadData();
    }

    private void addListeners()
    {
        wbtnBack.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listenerBack != null)
                    _listenerBack.onBackClick();
            }
        });

        wcbStates.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                loadStationsByState(wcbStates.getSelectedItem().toString());
            }
        });

        wbtnAddToMyFavorites.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listenerAddFavorite != null)
                    _listenerAddFavorite.onAddFavoriteClick(states.get(wcbStates.getSelectedItem().toString()).getStation(wcbStations.getSelectedItem().toString()));
            }
        });

    }

    private void loadData()
    {
        loadStates();
        if (wcbStates.getItemCount() > 0)
            wcbStates.setSelectedIndex(0);
    }

    private void loadStates()
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (State state : states)
            model.addElement(state.getName());
        wcbStates.setModel(model);
    }

    private void loadStationsByState(String name)
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Station station : states.get(name))
            model.addElement(station.getName());
        wcbStations.setModel(model);
    }

    public final void setOnBackClickListener(OnBackClickListener listener)
    {
        _listenerBack = listener;
    }
    public final void setOnAddFavoriteClickListener(OnAddFavoriteClickListener listener)
    {
        _listenerAddFavorite = listener;
    }


    public interface OnAddFavoriteClickListener
    {
        void onAddFavoriteClick(Station station);
    }
}
