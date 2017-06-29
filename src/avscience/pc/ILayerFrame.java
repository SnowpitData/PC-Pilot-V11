package avscience.pc;
import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import avscience.ppc.User;
import avscience.ppc.Layer;

public class ILayerFrame extends Frame
{
	private int width = 464;
	private int height = 280;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	private MenuItem exitMenuItem = new java.awt.MenuItem();
	
	MainFrame mframe;
	PitHeaderFrame pframe;
///	LayerFrame lframe;
	int vspace = 38;
	Choice layers;
	
	CheckboxGroup iLayer;
	Checkbox iStart;
	Checkbox iEnd;
	Checkbox iNone;
	
	Label msg = new Label();
	avscience.ppc.User user;
	Font font = new Font(null, Font.BOLD, 15);
	
	int currentLayerNum;
	
	void initControls()
	{
		msg.setSize(340, 20);
		layers = new Choice();
	}
	
	public void save()
	{
		String lnum = layers.getSelectedItem();
		avscience.ppc.Layer l = pframe.getPit().getLayerByString(lnum);
		if ( l!= null )
		{
			String start = l.getStartDepthString();
			String end = l.getEndDepthString();
			String ln = l.getLayerNumberString();
		
		
			if ( iStart.getState() )
        	{
        		pframe.getPit().setProblemInterface(start, ln);
        	}
        
        	else if ( iEnd.getState() )
        	{
        		pframe.getPit().setProblemInterface(end, ln);
        	}
        	else pframe.getPit().setProblemInterface("", ln);
		}
		
		pframe.saveWO();
		super.dispose();
	}
	
	public void exit()
	{
		super.dispose();
	}
	
	public void dispose()
	{
		save();
		super.dispose();
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if ( object == saveMenuItem) save();
			if ( object == exitMenuItem) exit();
		}
	}
	
	void buildForm()
	{
		System.out.println("build form.");
		removeAll();
		buildMenu();
		int ys=56;
    	int x=52;
    	int y=ys;
    	int vspace=24;
    	
    	Label ml = new Label("You have not selected your layer of greatest concern!");
    	ml.setFont(font);
    	ml.setSize(width-20, 20);
    	ml.setLocation(x, y);
    	add(ml);
    	y+=vspace;
    	
    	Label mll = new Label("Please select below.");
    	mll.setFont(font);
    	mll.setSize(width-20, 20);
    	mll.setLocation(x, y);
    	add(mll);
    	
    	y+=vspace;
    	
    	Label l = new Label("Current Layers");
    	l.setSize(220, 20);
    	l.setLocation(x, y);
    	add(l);
    	y+=vspace;
    	layers.setSize(240, 20);
    	layers.setLocation(x, y);
    	layers.removeAll();
    	String[] lys = pframe.getPit().getLayerStrings();
    	layers.add(" ");
    	for ( int i=0; i<lys.length; i++ )
    	{
    		layers.add(lys[i]);
    	}
    	add(layers);
    	y+=vspace;
	    Label nl = new Label("What part of the layer?");
	    nl.setSize(220, 18);
	    nl.setLocation(x,y);
	    nl.setVisible(true);
	    add(nl);
		y+=vspace;
		iLayer = new CheckboxGroup();
		
		if ( user.getMeasureFrom().equals("bottom"))
		{
			iStart = new Checkbox("Bottom", iLayer, false);
			iEnd = new Checkbox("Top", iLayer, false);
		}
		else
		{
			iStart = new Checkbox("Top", iLayer, false);
			iEnd = new Checkbox("Bottom", iLayer, false);
		}
		
		iNone = new Checkbox("Don't know", iLayer, true);
		
		iStart.setLocation(x, y);
		iStart.setSize(80, 20);
		iStart.setVisible(true);
		
		iEnd.setLocation(x+82, y);
		iEnd.setSize(80, 20);
		iEnd.setVisible(true);
		
		iNone.setLocation(x+2*82, y);
		iNone.setSize(80, 20);
		iNone.setVisible(true);
		
		add(iStart);
		add(iEnd);
		add(iNone);
	 	y+=vspace;
	 	y+=vspace;
    	msg.setLocation(x, y);
    	msg.setVisible(false);
    	add(msg);
	 	Label u = new Label("User: "+user.getName());
    	u.setLocation(x, y);
    	u.setSize(126, 20);
    	add(u);
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
	
	void displayMsg(String message)
	{
		msg.setSize(240, 20);
		msg.setText(message);
		msg.setVisible(true);
		try
		{
			Thread.sleep(1200);
		}
		catch(Exception e){System.out.println(e.toString());}
		msg.setVisible(false);
	}
	
	public ILayerFrame(LayerFrame lframe)
	{
		super("Snow Pilot - Interface Layer");
	    this.mframe = lframe.mframe;
	    this.pframe=lframe.pframe;
	   //// this.lframe=lframe;
		user = pframe.getPit().getUser();
		setLayout(null);
		if (mframe.smallscreen) height=600;
		this.setSize(width, height);
		this.setLocation(200, 160);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//
	
        saveMenuItem.setLabel("Save");
        exitMenuItem.setLabel("Exit");
    	menu.setLabel("Select..");
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        saveMenuItem.addActionListener(new MenuAction());
        exitMenuItem.addActionListener(new MenuAction());
        //
        initControls();
		buildForm();
		buildMenu();
	}
	
	public ILayerFrame(MainFrame mframe, PitHeaderFrame pframe)
	{
		super("Snow Pilot - Interface Layer");
	    this.mframe = mframe;
	    this.pframe=pframe;
	   //// this.lframe=lframe;
		user = pframe.getPit().getUser();
		setLayout(null);
		if (mframe.smallscreen) height=600;
		this.setSize(width, height);
		this.setLocation(200, 160);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		//
	
        exitMenuItem.setLabel("Exit");
        saveMenuItem.setLabel("Save");
       
    	menu.setLabel("Select..");
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        exitMenuItem.addActionListener(new MenuAction());
        saveMenuItem.addActionListener(new MenuAction());
        //
        initControls();
		buildForm();
		buildMenu();
	}
	
	
	void buildMenu()
    {
		saveMenuItem.addActionListener(new MenuAction());
		exitMenuItem.addActionListener(new MenuAction());
    	menu.add(saveMenuItem);
    	menu.add(exitMenuItem);
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == ILayerFrame.this )
			{
				ILayerFrame.this.dispose();
			}
		}
	}
	
	
}	
	