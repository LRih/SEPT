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
    private final WebButton buttonAddStation;

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

        buttonAddStation = new WebButton("Add Station", SwingUtils.createImageIcon("/Images/add_16x16.png", "Add"));
        buttonAddStation.setDefaultButtonShadeColor(new Color(180, 180, 180));
        buttonAddStation.setBottomSelectedBgColor(new Color(154, 205, 50));
        buttonAddStation.setDrawShade(false);
        buttonAddStation.setBottomBgColor(new Color(240, 255, 240));
        buttonAddStation.setFont(Style.FONT_BENDER_13);
        add(buttonAddStation, "cell 1 3");

        WebLabel labelWelcomeToBom = new WebLabel();
        labelWelcomeToBom.setFont(Style.FONT_13);
        labelWelcomeToBom.setForeground(new Color(255, 140, 0));
        labelWelcomeToBom.setFontSize(20);
        labelWelcomeToBom.setHorizontalAlignment(SwingConstants.LEFT);
        labelWelcomeToBom.setText("Welcome to BOM Weather.");
        add(labelWelcomeToBom, "cell 1 4");

        WebLabel labelClickaddStation = new WebLabel();
        labelClickaddStation.setFont(Style.FONT_13);
        labelClickaddStation.setText("Click \"Add Station\" to get started");
        add(labelClickaddStation, "cell 1 5");

        addListeners();
    }

    private void addListeners()
    {
        buttonAddStation.addActionListener(new ActionListener()
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
