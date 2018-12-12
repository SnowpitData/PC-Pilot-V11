package avscience.pc;

import java.awt.*;
import java.util.*;
import avscience.desktop.*;
import java.util.Date;
import avscience.ppc.*;
import avscience.wba.DensityProfile;
import avscience.wba.TempProfile;
import avscience.wba.ShearTests;
import avscience.wba.Precipitation;
import avscience.wba.*;
import java.net.*;
import java.io.*;
import java.awt.event.*;

public class PitSearchFrame extends Frame implements TimeFrame
{
	private static final String server="http://www.kahrlconsulting.com:8087/avscience/PitListServlet";
    org.compiere.grid.ed.Calendar estDate1;
    org.compiere.grid.ed.Calendar estDate2;
    java.awt.List locations = new java.awt.List();
    java.awt.List states = new java.awt.List();
    java.awt.List range = new java.awt.List();
    java.awt.List precip = new java.awt.List();
    java.awt.List sky = new java.awt.List();
    java.awt.List wind = new java.awt.List();
    java.awt.List winDir = new java.awt.List();
    java.awt.List winLoad = new java.awt.List();
    java.awt.List stability = new java.awt.List();
    java.awt.List activities = new java.awt.List();
    java.awt.List pits = new java.awt.List();
    private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private Menu fileMenu = new java.awt.Menu();
	private MenuItem layerMenuItem = new java.awt.MenuItem();
    private MenuItem testMenuItem = new java.awt.MenuItem();
    private MenuItem saveSelectedMenuItem = new java.awt.MenuItem();
    private MenuItem saveAllMenuItem = new java.awt.MenuItem();
    private MenuItem saveInPLMenuItem = new java.awt.MenuItem();
    Label time1 = new Label("");
    Label time2 = new Label("");
    Button setTime1 = new Button("Set Time");
    Button setTime2 = new Button("Set Time");
    Button getPits = new Button("Get Pits");
    Checkbox t1before;
    Checkbox t2before;
    Checkbox t1after;
    Checkbox t2after;
    CheckboxGroup tg1 = new CheckboxGroup();
    CheckboxGroup tg2 = new CheckboxGroup();
    ElvTextItemOp elv1;
    ElvTextItemOp elv2;
    Checkbox hasPL;
   // Choice elops1 = new Choice();
  ///  Choice elops2 = new Choice();
    
    int startx = 20;
    int starty = 60;
    int yspace=26;
    int colSpace = 320;
    int col3Space = 224;
    Choice ns1 = new Choice();
    Choice ew1 = new Choice();
    Choice ns2 = new Choice();
    Choice ew2 = new Choice();
    LatTextItem lat1;
    LatTextItem lat2;
    LonTextItem lon1;
    LonTextItem lon2;
    DegTextItemOp aspect1;
    DegTextItemOp aspect2;
    DegTextItemOp incline1;
    DegTextItemOp incline2;
   // Choice aops1 = new Choice();
  //  Choice aops2 = new Choice();
    TempTextItemOP temp1;
    TempTextItemOP temp2;
  //  Choice tops1 = new Choice();
//    Choice tops2 = new Choice();
    Choice operator = new Choice();
    TextArea query = new TextArea();
    Button exQry = new Button("Execute Query");
    Label noPits = new Label(" ");
   // String [][] pitlist;
    Hashtable pitSers = new Hashtable();
    long ts1;
    long ts2;
    Hashtable currentPits = new Hashtable();
    public MainFrame mf;
    TestSearchFrame testSearchFrame;
    LayerSearchFrame layerSearchFrame;
    Vector subFrames = new Vector();
    
    public PitSearchFrame(MainFrame mf)
    {
    	super("Search for Pits");
    	this.mf = mf;
        int wdth=940;
        int ht=960;
        if (mf.macos) starty=26;
        this.setMaximizedBounds(new Rectangle(wdth, ht));
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
    	setLayout(null);
    	SymWindow swindow = new SymWindow();
    	this.addWindowListener(swindow);
    	estDate1 = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate1.addWindowListener(swindow);
        estDate1.setVisible(false);
        
        estDate2 = new org.compiere.grid.ed.Calendar(this, "Select Date/Time", null, 16, this);
        estDate2.addWindowListener(swindow);
        estDate2.setVisible(false);
        estDate2.setLocation(260, 80);
        MenuAction mnac = new MenuAction();
        setTime1.addActionListener(mnac);
        setTime2.addActionListener(mnac);
        getPits.addActionListener(mnac);
        pits.addActionListener(mnac);
        exQry.addActionListener(mnac);
        initControls();
    	buildForm();
    }
    
    public void updateEstDate()
    {
    	if ( estDate1.isVisible() )
        {
        	ts1= estDate1.getTimestamp().getTime();
        	System.out.println("TS: "+ts1);
        	Date d = new Date(ts1);
        	time1.setText(d.toString());
        	System.out.println(d.toString());
        }
        if ( estDate2.isVisible() )
        {
        	ts2= estDate2.getTimestamp().getTime();
        	Date d = new Date(ts2);
        	time2.setText(d.toString());
        }
    }
    
