package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.ppc.User;
import avscience.ppc.XMLReader;

public class PitListFrame extends Frame implements ListFrame
{
	private int width = 400;
	private int height = 420;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem editMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	private MenuItem importMenuItem = new java.awt.MenuItem();
	public Choice pits;
	avscience.ppc.User user;
	MainFrame mframe;

	public PitListFrame(MainFrame mframe)
	{
		super("Snow Pilot - Edit Pits");
		
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
	
	
	void deletePit()
	{
		int idx = pits.getSelectedIndex();
		mframe.store.deletePit(idx);
		rebuildList();
		mframe.rebuildList();
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if ( object==editMenuItem ) 
			{
				avscience.ppc.PitObs pit = mframe.store.getPit(pits.getSelectedIndex());
				if ( pit==null )
				{
					System.out.println("Pit Null.");
					String serial = mframe.store.getNewSerial();
					pit = new avscience.ppc.PitObs(user, serial, mframe.bld, mframe.version);
				}
				
				mframe.showPitHeaderFrame(true, pit, PitListFrame.this);
			}
			if ( object==deleteMenuItem ) 
                        {
                            avscience.ppc.PitObs pit = mframe.store.getPit(pits.getSelectedIndex());
                            new DeletePitDialog(PitListFrame.this, pit, mframe.store);
                        }
			if ( object==importMenuItem )
			{
				try 
			       {
			            FileDialog dialog = new FileDialog(PitListFrame.this, "Import Pit from XML", FileDialog.LOAD);
			            dialog.setFilenameFilter(new SPFileFilter());
			            dialog.setVisible(true);
			            
			            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
			            {
			                File f = new File(dialog.getDirectory()+"//"+dialog.getFile());
			                XMLReader reader = new XMLReader();
			                avscience.ppc.PitObs pit = reader.getPit(f);
			                
			                if ( pit!=null ) 
			                {
                                            mframe.store.addPit(pit);
                                            new PitFrame(pit, mframe, false);
                                            rebuildList();
                                            mframe.rebuildList();
			                }
				      }
				     }
				     catch (Exception e) {e.printStackTrace();}
			}
		}
	}
	
	private void buildMenu()
    {
        editMenuItem.setLabel("Edit Pit");
        deleteMenuItem.setLabel("Delete Pit");
        importMenuItem.setLabel("Import Pit");
    	menu.setLabel("Select..");
        menu.add(editMenuItem);
        menu.add(deleteMenuItem);
        menu.add(importMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        editMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
        importMenuItem.addActionListener(new MenuAction());
    }
    
    public void rebuildList()
	{
		pits.removeAll();
		String[] pts = mframe.store.getPitNames(false);
		for ( int i = 0; i < pts.length; i++ )
		{
			pits.add(pts[i]);
		}
	}
	
	void buildForm()
	{
		pits = new Choice();
		setLayout(null);
		String[] pts = mframe.store.getPitNames(false);
		for ( int i = 0; i < pts.length; i++ )
		{
			pits.add(pts[i]);
		}
		Label l = new Label("Pits:");
		l.setSize(180, 20);
		l.setLocation(100, 120);
		add(l);
		pits.setSize(180, 20);
		pits.setLocation(100, 160);
		add(pits);
	}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == PitListFrame.this ) PitListFrame.this.dispose();
		}
	}
	
	public class SPFileFilter implements FilenameFilter
	{
	   public boolean accept(File dir, String file)
	   {
	       file = file.trim();
	       String end = file.substring(file.length()-4, file.length());
	       if ( end.equals(".xml")) return true;
	       else return false;
	   }
    }
	
}