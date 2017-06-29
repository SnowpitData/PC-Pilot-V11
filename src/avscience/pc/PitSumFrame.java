package avscience.pc;

import java.awt.*;
import avscience.wba.*;
import avscience.ppc.*;
import java.util.*;
import avscience.desktop.*;
import java.sql.Timestamp;
import javax.swing.*;

public class PitSumFrame extends Frame implements TimeFrame
{
    private PitFrame  pFrame;
    private int width = 880;
    private int height = 788;
    int colSpace=260;
    int xx;
    int yy;
    int starty=60;
    int startx=20;
    int yspace=26;
    int marg = 192;
    Label loc;
 //  	long ts = 0;
    org.compiere.grid.ed.Calendar estDate;
    Label lDate;
    Button setDate;
    TextItem range;
    ElvTextItem elv;
    LonTextItem lon;
    LatTextItem lat;
    ////
    DegTextItem aspect;
    DegTextItem slope;
    TempTextItem temp;
    ///NoteTextItem notes;
    SPTextArea notes;
    TextItem[] layerNotes;
    TextItem[] testNotes;
    String[] layers;
    DepthTextItem penetration;
    DepthTextItem heightOfSnowpack;
    Choice footSki=new Choice();
    Choice state = new Choice();
    Choice precip = new Choice();
    Choice skyCover = new Choice();
    Choice windSpeed = new Choice();
    Choice winDir = new Choice();
    Choice windLoad = new Choice();
    Choice stability = new Choice();
    Choice aviLoc;
    Checkbox[] activities = new Checkbox[11];
    Checkbox testPit;
    Checkbox bcPit;
    Checkbox saPit;
    Checkbox aviPit;
    Checkbox sharePit;
    
    UTMTextItem east;
    UTMTextItem north;
    TextItem utmZone;
    boolean utm=false;
    
    private MenuBar mainMenuBar = new java.awt.MenuBar();
    private Menu menu = new java.awt.Menu();
    private MenuItem exitMenuItem = new java.awt.MenuItem();
    private MenuItem saveMenuItem = new java.awt.MenuItem();
    boolean invert;
    
    public PitSumFrame(PitFrame frame)
    {
        super("Edit Pit");
        pFrame = frame;
       
        this.setSize(width, height);
        this.setVisible(true);
        if ( pFrame.getPit().getMeasureFrom()==null ) invert = true;
  		else if (!( pFrame.getPit().getMeasureFrom().equals("top") )) invert = true;
       
        if (pFrame.getPit().getLocation().type!=null) utm = pFrame.getPit().getLocation().type.equals("UTM");
        init();
    }
    
    
    public void updateEstDate()
    {
	System.out.println("updateEstDate():PitHeaderFrame");
	Timestamp tts = estDate.getTimestamp();
	long ts = tts.getTime();
	Date dd = new Date(ts);
      	lDate.setText(dd.toString());
      	pFrame.getPit().setTimestamp(ts);
      	
     }
    
    void showDatePopup()
    {
    	Timestamp timestamp = new Timestamp(pFrame.getPit().getTimestamp());
    	estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", timestamp, 16, this);
    	estDate.setSize(380, 320);
       	estDate.setVisible(true);
    }
    
    void exit()
    {
        this.dispose();
    }
    
    void save()
    {
       updatePitFromForm();
        if (pFrame!=null) pFrame.updatePit();
        exit();
    }
    
