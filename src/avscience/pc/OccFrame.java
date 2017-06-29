package avscience.pc;

import java.awt.*;
import avscience.wba.*;
import java.util.*;
import avscience.desktop.*;
import java.util.Date;
import avscience.ppc.*;
import java.sql.Timestamp;
import java.net.*;
import java.util.Properties;

public class OccFrame extends Frame implements TimeFrame
{   
	avscience.ppc.PitObs pit = new avscience.ppc.PitObs();
	public avscience.ppc.AvOccurence occ;
	org.compiere.grid.ed.Calendar estDate;
//	long ts;
	Label lDate;
    Button setDate;
    Choice state;
    TextItem loc;
    TextItem range;
    ElvTextItem elv;
    LonTextItem lon;
    LatTextItem lat;
    ElvTextItem elvStart;
    ElvTextItem elvDebris;
    ElvTextItem fracWidth;
    ElvTextItem fracLength;
    ElvTextItem aviLength;
    DegTextItem primeAspect;
    DegTextItem aspect1;
    DegTextItem aspect2;
    Choice aviType;
    Choice wcStart;
    Choice wcDeposit;
    Choice triggerType;
    Choice triggerCode;
    Choice USSize;
    Choice CDNSize;
    DepthTextItem avgFractDepth;
    DepthTextItem maxFractDepth;
    Choice levelOfBedSurface;
    TypeDisplay weakLayerType;
    Button setWeakLayerType;
    TypeDisplay typeAbove;
    Button setTypeAbove;
    TypeDisplay typeBelow;
    Button setTypeBelow;
    GrainTypeSelectionFrame tcWeak;
    GrainTypeSelectionFrame tcAbove;
    GrainTypeSelectionFrame tcBelow;
    Choice weakLayerHardness;
    Choice hardnessAbove;
    Choice hardnessBelow;
    Choice hsuffixWeak;
    Choice hsuffixAbove;
    Choice hsuffixBelow;
   
    Choice sizeAbove;
    Choice sizeWL;
    Choice sizeBelow;
    Choice sizeSuffixAbove;
    Choice sizeSuffixWL;
    Choice sizeSuffixBelow;
    Choice snowPackType;
    SlopeAngleTextItem avgAngle;
    SlopeAngleTextItem maxAngle;
    SlopeAngleTextItem minAngle;
    DegTextItem alphaAngle;
    DepthTextItem depthDeposit;
    ElvTextItem lengthDeposit;
    ElvTextItem widthDeposit;
    IntTextItem areaDeposit;
    RhoTextItem rhoDeposit;
    Choice noCaught;
    Choice noPeoplePartBuried;
    Choice noPeopleTotalBuried;
    Choice noInjuries;
    Choice noFatalities;
    IntTextItem bldgDamage;
    IntTextItem eqDamage;
    IntTextItem vehDamage;
    IntTextItem miscDamage;
    IntTextItem totalDamage;
    TextArea ocComments;
    Label cul;
    Choice causeRelease;
    Choice sympathetic;
    ElvTextItem sympDistance;
    Button crownObs;
    ProfileDisplay profile;
    Button profileType; 
    ProfileCanvas profileCanvas;
    avscience.ppc.User u;
    int width = 980;
    int height = 740;
    int colSpace=324;
    int xx;
    int yy;
    int starty=4;
    int startx=24;
    int yspace=24;
    public MainFrame mf;
    String defaultType = "Natural";
    String defaultrms= "no";
    public Vector subFrames = new Vector();
    boolean edit = false;
    boolean smallscreen;
    Panel p;
    ScrollPane sp;
    private int swidth=780;
	private int sheight=520;
    SymWindow winlisten = new SymWindow();
    private MenuBar mainMenuBar = new java.awt.MenuBar();
    private Menu menu = new java.awt.Menu();
    private MenuItem exitMenuItem = new java.awt.MenuItem();
    private MenuItem saveMenuItem = new java.awt.MenuItem();
    private MenuItem deleteMenuItem = new java.awt.MenuItem();
    private MenuItem crownMenuItem = new java.awt.MenuItem();
    PitApplet applet;
    boolean webEdit;
    final static int maxDataLength=4500;
    avscience.desktop.OccFrame occFrame;
    
    public OccFrame(boolean edit, MainFrame mf)
    {
    	super();
    	this.mf = mf;
    	this.edit=edit;
    	String serial = mf.store.getNewSerial();
        pit = new avscience.ppc.PitObs(mf.getUser(), serial, mf.bld, mf.version);
    	setTitle("Snow Pilot - Add/Edit Avalanche Occurence");
    	addWindowListener(winlisten);
    	u = new avscience.ppc.User();
    	if (edit)
    	{
    		setTitle("Snow Pilot - Edit Avalanche Occurence");
    		u = pit.getUser();
    	}
    	else
    	{
    		setTitle("Snow Pilot - Add Avalanche Occurence");
    		u = mf.getUser();
    	}
    	initControls();
    	buildMenu();
    	buildForm(edit);
    	avscience.ppc.Location l = mf.getCurrentLocation();
    	if ( l !=null ) popLocation(l);
    }
    
    
    public OccFrame(avscience.ppc.AvOccurence occ, avscience.ppc.PitObs pit, MainFrame mf, boolean edit, PitApplet applet, avscience.desktop.OccFrame occFrame)
    {
    	super();
    	this.occ = occ;
    	this.mf = mf;
    	this.edit=edit;
    	this.occFrame = occFrame;
    	this.occ.setEdited(true);
    	setTitle("Snow Pilot - Add/Edit Avalanche Occurence");
    	addWindowListener(winlisten);
    	this.applet = applet;
    	if ( applet == null ) webEdit = false;
    	else webEdit = applet.superuser;
    	String serial = occ.getSerial();
    	if ( webEdit ) this.pit = pit;
    	else this.pit = mf.store.getPit(serial);
    	if (pit == null)
    	{
    		System.out.println("occ-pit null");
    		pit = new avscience.ppc.PitObs();
    	}
    	u = new avscience.ppc.User();
    	if (edit)
    	{
    		setTitle("Snow Pilot - Edit Avalanche Occurence");
    		u = pit.getUser();
    	}
    	else
    	{
    		setTitle("Snow Pilot - Add Avalanche Occurence");
    		u = mf.getUser();
    	}
    	
    	initControls();
    	buildMenu();
    	buildForm(edit);
    	popForm();
    	System.out.println("Occ serial: "+occ.getSerial());
    	System.out.println("Pit serial: "+pit.getSerial());
    }
    
