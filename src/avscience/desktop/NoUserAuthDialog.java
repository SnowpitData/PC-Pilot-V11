package avscience.desktop;

import java.awt.*;
import java.awt.event.*;

public class NoUserAuthDialog extends Dialog
{
    Label label1;
    Label label2;
    String user;
    boolean fComponentsAdjusted;
    AvApp frame;
    Button okButton;
    
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == NoUserAuthDialog.this)
                NoUserAuthDialog_WindowClosing(windowevent);
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


    public NoUserAuthDialog(Frame frame1, boolean flag, String s)
    {
        super(frame1, flag);
        label1 = new Label();
        label2 = new Label();
        user = "";
        fComponentsAdjusted = false;
        frame = null;
        okButton = new Button();
        user = s;
        frame = (AvApp)frame1;
        setLayout(null);
        setSize(337, 135);
        setVisible(false);
        setLocation(0, 0);
        okButton.setLabel("  OK  ");
        add(okButton);
        okButton.setFont(new Font("Dialog", 1, 12));
        okButton.setBounds(120, 100, 79, 24);
        setTitle("Web user: " + s + " not authorized!");
        label1.setText("Please set your registration email");
        label1.setAlignment(1);
        add(label1);
        label1.setBounds(78, 33, 240, 23);
        label2.setText("so that it is the same as PDA email.");
        label2.setAlignment(1);
        add(label2);
        label2.setBounds(78, 64, 240, 23);
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

    void NoUserAuthDialog_WindowClosing(WindowEvent windowevent)
    {
        dispose();
    }
}
