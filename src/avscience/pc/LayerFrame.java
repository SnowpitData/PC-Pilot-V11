package avscience.pc;
import java.awt.*;
import avscience.desktop.*;
import avscience.wba.*;
import java.util.*;
import java.io.*;
import java.awt.event.*;
import avscience.ppc.User;
public class LayerFrame extends Frame
{
	private int width = 424;
	private int height = 960;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem addMenuItem = new java.awt.MenuItem();
	private MenuItem editMenuItem = new java.awt.MenuItem();
	private MenuItem saveMenuItem = new java.awt.MenuItem();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
	private MenuItem backMenuItem = new java.awt.MenuItem();
	
	MainFrame mframe;
	PitHeaderFrame pframe;
	//int vspace = 30;
	Choice layers;
	DepthTextItem startDepth;
	DepthTextItem endDepth;
	boolean editing;
	boolean criticalLayer=false;
	Choice useMultHardness;
	Choice useMultType;
	Choice useMultSize;
	Choice useMultDensity;
	Button add;
	boolean multHardness;
	boolean multType;
	boolean multSize;
	boolean multDensity;
	
	Choice hard1;
	Choice hard2;
	Choice hsuffix1;
	Choice hsuffix2;
	Choice size1;
	Choice size2;
	Choice sunits1;
	Choice sunits2;
	Choice sizeSuffix1;
	Choice sizeSuffix2;
	
	CheckboxGroup iLayer;
	Checkbox iStart;
	Checkbox iEnd;
	Checkbox iNone;
	
	TypeDisplay type1;
	TypeDisplay type2;
	Choice waterContent;
	RhoTextItem rho1;
	RhoTextItem rho2;
	Button selectGT1;
	Button selectGT2;
	Checkbox weakInt;
	TextItem notes;
	public Vector subFrames = new Vector();
	Label msg;// = new Label();
	avscience.ppc.User user;
	Label wil;
	
