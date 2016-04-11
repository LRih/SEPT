package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;

import Utils.JavaUtils;

import com.alee.laf.button.WebButton;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;
	/**
	 * Create the panel.
	 */
	public MainPanel(Main m) {
		main = m;
		
		setLayout(new MigLayout("", "[80%,grow][20%]", "[][grow]"));
		
		WebLabel wblblWeatherStations = new WebLabel();
		wblblWeatherStations.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblWeatherStations.setText("Weather Stations");
		add(wblblWeatherStations, "flowx,cell 0 0");
		
		WebButton webButton = new WebButton("", JavaUtils.createImageIcon("/Images/add_16x16.png", "Add"));
		webButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showState(1);
			}
		});
		webButton.setFont(new Font("Bender", Font.PLAIN, 13));
		add(webButton, "cell 1 0, right");
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][][][][][][][][][][][][][]"));
		
        
        StationCell cell = null;
        for (int i = 0; i < 10; i++) {
        	cell = new StationCell();
			panel.add(cell, "cell 0 "+i+", grow");
		}

		WebScrollPane webScrollPane = new WebScrollPane ( panel, false, true );
        webScrollPane.setPreferredSize ( new Dimension ( 0, 0 ) );
        add(webScrollPane, "cell 0 1 2 1,grow");
	}

}
