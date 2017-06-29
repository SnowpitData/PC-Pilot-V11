package avscience.desktop;

import java.awt.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import avscience.pc.MainFrame;
import avscience.pc.PitFrame;
import avscience.ppc.PitObs;
import avscience.ppc.AvOccurence;
import avscience.ppc.Location;

public class OccFrame extends Frame
{

	public avscience.ppc.AvOccurence AvOcc;
	private OccCanvas canvas;
	
	private int width = 800;
	private int height = 600;
	private int margin = 16;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	private MenuItem crownObsItem = new java.awt.MenuItem();
	private MenuItem editItem = new java.awt.MenuItem();
	///private DataStore store;
	public AvApp app;
	public avscience.ppc.PitObs pit;
	private PitFrame frame;
	avscience.pc.MainFrame mf;
	LinkedHashMap attributes = new LinkedHashMap();
	//private AvOccurence obs;
	boolean webEdit = false;
	PitApplet applet;
	public boolean embedded = false;
	String dtime = "";
	
	/*public OccFrame(MainFrame mf, avscience.ppc.AvOccurence AvOcc, avscience.ppc.PitObs pit, DataStore store, AvApp app, PitFrame frame, PitApplet applet, boolean embedded)
	{
		super("Avalanche Occurrence");
		this.setSize(width, height);
		this.setVisible(true);
		this.mf = mf;
	//	AvOcc = new CharacterCleaner().cleanStrings(AvOcc);
		this.store = store;
		this.app = app;
		this.pit = pit;
		this.store = store;
		this.frame = frame;
		this.AvOcc = AvOcc;
		if (applet!=null) this.webEdit = applet.superuser;
		if (applet!=null) this.applet = applet;
		if ( applet == null ) webEdit = false;
		this.setMaximizedBounds(new Rectangle(width, height));
		canvas = new OccCanvas(AvOcc, pit);
		canvas.setLocation(margin, 2*margin);
		canvas.setVisible(true);
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		this.add(canvas);
		buildMenu();
		
	}*/
	
	public OccFrame(MainFrame mf, avscience.ppc.AvOccurence AvOcc, avscience.ppc.PitObs pit, AvApp app, PitFrame frame, PitApplet applet)
	{
		super("Avalanche Occurrence");
		this.embedded = embedded;
		this.setSize(width, height);
		this.setVisible(true);
		this.mf = mf;
		this.app = app;
		this.pit = pit;
		this.frame = frame;
		this.AvOcc = AvOcc;
		if (applet!=null) this.webEdit = applet.superuser;
		if (applet!=null) this.applet = applet;
		if ( applet == null ) webEdit = false;
		this.setMaximizedBounds(new Rectangle(width, height));
		canvas = new OccCanvas(AvOcc, pit);
		canvas.setLocation(margin, 2*margin);
		canvas.setVisible(true);
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		this.add(canvas);
		buildMenu();
	}
	
	public PitFrame getPitFrame()
	{
		return frame;
	}
	
