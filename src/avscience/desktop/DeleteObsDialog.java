package avscience.desktop;

import avscience.ppc.PitObs;
import java.awt.*;
import java.awt.event.*;
import avscience.pc.SPV5DataStore;

public class DeleteObsDialog extends Dialog
{
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == DeleteObsDialog.this)
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


    public DeleteObsDialog(OccFrame occframe, boolean flag, PitObs pitobs, SPV5DataStore datastore)
    {
        super(occframe, flag);
        fComponentsAdjusted = false;
        frame = null;
        yesButton = new Button();
        noButton = new Button();
        label1 = new Label();
        pit = pitobs;
        store = datastore;
        frame = occframe;
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
        label1.setText("Delete Observation");
        label1.setAlignment(1);
        add(label1);
        label1.setBounds(78, 33, 180, 23);
        setTitle("Avalanche Science - Delete Observation");
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

    public DeleteObsDialog(OccFrame occframe, String s, boolean flag, PitObs pitobs, SPV5DataStore datastore)
    {
        this(occframe, flag, pitobs, datastore);
        pit = pitobs;
        store = datastore;
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

    void yesButton_ActionPerformed()
    {
        try
        {
            store.removePit(pit.getName().trim());
            store.removeOcc(pit.getName().trim());
            pit = null;
            frame.mf.saveData();
            frame.getPitFrame().setVisible(false);
            frame.getPitFrame().dispose();
            frame.setVisible(false);
            frame.dispose();
            frame = null;
            System.gc();
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

    private PitObs pit;
    private SPV5DataStore store;
    boolean fComponentsAdjusted;
    OccFrame frame;
    Button yesButton;
    Button noButton;
    Label label1;
}
