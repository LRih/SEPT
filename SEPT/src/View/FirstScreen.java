package View;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Utils.JavaUtils;

import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import com.alee.extended.image.WebImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FirstScreen extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Main main = null;

	/**
	 * Create the panel.
	 */
	public FirstScreen(Main m) {
		this.main = m;
		
		setLayout(new MigLayout("", "[grow][center][grow]", "[grow][grow][center][center][center][center][grow][grow][grow]"));
		
		WebImage webImage = new WebImage(JavaUtils.createImageIcon("/Images/logo_big.png", "logo"));
		add(webImage, "cell 1 2");

		WebButton wbtnAddStation = new WebButton("Add Station", JavaUtils.createImageIcon("/Images/add_16x16.png", "Add"));
		wbtnAddStation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.showState(1);
			}
		});
		wbtnAddStation.setFont(new Font("Bender", Font.PLAIN, 13));
		add(wbtnAddStation, "cell 1 3");

		WebLabel wblblWelcomeToBom = new WebLabel();
		wblblWelcomeToBom.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblWelcomeToBom.setForeground(new Color(255, 140, 0));
		wblblWelcomeToBom.setFontSize(20);
		wblblWelcomeToBom.setHorizontalAlignment(SwingConstants.LEFT);
		wblblWelcomeToBom.setText("Welcome to BOM Weather.");
		add(wblblWelcomeToBom, "cell 1 4");

		WebLabel wblblClickaddStation = new WebLabel();
		wblblClickaddStation.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblClickaddStation.setText("Click \"Add Station\" to get started");
		add(wblblClickaddStation, "cell 1 5");

	}

}
