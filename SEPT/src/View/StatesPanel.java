package View;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.LineBorder;
import net.miginfocom.swing.MigLayout;

public class StatesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public StatesPanel() {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[]", "[]"));
		
	}

}
