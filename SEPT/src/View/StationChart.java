package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.Station;
import Model.StationData;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;

public class StationChart extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebLabel wblblMildura;
	private WebLabel wblblVictoria;
	private StationData data;

	/**
	 * Create the panel.
	 */
	public StationChart(final MainPanel m) {
		setBackground(new Color(240, 248, 255));
		setLayout(new MigLayout("", "[][][][]", "[][][][][][][][]"));

		setLayout(new MigLayout("", "[10%][][][grow]", "[][][][][][][][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setDrawShade(false);
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(0);
			}
		});
		wbtnBack.setText("Back");
		add(wbtnBack, "cell 0 0");

		 wblblMildura = new WebLabel();
		wblblMildura.setText("-");
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));

		add(wblblMildura, "cell 1 0,aligny bottom");

		 wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("-");
		add(wblblVictoria, "cell 2 0,aligny bottom, gapy 0 3");

	}

	public void setStation(Station station, StationData data) {
		this.data = data;
		
		wblblMildura.setText(station.getName());
		wblblVictoria.setText(station.getState().getName());
	}
	

}
