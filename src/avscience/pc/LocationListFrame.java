package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.ppc.User;

public class LocationListFrame extends Frame
{
	private int width = 400;
	private int height = 420;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem addMenuItem = new java.awt.MenuItem();
	private MenuItem editMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	public Choice locations;
	avscience.ppc.User user;
	MainFrame mframe;
	
	public LocationListFrame(MainFrame mframe)
	{
		super("Snow Pilot - Locations");
		
	    this.mframe = mframe;
		user = mframe.getUser();
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(120, 120);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
        buildMenu();
		buildForm();
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if ( object==addMenuItem ) new LocationFrame(mframe, LocationListFrame.this, false);
            if ( object==editMenuItem )
            {
            	String lname = locations.getSelectedItem();
            	if ((lname!=null)&&(lname.trim().length()>0)) new LocationFrame(mframe, LocationListFrame.this, true);
            }
            if ( object==deleteMenuItem ) deleteLocation();
		}
	}
	
	void deleteLocation()
	{
		String lname = locations.getSelectedItem();
		if ((lname!=null)&&(lname.trim().length()>0))
		{
			mframe.store.removeLocation(lname);
			rebuildList();
			mframe.rebuildList();
		}
	}
	
	private void buildMenu()
    {
        addMenuItem.setLabel("Add Location");
        editMenuItem.setLabel("Edit Location");
        deleteMenuItem.setLabel("Delete Location");
    	menu.setLabel("Select..");
        menu.add(addMenuItem);
        menu.add(editMenuItem);
        menu.add(deleteMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        addMenuItem.addActionListener(new MenuAction());
        editMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
    }
    
    public void rebuildList()
	{
		locations.removeAll();
		String[] locs = mframe.store.getLocationNames();
		for ( int i = 0; i < locs.length; i++ )
		{
			if (locs[i]!=null) locations.add(locs[i]);
		}
	}
	
	void buildForm()
	{
		locations = new Choice();
		setLayout(null);
		String[] locs = mframe.store.getLocationNames();
		for ( int i = 0; i < locs.length; i++ )
		{
			if (locs[i]!=null) locations.add(locs[i]);
		}
		Label l = new Label("Locations:");
		l.setSize(180, 20);
		l.setLocation(100, 120);
		add(l);
		locations.setSize(180, 20);
		locations.setLocation(100, 160);
		add(locations);
	}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == LocationListFrame.this ) LocationListFrame.this.dispose();
		}
	}
	
}