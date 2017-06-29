
package avscience.pc;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import avscience.wba.*;


/** This the Quit Dialog for the Fabrix Viewer.*/
public class SaveDialog extends Dialog
{
    public SaveDialog(PitSumFrame parent)
    {
        super((Frame)parent, true);
        
        frame = parent;
        
        //{{INIT_CONTROLS
        setLayout(null);
        setSize(337,135);
        setVisible(false);
        yesButton.setLabel(" Yes ");
        add(yesButton);
        yesButton.setFont(new Font("Dialog", Font.BOLD, 12));
        yesButton.setBounds(72,80,79,22);
        noButton.setLabel("  No  ");
        add(noButton);
        noButton.setFont(new Font("Dialog", Font.BOLD, 12));
        noButton.setBounds(185,80,79,22);
        label1.setText("Save Changes?");
        label1.setAlignment(java.awt.Label.CENTER);
        add(label1);
        label1.setBounds(78,33,180,23);
        setTitle("Snow Pilot - Save Pit");
        //}}

        //{{REGISTER_LISTENERS
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
        SymAction lSymAction = new SymAction();
        noButton.addActionListener(lSymAction);
        yesButton.addActionListener(lSymAction);
        //}}
    }

    public void addNotify()
    {
        // Record the size of the window prior to calling parents addNotify.
        Dimension d = getSize();
        
        super.addNotify();

        if (fComponentsAdjusted)
            return;

        // Adjust components according to the insets
        setSize(getInsets().left + getInsets().right + d.width, getInsets().top + getInsets().bottom + d.height);
        Component components[] = getComponents();
        for (int i = 0; i < components.length; i++)
        {
            Point p = components[i].getLocation();
            p.translate(getInsets().left, getInsets().top);
            components[i].setLocation(p);
        }
        fComponentsAdjusted = true;
    }

    /**
     * Shows or hides the component depending on the boolean flag b.
     * @param b  if true, show the component; otherwise, hide the component.
     * @see java.awt.Component#isVisible
     */
    public void setVisible(boolean b)
    {
        if(b)
        {
            Rectangle bounds = getParent().getBounds();
            Rectangle abounds = getBounds();
    
            setLocation(bounds.x + (bounds.width - abounds.width)/ 2,
                 bounds.y + (bounds.height - abounds.height)/2);
            Toolkit.getDefaultToolkit().beep();
        }
        super.setVisible(b);
    }

    // Used for addNotify check.
    boolean fComponentsAdjusted = false;
    // Invoking frame
    PitSumFrame frame = null;

    //{{DECLARE_CONTROLS
    java.awt.Button yesButton = new java.awt.Button();
    java.awt.Button noButton = new java.awt.Button();
    java.awt.Label label1 = new java.awt.Label();
    //}}

    class SymAction implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent event)
        {
            Object object = event.getSource();
            if (object == yesButton)
                yesButton_ActionPerformed();
            else if (object == noButton)
                noButton_ActionPerformed(event);
        }
    }

    void yesButton_ActionPerformed()
    {
        try {
                frame.save();
            }   
            catch (Exception e) {System.out.println(e.toString());}
    }

    void noButton_ActionPerformed(java.awt.event.ActionEvent event)
    {
        frame.exit();
        this.dispose();
    }

    class SymWindow extends java.awt.event.WindowAdapter
    {
        public void windowClosing(java.awt.event.WindowEvent event)
        {
            Object object = event.getSource();
            if (object == SaveDialog.this)
            SaveDialog_WindowClosing();
        }
    }

    void SaveDialog_WindowClosing()
    {
    	//save();
        this.dispose();
    }

}