	void saveOcc()
	{
		FileDialog dialog = new FileDialog(this, "Save Occurence", FileDialog.SAVE);
        dialog.setFilenameFilter(new SPFileFilter());
        dialog.setFile(pit.getLocation().getName().trim()+".txt");
        dialog.setVisible(true);
        File file = null;
        if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
        {
            file = new File(dialog.getDirectory()+"\\"+dialog.getFile());
        }
		//File file = new File("Avocc.txt");
		FileOutputStream out = null;
		PrintWriter writer = null;
		if ( file==null ) return;
		try
		{
			out = new FileOutputStream(file);
			writer = new PrintWriter(out);
		}
		catch(Exception ex){System.out.println(ex.toString());}
		/////////////////////
		long ltime = pit.getTimestamp();
    	try
    	{
    		if ( ltime > 0 ) 
    		{
    			Date date = new Date(ltime);
    			dtime = date.toString();
    		}
    	}
    	catch(Exception e){System.out.println(e.toString());}
        setLabels();
        Location loc = pit.getLocation();
       // avscience.util.Hashtable atts = null;
        Enumeration e = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("User: " + pit.getUser().getName() + "\n");
        buffer.append("Avalanche Occurrence Record: \n");
        buffer.append(AvOcc.getPitName() + "\n");
        buffer.append("Location: \n");
        buffer.append(loc.toString() + "\n");
      
        try
        {
            AvOcc.put("dtime", dtime);
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
        String l = null;
        String v = null;
        Iterator keys = AvOcc.sortedKeys();
        while ( keys.hasNext() )
        {
            String s = keys.next().toString();
            try
            {
                v =  AvOcc.get(s).toString();
            }
            catch(Exception ee)
            {
                System.out.println(ee.toString());
                v="";
            }
            l = (String) attributes.get(s);
            s = l + " " + v + "\n";
            if (( v!=null ) && ( v.trim().length() > 0 ))
            {
            	if (!( s==null ))buffer.append(s);
            }
        }
        
        try
        {
        	writer.print(buffer.toString());
        	writer.flush();
        	writer.close();
        }
        catch(Exception ex){System.out.println(ex.toString());}
	}
	
	void setLabels()
    {
        avscience.ppc.User u = pit.getUser();
        if ( u==null ) u = new avscience.ppc.User();
        attributes.put("pitObs", "Name: ");
        attributes.put("dtime", "Date/Time: ");
        attributes.put("estDate", "Estimated date: ");
        attributes.put("estTime", "Estimated Time: ");
        attributes.put("elvStart", "Elevation Start: (" + u.getElvUnits() + ") ");
        attributes.put("elvDeposit", "Elevation Deposit: (" + u.getElvUnits() + ") ");
        attributes.put("fractureWidth", "Fracture Width: (" + u.getElvUnits() + ") ");
        attributes.put("fractureLength", "Fracture Length: (" + u.getElvUnits() + ") ");
        attributes.put("lengthOfAvalanche", "Avalanche Length: (" + u.getElvUnits() + ") ");
       
        attributes.put("aspect", "Primary Aspect: ");
        attributes.put("aspect1", "Aspect 1: ");
        attributes.put("aspect2", "Aspect 2: ");
        attributes.put("type", "Type: ");
        attributes.put("wcStart", "Water Content Start: ");
        attributes.put("wcDeposit", "Water Content Deposit: ");
        attributes.put("triggerType", "Trigger Type: ");
        attributes.put("triggerCode", "Trigger Code: ");
        attributes.put("causeOfRelease", "Cause of release: ");
        attributes.put("sympathetic", "Sympathetic? ");
        attributes.put("sympDistance", "Sympathetic/remote distance: ");
        
        attributes.put("USSize", "Size relative to Path: ");
        attributes.put("CASize", "Size destructive force: ");
        attributes.put("avgFractureDepth", "Average Fracture Depth: (" + u.getDepthUnits() + ") " );
        attributes.put("maxFractureDepth", "Max. Fracture Depth: (" + u.getDepthUnits() + ") ");
        attributes.put("levelOfBedSurface", "Level Of Bed Surface: ");
        attributes.put("weakLayerType", "Weak Layer Type: ");
        attributes.put("crystalSize", "Weak Layer Crystal Size: ");
        attributes.put("sizeSuffix", "Weak Layer Size suffix: ");
        attributes.put("weakLayerHardness", "Weak Layer Hardness: ");
        attributes.put("hsuffix", "Weak Layer Hardness suffix: ");
        
        attributes.put("crystalTypeAbove", "Crystal Type Above: ");
        attributes.put("crystalSizeAbove", "Crystal Size Above: ");
        attributes.put("sizeSuffixAbove", "Size suffix above: ");
        attributes.put("hardnessAbove", "Hardness above: ");
        attributes.put("hsuffixAbove", "Hardness suffix above: ");
        
        attributes.put("crystalTypeBelow", "Crystal Type Below: ");
        attributes.put("crystalSizeBelow", "Crystal Size Below: ");
        attributes.put("sizeSuffixBelow", "Size suffix below: ");
        attributes.put("hardnessBelow", "Hardness below: ");
        attributes.put("hsuffixBelow", "Hardness suffix below: ");
        attributes.put("snowPackType", "Snow Pack Typology: ");
        attributes.put("avgStartAngle", "Avg Start Angle: ");
        attributes.put("maxStartAngle", "Max Start Angle: ");
        attributes.put("minStartAngle", "Min Start Angle: ");
        attributes.put("alphaAngle", "Alpha Angle: ");
        attributes.put("depthOfDeposit",  "Depth of deposit: (" + u.getDepthUnits() + ") ");
        attributes.put("lengthOfDeposit", "Length of deposit: ");
        attributes.put("widthOfDeposit", "Width of deposit: ");
        attributes.put("densityOfDeposit", "Density of deposit (" + u.getRhoUnits() + ") ");
  //      attributes.put("areaOfDeposit", "Area of deposit: (square-" + u.getElvUnits() + ") ");
  		attributes.put("numPeopleCaught", "Number of people caught: ");
        attributes.put("numPeoplePartBuried", "Number of people part buried: ");
        attributes.put("numPeopleTotalBuried", "Number of people totally buried: ");
        
        attributes.put("injury", "Injuries: ");
        attributes.put("fatality", "Fatalites: ");
        
        attributes.put("bldgDmg", "Building Damage US $: ");
        attributes.put("eqDmg", "Equipment Damage US $: ");
        attributes.put("vehDmg", "Vehicle Damage US $: ");
        attributes.put("miscDmg", "Misc Damage US $: ");
        attributes.put("estDamage", "Total Damage US $: ");
        
        
        attributes.put("comments", "Comments: ");
        attributes.put("hasPit", "Has pit observation? ");
    }
	
	public class SPFileFilter implements FilenameFilter
	{
	   public boolean accept(File dir, String file)
	   {
	       file = file.trim();
	       String end = file.substring(file.length()-4, file.length());
	       if ( end.equals(".txt")) return true;
	       else return false;
	   }
    }
	
	public AvApp getApp()
	{
		return app;
	}
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == OccFrame.this)
				AvApp_WindowClosing(event);
		}
	}
	
	private void buildMenu()
    {
    	
    	deleteMenuItem.setLabel("delete observation");
    	saveMenuItem.setLabel("save observation");
    	crownObsItem.setLabel("Crown Obs.");
    	if ( app!=null ) menu.setLabel("Delete/save");
    	else menu.setLabel("Select");
    	if ( app!=null ) menu.add(deleteMenuItem);
    	if (!embedded) menu.add(saveMenuItem);
    	//if ( AvOcc.hasPit()) 	
    	menu.add(crownObsItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
    	MenuAction mnac = new MenuAction();
    	deleteMenuItem.addActionListener(mnac);
    	saveMenuItem.addActionListener(mnac);
    	crownObsItem.addActionListener(mnac);
    	if ( webEdit )
    	{
    		editItem = new MenuItem();
    		editItem.setLabel("Edit Occurence");
    		editItem.addActionListener(mnac);
    		menu.add(editItem);
    	}
    }
	
	void AvApp_WindowClosing(java.awt.event.WindowEvent event)
	{
		
	//	(new CloseFrameDialog(this, true)).setVisible(true);
		this.dispose();	 
		
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
		   /// if (object == deleteMenuItem) deleteObs();
		    if (object == saveMenuItem) saveOcc();
		    if (object == crownObsItem ) editPit();
		    if (object == editItem ) new avscience.pc.OccFrame(AvOcc, pit, mf, true, applet, OccFrame.this).setVisible(true);
		}
	}
	
	void editPit()
    {
    	AvOcc.setHasPit(true);
    //	if ( applet!=null) applet.superuser = false;
    	
    	avscience.pc.PitFrame pframe = new avscience.pc.PitFrame(pit, mf,true , applet);
    
    }
	
	/*void deleteObs()
	{
		(new DeleteObsDialog(this, true, new PitObs(pit.toJSON()), store)).setVisible(true);	 
	}*/
	
}	
	