package avscience.desktop;

import java.awt.*;
import java.awt.event.*;

public class CloseFrameDialog extends Dialog
{
    boolean fComponentsAdjusted;
    Frame frame;
    Button yesButton;
    Button noButton;
    Label label1;
    
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == CloseFrameDialog.this)
                QuitDialog_WindowClosing(windowevent);
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
            if(obj == yesButton)
                yesButton_ActionPerformed(actionevent);
            else
            if(obj == noButton)
                noButton_ActionPerformed(actionevent);
        }

        SymAction()
        {
        }
    }


    public CloseFrameDialog(Frame frame1, boolean flag)
    {
        super(frame1, flag);
        fComponentsAdjusted = false;
        frame = null;
        yesButton = new Button();
        noButton = new Button();
        label1 = new Label();
        frame = frame1;
        setLayout(null);
        setSize(337, 135);
        setVisible(false);
        yesButton.setLabel(" Yes ");
        add(yesButton);
        yesButton.setFont(new Font("Dialog", 1, 12));
        yesButton.setBounds(72, 80, 79, 22);
        noButton.setLabel("  No  ");
        add(noButton);
        noButton.setFont(new Font("Dialog", 1, 12));
        noButton.setBounds(185, 80, 79, 22);
        label1.setText("Close Window");
        label1.setAlignment(1);
        add(label1);
        label1.setBounds(78, 33, 180, 23);
        setTitle("Avalanche Science - Close window");
        SymWindow symwindow = new SymWindow();
        addWindowListener(symwindow);
        SymAction symaction = new SymAction();
        noButton.addActionListener(symaction);
        yesButton.addActionListener(symaction);
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

    public CloseFrameDialog(Frame frame1, String s, boolean flag)
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

    void yesButton_ActionPerformed(ActionEvent actionevent)
    {
        try
        {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
            System.gc();
            dispose();
        }
        catch(Exception exception) { }
    }

    void noButton_ActionPerformed(ActionEvent actionevent)
    {
        dispose();
    }

    void QuitDialog_WindowClosing(WindowEvent windowevent)
    {
        dispose();
    }
}
