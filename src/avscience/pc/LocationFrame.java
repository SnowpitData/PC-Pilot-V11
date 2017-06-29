package avscience.pc;

import java.awt.*;
import avscience.desktop.*;
import avscience.ppc.Location;
import avscience.ppc.User;

public class LocationFrame extends Frame
{
	private int width = 300;
	private int height = 320;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	NoteTextItem name;
	Choice state = new Choice();
	TextItem range;
	ElvTextItem elv;
	LonTextItem lon;
	LatTextItem lat;
	TextItem id;
	UTMTextItem east;
	UTMTextItem north;
	TextItem utmZone;
	LocationListFrame lframe;
	MainFrame mframe;
	int vspace = 36;
	avscience.ppc.User user;
	boolean utm=false;
	
	void popForm(Location l)
	{
		System.out.println("popForm(): ");
		if (l!=null)
		{
			name.setText(l.getName());
			range.setText(l.getRange());
			state.select(l.getState());
			System.out.println("elv: "+l.getElv());
			elv.setText(l.getElv());
			if ( utm )
			{
                            utmZone.setText(l.zone);
                            east.setText(l.east);
                            north.setText(l.north);
			}
			else
			{
                            lon.setText(l.getLongitude());
                            lat.setText(l.getLat());
			}
			id.setText(l.getID());
		}
		else System.out.println("Location is null.");
	}
	
	Location getCurrentLocation()
	{
		System.out.println("getCurrentLocation.");
		Location l = mframe.store.getLocation(lframe.locations.getSelectedItem());
		if (l==null) System.out.println("Locaton is null!!");
		return l;
	}
	
	Location getLocationFromForm()
	{
		avscience.ppc.Location loc = new avscience.ppc.Location();
                try
                {
                    if (utm) loc = new Location(user, name.getText().trim(), state.getSelectedItem(), range.getText().trim(), utmZone.getText(), east.getText(), north.getText(), elv.getText(), id.getText().trim());
                    else loc = new Location(user, name.getText().trim(), state.getSelectedItem(), range.getText().trim(), lat.getText(), lon.getText(), elv.getText(), id.getText().trim());
                }
                catch(Exception e)
                {
                    System.out.println(e.toString());
                }
                
		return loc;
	}
    
	void buildForm()
	{
		System.out.println("buildForm()");
		int ys=56;
    	int x=24;
    	int y=ys;
    	int vspace=24;
    	int hspace=180;
    	Label l = new Label("User: "+user.getName());
    	l.setLocation(x, y);
    	l.setSize(180, 20);
    	add(l);
    	y+=vspace;
		name = new NoteTextItem("Location", x, y, 20);
		name.setLabel("Location");
		name.setMaxLength(20);
		add(name);
		y+=vspace;
		
		Label sl = new Label("State/Prov");
        sl.setSize(78, 20);
        sl.setVisible(true);
        sl.setLocation(x, y);
        add(sl);
        
        
        String[] states = new StateProv().getList();
        for (int i=0;i<states.length;i++)
        {
        	state.add(states[i]);
        }
        state.setLocation(x+82, y);
        state.setSize(160,20);
        state.setVisible(true);
		add(state);
		state.select(user.getState());
		y+=vspace;
		range = new TextItem("Mtn Range", x, y);
		add(range);
		y+=vspace;
		elv = new ElvTextItem("Elevation "+user.getElvUnits(), user.getElvUnits(), x, y);
		add(elv);
		y+=vspace;
		if (utm)
		{
			utmZone = new TextItem("UTM Zone", x, y);
			add(utmZone);
			y+=vspace;
			east = new UTMTextItem("East:", x, y);
			add(east);
			y+=vspace;
			north = new UTMTextItem("North:", x, y);
			add(north);
		}
		else
		{
			lon = new LonTextItem("Lon. "+user.getLongType(), x, y);
			add(lon);
			y+=vspace;
			lat = new LatTextItem("Lat. "+user.getLatType(), x, y);
			add(lat);
		}
		
		y+=vspace;
		id = new TextItem("ID #", x, y);
	}
	
	public void saveLocation()
	{
                System.out.println("Save Location:: ");
		if ( name.getText().trim().length()>0)
		{
                    System.out.println("Getting  location from form:");
                    Location l = getLocationFromForm();
                    if ( l==null ) System.out.println("Layer is NULL !!");
                    else
                    {
                        String json = l.toString();
                        System.out.println("JSON:: "+json);
                        System.out.println("Adding Location:: ");
                        mframe.store.addLocation(l);
                        mframe.rebuildList();
                        lframe.rebuildList();
                    }
		}
		dispose();
	}
	
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
	
	public LocationFrame(MainFrame mframe, LocationListFrame lframe,boolean edit)
	{
		super("Snow Pilot - Add/Edit Location");
		System.out.println("LocationFrame....edit:"+edit);
	    this.mframe = mframe;
	    this.lframe=lframe;
		user = mframe.getUser();
		
		setLayout(null);
		
		this.setSize(width, height);
		this.setLocation(60, 50);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
        buildMenu();
        Location loc = null;
        if (!edit) 
        {
        	if (user.getCoordType().equals("UTM")) utm=true;
        }
        else
        {
        	loc = getCurrentLocation();
        	if (loc.type.equals("UTM")) utm=true;
        }
        System.out.println("utm: "+utm);
		buildForm();
		if ( edit ) popForm(loc);
	}
	
	private void buildMenu()
    {
        saveMenuItem.setLabel("Save Location");
    	menu.setLabel("Select..");
        menu.add(saveMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        saveMenuItem.addActionListener(new MenuAction());
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == LocationFrame.this ) 
			{
				LocationFrame.this.saveLocation();
				LocationFrame.this.dispose();
				
			}
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if ( object==saveMenuItem ) 
            {
            	saveLocation();
            	dispose();
            }
		}
	}
	
	
	
}	
	