    void buildMenu()
    {
    	layerMenuItem.setLabel("Layers");
    	testMenuItem.setLabel("Test");
    	saveSelectedMenuItem.setLabel("Save Selected Pits");
    	saveAllMenuItem.setLabel("Save All Pits");
    	saveInPLMenuItem.setLabel("Save Pits in PL format");
    	menu.add(layerMenuItem);
    	menu.add(testMenuItem);
    	fileMenu.add(saveSelectedMenuItem);
    	fileMenu.add(saveAllMenuItem);
    	fileMenu.add(saveInPLMenuItem);
    	mainMenuBar.add(menu);
    	mainMenuBar.add(fileMenu);
    	menu.setLabel("Layers/Tests");
    	fileMenu.setLabel("File");
    	setMenuBar(mainMenuBar);
    	MenuAction mnac = new MenuAction();
    	layerMenuItem.addActionListener(mnac);
    	testMenuItem.addActionListener(mnac);
    	saveSelectedMenuItem.addActionListener(mnac);
    	saveAllMenuItem.addActionListener(mnac);
    	saveInPLMenuItem.addActionListener(mnac);
    }
    
    
    void saveSelectedPitsToTextFile()
    {
    	avscience.ppc.PitObs pit=null;
    	StringBuffer buffer = new StringBuffer();
    	int[] indexes = pits.getSelectedIndexes();
    	for (int i=0; i<indexes.length; i++)
    	{
    		Integer I = new Integer(indexes[i]);
    		String dbserial = (String) pitSers.get(I);
			pit = (avscience.ppc.PitObs) currentPits.get(I);
    	
	    	avscience.ppc.User u = pit.getUser();
			avscience.ppc.Location loc = pit.getLocation();
			buffer.append(pit.getName());
			buffer.append(pit.getDateString()+ "\n");
			buffer.append("Observer ,"+u.getFirst()+ " "+ u.getLast()+" ,"+"email: "+u.getEmail()+" ," + "Phone: "+u.getPhone()+"\n");
			buffer.append("Location ,"+loc.getName()+ "\n");
			buffer.append("Mtn Range ,"+loc.getRange()+"\n");
			buffer.append("State/Prov ,"+loc.getState()+"\n");
			buffer.append("Elevation "+u.getElvUnits()+" ,"+loc.getElv()+"\n");
			buffer.append("Lat. ,"+loc.getLat()+"\n");
			buffer.append("Long. ,"+loc.getLongitude()+"\n");
			
			java.util.Hashtable labels = getPitLabels();
			///avscience.util.Hashtable atts = pit.attributes;
			java.util.Enumeration e = labels.keys();
			while ( e.hasMoreElements())
			{
                            String s = (String) e.nextElement();
                            String v = "";
                            try
                            {
                                v = (String) pit.get(s);
                            }
                            catch(Exception ex)
                            {
                                System.out.println(ex.toString());
                            }
                            String l = (String) labels.get(s);
                            s = l + " ," + v + "\n";
                            if (!( s.trim().equals("null")) ) buffer.append(s);
			}
			buffer.append("Activities: \n");
			java.util.Enumeration ee = pit.getActivities().elements();
			while ( ee.hasMoreElements())
			{
				String s = (String) ee.nextElement();
				buffer.append(s+"\n");
			}
			buffer.append("\n");
			buffer.append("Layer Data:, Depth units:, "+u.getDepthUnits()+", Density Units, "+u.getRhoUnits()+"\n");
			buffer.append("Layer start, Layer end, Hardness 1, Hardness 2, Crystal Form 1, Crystal Form 2, Crystal Size 1, Crystal Size 2, Size Units 1, Size Units 2, Density 1, Density 2, Water Content \n");
			java.util.Enumeration l = pit.getLayers();
			while ( l.hasMoreElements())
			{
				avscience.ppc.Layer layer = (avscience.ppc.Layer) e.nextElement();
				buffer.append(layer.getStartDepth()+", "+layer.getEndDepth()+", "+layer.getHardness1()+", "+layer.getHardness2()+", "+layer.getGrainType1()+", "+layer.getGrainType2()+", "+layer.getGrainSize1()+", "+layer.getGrainSize2()+", "+layer.getGrainSizeUnits1()+", "+layer.getGrainSizeUnits2()+", "+layer.getDensity1()+", "+layer.getDensity2()+", "+layer.getWaterContent()+"\n");
			}
			buffer.append("\n");
                        avscience.ppc.Layer cl = pit.getCriticalLayer();
                        if (cl!=null)
                        {
                            buffer.append("Critical Layer:  ");
                            buffer.append("\n");
                            buffer.append(cl.getStartDepth()+", "+cl.getEndDepth()+", "+cl.getHardness1()+", "+cl.getHardness2()+", "+cl.getGrainType1()+", "+cl.getGrainType2()+", "+cl.getGrainSize1()+", "+cl.getGrainSize2()+", "+cl.getGrainSizeUnits1()+", "+cl.getGrainSizeUnits2()+", "+cl.getDensity1()+", "+cl.getDensity2()+", "+cl.getWaterContent()+"\n");
                            
                        }
                        
                        if ( pit.getCriticalLayerDepth() > 0 )
                        {
                            buffer.append("Critical interface depth: "+ pit.iDepth);
                            buffer.append("\n");
                        }
                        
			buffer.append("Test Data: \n");
			buffer.append("Test, Score, Shear quality, Depth \n");
			java.util.Enumeration tests = pit.getShearTests();
			while ( tests.hasMoreElements())
			{
                            avscience.ppc.ShearTestResult result = (avscience.ppc.ShearTestResult) e.nextElement();
                            buffer.append(result.getCode()+", "+result.getScore()+", "+result.getQuality()+", "+result.getDepth()+"\n");
			}
			
			buffer.append("\n");
			buffer.append("Temperature Data:, Temp Units:, "+u.getTempUnits()+"\n");
			buffer.append("Depth, Temperature \n");
			Enumeration dpths=null;
			if ( pit.hasTempProfile())
			{
				TempProfile tp = pit.getTempProfile();
				try
				{
					dpths = tp.getDepths().elements();
				
					if ( dpths!=null )
					{
						while (dpths.hasMoreElements())
						{
							Integer depth = (Integer)dpths.nextElement();
							int t = tp.getTemp(depth);
							t=t/10;
							buffer.append(depth.toString()+", "+t+"\n");
						}
					}
				}
				catch(Exception xx)
				{
					System.out.println(xx.toString());
				}
			}
			/////
			buffer.append("\n");
			buffer.append("Density Data:, Density Units:, "+u.getRhoUnits()+"\n");
			buffer.append("Depth, Density \n");
			DensityProfile dp = pit.getDensityProfile();
			
			if ( dp!=null )
			{
				try
				{
					Enumeration dts = dp.getDepths().elements();
			
					if ( dts!=null ) 
					{
						while (dpths.hasMoreElements())
						{
							Integer depth = (Integer)dts.nextElement();
							String rho = dp.getDensity(depth);
							buffer.append(depth.toString()+", "+rho+"\n");
						}
					}
				}
				catch(Exception xxx)
				{
					System.out.println(xxx.toString());
				}
			}
			buffer.append("\n\n\n\n");
    	}
		////////
	   try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit File", FileDialog.SAVE);
          ///  dialog.setFilenameFilter(new SPTextFileFilter());
          //	dialog.setDirectory("/Desktop");
            dialog.setMode(FileDialog.SAVE);
           // dialog.setFile(pit.getLocation().getName().trim()+"-"+pit.getDate()+"-"+pit.getTime()+".txt");
          // 	dialog.setFile("MyPits.txt");
            //dialog.setFile("test.txt.txt");
            dialog.setVisible(true);
            
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File file = new File(dialog.getDirectory()+"\\"+dialog.getFile()+".txt");
                FileOutputStream out = null;
				PrintWriter writer = null;
				
				try
				{
					out = new FileOutputStream(file);
					writer = new PrintWriter(out);
				}
				catch(Exception ex){System.out.println(ex.toString());}
				try
		        {
		        	writer.print(buffer.toString());
		        	writer.flush();
		        	writer.close();
		        }
		        catch(Exception ex){System.out.println(ex.toString());}
    		
            }
       }
       catch(Exception exx){System.out.println(exx.toString());}
    }
    /////////
    void saveSelectedPitsToTextFileInIlayerFormat()
    {
    	avscience.ppc.PitObs pit=null;
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("Header data,,,,,,Layer below PL,,,,,PL,,,,,Layer above PL,,,,,2 layers above PL,,,,,Top layer,,,,\n");
    	buffer.append("Location,Range,State,Observer,Date,Incline,Grain type,Grain size,layer depth bottom,Layer depth top,Hardness,Grain type,Grain size,layer depth bottom,Layer depth top,Hardness,Grain type,Grain size,layer depth bottom,Layer depth top,Hardness,Grain type,Grain size,layer depth bottom,Layer depth top,Hardness,Grain type,Grain size,layer depth bottom,Layer depth top,Hardness \n");
    ///	int[] indexes = pits.getSelectedIndexes();
    	Enumeration<avscience.ppc.PitObs> pits = currentPits.elements();
    //	for (int i=0; i<indexes.length; i++)
    	while ( pits.hasMoreElements() )
    	{
    	//	Integer I = new Integer(indexes[i]);
    	//	String dbserial = (String) pitSers.get(I);
			pit = pits.nextElement() ;
			if (pit.hasProblemInterface())
			{
		    	avscience.ppc.User u = pit.getUser();
				avscience.ppc.Location loc = pit.getLocation();
				buffer.append(loc.getName()+" ,");
				buffer.append(loc.getRange()+ " ,");
				buffer.append(loc.getState()+ " ,");
				buffer.append(u.getFirst()+ " "+ u.getLast()+" ,");
				buffer.append(pit.getDate()+ " ,");
				buffer.append(pit.getIncline()+ " ,");
				
				
				avscience.ppc.Layer pl = new avscience.ppc.Layer();
				Vector layers = pit.getLayersVector();
				int cl=0;
				Enumeration e = layers.elements();
				while ( e.hasMoreElements())
				{
					avscience.ppc.Layer l = (avscience.ppc.Layer)e.nextElement();
					if ( pit.layerIsCritical(l)) 
					{
						pl=l;
						cl++;
						break;
					}
				}
				avscience.ppc.Layer layerBelow = new avscience.ppc.Layer();
				avscience.ppc.Layer layerAbove = new avscience.ppc.Layer();
				avscience.ppc.Layer secAbove = new avscience.ppc.Layer();
				avscience.ppc.Layer top = new avscience.ppc.Layer();
				
				int abv = cl+1;
				if ( abv < layers.size() ) layerAbove=(avscience.ppc.Layer) layers.get(abv);
				int sabv = cl+2;
				if ( sabv < layers.size() ) secAbove=(avscience.ppc.Layer) layers.get(sabv);
				int blw = cl-1;
				if ( blw >-1 ) layerBelow=(avscience.ppc.Layer) layers.get(blw);
				
				top = (avscience.ppc.Layer)layers.get(layers.size()-1);
				
				buffer.append(writeLayerToString(layerBelow));
				buffer.append(" ,");
				buffer.append(writeLayerToString(pl));
				buffer.append(" ,");
				buffer.append(writeLayerToString(layerAbove));
				buffer.append(" ,");
				buffer.append(writeLayerToString(secAbove));
				buffer.append(" ,");
				buffer.append(writeLayerToString(top));
				buffer.append("\n");
			}
			
    	}
			
	   try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit File", FileDialog.SAVE);
            dialog.setMode(FileDialog.SAVE);
            dialog.setVisible(true);
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File file = new File(dialog.getDirectory()+"\\"+dialog.getFile()+".txt");
                FileOutputStream out = null;
				PrintWriter writer = null;
				
				try
				{
					out = new FileOutputStream(file);
					writer = new PrintWriter(out);
				}
				catch(Exception ex){System.out.println(ex.toString());}
				try
		        {
		        	writer.print(buffer.toString());
		        	writer.flush();
		        	writer.close();
		        }
		        catch(Exception ex){System.out.println(ex.toString());}
    		
            }
       }
       catch(Exception exx){System.out.println(exx.toString());}
    }
    
    String writeLayerToString(avscience.ppc.Layer l)
    {
    	StringBuffer buffer = new StringBuffer();
   
    	buffer.append(l.getGrainType1() +" ,"+l.getGrainSize1()+" ,");
    	buffer.append(l.getBottomDepth()+" ,"+ l.getTopDepth()+" ,");
    	buffer.append(l.getHardness1()+l.getHSuffix1());
    	return buffer.toString();
    }
   
    void saveAllPitsToTextFile()
    {
    	avscience.ppc.PitObs pit=null;
    	StringBuffer buffer = new StringBuffer();
    	Enumeration<avscience.ppc.PitObs> en = currentPits.elements();
    	while (en.hasMoreElements())
    	{
			pit = en.nextElement();
			
	    	avscience.ppc.User u = pit.getUser();
			avscience.ppc.Location loc = pit.getLocation();
			buffer.append(pit.getName());
			buffer.append(pit.getDateString()+ "\n");
			buffer.append("Observer ,"+u.getFirst()+ " "+ u.getLast()+" ,"+"email: "+u.getEmail()+" ," + "Phone: "+u.getPhone()+"\n");
			buffer.append("Location ,"+loc.getName()+ "\n");
			buffer.append("Mtn Range ,"+loc.getRange()+"\n");
			buffer.append("State/Prov ,"+loc.getState()+"\n");
			buffer.append("Elevation "+u.getElvUnits()+" ,"+loc.getElv()+"\n");
			buffer.append("Lat. ,"+loc.getLat()+"\n");
			buffer.append("Long. ,"+loc.getLongitude()+"\n");
			
			java.util.Hashtable labels = getPitLabels();
			///avscience.util.Hashtable atts = pit.attributes;
			java.util.Enumeration e = labels.keys();
			while ( e.hasMoreElements())
			{
				String s = (String) e.nextElement();
                                String v = "";
                                try
                                {
                                    v = (String) pit.get(s);
                                }
                                catch(Exception ex)
                                {
                                    System.out.println(ex.toString());
                                }
                                String l = (String) labels.get(s);
                                s = l + " ," + v + "\n";
                                if (!( s.trim().equals("null")) ) buffer.append(s);
			}
			buffer.append("Activities: \n");
			java.util.Enumeration ee = pit.getActivities().elements();
			while ( ee.hasMoreElements())
			{
				String s = (String) ee.nextElement();
				buffer.append(s+"\n");
			}
			buffer.append("\n");
			buffer.append("Layer Data:, Depth units:, "+u.getDepthUnits()+", Density Units, "+u.getRhoUnits()+"\n");
			buffer.append("Layer start, Layer end, Hardness 1, Hardness 2, Crystal Form 1, Crystal Form 2, Crystal Size 1, Crystal Size 2, Size Units 1, Size Units 2, Density 1, Density 2, Water Content \n");
			java.util.Enumeration l = pit.getLayers();
			while ( l.hasMoreElements())
			{
				avscience.ppc.Layer layer = (avscience.ppc.Layer) e.nextElement();
				buffer.append(layer.getStartDepth()+", "+layer.getEndDepth()+", "+layer.getHardness1()+", "+layer.getHardness2()+", "+layer.getGrainType1()+", "+layer.getGrainType2()+", "+layer.getGrainSize1()+", "+layer.getGrainSize2()+", "+layer.getGrainSizeUnits1()+", "+layer.getGrainSizeUnits2()+", "+layer.getDensity1()+", "+layer.getDensity2()+", "+layer.getWaterContent()+"\n");
			}
			buffer.append("\n");
			buffer.append("Test Data: \n");
			buffer.append("Test, Score, Shear quality, Depth \n");
			java.util.Enumeration tests = pit.getShearTests();
			while ( tests.hasMoreElements())
			{
                            avscience.ppc.ShearTestResult result = (avscience.ppc.ShearTestResult) e.nextElement();
                            buffer.append(result.getCode()+", "+result.getScore()+", "+result.getQuality()+", "+result.getDepth()+"\n");
			}
			
			buffer.append("\n");
			buffer.append("Temperature Data:, Temp Units:, "+u.getTempUnits()+"\n");
			buffer.append("Depth, Temperature \n");
			Enumeration dpths=null;
			if ( pit.hasTempProfile())
			{
				try
				{
					TempProfile tp = pit.getTempProfile();
					if (tp.getDepths()!=null)dpths = tp.getDepths().elements();
					
					if ( dpths!=null )
					{
						while (dpths.hasMoreElements())
						{
							Integer depth = (Integer)dpths.nextElement();
							int t = tp.getTemp(depth);
							t=t/10;
							buffer.append(depth.toString()+", "+t+"\n");
						}
					}
				}
				catch(Exception eee)
				{
					System.out.println(eee.toString());
				}
				
			}
			/////
			buffer.append("\n");
			buffer.append("Density Data:, Density Units:, "+u.getRhoUnits()+"\n");
			buffer.append("Depth, Density \n");
			DensityProfile dp = pit.getDensityProfile();
			
			if ( dp!=null )
			{
				try
				{
					Enumeration dts = dp.getDepths().elements();
			
					if ( dts!=null ) 
					{
						while (dts.hasMoreElements())
						{
							Integer depth = (Integer)dts.nextElement();
							String rho = dp.getDensity(depth);
							buffer.append(depth.toString()+", "+rho+"\n");
						}
					}
				}
				catch(Exception exx)
				{
					System.out.println(exx.toString());
				}
			}
			buffer.append("\n\n\n\n");
    	}
		////////
	   try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit File", FileDialog.SAVE);
          ///  dialog.setFilenameFilter(new SPTextFileFilter());
          //	dialog.setDirectory("/Desktop");
            dialog.setMode(FileDialog.SAVE);
           // dialog.setFile(pit.getLocation().getName().trim()+"-"+pit.getDate()+"-"+pit.getTime()+".txt");
          // 	dialog.setFile("MyPits.txt");
            //dialog.setFile("test.txt.txt");
            dialog.setVisible(true);
            
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File file = new File(dialog.getDirectory()+"\\"+dialog.getFile()+".txt");
                FileOutputStream out = null;
				PrintWriter writer = null;
				
				try
				{
					out = new FileOutputStream(file);
					writer = new PrintWriter(out);
				}
				catch(Exception ex){System.out.println(ex.toString());}
				try
		        {
		        	writer.print(buffer.toString());
		        	writer.flush();
		        	writer.close();
		        }
		        catch(Exception ex){System.out.println(ex.toString());}
    		
            }
       }
       catch(Exception exx){System.out.println(exx.toString());}
    }
    
    ///////////////////////////////////////////////////////////
    
    boolean checkPitLayers(avscience.ppc.PitObs pit)
    {
    	LayerAttributes atts = layerSearchFrame.getAttributesFromForm();
    	Enumeration e = pit.getLayers();
    	while ( e.hasMoreElements())
    	{
    		avscience.ppc.Layer layer = (avscience.ppc.Layer) e.nextElement();
    		if (checkLayer(layer, atts, pit.getUser()))
    		{
    			return true;
    		}
    	}
    	return false;
    }
    
    boolean checkLayer(avscience.ppc.Layer layer, LayerAttributes atts, avscience.ppc.User layerUser)
    {
    	boolean and=false;
    	boolean starDpthMatch=false;
    	boolean endDepthMatch=false;
    	boolean hardnessMatch=false;
    	boolean typeMatch=false;
    	boolean sizeMatch=false;
    	boolean rhoMatch=false;
    	boolean wcMatch=false;
    	
    	boolean checkStart=false;
    	boolean checkEnd=false;
    	boolean checkHardness=false;
    	boolean checkType=false;
    	boolean checkSize=false;
    	boolean checkRho=false;
    	boolean checkWc=false;
    	boolean passes = false;
    	
    	if ( atts.operator.equals("AND")) and = true;
    	
    	double sDepthLay = layer.getStartDepth();
    	if ( layerUser.getDepthUnits().equals("inches") )
    	{
    		sDepthLay = (int) java.lang.Math.round(sDepthLay/2.54);
    	}
    	double sDepthAtt = atts.getStartDepth();
    	if ( sDepthAtt >= 0 ) checkStart=true;
    	char sdpthOp = atts.startDepthOp;
  	
  		if ( checkStart )
  		{
			if ( sdpthOp == '=' )
			{
				if ( sDepthLay == sDepthAtt ) starDpthMatch = true;
			}
			else if ( sdpthOp == '<' )
			{
				if ( sDepthLay < sDepthAtt ) starDpthMatch = true;
			}
			else if ( sdpthOp == '>' )
			{
				if ( sDepthLay > sDepthAtt ) starDpthMatch = true;
			}
		}
    	////////////////
    	double eDepthLay = layer.getEndDepth();
    	if ( layerUser.getDepthUnits().equals("inches") )
    	{
    		eDepthLay = (int) java.lang.Math.round(sDepthLay/2.54);
    	}
    	int eDepthAtt = atts.getEndDepth();
    	if ( eDepthAtt >= 0 ) checkEnd=true;
    	char edpthOp = atts.endDepthOp;
  	
  		if ( checkEnd )
  		{
			if ( edpthOp == '=' )
			{
				if ( eDepthLay == eDepthAtt ) endDepthMatch = true;
			}
			else if ( edpthOp == '<' )
			{
				if ( eDepthLay < eDepthAtt ) endDepthMatch = true;
			}
			else if ( edpthOp == '>' )
			{
				if ( eDepthLay > eDepthAtt ) endDepthMatch = true;
			}
		}
    	
    	String[] aHardness = atts.hardness;
    	if ( aHardness.length > 0 ) checkHardness = true;
    	for ( int i = 0; i < aHardness.length; i++ )
    	{
    		if ( aHardness[i].equals(layer.getHardness1())) hardnessMatch = true;
    		if ( aHardness[i].equals(layer.getHardness2())) hardnessMatch = true;
    		if ( hardnessMatch ) break;
    	}
    	
    	if ( atts.grainType1.trim().length() > 0 ) checkType = true;
    	if ( atts.grainType2.trim().length() > 0 ) checkType = true;
    	if ( checkType )
    	{
	    	if ( atts.grainType1.trim().length() > 0 ) if (layer.getGrainType1().equals(atts.grainType1)) typeMatch = true;
	    	if ( atts.grainType2.trim().length() > 0 ) if (layer.getGrainType1().equals(atts.grainType2)) typeMatch = true;
	    	if ( atts.grainType1.trim().length() > 0 ) if (layer.getGrainType2().equals(atts.grainType1)) typeMatch = true;
	    	if ( atts.grainType2.trim().length() > 0 ) if (layer.getGrainType2().equals(atts.grainType2)) typeMatch = true;
	    }
    	float lgs1 = 0;
    	float lgs2 = 0;
    	
    	try
    	{
    		lgs1 = new Float(layer.getGrainSize1()).floatValue();
    	}
    	catch(Exception e){}
    	
    	try
    	{
    		lgs2 = new Float(layer.getGrainSize2()).floatValue();
    	}
    	catch(Exception e){}
    	if ( layer.getGrainSizeUnits1().equals("cm") ) lgs1=lgs1*10;
    	if ( layer.getGrainSizeUnits2().equals("cm") ) lgs2=lgs2*10;
    	char szOp = atts.grainSizeOp;
    	float ags = atts.getGrainSize();
    	if ( ags > 0.0f ) checkSize = true;
    	if ( checkSize )
    	{
			if ( szOp == '=' )
			{
				if ( lgs1 == ags ) sizeMatch = true;
				if ( lgs2 == ags ) sizeMatch = true;
			}
			else if ( szOp == '<' )
			{
				if ( lgs1 < ags ) sizeMatch = true;
				if ( lgs2 < ags ) sizeMatch = true;
			}
			else if ( szOp == '>' )
			{
				if ( lgs1 > ags ) sizeMatch = true;
				if ( lgs2 > ags ) sizeMatch = true;
			}
		}
    	
    	float arho = atts.getRho();
    	if ( arho > 0.0f ) checkRho = true;
    	float lrho1 = 0f;
    	float lrho2 = 0f;
    	try
    	{
    		lrho1 = new Float(layer.getDensity1()).floatValue();
    	}
    	catch(Exception e){}
    	try
    	{
    		lrho2 = new Float(layer.getDensity2()).floatValue();
    	}
    	catch(Exception e){}
    	
    	if ( layerUser.getRhoUnits().equals("lbs/cubic_ft"))
    	{
    		lrho1 = 16.19f*lrho1;
    		lrho2 = 16.19f*lrho2;
    	}
    	
    	char rhoOp = atts.rhoOp;
    	if ( checkRho )
    	{
	    	if ( rhoOp == '=' )
			{
				if ( lrho1 == arho ) rhoMatch = true;
				if ( lrho2 == arho ) rhoMatch = true;
			}
			else if ( rhoOp == '<' )
			{
				if ( lrho1 < arho ) rhoMatch = true;
				if ( lrho2 < arho ) rhoMatch = true;
			}
			else if ( rhoOp == '>' )
			{
				if ( lrho1 > arho ) rhoMatch = true;
				if ( lrho2 > arho ) rhoMatch = true;
			}
		}
    	
    	String[] wc = atts.waterContent;
    	String lwc = layer.getWaterContent();
    	if ( atts.waterContent.length > 0 ) checkWc = true;
    	
    	for ( int i = 0; i < wc.length; i++ )
    	{
    		if ( lwc.equals(wc[i])) 
    		{
    			wcMatch = true;
    			break;
    		}
    	}
    	
    	if ( and )
    	{
    		passes = true;
    		if ( checkStart ) passes = starDpthMatch;
    		if ( checkEnd ) passes = passes && endDepthMatch;
    		if ( checkHardness) passes = passes && hardnessMatch;
    		if ( checkType ) passes = passes && typeMatch;
    		if ( checkSize ) passes = passes && sizeMatch;
    		if ( checkRho ) passes = passes && rhoMatch;
    		if ( checkWc ) passes = passes && wcMatch;
    	}
    	else passes = ( starDpthMatch | endDepthMatch | hardnessMatch | typeMatch | sizeMatch | rhoMatch | wcMatch);
    	
    	return passes;
    }
    
    boolean checkPitTests(avscience.ppc.PitObs pit)
    {
    	System.out.println("Check pit tests: "+pit.getName());
    	System.out.println("# of tests: "+pit.getTestResultStrings().length);
    	StabilityTestAttributes atts = testSearchFrame.getAttributesFromForm();
    	String[] testNames = pit.getTestResultStrings();
    	for ( int i=0; i < testNames.length; i++ )
    	{
    		avscience.ppc.ShearTestResult result = pit.getShearTestResult(testNames[i]);
    		if ( checkTest(result, atts))
    		{
    			System.out.print("Test Passed for pit: "+pit.getName());
    			return true;
    		}
    		
    	}
    	
    	return false;
    }
    
  	boolean checkTest(avscience.ppc.ShearTestResult result, StabilityTestAttributes atts)
  	{
  		System.out.println("checkTest: "+result.toString());
  		boolean and = atts.operator.equals("AND");
  		boolean passes = false;
  		boolean scoreMatch = false;
  		//
  		boolean checkScore = false;
  		boolean checkQual = false;
  		boolean checkDepth = false;
  		boolean checkCT = false;
  		
		boolean qMatch=false;
		boolean depthMatch=false;
		boolean ctMatch=false;
  		String type = ShearTests.getInstance().getShearTestByCode(result.getCode()).getType();
		boolean typeMatch=false;
		System.out.println("Test Type: "+ type);
		System.out.println("Att test type: "+atts.testType);
		typeMatch = type.equals(atts.testType);
		System.out.println("type match? "+typeMatch);
  		
  		if ( typeMatch )
  		{
			for (int i=0; i<atts.testScores.length; i++ )
			{
				System.out.println("atts score: "+atts.testScores[i]);
				System.out.println("result score: "+result.getScore());
					
				if ( result.getScore().equals(atts.testScores[i]))
				{
					scoreMatch=true;
					System.out.println("scoreMatch: "+scoreMatch);
					break;
				}
			}
  		}
  		
  		if ( atts.shearQuality.length > 0 ) checkQual = true;
  		if ( atts.getDepth() > 0 ) checkDepth = true;
  		if ( atts.testScores.length > 0 ) checkScore = true;
  		if ( atts.CTScore > 0 ) checkCT = true;
  		
  		for ( int i=0; i<atts.shearQuality.length; i++ )
  		{
  			if ( result.getQuality().equals(atts.shearQuality[i]))
  			{
  				qMatch = true;
  				break;
  			}	
  		}
  		
  		int dpth = (int) result.getDepthSI();
  		System.out.println("res depth: "+dpth);
  		System.out.println("atts depth: "+atts.getDepth());
  		char dpthOp = atts.depthOp;
  		System.out.println("depthOp: "+dpthOp);
  		if ( checkDepth )
  		{
	  		if (dpth > 0)
	  		{
	  			if ( dpthOp == '=' )
	  			{
	  				if ( dpth == atts.getDepth() ) depthMatch = true;
	  			}
	  			else if ( dpthOp == '<' )
	  			{
	  				if ( dpth < atts.getDepth() ) depthMatch = true;
	  			}
	  			else if ( dpthOp == '>' )
	  			{
	  				if ( dpth > atts.getDepth() ) depthMatch = true;
	  			}
	  		}
	  	}
  		boolean isCTest = false;
  		if ( type.equals("Compression Test"))
  		{
  			isCTest = true;
  			String ct = result.getCTScore();
  			int ctScore = new Integer(ct).intValue();
  			char ctOp = atts.CTScoreOp;
  			if ( ctScore > 0 )
  			{
	  			if ( ctOp == '=' )
	  			{
	  				if ( ctScore == atts.CTScore ) ctMatch = true;
	  			}
	  			else if ( ctOp == '<' )
	  			{
	  				if ( ctScore < atts.CTScore ) ctMatch = true;
	  			}
	  			else if ( ctOp == '>' )
	  			{
	  				if ( ctScore > atts.CTScore ) ctMatch = true;
	  			}
  			}
  		}
  		
  		if ( and )
  		{
  			if ( !typeMatch ) return false;
  			else passes = true;
  			if ( checkScore ) passes = scoreMatch;
  			if ( checkQual ) passes = ( passes && qMatch );
  			if ( checkCT ) passes = ( passes && ctMatch );
  			if ( checkDepth ) passes = ( passes && depthMatch);
  			
  		}
  		else passes = (typeMatch | scoreMatch | qMatch | ctMatch | depthMatch);
  	
  		return passes;
  	}
    
    void popPitList(boolean form, boolean testFilter, boolean layerFilter)
    {
    	noPits.setText("Getting pits from DB");
        getPits.setEnabled(false);
    	String qry=query.getText();
    	query.setText(" ");
    	repaint();
    	boolean and = operator.getSelectedItem().equals("AND");
    	int count = 0;
    	String whereClause = "";
    	try
    	{
	    	pits.removeAll();
	    	currentPits = new Hashtable();
	    	pitSers = new Hashtable();
	    	if ( form ) whereClause = getWhereClauseFromForm();
	    	else whereClause = qry;
	    	query.setText(whereClause);
	    	
	    	String[][] v = getPitsFromQuery(whereClause);
                sortPitsbyObsDate(v);
	    //	Object[] keys = v.keySet().toArray();
                //Object[] keys = v[0];
                System.out.println("NUMBER OF PITS: "+v[0].length);
		for ( int i = 0; i < v[0].length; i++ )
	    	{
	    		//String serial = (String) keys[i];
	    		//String data = (String) v.get(serial);
                        String serial = v[0][i];
                        String name = v[1][i];
                        String data = v[2][i];
                        System.out.println("Pit Serial:  "+serial);
                        System.out.println("Name:  "+name);
                        
	    		
	    		if (( data!=null) && (data.trim().length()>9))
	    		{
	    			try
	    			{
		    			avscience.ppc.PitObs pit = new avscience.ppc.PitObs(data); 
		    			if ( pit!=null )
		    			{
				    		String nm = pit.getName();
				    		System.out.println("pit name: "+nm);
				    		if ( nm != null )
				    		{
				    			boolean add = false;
				    			boolean addTest = false;
				    			boolean addLay = false;
				    			if ( testFilter ) 
				    			{
				    				System.out.println("Filtering by test.");
				    				addTest = checkPitTests(pit);
				    			}
				    			
				    	
				    			if ( layerFilter )
				    			{
				    				addLay = checkPitLayers(pit);
				    			}
				    			
				    			
				    			if ( and ) add = addLay & addTest;
				    			else add = addLay | addTest;
				    			if (!testFilter && !layerFilter) add = true;
				    			if ( hasPL.getState() && !pit.hasProblemInterface()) add=false;
				    			if ( add )
				    			{
				    				System.out.println("adding pit: "+pit.getName());
				    				
					    			pits.add(nm);
					    			currentPits.put(new Integer(count), pit);
					    			pitSers.put(new Integer(count), serial);
					    			count++;
					    		}
					  
				    		}
				    	}
				    }
				    catch(Exception ee){System.out.println(ee.toString());}
		    	}
	    	}
	    	if (testFilter | layerFilter) noPits.setText(pits.getItemCount()+" pits filtered.");
	    	
	    }
	    catch(Exception e){System.out.println(e.toString());}
            getPits.setEnabled(true);
	    noPits.setText(currentPits.size()+" pits meet criteria.");
    }
    
    void sortPitsbyObsDate(String[][] pits)
    {
        noPits.setText("Processing pits...");
        getPits.setEnabled(false);
        boolean sorted = false;
        int length = pits[0].length;
        int i = 0;
        long [] pdates = new long[pits[0].length];
        avscience.ppc.PitObs pit = new avscience.ppc.PitObs();
        avscience.ppc.PitObs pitInc = pit = new avscience.ppc.PitObs();

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    String data = pits[2][i];
                    String serial = pits[0][i];
                    String name = pits[0][i];
                    long pdate = 0;
                    long pdateInc = 0;
                    
                    if (pdates[i]==0)
                    {
                        try
                        {
                            pit = new avscience.ppc.PitObs(data); 
                        }
                        catch(Exception e)
                        {
                            System.out.println(e.toString());
                        }
                        
                        pdate = pit.getTimestamp();
                        pdates[i]=pdate;
                    }
                    else pdate = pdates[i];
                    
                    String dataInc = pits[2][i+1];
                    String serialInc = pits[0][i+1];
                    String nameInc = pits[0][i+1];
                    
                    if (pdates[i+1]==0)
                    {
                        try
                        {
                            pitInc = new avscience.ppc.PitObs(dataInc); 
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex.toString());
                        }
                        pdateInc = pitInc.getTimestamp();
                        pdates[i+1]=pdateInc;
                    }
                    else pdateInc = pdates[i+1];
                    
                    if ( pdateInc > pdate )
                    {
                            pits[0][i] = serialInc;
                            pits[0][i+1] = serial;
                            
                            pits[1][i] = nameInc;
                            pits[1][i+1] = name;
                            
                            pits[2][i] = dataInc;
                            pits[2][i+1] = data;
                            
                            pdates[i]=pdateInc;
                            pdates[i+1]=pdate;
                            
                            sorted = false;
                    }
                }
            }
        }
       /// return pits;
    }
    
    private String[][] getPitsFromQuery(String whereClause)
    {
    	///LinkedHashMap pits = new LinkedHashMap();
        String [][] pits = new String[2][1];
    	try
        {
            String err = null;
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            //props.put("format", "pitsfromquery");
            props.put("format", "pitstringsarrayfromquery");
            whereClause = URLEncoder.encode(whereClause, "UTF-8");
            props.put("q", whereClause);
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            pits =  (String[][]) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        System.out.println("# of PITS: "+pits[0].length);
        noPits.setText(pits[0].length+" pits received.");
        return pits;
    }
    
  /*  void filterByHasPL()
    {
    	Enumeration<String> e = currentPits.keys();
    	while (e.hasMoreElements())
    	{
    		String s = e.nextElement();
    		avscience.ppc.PitObs pit = (avscience.ppc.PitObs) currentPits.get(s);
    	 currentPits.remove(s);
    	}
    }*/
    
    String getWhereClauseFromForm()
    {
    	boolean fa = false;
    	StringBuffer buffer = new StringBuffer();
    	buffer.append("WHERE");
    	String s =" ( ";
    	String op = " "+operator.getSelectedItem()+" ";
    	String e =" ) ";
    	String[] sts = states.getSelectedItems();
    	if ( sts.length > 0 ) if (!sts[0].equals("ALL")) buffer.append(s);
    	for (int i = 0; i < sts.length; i++)
    	{
            if (!sts[i].equals("ALL"))
            {
    		fa=true;
    		buffer.append(" STATE = '"+sts[i]+"'");
    		if (( sts.length > 1 ) && ( i != sts.length-1 ))buffer.append(" OR ");
            }
    	}
    	if ( sts.length > 0 ) if (!sts[0].equals("ALL")) buffer.append(e);
    	
    	String[] rngs = range.getSelectedItems();
    	if ( fa && rngs.length > 0) if (!rngs[0].equals("ALL")) buffer.append(op);
    	//if ( rngs.length > 0 ) buffer.append(s);
        if ( rngs.length > 0 ) if (!rngs[0].equals("ALL")) buffer.append(s);
    	for (int i = 0; i < rngs.length; i++)
    	{
            if (!rngs[i].equals("ALL"))
            {
    		fa=true;
    		buffer.append(" MTN_RANGE = '"+rngs[i]+"'");
    		if (( rngs.length > 1 ) && ( i != rngs.length-1 ))buffer.append(" OR ");
            }
    	}
    	if ( rngs.length > 0 ) if (!rngs[0].equals("ALL"))  buffer.append(e);
    	
    	String[] locs = locations.getSelectedItems();
    	if ( fa && locs.length > 0 ) buffer.append(op);
    	if ( locs.length > 0 ) buffer.append(s);
    	for (int i = 0; i < locs.length; i++)
    	{
    		fa=true;
    		buffer.append(" LOC_NAME = '"+locs[i]+"'");
    		if (( locs.length > 1 ) && ( i != locs.length-1 ))buffer.append(" OR ");
    	}
    	if ( locs.length > 0 ) buffer.append(e);
    	
    	String[] precs = precip.getSelectedItems();
    	if ( fa && precs.length > 0 ) buffer.append(op);
    	if ( precs.length > 0 ) buffer.append(s);
    	for (int i = 0; i < precs.length; i++)
    	{
    		fa=true;
    		buffer.append(" PRECIP = '"+precs[i]+"'");
    		if (( precs.length > 1 ) && ( i != precs.length-1 ))buffer.append(" OR ");
    	}
    	if ( precs.length > 0  && fa ) buffer.append(e);
    	
    	String[] speed = wind.getSelectedItems();
    	if ( fa && speed.length > 0 ) buffer.append(op);
    	if ( speed.length > 0 ) buffer.append(s);
    	for (int i = 0; i < speed.length; i++)
    	{
    		fa=true;
    		buffer.append(" WIND_SPEED = '"+speed[i]+"'");
    		if (( speed.length > 1 ) && ( i != speed.length-1 ))buffer.append(" OR ");
    	}
    	if ( speed.length > 0 ) buffer.append(e);
    	
    	String[] dir = winDir.getSelectedItems();
    	if ( fa && dir.length > 0 ) buffer.append(op);
    	if ( dir.length > 0 ) buffer.append(s);
    	for (int i = 0; i < dir.length; i++)
    	{
    		fa=true;
    		buffer.append(" WIND_DIR = '"+dir[i]+"'");
    		if (( dir.length > 1 ) && ( i != dir.length-1 ))buffer.append(" OR ");
    	}
    	if ( dir.length > 0  ) buffer.append(e);
    	
    	String[] load = winLoad.getSelectedItems();
    	if ( fa && load.length > 0 ) buffer.append(op);
    	if ( load.length > 0 ) buffer.append(s);
    	for (int i = 0; i < load.length; i++)
    	{
    		fa=true;
    		buffer.append(" WINDLOAD = '"+load[i]+"'");
    		if (( load.length > 1 ) && ( i != load.length-1 ))buffer.append(" OR ");
    	}
    	if ( load.length > 0 ) buffer.append(e);
    	
    	String[] sk = sky.getSelectedItems();
    	if ( fa && sk.length > 0 ) buffer.append(op);
    	if ( sk.length > 0 ) buffer.append(s);
    	for (int i = 0; i < sk.length; i++)
    	{
    		fa=true;
    		buffer.append(" SKY_COVER = '"+sk[i]+"'");
    		if (( sk.length > 1 ) && ( i != sk.length-1 ))buffer.append(" OR ");
    	}
    	if ( sk.length > 0 ) buffer.append(e);
    	
    	String[] stab = stability.getSelectedItems();
    	if ( fa && stab.length > 0 ) buffer.append(op);
    	if ( stab.length > 0 ) buffer.append(s);
    	for (int i = 0; i < stab.length; i++)
    	{
    		fa=true;
    		buffer.append(" STABILITY = '"+stab[i]+"'");
    		if (( stab.length > 1 ) && ( i != stab.length-1 ))buffer.append(" OR ");
    	}
    	if ( stab.length > 0 ) buffer.append(e);
    	String e1 = elv1.getText();
    	if ( e1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ELEVATION "+elv1.getOperatorValue()+ " "+e1);
    	}
    	
    	String e2 = elv2.getText();
    	if ( e2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ELEVATION "+elv2.getOperatorValue()+ " "+e2);
    	}
    	//////////
    	String a1 = aspect1.getText();
    	if ( a1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ASPECT "+aspect1.getOperatorValue()+ " "+a1);
    	}
    	
    	String a2 = aspect2.getText();
    	if ( a2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" ASPECT "+aspect2.getOperatorValue()+ " "+a2);
    	}
    	///////////////////////
    	String i1 = incline1.getText();
    	if ( i1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" INCLINE "+incline1.getOperatorValue()+ " "+i1);
    	}
    	
    	String i2 = incline2.getText();
    	if ( i2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" INCLINE "+incline2.getOperatorValue()+ " "+i2);
    	}
    	//////////////////////////////
    	///////////
    	String t1 = temp1.getText();
    	if ( t1.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" AIR_TEMP "+temp1.getOperatorValue()+ " "+t1);
    	}
    	
    	String t2 = temp2.getText();
    	if ( t2.trim().length() > 0 )
    	{
    		if ( fa )buffer.append(op);
    		fa=true;
    		buffer.append(" AIR_TEMP "+temp2.getOperatorValue()+ " "+t2);
    	}
    
    	s = lat1.getText();
        s = s.trim();
        if ( s.length() > 0 ) 
        {
            float lat = (new Float(s)).floatValue();
            if (fa) buffer.append(" AND LAT > " + lat);
            else buffer.append(" LAT > " + lat);
            fa = true;
        }
        
        s = lat2.getText();
        s = s.trim();
        if ( s.length() > 0 ) 
        {
            float lat = (new Float(s)).floatValue();
            if (fa) buffer.append(" AND LAT < " + lat);
            else buffer.append(" LAT < " + lat);
            fa = true;
        }
        
        s = lon1.getText();
        s = s.trim();
        if ( s.length() > 0 ) 
        {
            float l = (new Float(s)).floatValue();
            if (fa) buffer.append(" AND LONGITUDE > " + l);
            else buffer.append(" LONGITUDE > " + l);
            fa = true;
        }
        
        s = lon2.getText();
        s = s.trim();
        if ( s.length() > 0 ) 
        {
            float l = (new Float(s)).floatValue();
            if (fa) buffer.append(" AND LONGITUDE < " + l);
            else buffer.append(" LONGITUDE < " + l);
            fa = true;
        }
    	
    	Checkbox cb1 = tg1.getSelectedCheckbox();
    	if (( ts1 > 0 ) && (cb1!=null))
    	{
    		if ( fa ) buffer.append(op);
    		fa = true;
    		buffer.append(" OBS_DATE ");
    		if ( cb1 == t1after ) buffer.append("> ");
    		else buffer.append("< ");
    		buffer.append(getDateString(ts1));
    	}
    	
    	Checkbox cb2 = tg2.getSelectedCheckbox();
    	if (( ts2 > 0 ) && (cb2!=null))
    	{
    		if ( fa ) buffer.append(op);
    		fa = true;
    		buffer.append(" OBS_DATE ");
    		if ( cb2 == t2after ) buffer.append("> ");
    		else buffer.append("< ");
    		buffer.append(getDateString(ts2));
    	}
    	
    	String[] acts = activities.getSelectedItems();
    	if ( fa && acts.length > 0 ) buffer.append(op);
    	if ( acts.length > 0 ) buffer.append(s);
    	for (int i = 0; i < acts.length; i++)
    	{
    		buffer.append(" ACTIVITIES LIKE '%"+acts[i]+"%'");
    		if (( acts.length > 1 ) && ( i != acts.length-1 ))buffer.append(" OR ");
    	}
    	
    	System.out.println("Where clause:: "+buffer.toString());
    	return buffer.toString();
    }

    
    String getDateString(long time)
    {
    	//time = System.currentTimeMillis();
    	java.util.Calendar cal = java.util.Calendar.getInstance();
    	cal.setTimeInMillis(time);
    	String month = "";
    	String day = "";
    	int yr = cal.get(java.util.Calendar.YEAR);
    	String year = yr+"";
    	int mnth = cal.get(java.util.Calendar.MONTH)+1;
    	if ( mnth < 10 ) month = "0"+mnth;
    	else month = mnth+"";

    	int dy = cal.get(java.util.Calendar.DAY_OF_MONTH);
    	if ( dy < 10 ) day = "0"+dy;
    	else day = dy+"";
    	
    	int hr = cal.get(java.util.Calendar.HOUR_OF_DAY);
    	int mn = cal.get(java.util.Calendar.MINUTE);
    	int sc = cal.get(java.util.Calendar.SECOND);
    	
    	String hour = "";
    	if ( hr < 10 ) hour = "0"+hr;
    	else hour = hr+"";
    	String min = "";
    	if ( mn < 10 ) min = "0"+mn;
    	else min = mn+"";
    	String sec = "";
    	if ( sc < 10 ) sec = "0"+sc;
    	else sec = sc+"";
    	
    	
    	String res = year+month+day+hour+min+sec;
    	System.out.println("getDateString()");
    	System.out.println("res: "+res);
    	return res;
	}
	
    void buildForm()
    {
    	int xx = startx;
        int yy = starty;
        Label ol = new Label("Operator: ");
        ol.setSize(120, 20);
        ol.setLocation(xx, yy);
        add(ol);
        operator.setLocation(xx+124, yy);
        operator.setSize(88, 20);
        add(operator);
        operator.select("AND");
        yy+=yspace;
    	Label lcs = new Label("Locations:");
    	lcs.setSize(120, 20);
    	lcs.setLocation(xx, yy);
    	add(lcs);
    	locations.setLocation(xx, yy+yspace);
    	
    	add(locations);
    	locations.setSize(200, 120);
    	
    	xx+=col3Space;
    	Label sts = new Label("States/Provs:");
    	sts.setSize(128, 20);
    	sts.setLocation(xx, yy);
    	add(sts);
    	states.setLocation(xx, yy+yspace);
    	states.setMultipleMode(true);
    	Enumeration e = getStateList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		states.add(s);
    	}
    	add(states);
    	states.setSize(200, 120);
    	
    	xx+=col3Space;
    	Label rngs = new Label("Ranges:");
    	rngs.setSize(128, 20);
    	rngs.setLocation(xx, yy);
    	add(rngs);
    	range.setLocation(xx, yy+yspace);
    	range.setMultipleMode(true);
    	e = getRangeList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		range.add(s);
    	}
    	add(range);
    	range.setSize(200, 120);
    	
    	
    	xx=startx;
    	yy+=6*yspace;
    	Label t1 = new Label("Time 1");
    	t1.setSize(48, 20);
    	t1.setLocation(xx, yy);
    	add(t1);
    	time1.setSize(184, 20);
    	time1.setLocation(xx+50, yy);
    	add(time1);
    	xx+=colSpace;
    	Label t2 = new Label("Time 2");
    	t2.setSize(48, 20);
    	t2.setLocation(xx, yy);
    	add(t2);
    	time2.setSize(72, 20);
    	time2.setLocation(xx+50, yy);
    	add(time2);
    	hasPL = new Checkbox("Has Problem Layer", false);
    	hasPL.setLocation(xx+124, yy);
    	hasPL.setSize(160, 20);
    	hasPL.setVisible(true);
    	add(hasPL);
    	yy+=yspace;
    	xx=startx;
    	
    	t1after = new Checkbox("after", tg1, false);
    	t2after = new Checkbox("after", tg2, false);
    	t1before = new Checkbox("before", tg1, false);
    	t2before = new Checkbox("before", tg2, false);
    	
    	
    	t1before.setLocation(xx, yy);
    	t1before.setSize(90, 20);
    	add(t1before);
    	yy+=yspace;
    	t1after.setLocation(xx, yy);
    	t1after.setSize(90, 20);
    	add(t1after);
    	yy-=yspace;
    	setTime1.setSize(84, 24);
    	setTime1.setLocation(xx+96, yy);
    	add(setTime1);
    	xx+=colSpace;
    	t2before.setLocation(xx, yy);
    	t2before.setSize(90, 20);
    	add(t2before);
    	yy+=yspace;
    	t2after.setLocation(xx, yy);
    	t2after.setSize(90, 20);
    	add(t2after);
    	yy-=yspace;
    	setTime2.setSize(84, 24);
    	setTime2.setLocation(xx+96, yy);
    	add(setTime2);
    	
    	xx=startx;
    	yy+=2*yspace;
    	elv1 = new ElvTextItemOp("Elevation (m)", "m", xx, yy);
    	add(elv1);
    //	elops1.setLocation(xx+200, yy);
    //	add(elops1);
    	xx+=colSpace;
    	elv2 = new ElvTextItemOp("Elevation (m)", "m", xx, yy);
    	add(elv2);
    //	elops2.setLocation(xx+200, yy);
    //	add(elops2);
    	yy+=yspace;
    	xx=startx;
    
    	lon1 = new LonTextItem("W of", xx, yy);
    	add(lon1);
    	ew1.setLocation(xx+120, yy);
    	add(ew1);
    	xx+=colSpace/2;
    	lon2 = new LonTextItem("E of", xx, yy);
    	add(lon2);
    	ew2.setLocation(xx+120, yy);
    	add(ew2);
    	xx=colSpace+startx;
    	lat1 = new LatTextItem("N of", xx, yy);
    	add(lat1);
    	ns1.setLocation(xx+120, yy);
    	add(ns1);
    	xx+=colSpace/2;
    	lat2 = new LatTextItem("S of", xx, yy);
    	add(lat2);
    	ns2.setLocation(xx+120, yy);
    	add(ns2);
    	yy+=yspace;
    	xx=startx;
    	aspect1 = new DegTextItemOp("Aspect 1",xx, yy);
    	add(aspect1);
    	xx+=colSpace;
    	aspect2 = new DegTextItemOp("Aspect 2",xx, yy);
    	add(aspect2);
    	yy+=yspace;
    	xx=startx;
    	/////////////////
    	incline1 = new DegTextItemOp("Incline 1",xx, yy);
    	add(incline1);
    	xx+=colSpace;
    	incline2 = new DegTextItemOp("Incline 2",xx, yy);
    	add(incline2);
    	yy+=yspace;
    	xx=startx;
    	//////////////////
    	temp1 = new TempTextItemOP("Temp. 1 (C)", "C", xx, yy);
    	add(temp1);
    	xx+=colSpace;
    	temp2 = new TempTextItemOP("Temp. 2 (C)","C", xx, yy);
    	add(temp2);
    ///	tops2.setLocation(xx, yy);
    //	add(tops2);
    	yy+=yspace;
    	xx=startx;
    	Label pl = new Label("Precipitation");
    	pl.setSize(180, 20);
    	pl.setLocation(xx, yy);
    	add(pl);
    	precip.setSize(200, 100);
    	precip.setLocation(xx, yy+yspace);
    	add(precip);
    	xx+=col3Space;
    	Label ps = new Label("Wind Speed");
    	ps.setSize(180, 20);
    	ps.setLocation(xx, yy);
    	add(ps);
    	wind.setSize(200, 100);
    	wind.setLocation(xx, yy+yspace);
    	add(wind);
    	xx+=col3Space;
    	Label ss = new Label("Sky Cover");
    	ss.setSize(180, 20);
    	ss.setLocation(xx, yy);
    	add(ss);
    	sky.setSize(180, 100);
    	sky.setLocation(xx, yy+yspace);
    	add(sky);
    	yy+=5*yspace;
    	xx=startx;
    	Label wd = new Label("Wind Direction");
    	wd.setSize(140, 20);
    	wd.setLocation(xx, yy);
    	add(wd);
    	winDir.setSize(140, 84 );
    	winDir.setLocation(xx, yy+yspace);
    	add(winDir);
    	xx+=colSpace/2;
    	Label wl = new Label("Wind Loading");
    	wl.setSize(140, 20);
    	wl.setLocation(xx, yy);
    	add(wl);
    	winLoad.setSize(140, 84);
    	winLoad.setLocation(xx, yy+yspace);
    	add(winLoad);
    	xx+=colSpace/2;
    	Label acl = new Label("Activities");
    	acl.setSize(140, 20);
    	acl.setLocation(xx, yy);
    	add(acl);
    	activities.setSize(140, 84);
    	activities.setLocation(xx, yy+yspace);
    	add(activities);
    	xx+=colSpace/2;
    	Label sbl = new Label("Stability on similar slopes");
    	sbl.setSize(150, 20);
    	sbl.setLocation(xx, yy);
    	add(sbl);
    	stability.setSize(140, 84);
    	stability.setLocation(xx, yy+yspace);
    	add(stability);
    	
    	yy+=4.5*yspace;
    	xx=startx;
    	
    	query.setLocation(xx, yy-4);
    	query.setSize(620, 60);
    	add(query);
    	xx+=3*col3Space;
    	exQry.setLocation(xx, yy);
    	exQry.setSize(92, 24);
    	add(exQry);
    	
    	xx=3*col3Space+24;
    	yy = starty;
    	getPits.setLocation(xx, yy);
    	getPits.setSize(96, 22);
    	add(getPits);
    	yy+=yspace;
    	noPits.setSize(280, 20);
    	noPits.setLocation(xx, yy);
    	add(noPits);
    	yy+=yspace;
    	pits.setLocation(xx, yy);
    	pits.setSize(200, 540);
    	pits.setMultipleMode(true);
    	add(pits);
    }
    
    void initControls()
    {
    	String[] ops = {"<", ">", "="};
    	
    	/*for (int i = 0; i < ops.length; i++)
    	{
    		elops1.add(ops[i]);
    		elops2.add(ops[i]);
    		aops1.add(ops[i]);
    		aops2.add(ops[i]);
    		tops1.add(ops[i]);
    		tops2.add(ops[i]);
    	}*/
    	ew1.add("W");
    	ew1.add("E");
    	ns1.add("N");
    	ns1.add("S");
    	ew2.add("W");
    	ew2.add("E");
    	ns2.add("N");
    	ns2.add("S");
    	ItemListener listener = new iListener();
    	states.addItemListener(listener);
    	locations.addItemListener(listener);
    	range.addItemListener(listener);
    	String[] precips = Precipitation.getInstance().getDescriptions();
        int i = 0;
        for (i=0; i<precips.length; i++)
        {
            precip.add(precips[i]);
        }
        
        String[] covers = SkyCover.getInstance().getDescriptions();
        
        for (i=0; i<covers.length; i++)
        {
            sky.add(covers[i]);
        }
        
        String[] speeds = WindSpeed.getInstance().getDescriptions();
        
        for (i=0; i<speeds.length; i++)
        {
            wind.add(speeds[i]);
        }
        
        String[] dirs = WindDir.getInstance().getCodes();
        
        for (i=0; i<dirs.length; i++)
        {
            winDir.add(dirs[i]);
        }
        
        locations.setMultipleMode(true);
    	Enumeration e = getLocationList().elements();
    	while ( e.hasMoreElements())
    	{
    		String s = (String) e.nextElement();
    		locations.add(s);
    	}
    	precip.setMultipleMode(true);
    	sky.setMultipleMode(true);
    	wind.setMultipleMode(true);
    	winDir.setMultipleMode(true);
    	winLoad.setMultipleMode(true);
    	stability.setMultipleMode(true);
    	activities.setMultipleMode(true);
    	winLoad.add(" ");
    	winLoad.add("previous");
    	winLoad.add("yes");
    	winLoad.add("no");
    	
    	String[] stabilities = Stability.getInstance().getCodes();
        
        for (i=0; i<stabilities.length; i++)
        {
            stability.add(stabilities[i]);
        }
        
        String[] acts = AvActivity.getInstance().getCodes();
        for (i=0; i<acts.length; i++)
        {
            activities.add(acts[i]);
        }
        operator.add("OR");
        operator.add("AND");
        operator.select("OR");
        buildMenu();
    }
    
    void showDatePopup1()
    {
    	estDate1.setSize(380, 320);
       	estDate1.setVisible(true);
       	subFrames.add(estDate1);
    }
    
    void showDatePopup2()
    {
    	estDate2.setSize(380, 320);
       	estDate2.setVisible(true);
       	subFrames.add(estDate2);
    }
    
    public void add(TextItemType item)
    {
        add(item.getLabel());
        add(item.getField());
    }
    
    public void add(TextItemOp item)
    {
        add(item.getLabel());
        add(item.getField());
      	add(item.getOperator());
    }
    
    
    
    public class iListener implements ItemListener
    {
    	public void itemStateChanged(ItemEvent e)
    	{
    		
    		if ( e.getItemSelectable()==states )
    		{
    			String item = e.getItem().toString();	
				if ( item.equals("0"))
				{
					boolean selected = states.isIndexSelected(0);
					for (int j = states.getItemCount(); j >= 0 ; j--)
					{
						if (selected ) states.select(j);
						else states.deselect(j);
					} 
				}
    		}
    	}
    }
    
    class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			boolean testFilter = false;
			boolean layerFilter = false;
			if (( testSearchFrame != null ) && (testSearchFrame.isShowing())) testFilter = true;
			if (( layerSearchFrame != null ) && (layerSearchFrame.isShowing())) layerFilter = true;
			Object object = event.getSource();
			if ( object == setTime1 ) showDatePopup1();
			if ( object == setTime2 ) showDatePopup2();
			if ( object == getPits ) new GetPitsThread(testFilter, layerFilter).start();
			if ( object == exQry ) popPitList(false, false, false);
			if ( object == pits ) showPitFrame();
			if ( object == layerMenuItem ) showLayerSearchFrame();
			if ( object == testMenuItem ) showTestSearchFrame();
			if ( object == saveSelectedMenuItem ) saveSelectedPitsToTextFile();
			if ( object == saveAllMenuItem ) saveAllPitsToTextFile();
			if ( object == saveInPLMenuItem )saveSelectedPitsToTextFileInIlayerFormat();
		}
			
	 }
	 
	 void showLayerSearchFrame()
	 {
	 	if ( layerSearchFrame == null )
	 	{
	 		layerSearchFrame = new LayerSearchFrame(this);
	 		subFrames.add(layerSearchFrame);
	 	}
	 	boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof LayerSearchFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) layerSearchFrame.setVisible(true);
	 }
	 
	 void showTestSearchFrame()
	 {
	 	if ( testSearchFrame == null )
	 	{
	 		testSearchFrame = new TestSearchFrame(this);
	 		subFrames.add(testSearchFrame);
	 	}
	 	boolean showing = false;
		Frame[] frames = getFrames();
		for (int i=0; i<frames.length; i++)
		{
			Frame f = frames[i];
			if ((f instanceof TestSearchFrame) && (f!=null))
			{
				if (f.isShowing())
				{
					f.show();
					showing = true;
					break;
				}
			}
		}
		if (!showing) testSearchFrame.setVisible(true);
		
	 }
         
         class GetPitsThread extends Thread
         {
             boolean testFilter;
             boolean layerFilter;
             
             public GetPitsThread(boolean testFilter, boolean layerFilter)
             {
                 this.testFilter = testFilter;
                 this.layerFilter = layerFilter;
                 
             }
             
             public void run()
             {
                 popPitList(true, testFilter, layerFilter);
             }
         }
	 
	void showPitFrame()
	{
		int idx = pits.getSelectedIndex();
		Integer I = new Integer(idx);
		String dbserial = (String) pitSers.get(I);
		avscience.ppc.PitObs pit = (avscience.ppc.PitObs) currentPits.get(I);
	
		subFrames.add(new avscience.pc.PitFrame(pit, mf, true, dbserial));
	} 
    
	private Vector getStateList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "statelistall");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
        if ((list!=null) && (list.size()>0)) list.insertElementAt("ALL",0);
        
        return list;
    }
    
    private Vector getLocationList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "locationlist");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
        return list;
    }
    
    java.util.Hashtable getPitLabels()
    {
    	java.util.Hashtable attributes = new java.util.Hashtable();
    	
    	attributes.put("aspect", "Aspect");
    	attributes.put("incline", "Slope Angle");
    	attributes.put("precip", "Precipitation");
    	attributes.put("sky", "Sky Cover");
    	attributes.put("windspeed", "Wind Speed");
    	attributes.put("winDir", "Wind Direction");
    	attributes.put("windLoading", "Wind Loading");
    	
    	attributes.put("airTemp", "Air Temperature");
    	attributes.put("stability", "Stability on simular slopes");
    	
    	attributes.put("measureFrom", "Measure from: ");
    	
        attributes.put("date", "Date");
        attributes.put("time", "Time");
    	attributes.put("pitNotes", "Notes");
    	return attributes;
    }
    
    private Vector getRangeList()
    {
        Vector list = new Vector();
        try
        {
            URL url = new URL(server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("format", "rangelistall");
            InputStream in = msg.sendGetMessage(props);
            ObjectInputStream result = new ObjectInputStream(in);
            list = (Vector) result.readObject();
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
            return new Vector();
        }
        if ((list!=null) && (list.size()>0)) list.insertElementAt("ALL",0);     
        return list;
    }
    
    public void dispose()
    {
    	Enumeration e = subFrames.elements();
    	while ( e.hasMoreElements())
    	{
    		Object o = e.nextElement();
    		if (o instanceof Frame)
    		{
    			Frame f = (Frame)o;
    			try
    			{
    				f.dispose();
    			}
    			catch(Exception ex){System.out.println(ex.toString());}
    		}
    	}
    	super.dispose();
    }
    
    class SymWindow extends java.awt.event.WindowAdapter
    {   
        public void windowClosing(java.awt.event.WindowEvent event)
        {
        	System.out.println(getWhereClauseFromForm());
            Object object = event.getSource();
            if (object == PitSearchFrame.this) 
            {
            	dispose();
            	return;
            }
         /*   if ( object == estDate1 )
            {
            	ts1= estDate1.getTimestamp().getTime();
            	System.out.println("TS: "+ts1);
            	Date d = new Date(ts1);
            	time1.setText(d.toString());
            	System.out.println(d.toString());
            }
            if ( object == estDate2 )
            {
            	ts2= estDate2.getTimestamp().getTime();
            	Date d = new Date(ts2);
            	time2.setText(d.toString());
            }*/
            
        }
    }
}