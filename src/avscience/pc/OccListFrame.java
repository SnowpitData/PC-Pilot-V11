package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import avscience.ppc.AvOccurence;
import avscience.ppc.User;

public class OccListFrame extends Frame implements ListFrame
{
	private int width = 400;
	private int height = 420;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem editMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	public Choice occs;
	avscience.ppc.User user;
	MainFrame mframe;

	public OccListFrame(MainFrame mframe)
	{
		super("Snow Pilot - Edit Occurences");
		
	    this.mframe = mframe;
		user = mframe.getUser();
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(170, 170);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
        buildMenu();
		buildForm();
		rebuildList();
		occs.addItemListener(new OccListener());
	}
	
	
	void deleteOcc()
	{
		int idx = occs.getSelectedIndex();
		mframe.store.deleteOcc(idx);
		mframe.rebuildList();
		rebuildList();
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if ( object==editMenuItem ) editOcc();
			if ( object==deleteMenuItem ) new DeleteOccDialog(OccListFrame.this, true).setVisible(true);
		}
	}
	
	class OccListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		if ( e.getItemSelectable()==occs ) editOcc();
    	
    	}
    }
	
	void editOcc()
	{
		avscience.ppc.AvOccurence occ = mframe.store.getOcc(occs.getSelectedIndex());
		if ( occ!=null ) mframe.showOccFrame(occ);
	}
	
	private void buildMenu()
    {
        editMenuItem.setLabel("Edit Occurence");
        deleteMenuItem.setLabel("Delete Occurence");
    	menu.setLabel("Select..");
        menu.add(editMenuItem);
        menu.add(deleteMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        editMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
    }
    
    public void rebuildList()
	{
		occs.removeAll();
		String[] pts = mframe.store.getOccNames();
		for ( int i = 0; i < pts.length; i++ )
		{
			occs.add(pts[i]);
		}
	}
	
	void buildForm()
	{
		occs = new Choice();
		setLayout(null);
		String[] pts = mframe.store.getPitNames(false);
		for ( int i = 0; i < pts.length; i++ )
		{
			occs.add(pts[i]);
		}
		Label l = new Label("Occurences:");
		l.setSize(180, 20);
		l.setLocation(100, 120);
		add(l);
		occs.setSize(180, 20);
		occs.setLocation(100, 160);
		add(occs);
	}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == OccListFrame.this ) OccListFrame.this.dispose();
		}
	}
	
}