    void editPit()
    {
    	if ( occ == null ) save();
    	occ.setHasPit(true);
    	PitFrame pframe = null;
    	if ( webEdit) pframe = new avscience.pc.PitFrame(pit, mf, false, applet);
    	else pframe = new avscience.pc.PitFrame(pit, mf, false);
    	pframe.oframe = this;
    }
  
    public void buildForm(boolean edit)
    {
    	
    	if (u==null) u = new avscience.ppc.User();
    	setLayout(null);
    	setSize(width, height);
    	
    	xx = startx;
        yy = starty;
        loc = new TextItem("Location", xx, yy);
      	add(loc);
        
        yy+=yspace;
        Label sl = new Label("State/Prov");
        sl.setSize(78, 20);
        sl.setVisible(true);
        sl.setLocation(xx, yy);
        p.add(sl);
        state.setLocation(xx+80, yy);
        p.add(state);
        yy+=yspace;
        state.select(u.getState());
        range = new TextItem("Mtn Range", xx, yy);
        add(range);
        yy+=yspace;
        elv = new ElvTextItem("Elevation " + u.getElvUnits(), u.getElvUnits(), xx, yy);
        add(elv);
        yy+=yspace;
        
        lon = new LonTextItem("Long. "+u.getLongType(), xx, yy);
        add(lon);
        yy+=yspace;
        
        lat = new LatTextItem("Lat. "+u.getLatType(), xx, yy);
        add(lat);
        yy+=yspace;
        
        String s = "";
        if ( !edit) s = new java.util.Date().toString();
        lDate = new Label(s);
        lDate.setSize(220, 20);
        lDate.setLocation(xx, yy);
        lDate.setVisible(true);
        if (!edit) lDate.setText(new Date().toString());
        p.add(lDate);
        yy+=yspace;
        setDate = new Button("Set Date/Time");
        setDate.setLocation(xx, yy);
        setDate.setSize(98, 26);
        setDate.addActionListener(new MenuAction());
        p.add(setDate);
        yy+=yspace;
        elvStart = new ElvTextItem("Elevation of start zone "+u.getElvUnits(), u.getElvUnits(), xx, yy);
        elvStart.setLabelWidth(138);
        add(elvStart);
        yy+=yspace;
        elvDebris = new ElvTextItem("Elevation of debris "+u.getElvUnits(), u.getElvUnits(), xx, yy);
        add(elvDebris);
        elvDebris.setLabelWidth(120);
        yy+=yspace;
        fracWidth = new ElvTextItem("Fracture width "+u.getElvUnits(), u.getElvUnits(), xx, yy);
        add(fracWidth);
        fracWidth.setLabelWidth(102);
        yy+=yspace;
        fracLength = new ElvTextItem("Fracture length - top of fracture to stauchwall. "+u.getElvUnits(), u.getElvUnits(), xx, yy, true);
        add(fracLength);
        fracLength.setLabelWidth(colSpace);
        yy+=yspace;
        yy+=yspace;
        aviLength = new ElvTextItem("Avalanche length - top of fracture to bottom of debris. "+u.getElvUnits(), u.getElvUnits(), xx, yy, true);
        aviLength.setLabelWidth(colSpace);
        add(aviLength);
        yy+=yspace;
        yy+=yspace;
        primeAspect = new DegTextItem("Primary aspect ", xx, yy);
        add(primeAspect);
        primeAspect.setLabelWidth(92);
        yy+=yspace;
        aspect1 = new DegTextItem("Aspect range   ", xx, yy);
        aspect1.setLabelWidth(92);
        add(aspect1);
        yy+=yspace;
        aspect2 = new DegTextItem("to ", xx, yy);
        add(aspect2);
        aspect2.setLabelWidth(24);
        yy+=yspace;
        Label tl = new Label("Avalanche Type");
        tl.setSize(120, 20);
        tl.setLocation(xx, yy);
        p.add(tl);
       
        aviType.setLocation(xx+130, yy);
        p.add(aviType);
        yy+=yspace;
        Label wcs = new Label("Water Content Start");
        wcs.setSize(110, 20);
        wcs.setLocation(xx, yy);
        p.add(wcs);
        wcStart.setLocation(xx+120, yy);
        p.add(wcStart);
        yy+=yspace;
        
        Label wcd = new Label("Water Content Deposit");
        wcd.setSize(130, 20);
        wcd.setLocation(xx, yy);
        p.add(wcd);
        wcDeposit.setLocation(xx+132, yy);
        p.add(wcDeposit);
        yy+=yspace;
        Label ttl = new Label("Trigger type");
        ttl.setSize(120, 20);
        ttl.setLocation(xx, yy);
        p.add(ttl);
        yy+=yspace;
        
        triggerType.setLocation(xx, yy);
        p.add(triggerType);
        if (edit) defaultType=occ.getTriggerType();
        else defaultType = " ";
       // if ((defaultType==null)|(defaultType.trim().length()<1))defaultType = "Natural";
        triggerType.select(defaultType);
        yy+=yspace;
        
        triggerCode.removeAll();
       	String[] codes = TriggerType.getInstance().getDescriptions(defaultType);
       	for (int i=0; i<codes.length; i++)
       	{
       		triggerCode.add(codes[i]);
       	}
       	
       	Label tcl = new Label("Trigger code");
       	tcl.setSize(148, 22);
       	tcl.setLocation(xx, yy);
       	p.add(tcl);
       	yy+=yspace;
       	
       	triggerCode.setLocation(xx, yy);
       	p.add(triggerCode);
       	yy+=yspace;
       	
    	cul = new Label("Cause of release");
        cul.setSize(120,22);
        cul.setLocation(xx, yy);
        p.add(cul);
        causeRelease.setLocation(xx+140, yy);
        p.add(causeRelease);
       	yy+=yspace;
       	if ( defaultType.equals("Artificial - Human"))
       	{
       		cul.setVisible(true);
       		causeRelease.setVisible(true);
       	}
       	else
       	{
       		cul.setVisible(false);
       		causeRelease.setVisible(false);
       	}
       	xx+=colSpace;
        yy=starty;
        Label rsl = new Label("Remote or sympathetic?");
        rsl.setSize(146, 20);
        rsl.setLocation(xx, yy);
        p.add(rsl);
        yy+=yspace;
        sympathetic.setLocation(xx, yy);
        p.add(sympathetic);
        yy+=yspace;
        if (edit) 
        {
        	defaultrms=occ.getSympathetic();
        	if ((defaultrms==null)|(defaultrms.trim().length()<1))defaultrms = "no";
        	
        }
        else defaultrms = " ";
        sympathetic.select(defaultrms);
        
        sympDistance = new ElvTextItem("How far ("+u.getElvUnits()+")?", u.getElvUnits(), xx, yy);
        add(sympDistance);
        yy+=yspace;
        
       	if ( defaultrms.equals("remote") || defaultrms.equals("sympathetic")) sympDistance.setVisible(true);
       	else sympDistance.setVisible(false);
        /// add symp distance.
        Label sizeLab = new Label("Size - Relative to Path");
        sizeLab.setSize(130, 20);
        sizeLab.setLocation(xx, yy);
        p.add(sizeLab);
        
        USSize.setLocation(xx+140, yy);
        p.add(USSize);
        yy+=yspace;
        
        Label sLab = new Label("Size - Destructive Force");
        sLab.setSize(130, 20);
        sLab.setLocation(xx, yy);
        p.add(sLab);
        
        CDNSize.setLocation(xx+140, yy);
        p.add(CDNSize);
        
        yy+=yspace;
        avgFractDepth = new DepthTextItem("Avg. Fracture Depth "+u.getDepthUnits(), xx, yy);
        avgFractDepth.setLabelWidth(132);
        add(avgFractDepth);
        
        yy+=yspace;
        
        maxFractDepth = new DepthTextItem("Max. Fracture Depth "+u.getDepthUnits(), xx, yy);
        maxFractDepth.setLabelWidth(132);
        add(maxFractDepth);
        yy+=yspace;
        
        Label lv = new Label("Level of bed surface");
        lv.setSize(160, 20);
        lv.setLocation(xx, yy);
        p.add(lv);
        yy+=yspace;
        levelOfBedSurface.setLocation(xx, yy);
        p.add(levelOfBedSurface);
        yy+=yspace;
        Label wlct = new Label("Weak layer crystal type");
        wlct.setLocation(xx, yy);
        wlct.setSize(140, 18);
        p.add(wlct);
        weakLayerType.setLocation(xx+144, yy);
        weakLayerType.setVisible(true);
        p.add(weakLayerType);
        yy+=yspace;
        setWeakLayerType.setSize(88, 24);
        setWeakLayerType.setLocation(xx, yy);
        p.add(setWeakLayerType);
        
        yy+=yspace;
        Label wct = new Label("Weak layer crystal size (mm)");
        wct.setSize(162, 18);
        wct.setLocation(xx, yy);
        p.add(wct);
        
        sizeWL.setLocation(xx+166, yy);
        p.add(sizeWL);
        
        yy+=yspace;
        Label ls = new Label("<>");
        ls.setSize(24, 18);
        ls.setLocation(xx, yy);
        p.add(ls);
        
        sizeSuffixWL.setLocation(xx+28, yy);
        p.add(sizeSuffixWL);
        yy+=yspace;
        
        Label wlh = new Label("Weak layer hardness");
        wlh.setLocation(xx, yy);
        wlh.setSize(126, 18);
        p.add(wlh);
        
        weakLayerHardness.setLocation(xx+132, yy);
        p.add(weakLayerHardness);
        yy+=yspace;
        
        Label pm1 = new Label("+-");
        pm1.setLocation(xx, yy);
        pm1.setSize(24, 18);
        p.add(pm1);
        
        hsuffixWeak.setLocation(xx+24, yy);
        p.add(hsuffixWeak);
        //////////
        yy+=yspace;
        Label wlcta = new Label("Crystal type above weak layer");
        wlcta.setLocation(xx, yy);
        wlcta.setSize(164, 18);
        p.add(wlcta);
        typeAbove.setLocation(xx+168, yy);
        typeAbove.setVisible(true);
        p.add(typeAbove);
        yy+=yspace;
        setTypeAbove.setSize(88, 24);
        setTypeAbove.setLocation(xx, yy);
        p.add(setTypeAbove);
        
        yy+=yspace;
        Label wcta = new Label("Crystal size above weak layer (mm)");
        wcta.setSize(196,18);
        wcta.setLocation(xx, yy);
        p.add(wcta);
        
        sizeAbove.setLocation(xx+202, yy);
        p.add(sizeAbove);
        
        yy+=yspace;
        Label lsa = new Label("<>");
        lsa.setSize(24, 18);
        lsa.setLocation(xx, yy);
        p.add(lsa);
        
        sizeSuffixAbove.setLocation(xx+28, yy);
        p.add(sizeSuffixAbove);
        yy+=yspace;
        
        Label wlha = new Label("Hardness above weak layer");
        wlha.setLocation(xx, yy);
        wlha.setSize(158, 18);
        p.add(wlha);
        
        hardnessAbove.setLocation(xx+163, yy);
        p.add(hardnessAbove);
        yy+=yspace;
        
        Label pm1a = new Label("+-");
        pm1a.setLocation(xx, yy);
        pm1a.setSize(24, 18);
        p.add(pm1a);
        
        hsuffixAbove.setLocation(xx+24, yy);
        p.add(hsuffixAbove);
        ////////////////
        yy+=yspace;
        Label wlctb = new Label("Crystal type below weak layer");
        wlctb.setLocation(xx, yy);
        wlctb.setSize(164, 18);
        p.add(wlctb);
        typeBelow.setLocation(xx+168, yy);
        typeBelow.setVisible(true);
        p.add(typeBelow);
        yy+=yspace;
        setTypeBelow.setSize(88, 24);
        setTypeBelow.setLocation(xx, yy);
        p.add(setTypeBelow);
        
        yy+=yspace;
        Label wctb = new Label("Crystal size below weak layer (mm)");
        wctb.setSize(196,18);
        wctb.setLocation(xx, yy);
        p.add(wctb);
        
        sizeBelow.setLocation(xx+202, yy);
        p.add(sizeBelow);
        
        yy+=yspace;
        Label lsb = new Label("<>");
        lsb.setSize(24, 18);
        lsb.setLocation(xx, yy);
        p.add(lsb);
        
        sizeSuffixBelow.setLocation(xx+28, yy);
        p.add(sizeSuffixBelow);
        yy+=yspace;
        
        Label wlhb = new Label("Hardness below weak layer");
        wlhb.setLocation(xx, yy);
        wlhb.setSize(158, 18);
        p.add(wlhb);
        
        hardnessBelow.setLocation(xx+163, yy);
        p.add(hardnessBelow);
        yy+=yspace;
        
        Label pm1b = new Label("+-");
        pm1b.setLocation(xx, yy);
        pm1b.setSize(24, 18);
        p.add(pm1b);
        
        hsuffixBelow.setLocation(xx+24, yy);
        p.add(hsuffixBelow);
        xx+=colSpace;
        yy=starty;
        Label spt = new Label("Snow pack typology");
        spt.setSize(120, 18);
        spt.setLocation(xx, yy);
        p.add(spt); 
        profile.setLocation(xx+130, yy);
        p.add(profile);
        yy+=yspace;
        yy+=yspace;
        profileType.setLocation(xx, yy);
        profileType.setSize(120, 22);
        p.add(profileType);
        yy+=yspace;
        avgAngle = new SlopeAngleTextItem("Avg start angle", xx, yy);
        add(avgAngle);
        yy+=yspace;
        maxAngle = new SlopeAngleTextItem("Max start angle", xx, yy);
        add(maxAngle);
        yy+=yspace;
        minAngle = new SlopeAngleTextItem("Min start angle", xx, yy);
        add(minAngle);
		yy+=yspace;
        alphaAngle = new DegTextItem("Alpha angle", xx, yy);
        add(alphaAngle);
        /////////
        yy+=yspace;
        depthDeposit = new DepthTextItem("Depth of Deposit "+u.getElvUnits(), xx, yy);
        yy+=yspace;
        add(depthDeposit);
        lengthDeposit = new ElvTextItem("Length of Deposit "+u.getElvUnits(), u.getElvUnits(), xx, yy);
        yy+=yspace;
        add(lengthDeposit);
        widthDeposit = new ElvTextItem("Width of Deposit "+u.getElvUnits(), u.getElvUnits(), xx, yy);
        add(widthDeposit);
       
        yy+=yspace;
		rhoDeposit = new RhoTextItem("Density of deposit "+u.getRhoUnits(), u.getRhoUnits(), xx, yy);
		add(rhoDeposit);
		yy+=yspace;
		
		Label npc = new Label("No. of people caught");
		npc.setLocation(xx, yy);
		npc.setSize(170, 22);
		p.add(npc);
		noCaught.setLocation(xx+178, yy);
		p.add(noCaught);
		
		yy+=yspace;
		Label nppb = new Label("No. of people partially buried");
		nppb.setLocation(xx, yy);
		nppb.setSize(170, 22);
		p.add(nppb);
		noPeoplePartBuried.setLocation(xx+178, yy);
		p.add(noPeoplePartBuried);
		
		yy+=yspace;
		Label nptt = new Label("No. of people totally buried");
		nptt.setLocation(xx, yy);
		nptt.setSize(170, 22);
		p.add(nptt);
		noPeopleTotalBuried.setLocation(xx+178, yy);
		p.add(noPeopleTotalBuried);
		
		yy+=yspace;
		Label noinj = new Label("No. injuries");
		noinj.setLocation(xx, yy);
		noinj.setSize(170, 22);
		p.add(noinj);
		noInjuries.setLocation(xx+178, yy);
		p.add(noInjuries);
		
		yy+=yspace;
		Label nofat = new Label("No. fatalities");
		nofat.setLocation(xx, yy);
		nofat.setSize(170, 22);
		p.add(nofat);
		noFatalities.setLocation(xx+178, yy);
		p.add(noFatalities);
		
		yy+=yspace;
		bldgDamage = new IntTextItem("Building damage (US $)", xx, yy);
		add(bldgDamage);
		yy+=yspace;
		eqDamage = new IntTextItem("Equipment damage (US $)", xx, yy);
		add(eqDamage);
		yy+=yspace;
		vehDamage = new IntTextItem("Vehicle damage (US $)", xx, yy);
		add(vehDamage);
		yy+=yspace;
		miscDamage = new IntTextItem("Misc. damage (US $)", xx, yy);
		add(miscDamage);
		yy+=yspace;
		totalDamage = new IntTextItem("Total damage (US $)", xx, yy);
		add(totalDamage);
		yy+=yspace;
		Label com = new Label("Comments");
		com.setLocation(xx, yy);
		com.setSize(140, 22);
		p.add(com);
		yy+=yspace;
		ocComments = new TextArea();
		ocComments.setLocation(xx, yy);
		ocComments.setSize(286, 120);
		p.add(ocComments);
		long ts = System.currentTimeMillis();
        Timestamp tts = new Timestamp(ts);
        estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", tts, 16, this);
		estDate.addWindowListener(winlisten);
		estDate.setVisible(false);
    }
    
    
    
