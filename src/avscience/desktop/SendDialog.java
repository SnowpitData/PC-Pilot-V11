package avscience.desktop;

import java.awt.*;
import java.awt.event.*;

public class SendDialog extends Dialog
{
    boolean fComponentsAdjusted;
    Frame frame;
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent event)
        {
            Object object = event.getSource();
            if(object == SendDialog.this)
                OKDialog_WindowClosing(event);
        }

        SymWindow()
        {
            SendDialog.this.dispose();
        }
    }

    class SymAction
        implements ActionListener
    {

        public void actionPerformed(ActionEvent event)
        {
            Object object = event.getSource();
        }

        SymAction()
        {
            SendDialog.this.dispose();
        }
    }


    public SendDialog(Frame parent, boolean modal)
    {
        super(parent, modal);
        fComponentsAdjusted = false;
        frame = null;
        frame = parent;
        setLayout(null);
        setSize(337, 135);
        setVisible(false);
        Label l = new Label("Sending Data ..");
        l.setFont(new Font("Dialog", 1, 12));
        l.setBounds(120, 60, 160, 24);
        add(l);
        setTitle("Sending Data - please wait.");
        SymWindow aSymWindow = new SymWindow();
        addWindowListener(aSymWindow);
        SymAction lSymAction = new SymAction();
    }

    public void addNotify()
    {
        Dimension d = getSize();
        super.addNotify();
        if(fComponentsAdjusted)
            return;
        setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
        Component components[] = getComponents();
        for(int i = 0; i < components.length; i++)
        {
            Point p = components[i].getLocation();
            p.translate(getInsets().left, getInsets().top);
            components[i].setLocation(p);
        }

        fComponentsAdjusted = true;
    }

    public SendDialog(Frame parent, String title, boolean modal)
    {
        this(parent, modal);
        setTitle(title);
    }

    public void setVisible(boolean b)
    {
        if(b)
        {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();
            setLocation(bounds.x + (bounds.width - abounds.width) / 2, bounds.y + (bounds.height - abounds.height) / 2);
            Toolkit.getDefaultToolkit().beep();
        }
        super.setVisible(b);
    }

    void okButton_ActionPerformed(ActionEvent event)
    {
        dispose();
    }

    void OKDialog_WindowClosing(WindowEvent event)
    {
        dispose();
    }
}
