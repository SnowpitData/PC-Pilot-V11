package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.ppc.User;

public class PreferencesFrame extends Frame
{
	private int width = 440;
	private int height = 540;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	MainFrame mframe;
	avscience.ppc.User user;
	Choice from;
	Choice depthUnits;
	Choice tempUnits;
	Choice elvUnits;
	Choice lonType;
	Choice latType;
	Choice rhoUnits;
	Choice state;
	Choice fractureChar;
	Choice coordType;
	Choice hardScale;

	private void buildMenu()
        {
            saveMenuItem.setLabel("Save preferences");
            menu.setLabel("Select..");
            menu.add(saveMenuItem);
            mainMenuBar.add(menu);
            setMenuBar(mainMenuBar);
            saveMenuItem.addActionListener(new MenuAction());
        }
    
    public PreferencesFrame(MainFrame mframe)
    {
    	super("Snow Pilot - Preferences");
	    this.mframe = mframe;
            if (mframe.macos) 
            {
                width = 600;
                height = 580;
            }
		user = mframe.getUser();
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(150, 150);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
                buildMenu();
		buildForm();
		popForm();
    }
    
    void updateUser()
    {
    	user.setMeasureFrom(from.getSelectedItem());
    	user.setDepthUnits(depthUnits.getSelectedItem());
    	user.setElvUnits(elvUnits.getSelectedItem());
    	user.setTempUnits(tempUnits.getSelectedItem());
    	user.setLongType(lonType.getSelectedItem());
    	user.setLatType(latType.getSelectedItem());
    	user.setRhoUnits(rhoUnits.getSelectedItem());
    	user.setState(state.getSelectedItem());
    	user.setFractureCategory(fractureChar.getSelectedItem());
    	user.setCoordType(coordType.getSelectedItem());
    	user.hardnessScaling=hardScale.getSelectedItem();
    	mframe.store.addUser(user);
    }
    
    void popForm(User u)
    {
    	this.user = u;
    	popForm();
    }
    
    void popForm()
    {
    	from.select(user.getMeasureFrom());
    	depthUnits.select(user.getDepthUnits());
    	elvUnits.select(user.getElvUnits());
    	latType.select(user.getLatType());
    	lonType.select(user.getLongType());
    	rhoUnits.select(user.getRhoUnits());
    	tempUnits.select(user.getTempUnits());
    	state.select(user.getState());
    	fractureChar.select(user.fractureCat);
    	coordType.select(user.getCoordType());
    	hardScale.select(user.hardnessScaling);
    }
    
