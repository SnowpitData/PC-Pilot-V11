package avscience.desktop;

import avscience.pc.MainFrame;
import java.awt.Frame;

public class AvApp extends Frame
{

    public AvApp()
    {
        setVisible(false);
        (new MainFrame()).setVisible(true);
        dispose();
    }
}
