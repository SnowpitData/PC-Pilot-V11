package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.ppc.User;

public class TempFrame extends Frame
{
	private int width = 300;
	private int height = 320;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem addMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	
	Choice temps;
	Choice points;
	Button add;
	DepthTextItem depth;
	
	PitHeaderFrame pframe;
	MainFrame mframe;
	int vspace = 30;
	avscience.ppc.User user;
	
	void buildForm()
	{
		int ys=56;
    	int x=24;
    	int y=ys;
    	int vspace=30;
    	int hspace=180;
    	int i=0;
    	Label l = new Label("User: "+user.getName());
    	l.setLocation(x, y);
    	l.setSize(180, 20);
    	add(l);
    	y+=vspace;
		depth = new DepthTextItem("Depth "+user.getDepthUnits(), x, y);
		add(depth);
		y+=vspace;
		Label t = new Label("Temperature "+user.getTempUnits());
    	t.setLocation(x, y);
    	t.setSize(180, 20);
    	add(t);
		y+=vspace;
		temps = new Choice();
		String[] list = TempList.getInstance().getList(user.getTempUnits());
		for (i=0; i<list.length; i++ )
		{
			temps.add(list[i]);
		}
		temps.setLocation(x, y);
		temps.setSize(80, 20);
		add(temps);
		y+=vspace;
		Label cp = new Label("Current Points");
    	cp.setLocation(x, y);
    	cp.setSize(180, 20);
    	add(cp);
    	y+=vspace;
    	
    	points = new Choice();
    	points.setLocation(x, y);
    	points.setSize(180, 20);
    	add(points);
    	y+=vspace;
    	add = new Button("Add Point");
    	add.setLocation(x, y);
    	add.setSize(78, 28);
    	add(add);
    	
    	
    	avscience.ppc.PitObs pit = pframe.getPit();
    	TempProfile tp = pit.getTempProfile();
    	
    	if ( tp!=null )System.out.println(tp);
    	if ((tp!=null) && (tp.hasPoints()))
    	{
    		String[] pts=tp.getPoints();
    		for (i=0; i<pts.length; i++)
    		{
    			points.add(pts[i]);
    		}
    	}
    	add.addActionListener(new MenuAction());
	}
	
	void deletePoint()
	{
		avscience.ppc.PitObs pit = pframe.getPit();
		String p = points.getSelectedItem();
        int end = p.indexOf("Temp");
        int start = 6;
        String depth = p.substring(start, end);
        depth = depth.trim();
        TempProfile tp = pit.getTempProfile();
        tp.removePoint(depth);
        pframe.getPit().setTempProfile(tp);
        points.remove(p);
	}
	
	void addPoint()
	{
		avscience.ppc.PitObs pit = pframe.getPit();
		String temp = temps.getSelectedItem();
		String dpth = depth.getText();
		int d = new Integer(dpth).intValue();
		TempProfile tp=null;
		String point = new String("Depth " + dpth + " Temp " + temp);
        if ( (! pit.getTempProfile().hasPoints())||(pit.getTempProfile()==null) )
        {
            tp = new TempProfile(user.getTempUnits(), user.getDepthUnits());
            tp.addPoint(d, temp);
            pframe.getPit().setTempProfile(tp);
            points.add(point);
        }
        else
        {
            tp = pit.getTempProfile();
            boolean newPoint = tp.addPoint(d, temp);
            if ( newPoint ) points.add(point);
            
        }
        pframe.getPit().setTempProfile(tp);
        depth.setText("");
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
	public TempFrame(MainFrame mframe, PitHeaderFrame pframe)
	{
		super("Snow Pilot - Temperature Profile");
	    this.mframe = mframe;
	    this.pframe=pframe;
		user = pframe.getPit().getUser();
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(80, 240);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
        buildMenu();
		buildForm();
	}
	
	private void buildMenu()
    {
        addMenuItem.setLabel("Add Point");
        deleteMenuItem.setLabel("Delete Point");
    	menu.setLabel("Select..");
        menu.add(addMenuItem);
        menu.add(deleteMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        addMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == TempFrame.this ) TempFrame.this.dispose();
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if (( object==addMenuItem ) || ( object==add)) addPoint();
            if ( object==deleteMenuItem ) deletePoint();
		}
	}
	
	
	
}	
	