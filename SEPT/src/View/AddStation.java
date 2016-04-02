package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import com.alee.laf.combobox.WebComboBox;

import com.alee.laf.button.WebButton;

public class AddStation extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public AddStation() {
		
		setTitle("Add Station");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[60px][grow][]", "[][][][]"));
		
		JLabel lblSelectState = new JLabel("State");
		contentPane.add(lblSelectState, "cell 0 0,alignx trailing");

		String[] states = {"VIC", "NSW", "QLD", "SA", "WA", "NT", "TAS"};
		String[] areas = {"North", "East", "West", "South", "Central"};
		String[] stations = {"Melbourne Central", "Carlton", "Docklands", "Hawthorn", "Richmond"};
		
		WebComboBox cbStates = new WebComboBox(states);
		contentPane.add(cbStates, "cell 1 0,growx");
		
		JLabel lblArea = new JLabel("Area");
		contentPane.add(lblArea, "cell 0 1,alignx trailing");
		
		WebComboBox cbArea = new WebComboBox(areas);
		contentPane.add(cbArea, "cell 1 1,growx");
		
		JLabel lblStation = new JLabel("Station");
		contentPane.add(lblStation, "cell 0 2,alignx trailing");
		
		WebComboBox cbStation = new WebComboBox(stations);
		contentPane.add(cbStation, "cell 1 2,growx");
		
		WebButton wbtnAdd = new WebButton("Add");
		contentPane.add(wbtnAdd, "cell 1 3, growx");
	}

}
