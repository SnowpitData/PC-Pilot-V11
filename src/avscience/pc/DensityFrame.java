package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;

public class DensityFrame extends Frame
{
	private int width = 300;
	private int height = 320;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem addMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	
	RhoTextItem rho;
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
	
		rho = new RhoTextItem("Density "+user.getRhoUnits(), user.getRhoUnits(), x, y);
		add(rho);
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
    	DensityProfile tp = pit.getDensityProfile();
    	
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
        int end = p.indexOf("Density");
        int start = 6;
        String depth = p.substring(start, end);
        depth = depth.trim();
        DensityProfile tp = pit.getDensityProfile();
        tp.removePoint(depth);
        pframe.getPit().setDensityProfile(tp);
        points.remove(p);
	}
	
	void addPoint()
	{	
		avscience.ppc.PitObs pit = pframe.getPit();
		String rh = rho.getText();
		String dpth = depth.getText();
		int d = new Integer(dpth).intValue();
		String point = new String("Depth " + dpth + " Density " + rh);
		DensityProfile tp=null;
        if ( ! pit.getDensityProfile().hasPoints() )
        {
            tp = new DensityProfile(user.getRhoUnits(), user.getDepthUnits());
            tp.addPoint(d, rh);
            points.add(point);
        }
        else
        {
            tp = pit.getDensityProfile();
            boolean newPoint = tp.addPoint(d, rh);
            if ( newPoint ) points.add(point);
        }
        pframe.getPit().setDensityProfile(tp);
        depth.setText("");
        rho.setText("");
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
	public DensityFrame(MainFrame mframe, PitHeaderFrame pframe)
	{
		super("Snow Pilot - Density Profile");
	    this.mframe = mframe;
	    this.pframe=pframe;
		user = pframe.getPit().getUser();
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(140, 210);
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
			if (object == DensityFrame.this ) DensityFrame.this.dispose();
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
	