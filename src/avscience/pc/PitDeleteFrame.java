/**
 * @(#)PitDeleteFrame.java
 *
 *
 * @author 
 * @version 1.00 2009/9/30
 */
 
package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import avscience.ppc.*;

public class PitDeleteFrame extends Frame
{
	private int width = 360;
	private int height = 620;
	MainFrame mf;
	ScrollPane sp = new ScrollPane();
	Panel p = new Panel();
    Button deleteSelected = new Button("Delete Pits");
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem selectAllMenuItem = new java.awt.MenuItem("Select All");
	private MenuItem selectNoneMenuItem = new java.awt.MenuItem("Select None");
	
    public PitDeleteFrame(MainFrame mf)
    {
    	this.mf=mf;
    	buildMenu();
    	buildForm();
    }
    
    class window extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == PitDeleteFrame.this) PitDeleteFrame.this.dispose();
		}
	}
	
    
    void buildForm()
    {
    	setTitle("Delete Pits");
    	setSize(width, height);
    	deleteSelected.setSize(180, 24);
    	deleteSelected.setLocation(80, height-72);
    	p.setSize(width-82, height-248);
    	sp.setSize(width-60, height-240);
    	sp.setLocation(24, 120);
    	Label pl = new Label("Current Pits");
    	pl.setSize(200, 20);
    	pl.setLocation(24,90);
    	setLayout(null);
    	p.setLayout(null);
    	int ys=56;
    	int x=24;
    	int y=ys;
    	int vspace=24;
    	String[] pts = mf.store.getPitNames(false);
		for (int i = 1; i < pts.length; i++ )
		{
			Checkbox cb = new Checkbox(pts[i]);
			cb.setLocation(x, y);
			cb.setSize(240, 20);
			p.add(cb);
			y+=vspace;
		}
		add(deleteSelected);
		add(pl);
		sp.add(p);
		add(sp);
		addWindowListener(new window());
    }
    
    void selectAll()
    {
    	Component[] comps = p.getComponents();
    	for (int i=0; i<comps.length; i++ )
    	{
   			Component c = comps[i];
   			if ( c instanceof Checkbox )
   			{
   				Checkbox cb = (Checkbox) c;
   				cb.setState(true);
   			}
    	}
    }
    
    void selectNone()
    {
    	Component[] comps = p.getComponents();
    	for (int i=0; i<comps.length; i++ )
    	{
   			Component c = comps[i];
   			if ( c instanceof Checkbox )
   			{
   				Checkbox cb = (Checkbox) c;
   				cb.setState(false);
   			}
    	}
    }
    
    public int getNumberOfSelected()
    {
    	int count=0;
    	Component[] comps = p.getComponents();
    	for (int i=0; i<comps.length; i++ )
    	{
   			Component c = comps[i];
   			if ( c instanceof Checkbox )
   			{
   				Checkbox cb = (Checkbox) c;
   				if (cb.getState())	count++;	 		
   			}
    	}
    	return count;
    }
    
    void deleteSelected()
    {
    	Component[] comps = p.getComponents();
    	for (int i=0; i<comps.length; i++ )
    	{
   			Component c = comps[i];
   			if ( c instanceof Checkbox )
   			{
   				Checkbox cb = (Checkbox) c;
   				if (cb.getState())
   				{
   					String name = cb.getLabel();
   					avscience.ppc.PitObs pit = mf.store.getPitByName(name);
   					mf.store.removePit(pit.getSerial()); 
   				}				
   			}
    	}
    }
    
    void buildMenu()
    {
    	menu.setLabel("Select..");
    	menu.add(selectAllMenuItem);
    	menu.add(selectNoneMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
    	MenuAction mnac = new MenuAction();
    	selectAllMenuItem.addActionListener(mnac);
    	selectNoneMenuItem.addActionListener(mnac);
    	deleteSelected.addActionListener(mnac);
    }
    
    void deletePits()
    {
    	new DPMDialog(this).setVisible(true);
    }
    
    class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object o = event.getSource();
			if (o==selectAllMenuItem)
			{
				selectAll();
			}
			if (o==selectNoneMenuItem)
			{
				selectNone();
			}
			
			if (o==deleteSelected) deletePits();
		}
	}
    
}