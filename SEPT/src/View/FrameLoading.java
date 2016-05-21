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
	private WebLabel labelLoading;

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
		setBounds(300, 300, 450, 100);
		contentPane = new JPanel();
		contentPane.setBackground(Style.LOADING_FRAME_BACKGROUND);
		contentPane.setBorder(new LineBorder(new Color(144, 238, 144)));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("ins -5 -15 -5 -15, gapy 0", "[grow]", "[grow]"));

		JPanel panelMain = new JPanel();
		panelMain.setBackground(Style.INTERNET_ON_BACKGROUND);
		contentPane.add(panelMain, "cell 0 0 1 1,grow");
		// ins -5 -5 -5 -5, gapy 0
		panelMain.setLayout(new MigLayout("ins -5 -5 -5 -5", "[35%][grow]", "[grow]"));

		WebImage logo = new WebImage(SwingUtils.createImageIcon("/Images/logo.png", "logo"));
		panelMain.add(logo, "cell 0 0, gapy 5 0, gapx 60 0");

		JPanel panelWelcome = new JPanel();
		panelWelcome.setBackground(Style.INTERNET_ON_BACKGROUND);
		panelMain.add(panelWelcome, "cell 1 0,grow");
		panelWelcome.setLayout(new MigLayout("ins 10 15 10 0", "[grow]", "[grow][]"));

		WebLabel labelBomWeather = new WebLabel();
		labelBomWeather.setForeground(Color.white);
		labelBomWeather.setFont(new Font("Century Gothic", Font.PLAIN, 13));
		labelBomWeather.setFontSize(30);
		labelBomWeather.setText("BOM Weather");
		panelWelcome.add(labelBomWeather, "cell 0 0,aligny center, gapy 15 0");

		labelLoading = new WebLabel();
		labelLoading.setFont(new Font("Bender", Font.ITALIC, 13));
		labelLoading.setForeground(new Color(240, 255, 255));
		labelLoading.setText("loading..");
		panelWelcome.add(labelLoading, "cell 0 1");
	}

	public void setLoadingText(String text) {
		labelLoading.setText("loading " + text + "...");
	}

}
