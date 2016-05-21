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

import com.alee.laf.combobox.WebComboBox;

import java.awt.Color;

/**
 * Add Station UI Panel
 */
public final class AddFavoritePanel extends JPanel
{
    private final WebComboBox comboboxStates;
    private final WebComboBox comboboxStations;
    private final WebButton buttonBack;
    private final WebButton buttonAddToMyFavorites;

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

        buttonBack = new WebButton();
        buttonBack.setFont(Style.FONT_BENDER_13);
        buttonBack.setText("< Back");
        add(buttonBack, "cell 0 0");

        JPanel panelContent = new JPanel();
        panelContent.setBackground(Color.WHITE);
        add(panelContent, "cell 0 1 2 1,grow");
        panelContent.setLayout(new MigLayout("", "[20%][grow][20%]", "[grow][grow][grow]"));

        JPanel panelPlaceHolder = new JPanel();
        panelPlaceHolder.setBackground(Color.WHITE);
        panelContent.add(panelPlaceHolder, "cell 0 1,grow");

        JPanel panelAddStation = new JPanel();
        panelAddStation.setBackground(Color.WHITE);
        panelContent.add(panelAddStation, "cell 1 1,grow");
        panelAddStation.setLayout(new MigLayout("", "[grow][70%,grow]", "[][][][]"));

        WebLabel labelAddStation = new WebLabel();
        panelAddStation.add(labelAddStation, "cell 1 0");
        labelAddStation.setForeground(new Color(0, 0, 0));
        labelAddStation.setFont(Style.FONT_16);
        labelAddStation.setText("Find your station");

        WebLabel labelSelectState = new WebLabel();
        labelSelectState.setText("Select State");
        panelAddStation.add(labelSelectState, "cell 0 1,alignx trailing");

        comboboxStates = new WebComboBox();
        comboboxStates.setFont(Style.FONT_13);
        comboboxStates.setDrawFocus(false);
        panelAddStation.add(comboboxStates, "cell 1 1,growx");

        WebLabel labelSelectStation = new WebLabel();
        labelSelectStation.setText("Select Station");
        panelAddStation.add(labelSelectStation, "cell 0 2,alignx trailing");

        comboboxStations = new WebComboBox();
        comboboxStations.setFont(Style.FONT_13);
        comboboxStations.setDrawFocus(false);
        panelAddStation.add(comboboxStations, "cell 1 2,growx");

        buttonAddToMyFavorites = new WebButton();
        buttonAddToMyFavorites.setFont(Style.FONT_BENDER_13);
        buttonAddToMyFavorites.setForeground(new Color(0, 128, 0));
        buttonAddToMyFavorites.setDefaultButtonShadeColor(new Color(240, 255, 240));
        buttonAddToMyFavorites.setBottomSelectedBgColor(new Color(154, 205, 50));
        buttonAddToMyFavorites.setBottomBgColor(new Color(240, 255, 240));
        buttonAddToMyFavorites.setDrawShade(false);
        buttonAddToMyFavorites.setText("Add to My Favorites");
        panelAddStation.add(buttonAddToMyFavorites, "cell 1 3");

        JPanel panelFiller = new JPanel();
        panelFiller.setBackground(Color.WHITE);
        panelContent.add(panelFiller, "cell 2 1,grow");

        addListeners();

        loadData();
    }

    private void addListeners()
    {
        buttonBack.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listenerBack != null)
                    _listenerBack.onBackClick();
            }
        });

        comboboxStates.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                loadStationsByState(comboboxStates.getSelectedItem().toString());
            }
        });

        buttonAddToMyFavorites.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listenerAddFavorite != null)
                    _listenerAddFavorite.onAddFavoriteClick(states.get(comboboxStates.getSelectedItem().toString()).getStation(comboboxStations.getSelectedItem().toString()));
            }
        });

    }

    private void loadData()
    {
        loadStates();
        if (comboboxStates.getItemCount() > 0)
            comboboxStates.setSelectedIndex(0);
    }

    private void loadStates()
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (State state : states)
            model.addElement(state.getName());
        comboboxStates.setModel(model);
    }

    private void loadStationsByState(String name)
    {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (Station station : states.get(name))
            model.addElement(station.getName());
        comboboxStations.setModel(model);
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
