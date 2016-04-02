package View;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;

public class WeatherStations extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public WeatherStations() {
		setBackground(Color.WHITE);
		setLayout(new MigLayout("", "[grow][]", "[][grow]"));
		
		JLabel lblWeatherStations = new JLabel("Weather Stations");
		add(lblWeatherStations, "cell 0 0, grow");
		
		WebButton wbtnAdd = new WebButton("Add");
		wbtnAdd.addActionListener(this);
		add(wbtnAdd, "cell 1 0");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = (JButton)e.getSource();
        System.out.println(button.getText());
        
     // Enabling frame decoration
        boolean decorateFrames = WebLookAndFeel.isDecorateFrames ();
        WebLookAndFeel.setDecorateFrames ( true );

        // Opening frame
        AddStation addStation = new AddStation ();
        addStation.pack ();
        addStation.setLocationRelativeTo ( this );
        addStation.setVisible ( true );

        // Restoring frame decoration option
        WebLookAndFeel.setDecorateFrames ( decorateFrames );

	}

}
