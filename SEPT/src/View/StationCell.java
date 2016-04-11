package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.button.WebButton;

public class StationCell extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public StationCell() {
		setLayout(new MigLayout("", "[20%][grow][10%][10%][10%]", "[][][]"));
		
		WebLabel wblblVictoria = new WebLabel();
		wblblVictoria.setText("Victoria");
		add(wblblVictoria, "cell 0 0");
		
		WebLabel wblblMildura = new WebLabel();
		wblblMildura.setText("Mildura");
		add(wblblMildura, "cell 1 0");
		
		WebLabel webLabel = new WebLabel();
		webLabel.setText("26");
		add(webLabel, "cell 2 0");
		
		WebLabel webLabel_1 = new WebLabel();
		webLabel_1.setText("19");
		add(webLabel_1, "cell 3 0");
		
		WebButton wbtnX = new WebButton();
		wbtnX.setText("X");
		add(wbtnX, "cell 4 0");

	}

}
