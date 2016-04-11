package View;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbLabel;
import com.alee.laf.label.WebLabel;
import java.awt.Font;
import com.alee.laf.combobox.WebComboBox;
import java.awt.Color;

public class AddStation extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;

	/**
	 * Create the panel.
	 */
	public AddStation(Main m) {
		main = m;

		setLayout(new MigLayout("", "[grow][grow]", "[][grow]"));

		WebButton wbtnBack = new WebButton();
		wbtnBack.setFont(new Font("Bender", Font.PLAIN, 13));
		wbtnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showMainScreen();
			}
		});
		wbtnBack.setText("< Back");
		add(wbtnBack, "cell 0 0");
		
		JPanel panel = new JPanel();
		add(panel, "cell 0 1 2 1,grow");
		panel.setLayout(new MigLayout("", "[grow]", "[grow][grow][grow]"));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, "cell 0 1,grow");
		panel_1.setLayout(new MigLayout("", "[30%][70%,grow]", "[][][][]"));
		
		WebLabel wblblAddStation = new WebLabel();
		panel_1.add(wblblAddStation, "cell 1 0");
		wblblAddStation.setForeground(new Color(0, 0, 0));
		wblblAddStation.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		wblblAddStation.setText("Find your station");
		
		WebLabel wblblSelectState = new WebLabel();
		wblblSelectState.setText("Select State");
		panel_1.add(wblblSelectState, "cell 0 1,alignx trailing");
		
		WebComboBox webComboBox = new WebComboBox();
		panel_1.add(webComboBox, "cell 1 1,growx");
		
		WebLabel wblblSelectStation = new WebLabel();
		wblblSelectStation.setText("Select Station");
		panel_1.add(wblblSelectStation, "cell 0 2,alignx trailing");
		
		WebComboBox webComboBox_1 = new WebComboBox();
		panel_1.add(webComboBox_1, "cell 1 2,growx");
		
		WebButton wbtnAddToMy = new WebButton();
		wbtnAddToMy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showState(0);
			}
		});
		wbtnAddToMy.setText("Add to My Favorites");
		panel_1.add(wbtnAddToMy, "cell 1 3");

	}

}
