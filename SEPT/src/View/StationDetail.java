package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Model.Station;
import Model.StationData;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StationDetail extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WebLabel wblblMildura;
	private WebLabel wblblHumid;
	private WebLabel wblblVictoria;
	private WebLabel wblblWindSse;
	private WebLabel wblblRainSinceam;
	private WebLabel wblblc;
	private WebLabel wblblPressQmh;
	private WebLabel wblblPress;
	private WebLabel wblblAirTemp;
	private WebLabel wblblDewPoint;
	private WebLabel wblblLastUpdate;
	private DateTimeFormatter dtfOut;

	/**
	 * Create the panel.
	 */
	public StationDetail(final MainPanel m) {
		
		setLayout(new MigLayout("", "[30%][grow][30%]", "[][][][][][][][grow]"));
		
		wblblMildura = new WebLabel();
		wblblMildura.setText("-");
		wblblMildura.setForeground(new Color(255, 69, 0));
		wblblMildura.setFont(new Font("Century Gothic", Font.PLAIN, 30));

		add(wblblMildura, "cell 0 0 2 1");

		wblblHumid = new WebLabel();
		wblblHumid.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblHumid.setText("-");
		add(wblblHumid, "cell 2 0,aligny bottom");

		wblblVictoria = new WebLabel();
		wblblVictoria.setFont(new Font("Bender", Font.PLAIN, 16));
		wblblVictoria.setText("-");
		add(wblblVictoria, "cell 0 1 2 1");

		wblblWindSse = new WebLabel();
		wblblWindSse.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblWindSse.setText("-");
		add(wblblWindSse, "cell 2 1");

		wblblRainSinceam = new WebLabel();
		wblblRainSinceam.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblRainSinceam.setText("Rain since 9am: -");
		add(wblblRainSinceam, "cell 2 2");

		wblblc = new WebLabel();
		wblblc.setForeground(new Color(60, 179, 113));
		wblblc.setFont(new Font("Futura", Font.PLAIN, 50));
		wblblc.setText("-°C");
		add(wblblc, "cell 1 2 1 3,alignx left,aligny top");

		wblblPressQmh = new WebLabel();
		wblblPressQmh.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPressQmh.setText("Press QNH hPa: -");
		add(wblblPressQmh, "cell 2 3");

		wblblPress = new WebLabel();
		wblblPress.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblPress.setText("Press MSL hPa: -");
		add(wblblPress, "cell 2 4,aligny top");

		wblblAirTemp = new WebLabel();
		wblblAirTemp.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblAirTemp.setText("App temp: -°C");
		add(wblblAirTemp, "cell 1 5,aligny bottom");

		WebButton wbtnViewChart = new WebButton();
		wbtnViewChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m.showState(1);
			}
		});
		wbtnViewChart.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wbtnViewChart.setDefaultButtonShadeColor(new Color(240, 255, 255));
		wbtnViewChart.setBottomSelectedBgColor(new Color(224, 255, 255));
		wbtnViewChart.setBottomBgColor(new Color(240, 248, 255));
		wbtnViewChart.setDrawShade(false);
		wbtnViewChart.setText("View Chart");
		add(wbtnViewChart, "cell 2 5,alignx left,aligny bottom");

		wblblDewPoint = new WebLabel();
		wblblDewPoint.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblDewPoint.setText("Dew Point: -°C");
		add(wblblDewPoint, "cell 1 6");

		 dtfOut = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm");

		wblblLastUpdate = new WebLabel();
		wblblLastUpdate.setFont(new Font("Century Gothic", Font.ITALIC, 11));
		wblblLastUpdate.setText("Last update: -");
		add(wblblLastUpdate, "cell 0 7 2 1,aligny bottom");
	}
	
	public void setStation(Station station, StationData data) {

		if (data.getReadings().get(0).getAirTemp() < 30) {
			setBackground(new Color(240, 248, 255));
			wblblc.setForeground(new Color(30, 144, 255));
		} else {
			setBackground(new Color(255, 248, 220));
			wblblc.setForeground(new Color(255, 99, 71));
		}
		
		wblblMildura.setText(station.getName());
		wblblHumid.setText("Humid: " + data.getReadings().get(0).getRelativeHumidity() + "%");
		wblblVictoria.setText(station.getState().getName());
		wblblWindSse.setText(
				"Wind: " + data.getReadings().get(0).getWindDir() + " " + data.getReadings().get(0).getWindSpdKmH()
						+ "-" + data.getReadings().get(0).getWindGustKmH() + " km/h");
		wblblRainSinceam.setText("Rain since 9am: " + data.getReadings().get(0).getRainTrace() + "mm");
		wblblc.setText(data.getReadings().get(0).getAirTemp() + "°C");
		wblblPressQmh.setText("Press QNH hPa: " + data.getReadings().get(0).getPressureQNH());
		wblblPress.setText("Press MSL hPa: " + data.getReadings().get(0).getPressureMSL());
		wblblAirTemp.setText("App temp: " + data.getReadings().get(0).getApparentTemp() + "°C");
		wblblDewPoint.setText("Dew Point: " + data.getReadings().get(0).getDewPt() + "°C");
		wblblLastUpdate.setText("Last update: " + dtfOut.print(data.getReadings().get(0).getLocalDateTime()));

	}

}