    void buildForm()
    {
    	int ys=78;
    	int x=24;
    	int y=ys;
    	int vspace=24;
    	int hspace=180;
    	int i=0;
    	Label l = new Label("User: "+user.getName());
    	l.setLocation(x, y);
    	l.setSize(180, 20);
    	add(l);
    	y+=vspace;
    	
    	String[] tb = new String[2];
    	tb[0]="top";
    	tb[1]="bottom";
    	from = new Choice();
    	
    	for ( i=0; i<tb.length; i++ )
    	{
    		from.add(tb[i]);
    	}
    	////
    	String[] dpt = new String[2];
    	dpt[0]="cm";
    	dpt[1]="inches";
    	depthUnits = new Choice();
    	
    	for ( i=0; i<dpt.length; i++ )
    	{
    		depthUnits.add(dpt[i]);
    	}
    	////
    	String[] elv = new String[2];
    	elv[0]="m";
    	elv[1]="ft";
    	elvUnits = new Choice();
    	
    	for ( i=0; i<elv.length; i++ )
    	{
    		elvUnits.add(elv[i]);
    	}
    	
    	String[] lt = new String[2];
    	lt[0]="N";
    	lt[1]="S";
    	latType = new Choice();
    	
    	for ( i=0; i<lt.length; i++ )
    	{
    		latType.add(lt[i]);
    	}
    	
    	String[] lo = new String[2];
    	lo[0]="W";
    	lo[1]="E";
    	lonType = new Choice();
    	
    	for ( i=0; i<lo.length; i++ )
    	{
    		lonType.add(lo[i]);
    	}
    	
    	String[] tp = new String[2];
    	tp[0]="C";
    	tp[1]="F";
    	tempUnits = new Choice();
    	
    	for ( i=0; i<tp.length; i++ )
    	{
    		tempUnits.add(tp[i]);
    	}
    	
    	String[] rh = new String[2];
    	rh[0]="kg/cubic_m";
    	rh[1]="lbs/cubic_ft";
    	rhoUnits = new Choice();
    	
    	for ( i=0; i<tp.length; i++ )
    	{
    		rhoUnits.add(rh[i]);
    	}
    	
    	fractureChar = new Choice();
    	fractureChar.add("Fracture Character");
    	fractureChar.add("Shear Quality");
    	fractureChar.select("Shear Quality");
    	
    	hardScale = new Choice();
    	hardScale.add("linear");
    	hardScale.add("exponential");
    	hardScale.select("linear");
    	
    	Label l1 = new Label("Depth zero from");
    	l1.setLocation(x, y);
    	l1.setSize(180, 20);
    	add(l1);
    	
    	y+=vspace;
    	from.setLocation(x, y);
    	from.setSize(100, 32);
    	add(from);
    	
    	y+=1.4*vspace;
    	Label l2= new Label("Depth Units");
    	l2.setLocation(x, y);
    	l2.setSize(180, 20);
    	add(l2);
    	
    	y+=vspace;
    	depthUnits.setLocation(x, y);
    	depthUnits.setSize(100, 32);
    	add(depthUnits);
    	
        y+=1.4*vspace;
    	//y+=vspace;
    	Label l3= new Label("Temp Units");
    	l3.setLocation(x, y);
    	l3.setSize(180, 20);
    	add(l3);
    	    	
    	y+=vspace;
    	tempUnits.setLocation(x, y);
    	tempUnits.setSize(80, 32);
    	add(tempUnits);
    	
    	state=new Choice();
    	String[] states = new StateProv().getList();
    	for (i=0; i<states.length; i++)
    	{
    		state.add(states[i]);
    	}
    	
    	//y+=vspace;
        y+=1.4*vspace;
    	Label sp = new Label("State/Prov");
    	sp.setLocation(x, y);
    	sp.setSize(180, 20);
    	add(sp);
    	y+=vspace;
    	state.setLocation(x, y);
    	state.setSize(112, 32);
    	add(state);
    	state.select("MT");
    	//y+=vspace;
        y+=1.4*vspace;
    	
    	Label ctl = new Label("Coordinate Type");
    	ctl.setLocation(x, y);
    	ctl.setSize(180, 20);
    	add(ctl);
    	y+=vspace;
    	String[] ctypes = {"Lat/Lon", "UTM"};
    	coordType = new Choice();
    	
    	for (int j=0;j<ctypes.length;j++)
    	{
    		coordType.add(ctypes[j]);
    	}
        coordType.setSize(100, 32);
    	coordType.setLocation(x, y);
    	add(coordType);
    	
    	y=ys;
       /// y+=1.4*vspace;
    	x+=hspace;
    	
    //	y+=vspace;
    	Label l4= new Label("Elevation Units");
    	l4.setLocation(x, y);
    	l4.setSize(180, 20);
    	add(l4);
    	
    	y+=vspace;
    	elvUnits.setLocation(x, y);
    	elvUnits.setSize(80, 32);
    	add(elvUnits);
    	
    	//y+=vspace;
        y+=1.4*vspace;
    	Label l5= new Label("Longitude type");
    	l5.setLocation(x, y);
    	l5.setSize(180, 20);
    	add(l5);
    	
    	y+=vspace;
    	lonType.setLocation(x, y);
    	lonType.setSize(80, 32);
    	add(lonType);
    	
    	//y+=vspace;
        y+=1.4*vspace;
    	Label l6= new Label("Latitude type");
    	l6.setLocation(x, y);
    	l6.setSize(180, 20);
    	add(l6);
    	
    	y+=vspace;
    	latType.setLocation(x, y);
    	latType.setSize(60, 32);
    	add(latType);
    	
    	///y+=vspace;
        y+=1.4*vspace;
    	Label l7= new Label("Density units");
    	l7.setLocation(x, y);
    	l7.setSize(180, 20);
    	add(l7);
    	
    	y+=vspace;
    	rhoUnits.setLocation(x, y);
    	rhoUnits.setSize(180, 32);
    	add(rhoUnits);
        y+=1.4*vspace;
    	//y+=vspace;
    	
    ///	String[] fracs = {"Fracture Character", "Shear Quality"};
    	Label fcl = new Label("Fracture Category");
    	fcl.setLocation(x, y);
    	fcl.setSize(100, 26);
    	add(fcl);
    	y+=vspace+8;
    	
    	fractureChar.setLocation(x, y);
    	fractureChar.setSize(180, 20);
    	add(fractureChar);
    	y+=vspace+8;
    	
    	Label hll = new Label("Hardness Scale");
    	hll.setLocation(x, y);
    	hll.setSize(100, 26);
    	add(hll);
    	y+=vspace+8;
    	
    	hardScale.setLocation(x, y);
    	hardScale.setSize(162, 20);
    	add(hardScale);
    }
    
    
    class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == PreferencesFrame.this) 
			{
				PreferencesFrame.this.save();
				PreferencesFrame.this.dispose();
			}
			
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if ( object==saveMenuItem ) save();
		}
	}
	
	void save()
	{
		updateUser();
		dispose();
	}
    
}