package View;

import Utils.URLWorker;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

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
        frmSept.setBounds(100, 100, 450, 300);
        frmSept.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmSept.getContentPane().setLayout(null);

        JLabel lblHelloSeptTeam = new JLabel("Hello SEPT Team!");
        lblHelloSeptTeam.setBounds(167, 95, 116, 39);
        frmSept.getContentPane().add(lblHelloSeptTeam);

        // test asynchronous load
        JButton btn = new JButton("print URL source");
        btn.setBounds(10, 10, 146, 39);
        btn.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                testLoadURL();
            }
        });
        frmSept.getContentPane().add(btn);
    }

    private void testLoadURL()
    {
        URLWorker worker = new URLWorker("http://www.bom.gov.au/fwo/IDV60901/IDV60901.95936.json");
        worker.setOnTaskCompleteListener(new URLWorker.OnTaskCompleteListener()
        {
            public final void onTaskComplete(String source)
            {
                System.out.println(source);
            }
            public final void onTaskFail()
            {
                System.out.println("URL load failed");
            }
        });
        worker.execute();
    }
}
