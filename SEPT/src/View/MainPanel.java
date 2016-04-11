package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;

import Utils.JavaUtils;

import com.alee.laf.button.WebButton;
import java.awt.Font;

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
		
		setLayout(new MigLayout("", "[80%][20%]", "[][grow]"));
		
		WebLabel wblblWeatherStations = new WebLabel();
		wblblWeatherStations.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblWeatherStations.setText("Weather Stations");
		add(wblblWeatherStations, "flowx,cell 0 0");
		
		WebButton webButton = new WebButton("", JavaUtils.createImageIcon("/Images/add_16x16.png", "Add"));
		webButton.setFont(new Font("Bender", Font.PLAIN, 13));
		add(webButton, "cell 1 0, right");

	}

}
