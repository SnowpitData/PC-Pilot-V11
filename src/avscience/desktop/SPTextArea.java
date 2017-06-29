package avscience.desktop;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import avscience.desktop.action.*;

public class SPTextArea extends JTextArea 
{
	
	public SPTextArea()
	{
		super();
		initialize();
	}
	
	protected void initialize()
    {
        this.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent evt)
            {
                requestFocus();
                dealWithMousePress(evt);
            }
        });
    }
	
	protected void dealWithMousePress(MouseEvent evt)
    {
        if(SwingUtilities.isRightMouseButton(evt))
        {
            JPopupMenu menu = new JPopupMenu();
            menu.add(new CutAction(this));
            menu.add(new CopyAction(this));
            menu.add(new PasteAction(this));
            menu.add(new DeleteAction(this));
            menu.addSeparator();
            menu.add(new SelectAllAction(this));
            Point pt = SwingUtilities.convertPoint(evt.getComponent(), evt.getPoint(), this);
            menu.show(this, pt.x, pt.y);
        }
    }
}