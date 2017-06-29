package avscience.pc;

import avscience.ppc.AvOccurence;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;
public class DeleteOccDialog extends Dialog
{
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == DeleteOccDialog.this)
                DeleteDialog_WindowClosing();
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
                yesButton_ActionPerformed();
            else
            if(obj == noButton)
                noButton_ActionPerformed(actionevent);
        }

        SymAction()
        {
        }
    }


    public DeleteOccDialog(OccListFrame occlistframe, boolean flag)
    {
        super(occlistframe, flag);
        fComponentsAdjusted = false;
        frame = null;
        yesButton = new Button();
        noButton = new Button();
        label1 = new Label();
        frame = occlistframe;
        init();
    }

    public DeleteOccDialog(OccFrame occframe, boolean flag)
    {
        super(occframe, flag);
        fComponentsAdjusted = false;
        frame = null;
        yesButton = new Button();
        noButton = new Button();
        label1 = new Label();
        oframe = occframe;
        init();
    }

    void init()
    {
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
        label1.setText("Delete Occurence");
        label1.setAlignment(1);
        add(label1);
        label1.setBounds(78, 33, 180, 23);
        if(frame != null)
            setTitle("Delete: " + frame.occs.getSelectedItem() + "?");
        else
            setTitle("Delete: " + oframe.occ.getPitName() + "?");
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

    void yesButton_ActionPerformed()
    {
        if(oframe != null)
        {
            oframe.deleteOcc();
            oframe.dispose();
        } else
        {
            frame.deleteOcc();
            frame.dispose();
        }
        try
        {
            dispose();
        }
        catch(Exception exception)
        {
            System.out.println(exception.toString());
        }
    }

    void noButton_ActionPerformed(ActionEvent actionevent)
    {
        dispose();
    }

    void DeleteDialog_WindowClosing()
    {
        dispose();
    }

    OccFrame oframe;
    boolean fComponentsAdjusted;
    OccListFrame frame;
    Button yesButton;
    Button noButton;
    Label label1;
}
