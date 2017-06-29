package avscience.desktop;

import avscience.pc.SPV5DataStore;
import avscience.pc.PitFrame;
import avscience.ppc.PitObs;
import java.awt.*;
import java.awt.event.*;
import java.io.PrintStream;

public class DeletePitDialog extends Dialog
{
    
    private PitObs pit;
    private SPV5DataStore store;
    boolean fComponentsAdjusted;
    PitFrame frame;
    Button yesButton;
    Button noButton;
    Label label1;
    
    class SymWindow extends WindowAdapter
    {

        public void windowClosing(WindowEvent windowevent)
        {
            Object obj = windowevent.getSource();
            if(obj == DeletePitDialog.this)
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


    public DeletePitDialog(PitFrame pitframe, boolean flag, PitObs pitobs, SPV5DataStore datastore)
    {
        super(pitframe, flag);
        fComponentsAdjusted = false;
        frame = null;
        yesButton = new Button();
        noButton = new Button();
        label1 = new Label();
        pit = pitobs;
        store = datastore;
        frame = pitframe;
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
        label1.setText("Delete Pit");
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

    public DeletePitDialog(PitFrame pitframe, String s, boolean flag, PitObs pitobs, SPV5DataStore datastore)
    {
        this(pitframe, flag, pitobs, datastore);
        pit = pitobs;
        store = datastore;
        setTitle(s);
    }
    
    public DeletePitDialog(avscience.pc.PitListFrame frame, PitObs pit, SPV5DataStore datastore)
    {
        super(frame, true);
        this.pit = pit;
        store = datastore;
        setTitle("Delete Pit Observation");
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
            if(pit.getCrownObs())
                store.removeOcc(pit.getName().trim());
            store.removePit(pit.getName().trim());
            frame.setVisible(false);
            frame.dispose();
            dispose();
            frame.mf.saveData();
            frame.mf.rebuildList();
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
    
}
