package avscience.pc;
import java.awt.*;
import java.awt.event.*;
import avscience.ppc.PitObs;

public class DPDialog extends Dialog
{
	private avscience.ppc.PitObs pit;
	private avscience.pc.SPV5DataStore store;
	public DPDialog(avscience.pc.PitFrame parent)
	{
		super((Frame)parent, true);
		this.pit = parent.pit;
		this.store = parent.mf.store;
	
        frame = parent;
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
		label1.setText("Delete Pit");
		label1.setAlignment(java.awt.Label.CENTER);
		add(label1);
		label1.setBounds(78,33,180,23);
		setTitle("Avalanche Science - Delete Observation");

		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		SymAction lSymAction = new SymAction();
		noButton.addActionListener(lSymAction);
		yesButton.addActionListener(lSymAction);
	}

	public void addNotify()
	{
	    Dimension d = getSize();
	    
		super.addNotify();

		if (fComponentsAdjusted) return;

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

	public DPDialog(PitFrame parent, String title)
	{
		this(parent);
		setTitle(title);
	}
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

	boolean fComponentsAdjusted = false;
	avscience.pc.PitFrame frame = null;

	java.awt.Button yesButton = new java.awt.Button();
	java.awt.Button noButton = new java.awt.Button();
	java.awt.Label label1 = new java.awt.Label();

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
		try
		{
			if ( frame.webEdit )
			{
				frame.removeWebPit(pit);
				if ( frame.applet!=null) frame.applet.reinit();
			}
			else
			{
				if ( pit.getCrownObs() ) store.removeOcc( pit.getSerial());
				store.removePit(pit.getSerial());
				frame.mf.rebuildList();
			}
		    frame.setVisible(false);
		    frame.dispose();
            this.dispose();
		 }   
		 catch (Exception e) {System.out.println(e.toString());}
	}

	void noButton_ActionPerformed(java.awt.event.ActionEvent event)
	{
		this.dispose();
	}

	class SymWindow extends java.awt.event.WindowAdapter
	{
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == DPDialog.this)
			DeleteDialog_WindowClosing();
		}
	}

	void DeleteDialog_WindowClosing()
	{
		this.dispose();
	}

}
