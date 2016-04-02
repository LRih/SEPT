package View;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;

import Controller.States;
import Model.State;
import net.miginfocom.swing.MigLayout;

//import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Dimension;
import java.util.ArrayList;

public final class StatesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tfSearch;

	/**
	 * Create the panel.
	 */
	public StatesPanel() {
		setForeground(new Color(255, 127, 80));
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[grow][]", "[][grow]"));

		tfSearch = new JTextField();
		tfSearch.setMinimumSize(new Dimension(10, 30));
		tfSearch.setBorder(new LineBorder(Color.LIGHT_GRAY));
		tfSearch.setToolTipText("search states");
		add(tfSearch, "cell 0 0,growx");
		tfSearch.setColumns(10);

		JButton btSearch = new JButton("Search States");
		btSearch.setForeground(new Color(255, 127, 80));
		btSearch.setMinimumSize(new Dimension(127, 30));
		btSearch.setBorder(new LineBorder(new Color(255, 69, 0)));
		add(btSearch, "cell 1 0,aligny bottom");

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		add(panel, "cell 0 1 2 1,grow");
		panel.setLayout(new MigLayout("", "[grow][grow][grow]", "[][]"));

		ArrayList<State> states = States.getStates();
		JButton btnState;
		for (int i = 0; i < states.size(); i++) {
			btnState = new JButton(states.get(i).getAbbr());
			btnState.setBackground(new Color(240, 255, 255));
			btnState.setMinimumSize(new Dimension(75, 50));
			btnState.setBorder(new LineBorder(new Color(211, 211, 211)));
			if (i % 3 == 0 && i != 0)
				panel.add(btnState, "wrap, growx");
			else
				panel.add(btnState, "growx");
		}

	}

}
