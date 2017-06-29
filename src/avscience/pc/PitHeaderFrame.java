package avscience.pc;

import java.awt.*;
import avscience.wba.*;
import java.util.*;
import avscience.desktop.*;
import java.util.Date;
import avscience.ppc.*;
import java.sql.Timestamp;
import javax.swing.*;

 public class PitHeaderFrame extends Frame implements TimeFrame
{
    private MainFrame  mFrame;
    private PitHeaderFrame pf;
    public avscience.pc.PitFrame pframe;
    avscience.ppc.PitObs pit;
    private int width = 484;
    private int height = 850;
    private int pheight = 840;
    int colSpace=260;
    int xx;
    int yy;
    int starty=70;
    int startx=20;
    int yspace=26;
    int marg = 192;
    NoteTextItem loc;
   // long ts = 0;
    org.compiere.grid.ed.Calendar estDate;
    Label lDate;
    Button setDate;
    TextItem range;
    ElvTextItem elv;
    LonTextItem lon;
    LatTextItem lat;
    UTMTextItem east;
    UTMTextItem north;
    TextItem utmZone;
    DegTextItem aspect;
    SlopeAngleTextItem slope;
    TempTextItem temp;
    SPTextArea notes;
    DepthTextItem penetration;
    DepthTextItem heightOfSnowpack;
    Choice footSki=new Choice();
    Choice state=new Choice();
    Choice precip = new Choice();
    Choice skyCover = new Choice();
    Choice windSpeed = new Choice();
    Choice winDir = new Choice();
    Choice windLoad = new Choice();
    Choice stability = new Choice();
    Checkbox[] activities = new Checkbox[11];
   
    private MenuBar mainMenuBar = new java.awt.MenuBar();
    private Menu menu = new java.awt.Menu();
    private MenuItem exitMenuItem = new java.awt.MenuItem();
    private MenuItem saveMenuItem = new java.awt.MenuItem();
    private MenuItem tempMenuItem = new java.awt.MenuItem();
    private MenuItem testMenuItem = new java.awt.MenuItem();
    private MenuItem layerMenuItem = new java.awt.MenuItem();
    private MenuItem densityMenuItem = new java.awt.MenuItem();
    private MenuItem pitGraphMenuItem = new java.awt.MenuItem();
	//boolean macos;
    boolean edit;
    PitListFrame lframe;
    avscience.ppc.User user;
    public Vector subFrames = new Vector();
    boolean webEdit = false;
    Choice aviLoc;
    Checkbox testPit;
    Checkbox bcPit;
    Checkbox saPit;
    Checkbox aviPit;
    Checkbox sharePit;
    boolean utm=false;
    Panel p = new Panel();
    
    public PitHeaderFrame(MainFrame frame, boolean edit, avscience.ppc.PitObs pit, PitListFrame lframe)
    {
        super("Add/Edit Pit");
        mFrame = frame;
        this.edit=edit;
        this.lframe=lframe;
        System.out.println("PitHeaderFraMe");
        pf = this;
        this.user = frame.getUser();
        System.out.println("User:: "+user);
        System.out.println("User name: "+user.getName());
        if (edit) 
        {
        	this.pit=pit;
        	utm = pit.getLocation().type.equals("UTM");
        }
        else
        {
        	String serial = mFrame.store.getNewSerial();
        	this.pit = new avscience.ppc.PitObs(this.user, serial, frame.bld, frame.version);
        	utm = this.user.getCoordType().equals("UTM");
        }
        if (mFrame.getSmallScreen()) height=580;
        this.setSize(width+20, height);
      
        this.setVisible(true);
       
        init();
    }
    