	int currentLayerNum;
	void initControls()
	{
                msg = new Label();
		msg.setSize(320, 20);
		String[] yesno = new String[2];
		String[] plusmin = new String[3];
		String[] sunits = new String[2];
		String[] ssuffix = new String[3];
		yesno[0]= "no";
		yesno[1]= "yes";
		plusmin[0]=" ";
		plusmin[1]="-";
		plusmin[2]="+";
		sunits[0]="mm";
		sunits[1]="cm";
		ssuffix[0]=" ";
		ssuffix[1]="<";
		ssuffix[2]=">";
		layers = new Choice();
		useMultHardness = new Choice();
		useMultHardness.setSize(72, 32);
	    for ( int i=0; i<yesno.length; i++)
	    {
	    	useMultHardness.add(yesno[i]);
	    }
	    String[] hardness = Hardness.getInstance().getCodes();
	    String[] sizes = GrainSize.getInstance().getCodes();
	    hard1 = new Choice();
	    hard1.setSize(72, 32);
	    for ( int i=0; i<hardness.length; i++ )
	    {
	    	hard1.add(hardness[i]);
	    }
	    hsuffix1 = new Choice();
	    hsuffix1.setSize(72, 32);
	    for ( int i=0; i<plusmin.length; i++ )
	    {
	    	hsuffix1.add(plusmin[i]);
	    }
	    hard2 = new Choice();
	    hard2.setSize(72, 32);
	    for ( int i=0; i<hardness.length; i++ )
	    {
	    	hard2.add(hardness[i]);
	    }
	    useMultType = new Choice();
	    useMultType.setSize(72, 32);
	    for ( int i=0; i<yesno.length; i++ )
	    {
	    	useMultType.add(yesno[i]);
	    }
	    hsuffix2 = new Choice();
	    hsuffix2.setSize(72, 32);
		for ( int i=0; i<plusmin.length; i++ )
		{
			hsuffix2.add(plusmin[i]);
		}
		useMultSize = new Choice();
		useMultSize.setSize(72, 32);
	 	for ( int i=0; i<yesno.length; i++ )
	    {
	    	useMultSize.add(yesno[i]);
	    }
	    sunits1 = new Choice();
	    sunits1.setSize(72, 32);
	 	for ( int i=0; i<sunits.length; i++ )
	 	{
	 		sunits1.add(sunits[i]);
	 	}
	 	
	 	size1 = new Choice();
	 	size1.setSize(72, 32);
	 	for ( int i=0; i<sizes.length; i++ )
	 	{
	 		size1.add(sizes[i]);
	 	}
	 	sizeSuffix1 = new Choice();
	 	sizeSuffix1.setSize(72, 32);
	 	for ( int i=0; i<ssuffix.length; i++ )
	 	{
	 		sizeSuffix1.add(ssuffix[i]);
	 	}
	 	
 		sunits2 = new Choice();
 		sunits2.setSize(72, 32);
	 	for ( int i=0; i<sunits.length; i++ )
	 	{
	 		sunits2.add(sunits[i]);
	 	}
	 	
	 	size2 = new Choice();
	 	size2.setSize(72, 32);
	 	for ( int i=0; i<sizes.length; i++ )
	 	{
	 		size2.add(sizes[i]);
	 	}
	 	sizeSuffix2 = new Choice();
	 	sizeSuffix2.setSize(72, 32);
	 	for ( int i=0; i<ssuffix.length; i++ )
	 	{
	 		sizeSuffix2.add(ssuffix[i]);
	 	}
	 	useMultDensity = new Choice();
	 	useMultDensity.setSize(72, 32);
	 	for ( int i=0; i<yesno.length; i++ )
	 	{
	 		useMultDensity.add(yesno[i]);
	 	}
	 	waterContent = new Choice();
	 	waterContent.setSize(124, 32);
	 	String[] wc = WaterContent.getInstance().getCodes();
	 	for ( int i=0; i<wc.length; i++ )
	 	{
	 		waterContent.add(wc[i]);
	 	}
	 	
	 	String start="Bottom";
        String end="Top";
        
        if (user.getMeasureFrom().equals("top")) 
        {
        	start="Top";
        	end = "Bottom";
        }
        else
        {
        	start="Bottom";
        	end = "Top";
        }
        
	 	type1 = new TypeDisplay();
		type2 = new TypeDisplay();
		rho1 = new RhoTextItem("Density "+user.getRhoUnits()+"  ",user.getRhoUnits());
		rho2 = new RhoTextItem("Density End "+user.getRhoUnits(), user.getRhoUnits());
		notes=new TextItem("Comments(20 char max)", 1, 1, 20);
		startDepth = new DepthTextItem(start+" Depth "+user.getDepthUnits(), 0, 0);
	    endDepth = new DepthTextItem(end+" Depth "+user.getDepthUnits(), 0, 0);
	    selectGT1 = new Button("Select Grain Type");
	    selectGT2 = new Button("Select Grain Type");
	    weakInt = new Checkbox("This layer is my greatest concern in the entire pit");
	    selectGT1.addActionListener(new MenuAction());
	    selectGT2.addActionListener(new MenuAction());
	    editMenuItem.addActionListener(new MenuAction());
	    useMultHardness.addItemListener(new MultChangeAction());
	    useMultType.addItemListener(new MultChangeAction());
	    useMultSize.addItemListener(new MultChangeAction());
	    useMultDensity.addItemListener(new MultChangeAction());
	    layers.addItemListener(new LayerListener());
	    weakInt.addItemListener(new CheckListener());
	    ////
	    useMultHardness.addMouseListener(new ClearFramesListener());
	    useMultSize.addMouseListener(new ClearFramesListener());
	    useMultDensity.addMouseListener(new  ClearFramesListener());
	    size1.addMouseListener(new  ClearFramesListener());
	    size2.addMouseListener(new  ClearFramesListener());
	    sunits1.addMouseListener(new ClearFramesListener());
	    sunits2.addMouseListener(new  ClearFramesListener());
	    sizeSuffix1.addMouseListener(new ClearFramesListener());
	    sizeSuffix2.addMouseListener(new ClearFramesListener());
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
	
	class LayerListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		if ( e.getItemSelectable()==layers )
    		{
    			clearSubFrames();
    			editLayer();
    		}
    	}
    }
    
    class CheckListener implements java.awt.event.ItemListener
    {
    	public void itemStateChanged(java.awt.event.ItemEvent e)
    	{
    		Object o = e.getSource();
    		if (o==weakInt)
    		{
    			if (weakInt.getState())
    			{
    				wil.setVisible(true);
    				iStart.setVisible(true);
    				iEnd.setVisible(true);
    				iNone.setVisible(true);
    			}
    			else
    			{
    				wil.setVisible(false);
    				iStart.setVisible(false);
    				iEnd.setVisible(false);
    				iNone.setVisible(false);
    			}
    		}
    	}
    }
	
	void clearForm()
	{
		System.out.println("clearForm");
		startDepth.setText("");
		endDepth.setText("");
		hard1.select(" ");
		hard2.select(" ");
		hsuffix1.select(" ");
		hsuffix2.select(" ");
		size1.select(" ");
		size2.select(" ");
		sizeSuffix1.select(" ");
		sizeSuffix2.select(" ");
		sunits1.select("mm");
		sunits2.select("mm");
		type1.setType("");
		type2.setType("");
		rho1.setText("");
		rho2.setText("");
		waterContent.select(" ");
		notes.setText("");
		multHardness=false;
		multType=false;
		multSize=false;
		multDensity=false;
		useMultHardness.select("no");
		useMultType.select("no");
		useMultDensity.select("no");
		useMultSize.select("no");
		weakInt.setState(false);
	}
	
	public void dispose()
	{
		if ( editing ) saveLayer();
		else addLayer();
		pframe.saveWO();
		clearSubFrames();
		super.dispose();
		
	}
	
	public void close()
	{
		clearSubFrames();
		if (!(pframe.getPit().hasProblemInterface())) mframe.showILayerFrame(pframe);
		pframe.repaint();
		dispose();
	}
	
	void buildForm(boolean edit)
	{
		System.out.println("build form.");
		removeAll();
		buildMenu(edit);
		int ys=76;
    	int x=24;
    	int y=ys;
    	int vspace=24;
        if (mframe.macos) vspace = 26;
    	boolean fromTop = true;
    	if (user.getMeasureFrom().equals("top")) fromTop = true;
    	else fromTop=false;
    	String tp="top";
    	String bt = "bottom";
    	if (!edit)
    	{
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
	    }
	    
	    if (fromTop) 
	    {
	    	startDepth.setLocation(x, y);
	    	add(startDepth);
	    	y+=vspace;
	    	endDepth.setLocation(x, y);
	    	add(endDepth);
	    	y+=vspace;
	    }
	    else
        {
	    	//////////
	    //	fromTop=false;
	    	endDepth.setLocation(x, y);
	    	add(endDepth);
	    	y+=vspace;
	    	startDepth.setLocation(x, y);
	    	add(startDepth);
	    	y+=vspace;
	    }
	    
	    Label mh = new Label("Use Multiple Hardness");
	    mh.setLocation(x, y);
	    mh.setSize(110, 20);
	    add(mh);
	    
	    useMultHardness.setLocation(x+150, y);
	    add(useMultHardness);
	    y+=vspace;
	    
	    if (!multHardness)
	    {
	    	Label hl1 = new Label("Hardness");
	    	hl1.setSize(100, 20);
	    	hl1.setLocation(x, y);
	    	add(hl1);
	    	hard1.setLocation(x+114, y);
	    	add(hard1);
	    
	    	Label hs1 = new Label("+-");
	    	hs1.setLocation(x+190, y);
	    	hs1.setSize(36, 20);
	    	add(hs1);
	    	hsuffix1.setLocation(x+228, y);
	    	add(hsuffix1);
	    	y+=vspace;
	    }
	    
	    else 
	    {
    		
			Label hl2 = new Label("Hardness "+tp);
		    hl2.setSize(100, 20);
		    hl2.setLocation(x, y);
		    add(hl2);
		    
		    hard2.setLocation(x+114, y);
		    add(hard2);
		   // y+=vspace;
		    Label hs2 = new Label("+-");
		    hs2.setLocation(x+190, y);
		    hs2.setSize(36, 20);
		    add(hs2);
		    hsuffix2.setLocation(x+228, y);
		    add(hsuffix2);
		    y+=vspace;
		    Label hl1 = new Label("Hardness "+bt);
	    	hl1.setSize(110, 20);
	    	hl1.setLocation(x, y);
	    	add(hl1);
	    	hard1.setLocation(x+114, y);
	    	add(hard1);
	    //	y+=vspace;
	    	Label hs1 = new Label("+-");
	    	hs1.setLocation(x+190, y);
	    	hs1.setSize(36, 20);
	    	add(hs1);
	    	hsuffix1.setLocation(x+228, y);
	    	add(hsuffix1);
	   
		    y+=vspace;
	    }
	    
	    Label mgt = new Label("Use Multiple Grain Type");
	    mgt.setSize(160, 20);
	    mgt.setLocation(x, y);
	    add(mgt);
	    
	    useMultType.setLocation(x+162, y);
	    add(useMultType);
	    y+=vspace;
	    
	    if (!multType)
	    {
	    	Label gt1 = new Label("Grain Type");
	    	gt1.setSize(100, 20);
	    	gt1.setLocation(x, y);
	    	add(gt1);
	    	type1.setLocation(x+158, y+10);
	    	add(type1);
	    	y+=vspace;
	    
	    	selectGT1.setSize(120, 20);
	    	selectGT1.setLocation(x, y);
	    	add(selectGT1);
	    	y+=vspace;
	    }
	    else
	    {
	    	Label gt1 = new Label("Primary Grain Type");
	    	gt1.setSize(144, 20);
	    	gt1.setLocation(x, y);
	    	add(gt1);
	    	type1.setLocation(x+158, y+10);
	    	add(type1);
	    	y+=vspace;
	    
	    	selectGT1.setSize(120, 20);
	    	selectGT1.setLocation(x, y);
	    	add(selectGT1);
	    	y+=vspace;
	    	//gt1.setText("Grain Type Start");
	    	Label gt2 = new Label("Secondary Grain Type");
		    gt2.setSize(144, 20);
		    gt2.setLocation(x, y);
		    add(gt2);
		    //y+=vspace;
		    type2.setLocation(x+158, y+8);
		    add(type2);
		    y+=vspace;
		    
		    selectGT2.setSize(120, 20);
		    selectGT2.setLocation(x, y);
		    add(selectGT2);
	    
	    	y+=vspace;
	    }
	    Label mgs = new Label("Use multiple grain size");
	    mgs.setSize(160, 20);
	    mgs.setLocation(x, y);
	    add(mgs);
	 	
	 	useMultSize.setLocation(x+162,y);
	 	add(useMultSize);
	 	y+=vspace;
	 	
	 	if (!multSize)
	 	{
	 		Label gs1 = new Label("Grain Size (mm)");
		 	gs1.setSize(100, 20);
		 	gs1.setLocation(x, y);
		 	add(gs1);
		 	
		 	size1.setLocation(x+110, y);
		 	add(size1);
		 	//Label ss1 = new Label("<>");
		 	//ss1.setSize(20, 20);
		 	//ss1.setLocation(x+168, y);
		 	//add(ss1);
		 	
		 	sizeSuffix1.setLocation(x+190, y);
		 	add(sizeSuffix1);
		 	y+=vspace;
	 	}
	 	else
	 	{
	 		Label gs1 = new Label("Grain Size 1 (mm)");
		 	gs1.setSize(128, 20);
		 	gs1.setLocation(x, y);
		 	add(gs1);
		 	
		 	size1.setLocation(x+130, y);
		 	add(size1);
		 	//Label ss1 = new Label("<>");
		 	//ss1.setSize(20, 20);
		 	//ss1.setLocation(x+188, y);
		 	//add(ss1);
		 	
		 	sizeSuffix1.setLocation(x+210, y);
		 	add(sizeSuffix1);
		 	y+=vspace;
		 	
		 	Label gs2 = new Label("Grain Size 2 (mm)");
		 	gs2.setSize(128, 20);
		 	gs2.setLocation(x, y);
		 	add(gs2);
		 	
		 	size2.setLocation(x+130, y);
		 	add(size2);
		 	
		 	//Label ss2 = new Label("<>");
		 	//ss2.setSize(20, 20);
		 	//ss2.setLocation(x+188, y);
		 	//add(ss2);
		 	sizeSuffix2.setLocation(x+210, y);
		 	add(sizeSuffix2);
		 	y+=vspace;
	 	}
	 	Label mr = new Label("Use multiple density");
	 	mr.setSize(130, 20);
	 	mr.setLocation(x, y);
	 	add(mr);
	 	
	 	useMultDensity.setLocation(x+150, y);
	 	add(useMultDensity);
	 	y+=1.4*vspace;
	 	
	 	if (!multDensity)
	 	{
	 		rho1.setLocation(x, y);
	 		add(rho1);
	 		y+=vspace;
	 	}
	 	else
	 	{
	 		if (fromTop)
	 		{
	 			rho2.setLocation(x, y);
	 			rho2.label.setText("Density Top: "+user.getRhoUnits());
		 		add(rho2);
		 		y+=vspace;
		 		rho1.label.setText("Density Bottom: "+user.getRhoUnits());
		 		rho1.setLocation(x, y);
		 		add(rho1);
		 		y+=vspace;
	 		}
	 		else
	 		{
	 			rho1.label.setText("Density Top: "+user.getRhoUnits());
	 			rho1.setLocation(x, y);
		 		add(rho1);
		 		y+=vspace;
		 		rho2.label.setText("Density Bottom: "+user.getRhoUnits());
		 		rho2.setLocation(x, y);
		 		add(rho2);
		 		y+=vspace;
	 		}
	 	}
	 	
	 	Label wl = new Label("Water Content");
	 	wl.setSize(90, 20);
	 	wl.setLocation(x, y);
	 	add(wl);
	 	waterContent.setLocation(x+98, y);
	 	add(waterContent);
	 	y+=vspace;
	 	y+=4;
                notes=new TextItem("Comments(20 char max)", 1, 1, 20);
	 	notes.setLocation(x, y);
	 	notes.setMaxLength(20);
	 	add(notes);
	 	
		y+=vspace;
	//	Label iLabel = new Label("Problematic interface?");
		weakInt.setLocation(x, y);
		weakInt.setVisible(true);
		weakInt.setSize(width, 20);
		add(weakInt);
		y+=vspace;
		wil = new Label("What part of the layer?");
		wil.setLocation(x, y);
		wil.setVisible(false);
		wil.setSize(width-20, 20);
		add(wil);
		y+=vspace;
		iLayer = new CheckboxGroup();
		if (user.getMeasureFrom().equals("bottom")) 
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
		iStart.setVisible(false);
		
		iEnd.setLocation(x+82, y);
		iEnd.setSize(80, 20);
		iEnd.setVisible(false);
		
		iNone.setLocation(x+2*82, y);
		iNone.setSize(128, 20);
		iNone.setVisible(false);
		
		add(iStart);
		add(iEnd);
		add(iNone);
	 	
	 	
    	
    //	y+=vspace;
	 	if (!edit)
	 	{
		 
                        y+=vspace;
		 	add = new Button("Add Layer");
		 	add.setLocation(x, y);
		 	add.setSize(72, 22);
		 	add(add);
		 	add.addActionListener(new MenuAction());
		}
		y+=vspace;
	 	Label u = new Label("User: "+user.getName());
                
        msg.setLocation(x, y);
    	msg.setVisible(true);
    	add(msg);
        y+=vspace;
    	u.setLocation(x, y);
    	u.setSize(126, 20);
    	add(u);
        
	}
	
	public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
    class ClearFramesListener implements java.awt.event.MouseListener
    {
    	public void mouseClicked(MouseEvent e)
    	{
    		clearSubFrames();
    	}
    	public void mouseEntered(MouseEvent e)
    	{
    		
    	}
    	
    	public void mouseExited(MouseEvent e)
    	{
    		
    	}
    	public void mousePressed(MouseEvent e)
    	{
    		
    	}
    	public void mouseReleased(MouseEvent e)
    	{
    		
    	}
    }
    
    class MultChangeAction implements java.awt.event.ItemListener
	{
		public void itemStateChanged(java.awt.event.ItemEvent event)
		{
			///clearSubFrames();
			Object object = event.getSource();
			if (( object == useMultHardness )||( object == useMultType )||( object == useMultSize )||( object == useMultDensity )) changeLayerForm();
			
		}
	}
	
	void displayMsg(String message)
	{
		//msg.setSize(260, 20);
		msg.setFont(new Font(null, Font.BOLD, 12));
		msg.setText(message);
		msg.setVisible(true);
		//try
		//{
		//	Thread.sleep(2200);
		//}
		//catch(Exception e){System.out.println(e.toString());}
		//msg.setVisible(false);
	}
	
	public LayerFrame(MainFrame mframe, PitHeaderFrame pframe)
	{
		super("Snow Pilot - Layers");
	    this.mframe = mframe;
	    this.pframe=pframe;
		user = pframe.getPit().getUser();
		setLayout(null);
		if (mframe.smallscreen) height=600;
		if (mframe.macos) height+=140;
		this.setSize(width, height);
		this.setLocation(160, 0);
		this.setVisible(true);
	
		this.setMaximizedBounds(new Rectangle(width, height));
		
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		
		addMenuItem.setLabel("Add this layer");
        editMenuItem.setLabel("Edit layer");
        saveMenuItem.setLabel("Save layer");
        deleteMenuItem.setLabel("Delete layer");
        backMenuItem.setLabel("back");
    	menu.setLabel("Select..");
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
        addMenuItem.addActionListener(new MenuAction());
        editMenuItem.addActionListener(new MenuAction());
        saveMenuItem.addActionListener(new MenuAction());
        deleteMenuItem.addActionListener(new MenuAction());
        backMenuItem.addActionListener(new MenuAction());
        
        initControls();
        layers.addItemListener(new LayerListener());
		buildForm(false);
		/////
	
	}
	
	void addLayer()
	{
		System.out.println("Add layer.");
		String stDepth = startDepth.getText().trim();
        String edDepth = endDepth.getText().trim();
        if (( stDepth.length()>0 ) && ( edDepth.length()>0))
        {
        	if (validateForm())
        	{
        		avscience.ppc.Layer l = getLayerFromForm(false);
				pframe.getPit().addLayer(l);
		        displayMsg("Layer added.");
		        if (pframe!=null) 
		        {
		        	pframe.saveWO();
		        	pframe.repaint();
		        	//if (pframe.pframe!=null) 
		        //	{
		        //		pframe.pframe.repaint();
		        	//	pframe.pframe.reinit();
		       /// 	}
		        }
		        multHardness=false;
				multType=false;
				multSize=false;
				multDensity=false;
				useMultHardness.select("no");
				useMultType.select("no");
				useMultDensity.select("no");
				useMultSize.select("no");
		        buildForm(false);
		        weakInt.setState(false);
		        clearForm();
		        if ( user.getMeasureFrom().equals("top"))
		        {
		        	startDepth.setText(l.getEndDepthString());
		        	endDepth.setFocus();
		        }
		        else 
		        {
		        	endDepth.setText(l.getStartDepthString());
		        	startDepth.setFocus();
		        }
		        
        	}
	    }
	    
	}
	
	
	avscience.ppc.Layer getLayerFromForm(boolean edit)
    {
    	System.out.println("getLayerFromForm()");
        String stDepth = startDepth.getText().trim();
        String edDepth = endDepth.getText().trim();
        System.out.println("start depth: "+stDepth);
        String h1 =  hard1.getSelectedItem();
        String h2 =  hard2.getSelectedItem();
        String hs1 = hsuffix1.getSelectedItem();
        String hs2 =  hsuffix2.getSelectedItem();
      
        String gt1;
        String gt2;
        
        gt1 = type1.getType();
        gt2 = type2.getType();
        
        String gs1 = size1.getSelectedItem();
        String gs2 = size2.getSelectedItem();
        String ssuf1 = sizeSuffix1.getSelectedItem();
        String ssuf2 = sizeSuffix2.getSelectedItem();
        String units1 = sunits1.getSelectedItem();
        String units2 = sunits2.getSelectedItem();
        String r1 = rho1.getText();
        String r2 = rho2.getText();
        System.out.println("rho1: "+r1);
        System.out.println("rho2: "+r2);
        String wc = waterContent.getSelectedItem();
        String comments = notes.getText();
        int layerNumber=0;
        if ( edit ) layerNumber=currentLayerNum;
        else layerNumber = pframe.getPit().getCurrentLayerNumber();
        //System.out.println("Layer Number: "+layerNumber);
        String ln = new Integer(layerNumber).toString();
        
        if ( weakInt.getState())
        {
        	pframe.getPit().iLayerNumber = ln;
        	criticalLayer=true;
        }
        else 
        {
        	if ( editing && criticalLayer )
        	{
        		pframe.getPit().iLayerNumber="";
        	}
        }
        
        if ( iStart.getState() )
        {
        	pframe.getPit().iLayerNumber = ln;
        	pframe.getPit().iDepth = stDepth;
        	System.out.println("weak interface at: "+ln+" depth: "+stDepth);
        //	criticalLayer=true;
        }
        
        if ( iEnd.getState() )
        {
        	pframe.getPit().iLayerNumber = ln;
        	pframe.getPit().iDepth = edDepth;
        	System.out.println("weak interface at: "+ln+" depth: "+edDepth);
        	//criticalLayer=true;
        }
        
        if (( iNone.getState() ) && ( weakInt.getState()))
        {
        	pframe.getPit().iDepth = "";
        }
        if ( editing && criticalLayer )
        {
        //	if (!iStart.getState() && !iEnd.getState() )
        //	{
        	//	pframe.getPit().iDepth = "";
        		if (!weakInt.getState()) 
        		{
        			pframe.getPit().iLayerNumber = "";
        			criticalLayer=false;
        		}
        //	}
        }
        if ( weakInt.getState()) criticalLayer=true;
        else criticalLayer=false;
        boolean fromTop=false;
        if ( user.getMeasureFrom().equals("top")) fromTop=true;
        else fromTop=false;
        avscience.ppc.Layer ll = new avscience.ppc.Layer(stDepth, edDepth, h1, hs1, h2, hs2, gt1, gt2, units1, units2, gs1, ssuf1, gs2, ssuf2, wc, r1, r2, comments, ln, fromTop);
        
        return ll;
        
    }
    
    boolean validateForm()
    {
    	boolean valid = true;
    	
    	double st = new Double(startDepth.getText()).doubleValue();
    	double ed = new Double(endDepth.getText()).doubleValue();
    	System.out.println("Validate form");
        System.out.println("Start depth "+st);
        System.out.println("end depth "+ed);
    	if ( ed < st)
    	{
    		valid=false;
                System.out.println("Valid ? "+valid);
    		displayMsg("Top depth cannot be lower than bottom depth!");
    	}
    	
    	String gt1 = type1.getType();
    	if ( gt1==null ) gt1="";
    	String gs1 =size1.getSelectedItem();
    	if ( gs1==null ) gs1="";
    	
    	String gt2 = type2.getType();
    	if ( gt2==null ) gt2="";
    	String gs2 =size2.getSelectedItem();
    	if ( gs2==null ) gs2="";
    	
    	if ( gs1.trim().length()>1 )
    	{
    		if ( gt1.trim().length()<1)
    		{
    			valid = false;
    			displayMsg("Please select a grain type 1!");
    		}
    	}
    	
    /*	if ( gs2.trim().length()>1 )
    	{
    		if ( gt2.trim().length()<1)
    		{
    			valid = false;
    			displayMsg("Please select a grain type 2!");
    		}
    	}*/
    	
    	return valid;
    }
	
	void changeLayerForm()
	{
		Component c = getFocusOwner();
		String yn = useMultHardness.getSelectedItem();
		if ( yn.equals("yes"))	multHardness = true;
		else
		{
			multHardness = false;
			hard2.select(" ");
			hsuffix2.select(" ");
		}
		
		yn = useMultType.getSelectedItem();
		if ( yn.equals("yes")) 
		{
			multType = true;
		//	multSize = true;
		//	useMultSize.select("yes");
		}
		else multType = false;
		
		yn = useMultSize.getSelectedItem();
		if ( yn.equals("yes"))
		{
			multSize = true;
		//	multType = true;
		}
		else multSize = false;
		
		yn = useMultDensity.getSelectedItem();
		if ( yn.equals("yes")) multDensity = true;
		else multDensity = false;
		if (!multDensity) 
		{
			rho1.label.setText("Density "+user.getRhoUnits()+" ");
			rho2.setText("");
		}
		if (!multHardness) 
		{
		//	hard2.select(0);
		//	hsuffix2.select(0);
		}
		if (!multType)
		{
			type2.setType("");
		}
		if (!multSize)
		{
		//	size2.select(0);
		//	sizeSuffix2.select(0);
		//	sunits2.select("mm");
		}
		buildForm(editing);
		
		c.requestFocus();;
		
	}
	
	void editLayer()
	{
		System.out.println("editLayer()");
		editing=true;
		String ln = layers.getSelectedItem().trim();
		if ( ln.length()>1)
		{
				avscience.ppc.Layer l = pframe.getPit().getLayerByString(ln);
			 	if (l==null)System.out.println("layer null.");
			 	
				if ( l!=null )
				{
					String lNum = l.getLayerNumber()+"";
					if ( lNum.equals(pframe.getPit().iLayerNumber)) criticalLayer=true;
					else criticalLayer=false;
					System.out.println("editing layer: "+l.toUIString());
					clearForm();
					//to do need to call first to set mult params fix with new method.
					popForm(l);
					buildForm(true);
					popForm(l);
				}
			
		}
	
	}
	
	
	void deleteLayer()
	{
		String ln = layers.getSelectedItem().trim();
		if ( ln.length()>1)
		{
			avscience.ppc.Layer l = pframe.getPit().getLayerByString(ln);
			pframe.getPit().removeLayer(l.getLString());
			displayMsg("Layer: "+l.toUIString()+" deleted.");
		}	
		clearForm();
		buildForm(false);
	}
	
	
	void popForm(avscience.ppc.Layer l)
	{
		if ( l==null ) return;
		System.out.println("pop form");
		currentLayerNum=l.getLayerNumber();
		System.out.println("start depth: "+l.getStartDepthString());
		startDepth.setText(l.getStartDepthString());
        endDepth.setText(l.getEndDepthString());
        System.out.println("depth poppped");
        hard1.select(l.getHardness1());
        hard2.select(l.getHardness2());
        System.out.println("hard popped");
        hsuffix1.select(l.getHSuffix1());
        hsuffix2.select(l.getHSuffix2());
        System.out.println("suff popped");
        type1.setType(l.getGrainType1());
        type2.setType(l.getGrainType2());
        System.out.println("type popped");
        size1.select(l.getGrainSize1());
        size2.select(l.getGrainSize2());
        System.out.println("size popped");
        sunits1.select(l.getGrainUnits1());
        sunits2.select(l.getGrainUnits2());
        System.out.println("units popped");
        sizeSuffix1.select(l.getGrainSuffix());
        sizeSuffix2.select(l.getGrainSuffix1());
        System.out.println("suff popped");
        rho1.setText(l.getDensity1());
        rho2.setText(l.getDensity2());
        System.out.println("rho popped");
        waterContent.select(l.getWaterContent());
        notes.setText(l.getComments());
        
        if ( l.getMultipleGrainType().equals("true")) 
        {
        	multType=true;
        	useMultType.select("yes");
        }
        else 
        {
        	multType=false;
        	useMultType.select("no");
        }
        
        if ( l.getMultipleGrainSize().equals("true")) 
        {
        	multSize=true;
        	useMultSize.select("yes");
        }
        else 
        {
        	multSize=false;
        	useMultSize.select("no");
        }
        
        if ( l.getMultipleDensity().equals("true"))
        {
        	multDensity=true;
        	useMultDensity.select("yes");
        }
        else 
        {
        	multDensity=false;
        	useMultDensity.select("no");
        }
        
        if ( l.getMultipleHardness().equals("true")) 
        {
        	multHardness=true;
        	useMultHardness.select("yes");
        }
        else 
        {
        	multHardness=false;
        	useMultHardness.select("no");
        }
        String lNum = l.getLayerNumber()+"";
       
        System.out.println("popForm");
        System.out.println("Layer number: "+lNum);
        System.out.println("I-Layer number: "+pframe.getPit().iLayerNumber);
        if ( lNum.equals(pframe.getPit().iLayerNumber))
        {
        	System.out.println("WEAK INTERFACE:");
        	System.out.println("I depth: "+pframe.getPit().iDepth);
        	System.out.println("Start depth: "+l.getStartDepthString());
        	weakInt.setState(true);
        	if ( l.getStartDepthString().trim().equals(pframe.getPit().iDepth.trim()))
        	{
        		System.out.println("weak interface start: ");
        		iStart.setState(true);
        		iEnd.setState(false);
        		iNone.setState(false);
        	}
        	else if ( (l.getEndDepthString()).equals(pframe.getPit().iDepth))
        	{
        		iStart.setState(false);
        		iEnd.setState(true);
        		iNone.setState(false);
        	}
        	else
        	{
        		iStart.setState(false);
        		iEnd.setState(false);
        		iNone.setState(true);
        	}
        }
        else weakInt.setState(false);
        if (pframe.getPit().layerIsCritical(l)) criticalLayer=true; 
	}
	

	
	void buildMenu(boolean edit)
    {
    	menu.removeAll();
    	if (!edit)
    	{
        	menu.add(addMenuItem);
        	menu.add(editMenuItem);
        }
        else
        {
        	menu.add(saveMenuItem);
        	menu.add(deleteMenuItem);
        	menu.add(backMenuItem);
        }
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == LayerFrame.this )
			{
				if ( editing ) saveLayer();
				else addLayer();
				LayerFrame.this.close();
			}
		}
	}
	
	void saveLayer()
	{
		System.out.println("Add layer.");
		String stDepth = startDepth.getText().trim();
        String edDepth = endDepth.getText().trim();
		//if (!((edDepth.equals("0"))&&(stDepth.equals("0"))))
		//{
		if (( stDepth.length()>0 ) && ( edDepth.length()>0))
		{
			if ( validateForm())
			{
				avscience.ppc.Layer l = getLayerFromForm(true);
	    		pframe.getPit().updateCurrentEditLayer(l);
	    		clearForm();
	    		buildForm(false);
	    		editing=false;
			}
    	}
    	else displayMsg("Enter a start and end depth.");
	//	}
		
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
		//	clearSubFrames();
            if ((object == addMenuItem ) || ( object == add))addLayer();
            if ( object == editMenuItem )editLayer();
            if ( object == deleteMenuItem )deleteLayer(); 
            if ( object == saveMenuItem ) saveLayer();
           
            if ( object == backMenuItem )
            {
            	clearForm();
            	buildForm(false);
            	editing=false;
            }
            if ( object == selectGT1 )
            {
            	if (type1.getType()==null) type1.setType("");
            	GrainTypeSelectionFrame gts1 = new GrainTypeSelectionFrame(type1);
            	gts1.setLocation(60, 140);
            	subFrames.add(gts1);
            ///	if (!multType) subFrames.add(new TypeCanvas(type1));
            //	else subFrames.add(new TypeCanvas(type1, true)); 
            }
            if ( object == selectGT2 )
            {
            	if ( type2.getType()==null ) type2.setType("");
            	GrainTypeSelectionFrame gts2 = new GrainTypeSelectionFrame(type2);
            	gts2.setLocation(400, 140);
            	subFrames.add(gts2);
            //	subFrames.add(new TypeCanvas(type2, false));
            }
            
		}
	}
}	
	