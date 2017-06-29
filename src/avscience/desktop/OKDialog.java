package avscience.desktop;

import java.awt.*;
import java.awt.event.*;

public class OKDialog extends Dialog
{
    
    boolean fComponentsAdjusted;
    Frame frame;
    Button okButton;
    
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == OKDialog.this)
                OKDialog_WindowClosing(windowevent);
        }

        SymWindow()
        {
        }
    }

    class SymAction
        implements ActionListener
    {

        public void actionPerformed(ActionEvent actionevent)
        {
            Object obj = actionevent.getSource();
            if(obj == okButton)
                okButton_ActionPerformed(actionevent);
        }

        SymAction()
        {
        }
    }


    public OKDialog(Frame frame1, boolean flag)
    {
        super(frame1, flag);
        fComponentsAdjusted = false;
        frame = null;
        okButton = new Button();
        frame = frame1;
        setLayout(null);
        setSize(337, 135);
        setVisible(false);
        okButton.setLabel("  OK  ");
        add(okButton);
        okButton.setFont(new Font("Dialog", 1, 12));
        okButton.setBounds(120, 60, 79, 24);
        setTitle("Data Sent");
        SymWindow symwindow = new SymWindow();
        addWindowListener(symwindow);
        SymAction symaction = new SymAction();
        okButton.addActionListener(symaction);
    }

    public void addNotify()
    {
        Dimension dimension = getSize();
        super.addNotify();
        if(fComponentsAdjusted)
            return;
        setSize(getInsets().left + getInsets().right + dimension.width, getInsets().top + getInsets().bottom + dimension.height);
        Component acomponent[] = getComponents();
        for(int i = 0; i < acomponent.length; i++)
        {
            Point point = acomponent[i].getLocation();
            point.translate(getInsets().left, getInsets().top);
            acomponent[i].setLocation(point);
        }

        fComponentsAdjusted = true;
    }

    public OKDialog(Frame frame1, String s, boolean flag)
    {
        this(frame1, flag);
        setTitle(s);
    }

    public void setVisible(boolean flag)
    {
        if(flag)
        {
            Rectangle rectangle = getParent().getBounds();
            Rectangle rectangle1 = getBounds();
            setLocation(rectangle.x + (rectangle.width - rectangle1.width) / 2, rectangle.y + (rectangle.height - rectangle1.height) / 2);
            Toolkit.getDefaultToolkit().beep();
        }
        super.setVisible(flag);
    }

    void okButton_ActionPerformed(ActionEvent actionevent)
    {
        dispose();
    }

    void OKDialog_WindowClosing(WindowEvent windowevent)
    {
        dispose();
    }
}
