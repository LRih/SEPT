package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class Main {

	private JFrame frmSept;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmSept.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSept = new JFrame();
		frmSept.setTitle("SEPT");
		frmSept.setBounds(100, 100, 450, 300);
		frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSept.getContentPane().setLayout(null);
		
		JLabel lblHelloSeptTeam = new JLabel("Hello SEPT Team!");
		lblHelloSeptTeam.setBounds(167, 95, 116, 39);
		frmSept.getContentPane().add(lblHelloSeptTeam);
	}
}