    public PitHeaderFrame(avscience.pc.PitFrame pframe)
    {
    	super("Add/Edit Pit");
        this.pframe=pframe;
        this.edit=true;
        pf = this;
        mFrame=pframe.mf;
        this.user = pframe.pit.getUser();
        try
        {
            this.pit=pframe.pit;
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
        if (pit.getLocation().type!=null) utm = pit.getLocation().type.equals("UTM");
        if (mFrame.getSmallScreen()) height=580;
        this.setSize(width+20, height);
        this.setVisible(true);
        //String os_name = System.getProperty( "os.name" );
		this.webEdit = pframe.webEdit;
        init();
    }
    
    public void exit()
    {
    	System.out.println("exit");
    	if ((edit) && (lframe!=null)) 
    	{
    		lframe.rebuildList();
    	}
    	if ( mFrame!=null ) mFrame.rebuildList();
    	if ( pframe!=null )
	    {
                avscience.ppc.PitObs p = new avscience.ppc.PitObs();
                try
                {
                    p = new avscience.ppc.PitObs(pit.toString());
                    pframe.updatePit(pit);
	 	}
                catch(Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
        this.dispose();
    }
    
    void saveWO()
    {
    	updatePitFromForm();
    	if ( webEdit ) pframe.updateWebPit(pit);
        else if ( mFrame!=null )
        {
            mFrame.store.addPit(pit);
            mFrame.rebuildList();
	}
	    
        if ( pframe!=null ) pframe.updatePit(pit);
        if ((edit) && (lframe!=null)) lframe.rebuildList();
    }
    
    void clearSubFrames()
    {
    	Enumeration e = subFrames.elements();
    	while ( e.hasMoreElements())
    	{
    		Frame f = (Frame) e.nextElement();
    		f.dispose();
    	}
    }
    
    public void save()
    {
    	System.out.println("PitHeaderFrame-save");
        updatePitFromForm();
       
       	if ( webEdit ) pframe.updateWebPit(pit);
       	else
       	{
        	if (mFrame!=null) mFrame.store.addPit(pit);
        }
        
        exit();
    }
    
    private void buildMenu()
    {
        saveMenuItem.setLabel("save changes");
        exitMenuItem.setLabel("exit");
        tempMenuItem.setLabel("Temperature Profile");
        layerMenuItem.setLabel("Layers");
        testMenuItem.setLabel("Stability Tests");
        densityMenuItem.setLabel("Density Profile");
        pitGraphMenuItem.setLabel("Pit Graph");
        menu.setLabel("Pit..");
        menu.add(saveMenuItem);
        
        menu.add(tempMenuItem);
        menu.add(layerMenuItem);
        menu.add(testMenuItem);
        menu.add(densityMenuItem);
        menu.add(pitGraphMenuItem);
        menu.add(exitMenuItem);
        mainMenuBar.add(menu);
        setMenuBar(mainMenuBar);
        saveMenuItem.addActionListener(new MenuAction());
        exitMenuItem.addActionListener(new MenuAction());
        tempMenuItem.addActionListener(new MenuAction());
        testMenuItem.addActionListener(new MenuAction());
        layerMenuItem.addActionListener(new MenuAction());
        pitGraphMenuItem.addActionListener(new MenuAction());
        densityMenuItem.addActionListener(new MenuAction());
    }
    
    public avscience.ppc.PitObs getPit()
    {
    	return pit;
    }
    
    class CheckListener implements java.awt.event.ItemListener
    {
    	public void itemStateChanged(java.awt.event.ItemEvent e)
    	{
    		Object o = e.getSource();
    		if (o == saPit)
    		{
    			if (saPit.getState()) bcPit.setVisible(true);
    			else bcPit.setVisible(false);
    		}
    		if (o==aviPit)
    		{
    			if (aviPit.getState()) aviLoc.setVisible(true);
    			else aviLoc.setVisible(false);
    		}
    	}
    }
    
    class MenuAction implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent event)
        {
            Object object = event.getSource();
            if (object == exitMenuItem)  exit();
            if (object == saveMenuItem) save();
            if (object == tempMenuItem) mFrame.showTempFrame(PitHeaderFrame.this);//subFrames.add(new TempFrame(mFrame, PitHeaderFrame.this));
            if (object == testMenuItem) mFrame.showTestFrame(PitHeaderFrame.this);// subFrames.add(new TestFrame(mFrame, PitHeaderFrame.this));
            if (object == densityMenuItem) mFrame.showDensityFrame(PitHeaderFrame.this);//subFrames.add(new DensityFrame(mFrame, PitHeaderFrame.this));
            if (object == layerMenuItem) mFrame.showLayerFrame(PitHeaderFrame.this);//subFrames.add(new LayerFrame(mFrame, PitHeaderFrame.this));
            if (object == pitGraphMenuItem) 
            {
            	updatePitFromForm();
            	///String data = pit.dataString();
            	subFrames.add(new avscience.pc.PitFrame(pit, mFrame, true));
            	saveWO();
            }
            if ( object == setDate ) showDatePopup();
        }
    }
    
    void init()
    {
    	System.out.println("init()");
    	if ( mFrame.macos ) yspace = 30;
        this.setLocation(200, 0);
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
      //  ts=System.currentTimeMillis();
        estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate.addWindowListener(aSymWindow);
        estDate.setVisible(false);
        buildMenu();
        this.setLayout(null);
        buildForm();
        if (edit) popForm();
      	else popLocation();
    }
    
    void buildForm()
    {
        if ( user==null ) user = new avscience.ppc.User();
        xx = startx;
        yy = starty;
      	loc = new NoteTextItem("Location", xx, yy, 20);
      	loc.setLabel("Location");
	    if ( edit )
	    {
	    	if ( pit.getLocation()!=null)
	    	{
	    		Label l = new Label( pit.getLocation().getName());
	    		l.setLocation(xx, yy);
	    		l.setSize(width/2, 20);
	    		p.add(l);
	    	}
	    }
      	else
      	{
	      	add(loc);
	    }
	    
	    p.setLayout(null);
	    if (mFrame.macos) pheight+=100;
	    p.setSize(width-2, pheight);
	    
        loc.setMaxLength(20);
        yy+=yspace;
        String s = "";
        if ( !edit) s = new java.util.Date().toString();
        lDate = new Label(s);
        lDate.setSize(220, 20);
        lDate.setLocation(xx, yy);
        lDate.setVisible(true);
        p.add(lDate);
        yy+=yspace;
        setDate = new Button("Set Date/Time");
        setDate.setLocation(xx, yy);
        setDate.setSize(98, 26);
        setDate.addActionListener(new MenuAction());
        p.add(setDate);
        yy+=yspace;
        testPit = new Checkbox("Practice Pit: This pit is not real.");
        testPit.setLocation(xx, yy);
        testPit.setSize(180, 20);
        testPit.setVisible(true);
        p.add(testPit);
        yy+=yspace;
        //////
        saPit = new Checkbox("Pit dug in a ski area");
        saPit.setLocation(xx, yy);
        saPit.setSize(180, 20);
        saPit.setVisible(true);
        saPit.addItemListener(new CheckListener());
        p.add(saPit);
        yy+=yspace;
        
        bcPit = new Checkbox("Pit representative of backcountry conditions");
        bcPit.setLocation(xx, yy);
        bcPit.setSize(254, 20);
        bcPit.setVisible(false);
        p.add(bcPit);
        yy+=yspace;
        //////////////////
        aviPit = new Checkbox("Pit dug adjacent to avalanche");
        aviPit.setLocation(xx, yy);
        aviPit.setSize(200, 20);
        aviPit.setVisible(true);
        aviPit.addItemListener(new CheckListener());
        p.add(aviPit);
        yy+=yspace;
        
        aviLoc = new Choice();
        aviLoc.addItem("crown");
        aviLoc.addItem("flank");
        aviLoc.addItem("other");
        aviLoc.setLocation(xx, yy);
        aviLoc.setSize(90, 20);
        aviLoc.setVisible(false);
        p.add(aviLoc);
        yy+=yspace;
        
        sharePit = new Checkbox("Pit viewable in public database");
        sharePit.setLocation(xx, yy);
        sharePit.setSize(254, 20);
        sharePit.setVisible(true);
        sharePit.setState(true);
        p.add(sharePit);
        yy+=yspace;
        /////////
        Label sl = new Label("State/Prov");
        sl.setSize(78, 20);
        sl.setVisible(true);
        sl.setLocation(xx, yy);
        p.add(sl);
        
       	//yy+=yspace;
       //	state.setVisible(true);
       	state.setSize(120, 32);
        String[] states = new StateProv().getList();
        for (int i=0;i<states.length;i++)
        {
        	state.add(states[i]);
        }
        state.setLocation(xx+84, yy);
        
        p.add(state);
       // yy+=yspace;
        yy+=yspace;
        if ((user.getState() == null ) || ( user.getState().trim().length()<2)) state.select("MT");
        else state.select(user.getState());
        
        range = new TextItem("Mtn Range", xx, yy);
        add(range);
        yy+=yspace;
        
        elv = new ElvTextItem("Elevation " + user.getElvUnits(), user.getElvUnits(), xx, yy);
        add(elv);
        yy+=yspace;
        
        if (utm)
        {
        	utmZone = new TextItem("UTM Zone", xx, yy);
			add(utmZone);
			yy+=yspace;
			east = new UTMTextItem("East:", xx, yy);
			add(east);
			yy+=yspace;
			north = new UTMTextItem("North:", xx, yy);
			add(north);
        }
        else
        {
        	lat = new LatTextItem("Lat. "+user.getLatType(), xx, yy);
	        add(lat);
	        yy+=yspace;
	        
	        lon = new LonTextItem("Long. "+user.getLongType(), xx, yy);
	        add(lon);
        }
        
        yy+=yspace;
        aspect = new DegTextItem("Aspect", xx, yy);
        add(aspect);
        yy+=yspace;
        
        slope = new SlopeAngleTextItem("Slope Angle", xx, yy);
        add(slope);
        yy+=yspace;
        
        temp = new TempTextItem("Air Temp.  "+user.getTempUnits(), user.getTempUnits(), xx, yy);
       
        add(temp);
        yy+=yspace;
        
        Label spl = new Label("Surface Penetrability:");
        spl.setSize(140, 18);
        spl.setLocation(xx, yy);
        
        p.add(spl);
        yy+=yspace;
        footSki = new Choice();
        footSki.add(" ");
        footSki.add("Foot");
        footSki.add("Ski");
        footSki.setSize(160, 32);
        footSki.setLocation(xx, yy);
        p.add(footSki);
        yy+=yspace;
        penetration = new DepthTextItem("Surface Penetration "+user.getDepthUnits(), xx, yy);
        add(penetration);
        yy+=yspace;
        
        heightOfSnowpack=new DepthTextItem("Height of Snowpack "+user.getDepthUnits(), xx, yy);
        add(heightOfSnowpack);
        yy+=yspace;
        ////
        String[] precips = Precipitation.getInstance().getDescriptions();
        int i = 0;
        for (i=0; i<precips.length; i++)
        {
            precip.add(precips[i]);
        }
        
        String[] covers = SkyCover.getInstance().getDescriptions();
        skyCover.add(" ");
        for (i=0; i<covers.length; i++)
        {
            skyCover.add(covers[i]);
        }
        
        String[] speeds = WindSpeed.getInstance().getDescriptions();
        
        for (i=0; i<speeds.length; i++)
        {
            windSpeed.add(speeds[i]);
        }
        
        String[] dirs = WindDir.getInstance().getCodes();
        
        for (i=0; i<dirs.length; i++)
        {
            winDir.add(dirs[i]);
        }
        
        windLoad.add(" ");
        windLoad.add("previous");
        windLoad.add("yes");
        windLoad.add("no");
        
        String[] stabilities = Stability.getInstance().getCodes();
        
        for (i=0; i<stabilities.length; i++)
        {
            stability.add(stabilities[i]);
        }
        xx+=colSpace;
        yy = starty-8;
        Label pLabel = new Label("Precipitation");
        pLabel.setLocation(xx,yy);
        pLabel.setVisible(true);
        pLabel.setSize(80,20);
        p.add(pLabel);
        yy+=yspace;
        precip.setLocation(xx,yy);
        precip.setSize(180, 32);
        p.add(precip);
        yy+=yspace;
        Label sLabel = new Label("Sky Cover");
        sLabel.setLocation(xx,yy);
        sLabel.setVisible(true);
        sLabel.setSize(80,20);
        p.add(sLabel);
        yy+=yspace;
        skyCover.setLocation(xx,yy);
        skyCover.setSize(160, 32);
        p.add(skyCover);
        yy+=yspace;
        
        Label wsLabel = new Label("Wind Speed");
        wsLabel.setLocation(xx,yy);
        wsLabel.setVisible(true);
        wsLabel.setSize(80,20);
        p.add(wsLabel);
        yy+=yspace;
        windSpeed.setLocation(xx,yy);
        windSpeed.setSize(160, 32);
        p.add(windSpeed);
        yy+=yspace;
        
        Label wdLabel = new Label("Wind Direction");
        wdLabel.setLocation(xx,yy);
        wdLabel.setVisible(true);
        wdLabel.setSize(110,20);
        p.add(wdLabel);
        yy+=yspace;
        winDir.setLocation(xx,yy);
        winDir.setSize(100, 32);
        p.add(winDir);
        yy+=yspace;
        
        Label wlLabel = new Label("Wind Loading");
        wlLabel.setLocation(xx,yy);
        wlLabel.setVisible(true);
        wlLabel.setSize(80,20);
        p.add(wlLabel);
        yy+=yspace;
        windLoad.setLocation(xx,yy);
        windLoad.setSize(120, 32);
        p.add(windLoad);
        yy+=yspace;
        
        Label stLabel = new Label("Stability on similar slopes");
        stLabel.setLocation(xx,yy);
        stLabel.setVisible(true);
        stLabel.setSize(180,20);
        p.add(stLabel);
        yy+=yspace;
        stability.setLocation(xx,yy);
        stability.setSize(120, 32);
        p.add(stability);
        yy+=4*yspace;
        ////
        int astart = yy+3*yspace;
        xx=startx;
        String[] acts = AvActivity.getInstance().getCodes();
        int nacts = acts.length;
        activities = new Checkbox[nacts];
       /* for ( i=0; i<4; i++)
        {
            activities[i]=new Checkbox(acts[i]);
            activities[i].setLocation(xx, yy+2*yspace);
            activities[i].setVisible(true);
            activities[i].setSize(180,20);
            add(activities[i]);
            yy+=yspace;
        }*/
        yy=astart-5*yspace;
        xx+=colSpace;
        for ( i=0; i<nacts; i++)
        {
            activities[i]=new Checkbox(acts[i]);
            activities[i].setLocation(xx, yy-yspace);
            activities[i].setVisible(true);
            activities[i].setSize(180,20);
            p.add(activities[i]);
            yy+=yspace;
        }
       	yy-=1.2*yspace;
      /// 	yy+=yspace;
        xx=startx;
        Label nl = new Label("Notes:");
        nl.setLocation(xx, yy);
        nl.setVisible(true);
        nl.setSize(200, 20);
        p.add(nl);
        yy+=yspace;
        notes = new SPTextArea();
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        notes.setSize(422, 420);
        notes.setEnabled(true);
        notes.setVisible(true);
        notes.enableInputMethods(true);
        notes.setBorder(BorderFactory.createLineBorder(Color.black));
        JScrollPane pane = new JScrollPane(notes);
        pane.setLocation(xx, yy);
        pane.setSize(434, 112);
        pane.setEnabled(true);
        pane.setVisible(true);
        p.add(pane);
        yy+=4*yspace;
        Label uu = new Label("User: "+user.getName());
        uu.setLocation(xx,yy+12);
        uu.setVisible(true);
        uu.setSize(200,20);
        p.add(uu);
      /*  ScrollPane sp = new ScrollPane();
	    setLayout(null);
	    p.setLocation(0, 56);
	    sp.setLocation(0, 0);
	    if (mFrame.getSmallScreen())
	    {
	    	sp.setSize(width+20, 640);
	    	setSize(width+20, 640);
	    	sp.add(p);
	    	add(sp);
	    }
        else add(p);*/
        ScrollPane sp = new ScrollPane();
	    sp.setSize(width+20, height);
	    setLayout(null);
	    sp.setLocation(0, 0);
	    sp.add(p);
        add(sp);
    }
    
    void showDatePopup()
    {
    	Timestamp timestamp = new Timestamp(pit.getTimestamp());
    	estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", timestamp, 16, this);
    	estDate.setSize(380, 320);
       	estDate.setVisible(true);
    }
    
    void popLocation()
    {
        Location l = mFrame.store.getLocation(mFrame.locations.getSelectedItem());
    	if ((l!=null)&&(l.getName().trim().length()>0))
    	{
    		loc.setText(l.getName().trim()+" "+l.getID());
    		state.select(l.getState());
    		range.setText(l.getRange());
    		elv.setText(l.getElv());
    		lat.setText(l.getLat());
    		lon.setText(l.getLongitude());
    	}
    }
    
    void popForm()
    {
    	System.out.println("phf popForm");
    	if ( pit!=null )
    	{
            avscience.ppc.Location l = pit.getLocation();
            if ( l!=null)
            {
                    loc.setText(l.getName());
                    state.select(l.getState());
                    range.setText(l.getRange());
                    if (l.type!=null)
                    {
                            if (l.type.equals("UTM"))
                            {
                                utmZone.setText(l.zone);
                                east.setText(l.east);
                                north.setText(l.north);
                            }
                            else
                            {
                                lat.setText(l.getLat());
                                lon.setText(l.getLongitude());
                            }
                    }
                    else 
                    {
                            lat.setText(l.getLat());
                            lon.setText(l.getLongitude());
                    }
                    elv.setText(l.getElv());
                 }
		    if ( pit.testPit.trim().equals("true")) testPit.setState(true);
		    else testPit.setState(false);
		    /////////
		    if ( pit.isSkiAreaPit()) saPit.setState(true);
		    else saPit.setState(false);
		    /////////
		    if ( pit.isBCPit()) bcPit.setState(true);
		    else bcPit.setState(false);
		    
		    sharePit.setState(pit.getShare());
	        aspect.setText(pit.getAspect()); 
	        slope.setText(pit.getIncline());  
	        temp.setText(pit.getAirTemp());
	        ////
	        footSki.select(pit.getSkiBoot());
	        penetration.setText(pit.getSurfacePen());
	        heightOfSnowpack.setText(pit.getHeightOfSnowpack());
                String precipTxt = Precipitation.getInstance().getDescription(pit.getPrecip());
	        precip.select(precipTxt);
                String skyCvr = SkyCover.getInstance().getDescription(pit.getSky());
                System.out.println("SkyCover: "+skyCvr);
	        skyCover.select(skyCvr);
                String wndSpdTxt = WindSpeed.getInstance().getDescription(pit.getWindspeed());
	        windSpeed.select(wndSpdTxt);
	        winDir.select(pit.getWinDir());
	        windLoad.select(pit.getWindLoading());
	        stability.select(pit.getStability());
	        String nts = pit.getPitNotes();
	       
	      	/////////////////////
	      	long ts = pit.getTimestamp();

	      	Date dd = new Date(ts);
      		lDate.setText(dd.toString());
	      	
	        notes.setText(nts);
	        java.util.Vector acs = pit.getActivities();
	        for ( int i=0; i<activities.length; i++ )
	        {
	        	if (acs!=null)
	        	{
		            java.util.Enumeration e = acs.elements();
		            if (e!=null)
		         	{
			            while ( e.hasMoreElements())
			            {
			                String s = (String) e.nextElement();
			                if (s.equals(activities[i].getLabel()))
			                {
			                     activities[i].setState(true);
			                    
			                }
			            }
			        }
		         }
	        }
	        aviLoc.select(pit.aviLoc);
	        aviPit.setState(pit.isAviPit());
	        if (pit.isSkiAreaPit()) bcPit.setVisible(true);
	    	else bcPit.setVisible(false);
		    if (pit.isAviPit()) aviLoc.setVisible(true);
	    	else aviLoc.setVisible(false);
    	}
    }
	     


	public void updateEstDate()
	{
		System.out.println("updateEstDate():PitHeaderFrame");
		Timestamp tts = estDate.getTimestamp();
		long ts = tts.getTime();
		Date dd = new Date(ts);
      	lDate.setText(dd.toString());
      	getPit().setTimestamp(ts);
      	
	}
    
    String[] getLayerStrings(Vector layers)
    {
    	String[] ls = new String[layers.size()];
    	Enumeration e = layers.elements();
    	int i=0;
    	while ( e.hasMoreElements())
    	{
    		ls[i]=e.nextElement().toString();
    		i++;
		}
		return ls;
    }
    
    private Vector sortAscendingLayers(Vector layers)
    {
        boolean sorted = false;
        int length = layers.size();
        int i = 0;
        avscience.ppc.Layer layer;
        avscience.ppc.Layer layerInc;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    layer = (avscience.ppc.Layer) layers.elementAt(i);
                    int n = layer.getStartDepthInt();
                    layerInc = (avscience.ppc.Layer) layers.elementAt(i+1);
                    int ninc = layerInc.getStartDepthInt();
                  
                    if ( ninc < n )
                    {
                            layers.setElementAt(layerInc, i);
                            layers.setElementAt(layer, i+1);
                            sorted = false;
                    }
                }
            }
        }
        return layers;
    }
    
    
    public void updatePitFromForm()
    {
    	System.out.println("updatePitFromForm");
    	pit.setEdited();
    	avscience.ppc.Location l = null;
    	if (utm) l = new avscience.ppc.Location(user, loc.getText().trim(), state.getSelectedItem(), range.getText(), utmZone.getText(), east.getText(), north.getText(), elv.getText(), "");
       	else l = new avscience.ppc.Location(user, loc.getText().trim(), state.getSelectedItem(), range.getText(), lat.getText(), lon.getText(), elv.getText(), "");
        pit.setLocation(l);
        System.out.println("User:: "+pit.getUser());
        pit.setAspect(aspect.getText());
        pit.setIncline(slope.getText());
        pit.setAirTemp(temp.getText());
        pit.setSkiBoot(footSki.getSelectedItem());
        pit.setSurfacePen(penetration.getText());
        pit.setHeightOfSnowpack(heightOfSnowpack.getText());
        String precipCode = Precipitation.getInstance().getCode(precip.getSelectedIndex());
        pit.setPrecip(precipCode);
        String skyCode = SkyCover.getInstance().getShortCode(skyCover.getSelectedIndex()-1);
        pit.setSky(skyCode);
        String wndSpd = WindSpeed.getInstance().getCode(windSpeed.getSelectedIndex());
        pit.setWindSpeed(wndSpd);
        pit.setWinDir(winDir.getSelectedItem());
        pit.setWindLoading(windLoad.getSelectedItem());
        pit.setStability(stability.getSelectedItem());
        pit.setPitNotes(notes.getText());
        
        if ( testPit.getState() ) pit.testPit="true";
        else pit.testPit="false";
        ///////
        if ( saPit.getState() ) pit.skiAreaPit="true";
        else pit.skiAreaPit="false";
        ////////
        if ( bcPit.getState() ) pit.bcPit="true";
        else pit.bcPit="false";
        ////////
        if ( aviPit.getState()) pit.aviPit = "true";
        else pit.aviPit = "false";
        pit.aviLoc = aviLoc.getSelectedItem();
        ///////////
        if ( bcPit.getState()) pit.bcPit = "true";
        else pit.bcPit = "false";
        ////////
        pit.setShare(sharePit.getState());
        
        java.util.Vector acts = new java.util.Vector();
        for ( int i=0; i<activities.length; i++)
        {
            if ( activities[i].getState())
            {
                acts.add(activities[i].getLabel());
            }
        }
        pit.setActivities(acts);
     //   System.out.println("ts:: "+ts);
    //    if (ts>0) pit.setTimestamp(ts);
    }
    
    public void add(TextItemType item)
    {
        p.add(item.getLabel());
        p.add(item.getField());
    }
    
    public void dispose()
    {
    	clearSubFrames();
    	super.dispose();
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
    {   
        public void windowClosing(java.awt.event.WindowEvent event)
        {
            Object object = event.getSource();
            if (object == PitHeaderFrame.this) new SavePitDialog(pf).setVisible(true);
         /*   if ( object == estDate )
            {
            	System.out.println("estDate WindowClosing.");
            	ts= estDate.getTimestamp().getTime();
            	System.out.println("time:: "+ts);
            	Date d = new Date(ts);
            	lDate.setText(d.toString());
            	System.out.println(d.toString());
            	
            }*/
        }
    }
    
    
}