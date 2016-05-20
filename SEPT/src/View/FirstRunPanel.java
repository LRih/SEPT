package View;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import Utils.SwingUtils;

import javax.swing.SwingConstants;
import java.awt.*;

import com.alee.extended.image.WebImage;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * First Screen UI. Show when user has no favorite station.
 */
public final class FirstRunPanel extends JPanel
{
    private final WebButton wbtnAddStation;

    private OnAddListener _listener;

    /**
     * Create the panel.
     */
    public FirstRunPanel()
    {
        setBackground(Color.WHITE);

        setLayout(new MigLayout("", "[grow][center][grow]", "[grow][grow][center][center][center][center][grow][grow][grow]"));

        WebImage webImage = new WebImage(SwingUtils.createImageIcon("/Images/logo_big.png", "logo"));
        add(webImage, "cell 1 2");

        wbtnAddStation = new WebButton("Add Station", SwingUtils.createImageIcon("/Images/add_16x16.png", "Add"));
        wbtnAddStation.setDefaultButtonShadeColor(new Color(180, 180, 180));
        wbtnAddStation.setBottomSelectedBgColor(new Color(154, 205, 50));
        wbtnAddStation.setDrawShade(false);
        wbtnAddStation.setBottomBgColor(new Color(240, 255, 240));
        wbtnAddStation.setFont(Style.FONT_BENDER_13);
        add(wbtnAddStation, "cell 1 3");

        WebLabel wblblWelcomeToBom = new WebLabel();
        wblblWelcomeToBom.setFont(Style.FONT_13);
        wblblWelcomeToBom.setForeground(new Color(255, 140, 0));
        wblblWelcomeToBom.setFontSize(20);
        wblblWelcomeToBom.setHorizontalAlignment(SwingConstants.LEFT);
        wblblWelcomeToBom.setText("Welcome to BOM Weather.");
        add(wblblWelcomeToBom, "cell 1 4");

        WebLabel wblblClickaddStation = new WebLabel();
        wblblClickaddStation.setFont(Style.FONT_13);
        wblblClickaddStation.setText("Click \"Add Station\" to get started");
        add(wblblClickaddStation, "cell 1 5");

        addListeners();
    }

    private void addListeners()
    {
        wbtnAddStation.addActionListener(new ActionListener()
        {
            public final void actionPerformed(ActionEvent e)
            {
                if (_listener != null)
                    _listener.onAddClick();
            }
        });
    }

    public final void setOnAddListener(OnAddListener listener)
    {
        _listener = listener;
    }


    public interface OnAddListener
    {
        void onAddClick();
    }
}
