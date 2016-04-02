package View;

import Utils.StationDataWorker;
import net.miginfocom.swing.MigLayout;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
//import net.miginfocom.swing.MigLayout;

import com.alee.laf.WebLookAndFeel;

public final class Main
{
    private JFrame frmSept;

    /**
     * Launch the application.
     */
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public final void run()
            {
                try
                {
                	WebLookAndFeel.install ();
                    Main window = new Main();
                    window.frmSept.setVisible(true);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main()
    {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize()
    {
        frmSept = new JFrame();
        frmSept.setTitle("SEPT");
        frmSept.setBounds(100, 100, 800, 600);
        frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmSept.getContentPane().setLayout(new MigLayout("", "[1000px][1000px][1000px]", "[40px][1000px]"));

        JLabel lblHelloSeptTeam = new JLabel("Hello SEPT Team!");
        frmSept.getContentPane().add(lblHelloSeptTeam, "cell 2 0,grow");

        // test asynchronous load
        JButton btn = new JButton("print URL source");
        frmSept.getContentPane().add(btn, "cell 0 0,alignx right,growy");
        
        WeatherStations panel = new WeatherStations();
        frmSept.getContentPane().add(panel, "cell 0 1 3 1,grow");
    }
}
