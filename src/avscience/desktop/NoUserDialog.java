package avscience.desktop;

import java.awt.*;
import java.awt.event.*;

public class NoUserDialog extends Dialog
{
    
    Label label1;
    String user;
    boolean fComponentsAdjusted;
    AvApp frame;
    Button okButton;
    
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == NoUserDialog.this)
                NoUserDialog_WindowClosing(windowevent);
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


    public NoUserDialog(Frame frame1, boolean flag, String s)
    {
        super(frame1, flag);
        label1 = new Label();
        user = "";
        fComponentsAdjusted = false;
        frame = null;
        okButton = new Button();
        user = s;
        frame = (AvApp)frame1;
        setLayout(null);
        setSize(337, 135);
        setVisible(false);
        setLocation(10, 10);
        okButton.setLabel("  OK  ");
        add(okButton);
        okButton.setFont(new Font("Dialog", 1, 12));
        okButton.setBounds(120, 60, 79, 24);
        setTitle("Web user does not exist!");
        label1.setText("Please register user: " + s + "!");
        label1.setAlignment(1);
        add(label1);
        label1.setBounds(78, 33, 240, 23);
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

    void NoUserDialog_WindowClosing(WindowEvent windowevent)
    {
        dispose();
    }
}
