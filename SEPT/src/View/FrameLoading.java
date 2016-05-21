package View;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.progressbar.WebProgressBar;

import Utils.SwingUtils;

import java.awt.Color;
import javax.swing.border.LineBorder;
import com.alee.extended.image.WebImage;
import java.awt.Font;

public class FrameLoading extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameLoading frame = new FrameLoading();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrameLoading() {
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 100);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 0));
		contentPane.setBorder(new LineBorder(new Color(144, 238, 144)));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("ins -5 -15 -5 -15, gapy 0", "[grow]", "[grow]"));
		
		JPanel panelMain = new JPanel();
		panelMain.setBackground(Style.INTERNET_ON_BACKGROUND);
		contentPane.add(panelMain, "cell 0 0 1 1,grow");
//		ins -5 -5 -5 -5, gapy 0
		panelMain.setLayout(new MigLayout("ins -5 -5 -5 -5", "[35%][grow]", "[grow]"));
		
		WebImage webImage = new WebImage(SwingUtils.createImageIcon("/Images/logo.png", "melbourne"));
		panelMain.add(webImage, "cell 0 0, gapy 5 0, gapx 60 0");
		
		JPanel panelWelcome = new JPanel();
		panelWelcome.setBackground(Style.INTERNET_ON_BACKGROUND);
		panelMain.add(panelWelcome, "cell 1 0,grow");
		panelWelcome.setLayout(new MigLayout("ins 10 15 10 0", "[grow]", "[grow]"));
		
		WebLabel wblblBomWeather = new WebLabel();
		wblblBomWeather.setForeground(new Color(255, 255, 255));
		wblblBomWeather.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		wblblBomWeather.setFontSize(30);
		wblblBomWeather.setText("BOM Weather");
		panelWelcome.add(wblblBomWeather, "cell 0 0,aligny center");
	}

}