    public void removeWebPit(avscience.ppc.PitObs pit)
    {
    	System.out.println("removeWebPit()");
    	String serial = pit.getSerial();
    	String username = pit.getUser().getName();
    	String name = pit.getDBName();
    	System.out.println("serial: "+serial+" user: "+username+" name: "+name);
    	try
        {
            URL url = new URL(mf.pitserver);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("TYPE", "DELETEPIT");
            props.put("SERIAL", serial);
            props.put("USERNAME", username);
            props.put("PITNAME", name);
            msg.sendGetMessage(props);
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    
    private void buildMenu()
    {
        saveMenuItem.setLabel("save changes");
        exitMenuItem.setLabel("exit");
        crownMenuItem.setLabel("Crown observation");
        deleteMenuItem.setLabel("Delete observation");
        menu.setLabel("Avalanche occurence..");
        menu.add(saveMenuItem);
        menu.add(exitMenuItem);
        menu.add(crownMenuItem);
        menu.add(deleteMenuItem);
        mainMenuBar.add(menu);
        setMenuBar(mainMenuBar);
        MenuAction men = new MenuAction();
        saveMenuItem.addActionListener(men);
        exitMenuItem.addActionListener(men);
        crownMenuItem.addActionListener(men);
        deleteMenuItem.addActionListener(men);
    }
    
    void deleteOcc()
    {
    	if ( webEdit ) 
    	{
    		deleteWebOcc();
    		removeWebPit(pit);
    		applet.reinit();
    		occFrame.dispose();
    	}
    	else
    	{
    		mf.store.removeOcc(occ.getSerial());
    		mf.store.removePit(occ.getSerial());
    		mf.rebuildList();
    	}
    }
    
    public void updateWebOcc(avscience.ppc.AvOccurence occ)
    {
    	System.out.println("updateWebOcc()");
    	try
        {
            URL url = new URL(mf.server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "occsend");
            //avscience.ppc.AvOccurence occ = store.getOcc(occSers[i]);
            String s = occ.toXML();
            s = URLEncoder.encode(s);
            props.put("OCCDATA", s);
            msg.sendGetMessage(props);
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }
    
    public void updateWebPit(avscience.ppc.PitObs pit)
    {
    	System.out.println("updateWebPit()");
    	try
        {
            URL url = new URL(mf.server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "pitsend");
            System.out.println("Pit name: "+pit.getName());
           // System.out.println("Pit dat: "+pit.dataString());
            String s = pit.toXML();
            
            int dsize=s.length();
            System.out.println("Data size: "+dsize);
            if ( dsize > maxDataLength )
            {
            	System.out.println("Dsize:: "+dsize);
            	String s1=s.substring(0, maxDataLength);
            	String s2=s.substring(maxDataLength, s.length());
            	s1 = URLEncoder.encode(s1);
            	s2 = URLEncoder.encode(s2);
            	HttpMessage msg1 = new HttpMessage(url);
            	Properties props1 = new Properties();
            	props1.put("format", "bigpitsend1");
            	HttpMessage msg2 = new HttpMessage(url);
            	Properties props2 = new Properties();
            	props2.put("format", "bigpitsend2");
            	props1.put("PITDATA1", s1);
            	props2.put("PITDATA2", s2);
            	msg1.sendGetMessage(props1);
            	Thread.sleep(1200);
            	msg2.sendGetMessage(props2);
            //	if ((msg1!=null ) && (msg2!=null)) sent = true;
            }
            else
            {
            	s = URLEncoder.encode(s);
                props.put("PITDATA", s);
                msg.sendGetMessage(props);
        	}
            
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }	
    }
    
    
    void deleteWebOcc()
    {
    	String serial = pit.getSerial();
    	String username = pit.getUser().getName();
    	String name = pit.getDBName();
    	System.out.println("serial: "+serial+" user: "+username+" name: "+name);
    	try
        {
            URL url = new URL(mf.pitserver);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("TYPE", "DELETEOCC");
            props.put("SERIAL", serial);
            props.put("USERNAME", username);
            props.put("PITNAME", name);
            msg.sendGetMessage(props);
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    
    class ChangeTriggerAction implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			Object object = event.getSource();
			if ( object == triggerType ) changeTriggerType();
			if ( object == sympathetic ) changeRMS();
			
		}
	}
	
	/*public void updateEstDate()
	{
		Date dd = new Date(ts);
      	lDate.setText(dd.toString());
      	Timestamp tts = new Timestamp(ts);
      	estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", tts, 16, this);
		estDate.addWindowListener(winlisten);
		estDate.setVisible(false);
	}*/
	
	void popLocation(avscience.ppc.Location l)
	{
		String name = l.getName();
		String selv = l.getElv();
		String sstate = l.getState();
		String srange = l.getRange();
		String slat = l.getLat();
		String slon = l.getLongitude();
		System.out.println("Loc name: "+name);
		System.out.println("Loc elv: "+selv);
		System.out.println("Loc state: "+sstate);
		System.out.println("Loc range: "+srange);
	//	System.out.println("Loc DATA:: "+l.dataString());
        loc.setText(name);
        range.setText(srange);
        elv.setText(selv);
        lat.setText(slat);
        lon.setText(slon);
        state.select(sstate);
	}
	
	void popForm()
	{
		System.out.println("popForm()");
        Location l = pit.getLocation();
        popLocation(l);
        long ts = pit.getTimestamp();
      	System.out.println("Timestamp: "+ts);
      	lDate.setText(new Date(ts).toString());
      /*	if ( ts > 0 )
      	{
      		updateEstDate();
      	}
      	else
      	{
	      	boolean udate = true;
	      	
	      	String dt = pit.getDate();
	      	if (dt.trim().length()<8) udate = false;
	      	String time = pit.getTime();
	      	if ( udate )
	      	{
	      	
		      	String yr="0";
		      	String mnth="0";
		      	String dy="0";
		      	String hr = "0";
		      	String min = "0";
		      	if (!(dt.trim().length()<8)) 
		      	{
		      		yr = dt.substring(0, 4);
		      		mnth = dt.substring(4, 6);
		      		dy = dt.substring(6, 8);
		      	}
		      	
		      	if ( time!= null )
		      	{
			      	if ( !(time.trim().length()<4))
			      	{
			      		hr = time.substring(0, 2);
			      		min = time.substring(2, 4);
			      	}
			     }
		      	
		      	int y = new Integer(yr).intValue();
		      	int m = new Integer(mnth).intValue()-1;
		      	int d = new Integer(dy).intValue();
		      	int h = new Integer(hr).intValue();
		      	int mn = new Integer(min).intValue();
		      	java.util.Calendar cal = java.util.Calendar.getInstance();
		      	cal.set(y, m, d, h, mn);
		      	ts = cal.getTimeInMillis();
		      	updateEstDate();
		     }
		     else
		     {
		     	String ds = pit.getDateString();
		     	if ( ds.trim().length() > 2) lDate.setText(ds);
		     	else lDate.setText(new Date().toString());
		     }
		}*/
        elvDebris.setText(occ.getElvDeposit());
        elvStart.setText(occ.getElvStart());
        fracLength.setText(occ.getFractureLength());
        fracWidth.setText(occ.getFractureWidth());
        aviLength.setText(occ.getLengthOfAvalanche());
        primeAspect.setText(occ.getAspect());
        aspect1.setText(occ.getAspect1());
        aspect2.setText(occ.getAspect2());
        aviType.select(occ.getType());
        wcStart.select(occ.getWcStart());
        wcDeposit.select(occ.getWcDeposit());
        triggerType.select(occ.getTriggerType());
        triggerCode.select(occ.getTriggerCode());
        
        sympathetic.select(occ.getSympathetic());
        if ( causeRelease!=null) causeRelease.select(occ.getCauseOfRelease());
        if ( sympDistance!=null) sympDistance.setText(occ.getSympDistance());
        USSize.select(occ.getUSSize());
        CDNSize.select(occ.getCASize());
        avgFractDepth.setText(occ.getAvgFractureDepth());
        maxFractDepth.setText(occ.getAvgFractureDepth());
        levelOfBedSurface.select(occ.getLevelOfBedSurface());
        weakLayerType.setType(occ.getWeakLayerType());
        weakLayerHardness.select(occ.getWeakLayerHardness());
        hardnessBelow.select(occ.getHardnessBelow());
        hardnessAbove.select(occ.getHardnessAbove());
        hsuffixWeak.select(occ.getHsuffix());
        hsuffixAbove.select(occ.getHsuffixAbove());
        hsuffixBelow.select(occ.getHsuffixBelow());
        typeAbove.setType(occ.getCrystalTypeAbove());
        typeBelow.setType(occ.getCrystalTypeBelow());
     
        sizeAbove.select(occ.getCrystalSizeAbove());
        sizeBelow.select(occ.getCrystalSizeBelow());
        sizeWL.select(occ.getCrystalSize());
        sizeSuffixAbove.select(occ.getSizeSuffixAbove());
        sizeSuffixBelow.select(occ.getSizeSuffixBelow());
        sizeSuffixWL.select(occ.getSizeSuffix());
        if (snowPackType!=null) snowPackType.select(occ.getSnowPackType());
        if (profile!=null) profile.setType(occ.getSnowPackType());
        avgAngle.setText(occ.getAvgStartAngle());
        minAngle.setText(occ.getMinStartAngle());
        maxAngle.setText(occ.getMaxStartAngle());
        alphaAngle.setText(occ.getAlphaAngle());
        depthDeposit.setText(occ.getDepthOfDeposit());
        widthDeposit.setText(occ.getWidthOfDeposit());
        lengthDeposit.setText(occ.getLengthOfDeposit());
        rhoDeposit.setText(occ.getDensityOfDeposit());
        noCaught.select(occ.getNumPeopleCaught());
        noPeoplePartBuried.select(occ.getNumPeoplePartBuried());
        noPeopleTotalBuried.select(occ.getNumPeopleTotalBuried());
        noInjuries.select(occ.getInjury());
        noFatalities.select(occ.getFatality());
        bldgDamage.setText(occ.getBldgDmg());
        eqDamage.setText(occ.getEqDmg());
        vehDamage.setText(occ.getVehDmg());
        miscDamage.setText(occ.getMiscDmg());
        totalDamage.setText(occ.getTotalDmg());
        ocComments.setText(occ.getComments());
        tcWeak.setType(occ.getWeakLayerType());
        tcAbove.setType(occ.getCrystalTypeAbove());
        tcBelow.setType(occ.getCrystalTypeBelow());
        profileCanvas.setProfileType(occ.getSnowPackType());
	}
	
	
	void updateOccurenceFromForm()
	{
       // if (ts>0) pit.setTimestamp(ts);
        occ.setElvDeposit(elvDebris.getText());
        occ.setElvStart(elvStart.getText());
        occ.setFractureLength(fracLength.getText());
        occ.setFractureWidth(fracWidth.getText());
        occ.setLengthOfAvalanche(aviLength.getText());
        occ.setAspect(primeAspect.getText());
        occ.setAspect1(aspect1.getText());
        occ.setAspect2(aspect2.getText());
       
        occ.setType(aviType.getSelectedItem());
        occ.setWcStart(wcStart.getSelectedItem());
        occ.setWcDeposit(wcDeposit.getSelectedItem());
        String tt = triggerType.getSelectedItem();
        if ( tt==null ) tt="";
        //if ( (tt==null)||(tt.trim().length()<1) ) tt="Natural Triggers";
        occ.setTriggerType(tt);
        occ.setTriggerCode(triggerCode.getSelectedItem());
        occ.setSympathetic( sympathetic.getSelectedItem());
        if ( causeRelease!=null) occ.setCauseOfRelease(causeRelease.getSelectedItem());
        if ( sympDistance!=null) occ.setSympDistance(sympDistance.getText());
        /////////
        occ.setUSSize(USSize.getSelectedItem());
        occ.setCASize(CDNSize.getSelectedItem());
        occ.setAvgFractureDepth(avgFractDepth.getText());
        occ.setMaxFractureDepth(maxFractDepth.getText());
        occ.setLevelOfBedSurface(levelOfBedSurface.getSelectedItem());
        occ.setWeakLayerType(weakLayerType.getType());
        occ.setWeakLayerHardness(weakLayerHardness.getSelectedItem());
        occ.setHardnessAbove( hardnessAbove.getSelectedItem());
        occ.setHardnessBelow( hardnessBelow.getSelectedItem());
        occ.setHsuffix(hsuffixWeak.getSelectedItem());
        occ.setHsuffixAbove(hsuffixAbove.getSelectedItem());
        occ.setHsuffixBelow(hsuffixBelow.getSelectedItem());
       	occ.setCrystalTypeAbove(typeAbove.getType());
     	occ.setCrystalTypeBelow(typeBelow.getType());
     
        occ.setCrystalSizeAbove(sizeAbove.getSelectedItem());
        occ.setCrystalSizeBelow(sizeBelow.getSelectedItem());
        occ.setCrystalSize(sizeWL.getSelectedItem());
        occ.setSizeSuffixAbove(sizeSuffixAbove.getSelectedItem());
        occ.setSizeSuffixBelow(sizeSuffixBelow.getSelectedItem());
        occ.setSizeSuffix(sizeSuffixWL.getSelectedItem());
    	occ.setSnowPackType(profile.toString());
        occ.setAvgStartAngle(avgAngle.getText());
        occ.setMaxStartAngle(maxAngle.getText());
        occ.setMinStartAngle(minAngle.getText());
        occ.setAlphaAngle(alphaAngle.getText());
        occ.setDepthOfDeposit(depthDeposit.getText());
        occ.setWidthOfDeposit(widthDeposit.getText());
        occ.setLengthOfDeposit(lengthDeposit.getText());
        occ.setDensityOfDeposit(rhoDeposit.getText());
        occ.setNumPeopleCaught(noCaught.getSelectedItem());
        occ.setNumPeoplePartBuried(noPeoplePartBuried.getSelectedItem());
        occ.setNumPeopleTotalBuried(noPeopleTotalBuried.getSelectedItem());
        occ.setInjury(noInjuries.getSelectedItem());
        occ.setFatality(noFatalities.getSelectedItem());
        occ.setBldgDmg(bldgDamage.getText());
        occ.setEqDmg(eqDamage.getText());
        occ.setVehDmg(vehDamage.getText());
        occ.setMiscDmg(miscDamage.getText());
        occ.setTotalDmg(totalDamage.getText());
        occ.setComments(ocComments.getText());
	}
	
	void changeRMS()
	{
		defaultrms = sympathetic.getSelectedItem();
		if ( defaultrms.equals("remote") || defaultrms.equals("sympathetic")) sympDistance.setVisible(true);
       	else sympDistance.setVisible(false);
	}
    
    void changeTriggerType()
    {
    	defaultType = triggerType.getSelectedItem();
    	triggerCode.removeAll();
       	String[] codes = TriggerType.getInstance().getDescriptions(defaultType);
       	for (int i=0; i<codes.length; i++)
       	{
       		triggerCode.add(codes[i]);
       	}
       	if ( defaultType.equals("Artificial - Human"))
       	{
       		cul.setVisible(true);
       		causeRelease.setVisible(true);
       	}
       	else
       	{
       		cul.setVisible(false);
       		causeRelease.setVisible(false);
       	}
    }
    
    void initControls()
    {
    	this.smallscreen=mf.smallscreen;
    	p = new Panel();
	    p.setSize(width, height);
	    
	    p.setLayout(null);
	    p.setLocation(0, 0);
	    
	//	sp = new ScrollPane();
	//	sp.doLayout();
	//    sp.add(p);
	
	   // Adjustable vadjust = sp.getVAdjustable();
	   // Adjustable hadjust = sp.getHAdjustable();
	  //  hadjust.setUnitIncrement(10);
	  //  vadjust.setUnitIncrement(10);
		
		add("Center", p);
		pack();
	/*	if (smallscreen)
		{
			setLocation(-4, 0);
			setSize(swidth, sheight);
			sp.setLocation(0, -18);
			sp.setSize(swidth+30, sheight+50);
		}
		else
		{
			sp.setLocation(0, -18);
			sp.setSize(width+4, height+4);
		}*/
		
    //	ts=System.currentTimeMillis();
    	MenuAction action = new MenuAction();
    	weakLayerType = new TypeDisplay("");
        typeAbove = new TypeDisplay("");
        typeBelow = new TypeDisplay("");
    	tcWeak = new GrainTypeSelectionFrame(weakLayerType, 20, 20);
    	tcAbove = new GrainTypeSelectionFrame(typeAbove, 100, 100);
    	tcBelow = new GrainTypeSelectionFrame(typeBelow, 220, 220);
    	subFrames.add(tcWeak);
    	subFrames.add(tcAbove);
    	subFrames.add(tcBelow);
    	tcWeak.setVisible(false);
    	tcBelow.setVisible(false);
    	tcAbove.setVisible(false);
        
        setWeakLayerType = new Button("Select Type");
        setTypeAbove = new Button("Select Type");
        setTypeBelow = new Button("Select Type");
        profileType = new Button("Select profile type");
        setWeakLayerType.addActionListener(action);
        setTypeAbove.addActionListener(action);
        setTypeBelow.addActionListener(action);
        profileType.addActionListener(action);
        profile = new ProfileDisplay(0);
        profileCanvas = new ProfileCanvas(profile);
        profileCanvas.setVisible(false);
        subFrames.add(profileCanvas);
        state = new Choice();
        state.setSize(64, 32);
        String[] states = new StateProv().getList();
        for (int i=0;i<states.length;i++)
        {
        	state.add(states[i]);
        }
        
        aviType = new Choice();
        aviType.setSize(84, 32);
        String[] types = AvalancheType.getInstance().getCodes();
        aviType.add(" ");
        aviType.select(" ");
        for (int i=0; i<types.length; i++)
        {
        	aviType.add(types[i]);
        }
        String[] wc = WaterContentSnow.getInstance().getDescriptions();
        
        wcStart = new Choice();
        wcStart.setSize(84, 32);
        for (int i =0; i<wc.length; i++)
        {
        	wcStart.add(wc[i]);
        }
        wcDeposit = new Choice();
        wcDeposit.setSize(84, 32);
        for (int i =0; i<wc.length; i++)
        {
        	wcDeposit.add(wc[i]);
        }
        
        String[] ttypes = TriggerType.getInstance().getDescriptions();
        triggerType = new Choice();
        triggerType.setSize(140, 32);
       // triggerType.add(" ");
       // triggerType.select(" ");
        for (int i=0; i<ttypes.length; i++)
        {
        	triggerType.add(ttypes[i]);
        }
        triggerType.addItemListener(new ChangeTriggerAction());
        sizeWL = new Choice();
        sizeWL.setSize(48, 32);
        sizeSuffixWL = new Choice();
        sizeSuffixWL.setSize(48, 32);
        sizeAbove = new Choice();
        sizeAbove.setSize(48, 32);
        sizeBelow = new Choice();
        sizeBelow.setSize(48, 32);
        sizeSuffixAbove = new Choice();
        sizeSuffixAbove.setSize(48, 32);
        sizeSuffixBelow = new Choice();
        sizeSuffixBelow.setSize(48, 32);
        hardnessAbove = new Choice();
        hardnessAbove.setSize(48, 32);
        hardnessBelow = new Choice();
        hardnessBelow.setSize(48, 32);
        weakLayerHardness = new Choice();
        weakLayerHardness.setSize(48, 32);
        hsuffixWeak = new Choice();
        hsuffixWeak.setSize(48, 32);
        hsuffixAbove = new Choice();
        hsuffixAbove.setSize(48, 32);
        hsuffixBelow = new Choice();
        hsuffixBelow.setSize(48, 32);
        String[] plusmin = new String[3];
		String[] ssuffix = new String[3];
		plusmin[0]=" ";
		plusmin[1]="+";
		plusmin[2]="-";
	
		ssuffix[0]=" ";
		ssuffix[1]="<";
		ssuffix[2]=">";
		String[] sizes = GrainSize.getInstance().getCodes();
		String[] hardness = Hardness.getInstance().getCodes();
		
		for (int i = 0; i<sizes.length; i++)
		{
			sizeWL.add(sizes[i]);
			sizeAbove.add(sizes[i]);
			sizeBelow.add(sizes[i]);
		}
		
		for (int i = 0; i<hardness.length; i++)
		{
			hardnessAbove.add(hardness[i]);
			hardnessBelow.add(hardness[i]);
			weakLayerHardness.add(hardness[i]);
		}
		
		for (int i = 0; i<ssuffix.length; i++)
		{
			sizeSuffixWL.add(ssuffix[i]);
			sizeSuffixAbove.add(ssuffix[i]);
			sizeSuffixBelow.add(ssuffix[i]);
		}
		
		for (int i = 0; i<plusmin.length; i++)
		{
			hsuffixWeak.add(plusmin[i]);
			hsuffixAbove.add(plusmin[i]);
			hsuffixBelow.add(plusmin[i]);
		}
		String[] stats = AvStats.getInstance().getCodes();
		noCaught = new Choice();
		noCaught.setSize(80, 32);
		noFatalities = new Choice();
		noFatalities.setSize(80, 32);
		noInjuries = new Choice();
		noInjuries.setSize(80, 32);
		noPeoplePartBuried = new Choice();
		noPeoplePartBuried.setSize(80, 32);
		noPeopleTotalBuried = new Choice();
		noPeopleTotalBuried.setSize(80, 32);
		for (int i = 0; i<stats.length; i++)
		{
			noCaught.add(stats[i]);
			noFatalities.add(stats[i]);
			noInjuries.add(stats[i]);
			noPeoplePartBuried.add(stats[i]);
			noPeopleTotalBuried.add(stats[i]);
		}
		
		triggerCode = new Choice();
		//triggerCode.setSize(80, 32);
		triggerCode.setSize(200, 32);
		String[] cu = new String[2];
        cu[0] = "c";
        cu[1] = "u";
        causeRelease = new Choice();
        causeRelease.setSize(48, 32);
        causeRelease.add(" ");
        causeRelease.select(" ");
        for (int i = 0; i<cu.length; i++)
        {
        	causeRelease.add(cu[i]);
        }
        
        CDNSize = new Choice();
        CDNSize.setSize(64, 32);
        CDNSize.add(" ");
        CDNSize.select(" ");
        String[] cdsize = AvalancheSizeCA.getInstance().getCodes();
        for (int i=0; i<cdsize.length; i++)
        {
        	CDNSize.add(cdsize[i]);
        }
        
        levelOfBedSurface = new Choice();
        levelOfBedSurface.setSize(200, 32);
        levelOfBedSurface.add(" ");
        levelOfBedSurface.select(" ");
        String[] lbs = BedSurface.getInstance().getDescriptions();
        for (int i=0; i<lbs.length; i++)
        {
        	levelOfBedSurface.add(lbs[i]);
        }
        
        sympathetic = new Choice();
        sympathetic.setSize(160, 32);
        sympathetic.add(" ");
        sympathetic.select(" ");
        sympathetic.add("no");
        sympathetic.add("remote");
        sympathetic.add("sympathetic");
        sympathetic.addItemListener(new ChangeTriggerAction());
        String[] ussize = AvalancheSizeUS.getInstance().getCodes();
        USSize = new Choice();
        USSize.setSize(64, 32);
        USSize.add(" ");
        USSize.select(" ");
        for (int i=0; i<ussize.length; i++)
        {
        	USSize.add(ussize[i]);
        }
        
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
    {   
        public void windowClosing(java.awt.event.WindowEvent event)
        {
            Object object = event.getSource();
            if (object == OccFrame.this) new SaveOccDialog(OccFrame.this).setVisible(true);
            
          /*  if ( object == estDate )
            {
            	System.out.println("estDate WindowClosing.");
            	ts= estDate.getTimestamp().getTime();
            	Date d = new Date(ts);
            	lDate.setText(d.toString());
            	
            }*/
         }
    }
    
    void save()
    {
    	System.out.println("save()");
    	String serial="";
    	if (!edit)
    	{
	    	serial = mf.store.getNewSerial();
	        pit = new avscience.ppc.PitObs(u,serial, mf.bld, mf.version);
	        
	        occ = new avscience.ppc.AvOccurence();
        	occ.setSerial(serial);
    	}
        
        updateOccurenceFromForm();
        Location l = getLocationFromForm();
        pit.setLocation(l);
        pit.setCrownObs(true);
        occ.setPitName(pit.getName());
        if ( webEdit )
        {
        	System.out.println("webEdit");
        	System.out.println("pit serial: "+pit.getSerial());
        	System.out.println("occ serial: "+occ.getSerial());
        	updateWebPit(pit);
        	updateWebOcc(occ);
        	occFrame.dispose();
        	applet.reinit();
        }
        else
        {
	        mf.store.addOcc(occ);
	        mf.store.addPit(pit);
	        mf.rebuildList();
	     }
        dispose();
    }
    
    public Location getLocationFromForm()
    {
        User u = new User();
        try
        {
            u = new User(pit.getUser().toString());
        }
        catch(Exception ex)
        {
            System.out.println(ex.toString());
        }
    	
    	Location l = new Location(u, loc.getText(), state.getSelectedItem(), range.getText(), lat.getText(), lon.getText(), elv.getText(), "");
    
    	return l;
    }
    	
    
    public void exit()
    {
    	this.dispose();
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
    
    public void add(TextItemType item)
    {
        p.add(item.getLabel());
        p.add(item.getField());
    }
    
  /*  void showDatePopup()
    {
    	estDate.setSize(380, 320);
       	estDate.setVisible(true);
    }*/
     public void updateEstDate()
	{
		System.out.println("updateEstDate():PitHeaderFrame");
		Timestamp tts = estDate.getTimestamp();
		long ts = tts.getTime();
		Date dd = new Date(ts);
      	lDate.setText(dd.toString());
      	pit.setTimestamp(ts);
	}
    
    void showDatePopup()
    {
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    	if (pit!=null) timestamp = new Timestamp(pit.getTimestamp());
    	estDate = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", timestamp, 16, this);
    	estDate.setSize(380, 320);
       	estDate.setVisible(true);
    }
    
    public void dispose()
    {
    	clearSubFrames();
    	super.dispose();
    }
    
    class MenuAction implements java.awt.event.ActionListener
    {
        public void actionPerformed(java.awt.event.ActionEvent event)
        {
            Object object = event.getSource();
            if (object == setDate) showDatePopup();
            if ( object == setWeakLayerType ) tcWeak.show();
            if ( object == setTypeAbove ) tcAbove.show();
            if ( object == setTypeBelow ) tcBelow.show();
            if ( object == profileType )  profileCanvas.show();
        	if ( object == saveMenuItem ) save();
        	if ( object == exitMenuItem ) exit();
        	if ( object == crownMenuItem ) editPit();
        	if ( object == deleteMenuItem ) new DeleteOccDialog(OccFrame.this, true).setVisible(true);
        }
    }
}