    private void buildMenu()
    {
        saveMenuItem.setLabel("save changes");
        exitMenuItem.setLabel("exit");
        menu.setLabel("Pit..");
        menu.add(saveMenuItem);
        menu.add(exitMenuItem);
        mainMenuBar.add(menu);
        setMenuBar(mainMenuBar);
        saveMenuItem.addActionListener(new MenuAction());
        exitMenuItem.addActionListener(new MenuAction());
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
            if ( object == setDate ) showDatePopup();
        }
    }
  
    
    void init()
    {
        this.setLocation(6, 0);
        SymWindow aSymWindow = new SymWindow();
        this.addWindowListener(aSymWindow);
        estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate.addWindowListener(aSymWindow);
        estDate.setVisible(false);
        buildMenu();
        this.setLayout(null);
        buildForm();
        popForm();
    }
    
    void buildForm()
    {
    	System.out.println("buildForm");
        avscience.ppc.User u = pFrame.getPit().getUser();
       
        if ( u==null ) u = new avscience.ppc.User();
        xx = startx;
        yy = starty;
        loc = new Label(pFrame.getPit().getLocation().getName());
        loc.setLocation(xx,yy);
        loc.setVisible(true);
        loc.setSize(120,20);
        this.add(loc);
        yy+=yspace;
        lDate = new Label("");
        lDate.setSize(220, 20);
        lDate.setLocation(xx, yy);
        lDate.setVisible(true);
        add(lDate);
        yy+=yspace;
        setDate = new Button("Set Date/Time");
        setDate.setLocation(xx, yy);
        setDate.setSize(98, 26);
        setDate.addActionListener(new MenuAction());
        add(setDate);
        yy+=yspace;
        testPit = new Checkbox("Practice Pit: This pit is not real.");
        testPit.setLocation(xx, yy);
        testPit.setSize(180, 20);
        testPit.setVisible(true);
        add(testPit);
        yy+=yspace;
        ////////////////
        saPit = new Checkbox("Pit dug in a ski area");
        saPit.setLocation(xx, yy);
        saPit.setSize(180, 20);
        saPit.setVisible(true);
        saPit.addItemListener(new CheckListener());
        add(saPit);
        yy+=yspace;
        //////////////////
        ////////////////
        bcPit = new Checkbox("Pit representative of backcountry");
        bcPit.setLocation(xx, yy);
        bcPit.setSize(240, 20);
        bcPit.setVisible(false);
        add(bcPit);
        yy+=yspace;
        //////////////////
        aviPit = new Checkbox("Pit dug adjacent to avalanche");
        aviPit.setLocation(xx, yy);
        aviPit.setSize(200, 20);
        aviPit.setVisible(true);
        aviPit.addItemListener(new CheckListener());
        add(aviPit);
        yy+=yspace;
        
        aviLoc = new Choice();
        aviLoc.addItem("crown");
        aviLoc.addItem("flank");
        aviLoc.addItem("other");
        aviLoc.setLocation(xx, yy);
        aviLoc.setSize(90, 20);
        aviLoc.setVisible(false);
        add(aviLoc);
        yy+=yspace;
        
        sharePit = new Checkbox("Pit viewable in public database");
        sharePit.setLocation(xx, yy);
        sharePit.setSize(240, 20);
        sharePit.setVisible(true);
        sharePit.setState(pFrame.mf.getUser().getShare());
        add(sharePit);
        //yy+=yspace;
        /////////
        Label sl = new Label("State/Prov");
        sl.setSize(78, 20);
        sl.setVisible(true);
        sl.setLocation(xx, yy);
        add(sl);
        
        String[] states = new StateProv().getList();
        for (int i=0;i<states.length;i++)
        {
        	state.add(states[i]);
        }
        state.setLocation(xx+80, yy);
        state.setSize(120, 32);
        add(state);
        yy+=yspace;
        if ((u.getState() == null ) || ( u.getState().trim().length()<2)) state.select("MT");
        else state.select(u.getState());
        
        range = new TextItem("Mtn Range", xx, yy);
        add(range);
        yy+=yspace;
        
        elv = new ElvTextItem("Elevation " + u.getElvUnits(), u.getElvUnits(), xx, yy);
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
	        
	        lon = new LonTextItem("Long. "+u.getLongType(), xx, yy);
	        add(lon);
	        yy+=yspace;
	        
	        lat = new LatTextItem("Lat. "+u.getLatType(), xx, yy);
	        add(lat);
        }
        yy+=yspace;
        
        aspect = new DegTextItem("Aspect", xx, yy);
        add(aspect);
        yy+=yspace;
        
        slope = new DegTextItem("Slope Angle", xx, yy);
        add(slope);
        yy+=yspace;
        
        temp = new TempTextItem("Air Temp.  "+u.getTempUnits(), u.getTempUnits(), xx, yy);
       
        add(temp);
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
        ////
        Label spl = new Label("Surface Penetrability:");
        spl.setSize(140, 18);
        spl.setLocation(xx, yy);
        
        add(spl);
        yy+=yspace;
        footSki = new Choice();
        footSki.add(" ");
        footSki.add("Foot");
        footSki.add("Ski");
        footSki.setLocation(xx, yy);
        add(footSki);
        yy+=yspace;
        penetration = new DepthTextItem("Surface Penetration "+u.getDepthUnits(), xx, yy);
        add(penetration);
        yy+=yspace;
        
        heightOfSnowpack=new DepthTextItem("Height of Snowpack "+u.getDepthUnits(), xx, yy);
        add(heightOfSnowpack);
        yy+=yspace;
        ////
        yy = starty;
        //if (macos)xx+=(colSpace-40);
        //else xx+=colSpace;
        xx+=colSpace;
        Label pLabel = new Label("Precipitation");
        pLabel.setLocation(xx,yy);
        pLabel.setVisible(true);
        pLabel.setSize(80,20);
        add(pLabel);
        yy+=yspace;
        precip.setLocation(xx,yy);
        precip.setSize(180, 32);
        add(precip);
        yy+=yspace;
        
        Label sLabel = new Label("Sky Cover");
        sLabel.setLocation(xx,yy);
        sLabel.setVisible(true);
        sLabel.setSize(80,20);
        add(sLabel);
        yy+=yspace;
        skyCover.setLocation(xx,yy);
        skyCover.setSize(160, 32);
        add(skyCover);
        yy+=yspace;
        
        Label wsLabel = new Label("Wind Speed");
        wsLabel.setLocation(xx,yy);
        wsLabel.setVisible(true);
        wsLabel.setSize(80,20);
        add(wsLabel);
        yy+=yspace;
        windSpeed.setLocation(xx,yy);
        windSpeed.setSize(160, 32);
        add(windSpeed);
        yy+=yspace;
        
        Label wdLabel = new Label("Wind Direction");
        wdLabel.setLocation(xx,yy);
        wdLabel.setVisible(true);
        wdLabel.setSize(110,20);
        add(wdLabel);
        yy+=yspace;
        winDir.setLocation(xx,yy);
        winDir.setSize(100, 32);
        add(winDir);
        yy+=yspace;
        
        Label wlLabel = new Label("Wind Loading");
        wlLabel.setLocation(xx,yy);
        wlLabel.setVisible(true);
        wlLabel.setSize(80,20);
        add(wlLabel);
        yy+=yspace;
        windLoad.setLocation(xx,yy);
        windLoad.setSize(120, 32);
        add(windLoad);
        yy+=yspace;
        
        Label stLabel = new Label("Stability on similar slopes");
        stLabel.setLocation(xx,yy);
        stLabel.setVisible(true);
        stLabel.setSize(180,20);
        add(stLabel);
        yy+=yspace;
        stability.setLocation(xx,yy);
        stability.setSize(120, 32);
        add(stability);
        yy+=5*yspace;
        ////
        int astart = yy;
        xx=startx;
        String[] acts = AvActivity.getInstance().getCodes();
        int nacts = acts.length;
        activities = new Checkbox[nacts];
      /*  for ( i=0; i<4; i++)
        {
            activities[i]=new Checkbox(acts[i]);
            activities[i].setLocation(xx, yy);
            activities[i].setVisible(true);
            activities[i].setSize(180,20);
            add(activities[i]);
            yy+=yspace;
        }*/
        yy=astart-4*yspace;
        xx+=colSpace;
        for ( i=0; i<nacts; i++)
        {
            activities[i]=new Checkbox(acts[i]);
            activities[i].setLocation(xx, yy);
            activities[i].setVisible(true);
            activities[i].setSize(180,20);
            add(activities[i]);
            yy+=yspace;
        }
        
        yy=starty;
       //if (macos)xx+=colSpace-76;
       // else xx+=colSpace-40;
       	xx+=colSpace-40;
        Label tnotes = new Label("Test Notes");
        tnotes.setLocation(xx, yy);
        tnotes.setVisible(true);
        tnotes.setSize(80, 20);
        add(tnotes);
        yy+=yspace;
        
        java.util.Enumeration tests = pFrame.getPit().getShearTests();
        java.util.Vector v = new java.util.Vector();
        while ( tests.hasMoreElements())
        {
        	avscience.ppc.ShearTestResult res = (avscience.ppc.ShearTestResult) tests.nextElement();
        	v.add(res);
        }
        
        if ( invert ) v = Sorter.sortDescendingTests(v);
        else v = Sorter.sortAscendingTests(v);
        tests = v.elements();
        
        testNotes = new TextItem[pFrame.getPit().getTestResultStrings().length];
        i=0;
        while ( tests.hasMoreElements())
        {
        	avscience.ppc.ShearTestResult res = (avscience.ppc.ShearTestResult) tests.nextElement();
        	String dp = res.getDepth();
        	String s = res.getCode()+" "+dp;
        	testNotes[i] = new TextItem(s, xx, yy, 16);
        	add(testNotes[i]);
        	yy+=yspace;
        	i++;
        }
        yy=starty;
        xx+=colSpace/2+22;
        Label lnotes = new Label("Layer Notes");
        lnotes.setLocation(xx, yy);
        lnotes.setVisible(true);
        lnotes.setSize(80, 20);
        add(lnotes);
        yy+=yspace;
        java.util.Vector lays = new java.util.Vector();
        java.util.Enumeration ls = pFrame.getPit().getLayers();
        while ( ls.hasMoreElements())
        {
        	avscience.ppc.Layer l = (avscience.ppc.Layer) ls.nextElement();
        	lays.add(l);
        }
        if ( invert ) lays = Sorter.sortDescendingLayers(lays);
        else lays = Sorter.sortAscendingLayers(lays);
        
        layers = getLayerStrings(lays);
        layerNotes = new TextItem[layers.length];
        
        for (i = 0; i < layers.length; i++)
        {
        	layerNotes[i]=new TextItem(layers[i], xx, yy, 16);
        	add(layerNotes[i]);
        	yy+=yspace;
        }
       
        xx=startx;
      	Label nl = new Label("Notes:");
      	nl.setSize(200, 20);
      	nl.setLocation(xx, 660);
      	nl.setVisible(true);
      	add(nl);
      	
      	notes = new SPTextArea();
        notes.setLineWrap(true);
        notes.setWrapStyleWord(true);
        notes.setSize(width-50, 120);
        notes.setBorder(BorderFactory.createLineBorder(Color.black));
      	JScrollPane pane = new JScrollPane(notes);
      	pane.setLocation(xx, 680);
      	pane.setSize(width-50, 84);
      	pane.setVisible(true);
      	add(pane);
      
    }
    
    void popForm()
    {
    	System.out.println("popForm");
        Location l = pFrame.getPit().getLocation();
        state.select(l.getState());
        range.setText(l.getRange());
        if (utm)
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
        
        elv.setText(l.getElv()); 
        aspect.setText(pFrame.getPit().getAspect()); 
        slope.setText(pFrame.getPit().getIncline());  
        temp.setText(pFrame.getPit().getAirTemp());
        footSki.select(pFrame.getPit().getSkiBoot());
	penetration.setText(pFrame.getPit().getSurfacePen());
	heightOfSnowpack.setText(pFrame.getPit().getHeightOfSnowpack());
        String precipText = Precipitation.getInstance().getDescription(pFrame.getPit().getPrecip());
        precip.select(precipText);
        
        String skyCvr = SkyCover.getInstance().getDescription(pFrame.getPit().getSky());
        System.out.println("popForm: SkyCover "+ skyCvr);
        skyCover.select(skyCvr);
        String wndSpdText = WindSpeed.getInstance().getDescription(pFrame.getPit().getWindspeed());
        windSpeed.select(wndSpdText);
        winDir.select(pFrame.getPit().getWinDir());
        windLoad.select(pFrame.getPit().getWindLoading());
        stability.select(pFrame.getPit().getStability());
        String nts = pFrame.getPit().getPitNotes();
      	long ts = pFrame.getPit().getTimestamp();
      	System.out.println("Timestamp: "+ts);
	Date dd = new Date(ts);
      	lDate.setText(dd.toString());
	      
        notes.setText(nts);
        if (pFrame.getPit().testPit.trim().equals("true")) testPit.setState(true);
        else testPit.setState(false);
        
        if ( pFrame.getPit().isSkiAreaPit() ) saPit.setState(true);
        else saPit.setState(false);
        if ( pFrame.getPit().isBCPit() ) bcPit.setState(true);
        else bcPit.setState(false);
        sharePit.setState(pFrame.getPit().getShare());
        java.util.Vector acs = pFrame.getPit().getActivities();
        for ( int i=0; i<activities.length; i++ )
        {
            java.util.Enumeration e = acs.elements();
            while ( e.hasMoreElements())
            {
                String s = (String) e.nextElement();
                if (s.equals(activities[i].getLabel()))
                {
                     activities[i].setState(true);
                    
                }
            }
        }
        
        for (int i=0; i<layers.length; i++)
        {
        	avscience.ppc.Layer ll = pFrame.getPit().getLayerByString(layers[i]);
        	if (ll!=null)
        	{
	        	String c = ll.getComments();
	        	if ( c.length()>20 ) c = c.substring(0, 20);
	        	layerNotes[i].setMaxLength(20);
	        	layerNotes[i].setText(c);
	        }
	        else System.out.println("Layer null."+layers[i]);
        }
        int i=0;
        java.util.Enumeration e = pFrame.getPit().getShearTests();
        java.util.Vector v = new java.util.Vector();
        while ( e.hasMoreElements() ) 
        {
        	avscience.ppc.ShearTestResult test = (avscience.ppc.ShearTestResult) e.nextElement();
        	v.add(test);
        }
        if ( invert ) v = Sorter.sortDescendingTests(v);
        else v = Sorter.sortAscendingTests(v);
        e = v.elements();
        while ( e.hasMoreElements())
        {
        	avscience.ppc.ShearTestResult res = (avscience.ppc.ShearTestResult) e.nextElement();
        	String sc = res.getComments();
        	if ( sc.length()>20 ) sc = sc.substring(0, 20);
        	testNotes[i].setText(sc);
        	i++;
        }
        aviLoc.select(pFrame.getPit().aviLoc);
        aviPit.setState(pFrame.getPit().isAviPit());
        if (pFrame.getPit().isSkiAreaPit()) bcPit.setVisible(true);
    	else bcPit.setVisible(false);
	    if (pFrame.getPit().isAviPit()) aviLoc.setVisible(true);
    	else aviLoc.setVisible(false);
    }
    
    String[] getLayerStrings(java.util.Vector layers)
    {
    	String[] ls = new String[layers.size()];
    	java.util.Enumeration e = layers.elements();
    	int i=0;
    	while ( e.hasMoreElements())
            {
                Layer l = (Layer) e.nextElement();
    		ls[i]=l.getComments();
    		i++;
            }
	return ls;
        }
    
    public void updatePitFromForm()
    {
    	pFrame.getPit().setEdited();
        String name = pFrame.getPit().getLocation().getName();
        User u = pFrame.getPit().getUser();
        Location l=null;
        if (utm) l=new Location(u, name, state.getSelectedItem(), range.getText(), utmZone.getText(), east.getText(), north.getText(), elv.getText(), "");
        else l = new Location(u, name, state.getSelectedItem(), range.getText(), lat.getText(), lon.getText(), elv.getText(), "");
        pFrame.getPit().setLocation(l);
        pFrame.getPit().setAspect(aspect.getText());
        pFrame.getPit().setIncline(slope.getText());
        pFrame.getPit().setAirTemp(temp.getText());
        pFrame.getPit().setSkiBoot(footSki.getSelectedItem());
        pFrame.getPit().setSurfacePen(penetration.getText());
        pFrame.getPit().setHeightOfSnowpack(heightOfSnowpack.getText());
        String precipCode = Precipitation.getInstance().getCode(precip.getSelectedIndex());
        pFrame.getPit().setPrecip(precipCode);
        
        String skyCode = SkyCover.getInstance().getShortCode(skyCover.getSelectedIndex()-1);
        System.out.println("Update Pit, Sky Code: "+skyCode);
        pFrame.getPit().setSky(skyCode);
        String windspdCode = WindSpeed.getInstance().getCode(windSpeed.getSelectedIndex());
        pFrame.getPit().setWindSpeed(windspdCode);
        pFrame.getPit().setWinDir(winDir.getSelectedItem());
        pFrame.getPit().setWindLoading(windLoad.getSelectedItem());
        pFrame.getPit().setStability(stability.getSelectedItem());
        pFrame.getPit().setPitNotes(notes.getText());
        if ( testPit.getState()) pFrame.getPit().testPit = "true";
        else pFrame.getPit().testPit = "false";
        if ( saPit.getState()) pFrame.getPit().skiAreaPit = "true";
        else pFrame.getPit().skiAreaPit = "false";
        if ( aviPit.getState()) pFrame.getPit().aviPit = "true";
        else pFrame.getPit().aviPit = "false";
        pFrame.getPit().aviLoc = aviLoc.getSelectedItem();
        ///////////
        if ( bcPit.getState()) pFrame.getPit().bcPit = "true";
        else pFrame.getPit().bcPit = "false";
        pFrame.getPit().setShare(sharePit.getState());
        ////////
       // long ts = estDate.getTimestamp().getTime();
        
        java.util.Vector acts = new java.util.Vector();
        for ( int i=0; i<activities.length; i++)
        {
            if ( activities[i].getState())
            {
                acts.add(activities[i].getLabel());
            }
        }
        pFrame.getPit().setActivities(acts);
        
        for (int i=0; i<layers.length; i++)
        {
        
        	avscience.ppc.Layer ll = pFrame.getPit().getLayerByString(layers[i]);
        	String ss = layerNotes[i].getText();
        	if (ss.length()>20) ss=ss.substring(0, 20);
        	if (ll!=null)
        	{
	        	ll.setComments(ss);
	        	pFrame.getPit().updateCurrentEditLayer(ll);
	        }
	        if (ll==null) System.out.println("Layer: "+layers[i]+" null.");
        }
        int i = 0;
        java.util.Enumeration e = pFrame.getPit().getShearTests();
        java.util.Vector v = new java.util.Vector();
        while ( e.hasMoreElements() ) 
        {
        	avscience.ppc.ShearTestResult test = (avscience.ppc.ShearTestResult) e.nextElement();
        	v.add(test);
        }
        if ( invert ) v = Sorter.sortDescendingTests(v);
        else v = Sorter.sortAscendingTests(v);
        e = v.elements();
        while ( e.hasMoreElements())
        {
        	avscience.ppc.ShearTestResult res = (avscience.ppc.ShearTestResult) e.nextElement();
        	System.out.println(res.toString());
        	String ss=testNotes[i].getText();
        	if (ss.length()>20) ss=ss.substring(0, 20);
        	res.setComments(ss);
        	i++;
        }
    }
    
    public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
    
    class SymWindow extends java.awt.event.WindowAdapter
    {   
        public void windowClosing(java.awt.event.WindowEvent event)
        {
            Object object = event.getSource();
            if (object == PitSumFrame.this) new SaveDialog(PitSumFrame.this).setVisible(true);
         /*   if ( object == estDate )
            {
            	System.out.println("estDate WindowClosing.");
            	ts= estDate.getTimestamp().getTime();
            	Date d = new Date(ts);
            	lDate.setText(d.toString());
            	
            }*/
        }
    }
}