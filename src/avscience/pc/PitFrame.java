package avscience.pc;

import java.awt.*;
import java.awt.print.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import avscience.desktop.*;
import avscience.ppc.*;
import avscience.wba.DensityProfile;
import avscience.wba.TempProfile;
import java.net.*;
import java.util.Properties;
import java.util.*;

public class PitFrame extends Frame implements Printable
{
	public avscience.ppc.PitObs pit;
    avscience.desktop.PitCanvas canvas;
    PitInfoCanvas iCanvas;
    
    Panel p;
    ScrollPane sp;
	private int width = 994;
	private int height = 848;
//	private int swidth=786;
//	private int sheight=542;
	private boolean smallscreen=false;
	private MenuBar mainMenuBar = new java.awt.MenuBar();
	private Menu menu = new java.awt.Menu();
	private MenuItem deleteMenuItem = new java.awt.MenuItem();
    private MenuItem editMenuItem = new java.awt.MenuItem();
    private MenuItem fullEditMenuItem = new java.awt.MenuItem();
    private MenuItem printMenuItem = new java.awt.MenuItem();
    private MenuItem saveMenuItem = new java.awt.MenuItem();
    private MenuItem saveFileMenuItem = new java.awt.MenuItem();
    private MenuItem saveAsXMLMenuItem = new java.awt.MenuItem();
    private MenuItem saveAsCAAMLMenuItem = new java.awt.MenuItem();
	public avscience.pc.MainFrame mf;
	boolean forDisplay=false;
	public boolean webEdit = false;
	public PitApplet applet;
	public avscience.pc.OccFrame oframe;
	public avscience.desktop.OccFrame ooframe;
	String dbserial = "";
	final static int maxDataLength=4500;
	
	public PitFrame(avscience.ppc.PitObs pit, MainFrame mf, boolean forDisplay)
	{
		super("Pit Observation: "+pit.getName());
		this.pit = pit;
		this.mf = mf;
		if (mf!=null) this.smallscreen=mf.smallscreen;
		this.forDisplay=forDisplay;
		this.setSize(width, height);
		this.setLocation(1, 1);
		this.setVisible(true);
		
		this.setMaximizedBounds(new Rectangle(width, height));
		initFrame();
        buildMenu();
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
	}
	
	public PitFrame(avscience.ppc.PitObs pit, MainFrame mf, boolean forDisplay, String dbserial)
	{
		this(pit, mf, forDisplay);
		this.dbserial = dbserial;
		webEdit = mf.checkSuperUser();
		buildMenu();
	}
	
	public PitFrame(avscience.ppc.PitObs pit, MainFrame mf, boolean forDisplay, PitApplet applet)
	{
		super("Pit Observation: "+pit.getName());
		this.width = width;
	//	System.out.println("DATA:: "+pit.dataString());
		this.height = height;
		this.pit = pit;
		this.mf = mf;
		setBackground(Color.WHITE);
		if (mf!=null) this.smallscreen=mf.smallscreen;
		this.forDisplay=forDisplay;
		this.setSize(width, height);
		this.setLocation(1, 1);
		this.setVisible(true);
		if (applet!=null) this.webEdit = applet.superuser;
		if (applet!=null) this.applet = applet;
		if ( applet == null ) webEdit = false;
		else webEdit = mf.checkSuperUser();
		this.setMaximizedBounds(new Rectangle(width, height));
		initFrame();
        buildMenu();
		SymWindow aSymWindow = new SymWindow();
		this.addWindowListener(aSymWindow);
		int idx = applet.pitList.getSelectedIndex();
		if(applet!=null) dbserial = applet.pitlist[1][idx];
	//	System.out.println("DATA: "+pit.dataString());
		
	}
	
	void initFrame()
	{
		this.setSize(width, height);
		this.setLocation(1, 1);
		this.setMaximizedBounds(new Rectangle(width, height));
                this.setExtendedState(Frame.MAXIMIZED_BOTH);
		boolean escale=false;
		if (mf!=null) escale = mf.getUser().hardnessScaling.equals("exponential");
		canvas = new avscience.desktop.PitCanvas(pit, width, height-160, escale, 0);
        canvas.setLocation(20, 110);
		canvas.setVisible(true);
		boolean macos=false;
		if (mf==null)macos=true;
		else macos=mf.macos;
        iCanvas = new PitInfoCanvas(pit, width, 110, macos);
        iCanvas.setLocation(20, 0);
      
        p = new Panel();
        p.setSize(width, height);
        
        p.setLayout(null);
        p.setLocation(0, 0);
        
        p.add(canvas);
		p.add(iCanvas);
	
		boolean smallscreen=false;
		if (mf!=null) smallscreen = mf.getSmallScreen();
	//	sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		if (!smallscreen) sp = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
		else sp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
	//	sp = new ScrollPane(ScrollPane.SCROLLBARS_NEVER);
		sp.doLayout();
        sp.add(p);

        Adjustable vadjust = sp.getVAdjustable();
        Adjustable hadjust = sp.getHAdjustable();
        hadjust.setUnitIncrement(10);
        vadjust.setUnitIncrement(10);
		if (smallscreen) 
		{
			int swidth = getMaxScreenWidth()-72;
			int sheight = getMaxScreenHeight()-72;
			if (swidth > (width+24) ) swidth=width+24;
			sp.setSize(swidth, sheight);
			setSize(swidth+12, sheight+12);
		}
        else sp.setSize(width, height);

		add("Center", sp);
		pack();
	}
	
	public int getMaxScreenHeight()
	{
		GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = local.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		return mode.getHeight();
	}
	
	public int getMaxScreenWidth()
	{
		GraphicsEnvironment local = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = local.getDefaultScreenDevice();
		DisplayMode mode = device.getDisplayMode();
		return mode.getWidth();
	}
	
	public class SPFileFilter implements FilenameFilter
	{
	   public boolean accept(File dir, String file)
	   {
	       file = file.trim();
	       String end = file.substring(file.length()-4, file.length());
	       if ( end.equals(".jpg")) return true;
	       else return false;
	   }
    }
    
    public class SPTextFileFilter implements FilenameFilter
	{
	   public boolean accept(File dir, String file)
	   {
	       //file = file.trim();
	      // String end = file.substring(file.length()-4, file.length());
	      // if ( end.equals(".txt")) return true;
	       //else return false;
	       return true;
	   }
    }
    
  /*  public void paintForJPG(Graphics2D gg)
    {
    	iCanvas.paint(gg);
    	canvas.paint(gg);
    }*/
    
    
    public void doPaint(Graphics2D gg)
    {
    	///fixes bug with 'all black' graphs when saving or printing on Mac OS X.
    
    	gg.setColor(Color.WHITE);
      	gg.fillRect(0, 0, width, height+38);
      	gg.setColor(Color.BLACK);
      	gg.translate(0, 36);
      	iCanvas.paint(gg);
      	gg.translate(0, 108);
      	canvas.paint(gg);
     
    }
    
    public void saveAsXML()
    {
       try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit as XML", FileDialog.SAVE);
            dialog.setFilenameFilter(new SPFileFilter());
            dialog.setFile(pit.getLocation().getName().trim());
            dialog.setVisible(true);
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File f = new File(dialog.getDirectory()+"\\"+dialog.getFile()+".xml");
                XMLWriter writer = new XMLWriter();
                writer.writeToXML(pit, f);
	        }
	     }
	     catch (Exception e) {e.printStackTrace();}
    }
	public void saveAsCAAML()
    {
       try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit as CAAML", FileDialog.SAVE);
            dialog.setFilenameFilter(new SPFileFilter());
            dialog.setFile(pit.getLocation().getName().trim());
            dialog.setVisible(true);
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File f = new File(dialog.getDirectory()+"\\"+dialog.getFile()+".xml");
                new CAAMLWriter(pit, f);
               // XMLWriter writer = new XMLWriter(f);
               // writer.writePitToXML(pit);
	        }
	     }
	     catch (Exception e) {e.printStackTrace();}
    }
	public void saveImage()
	{
       BufferedImage image = getPitImage();
       try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit Image", FileDialog.SAVE);
            dialog.setFilenameFilter(new SPFileFilter());
            String sfile = pit.getLocation().getLocName().trim()+".jpg";
            dialog.setFile(sfile);
            dialog.setVisible(true);
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
            	String ssfile = dialog.getFile();
            	if (!ssfile.contains(".jpg"))  ssfile = ssfile+".jpg";
                File f = new File(dialog.getDirectory()+"\\"+ssfile);
                saveImageJPG(f, image);
	        }
	     }
	     catch (Exception e) {e.printStackTrace();}
    }
    
    public BufferedImage getPitImage()
    {
       BufferedImage image=null;
       try
       {
	       Graphics2D gg = null;
		   image = new BufferedImage( width,height-8,BufferedImage.TYPE_INT_RGB);
		  // gg=GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(image);
		   gg=(Graphics2D) image.getGraphics();
		   gg.translate(0,-36);
	   	   doPaint(gg);
	   	   gg.dispose();
	   	   gg.finalize();
	   	   
       }
       catch(Exception e)
       {
           System.out.println(e.toString());
       }
       return image;
    }
    
    void saveImageJPG(File f, BufferedImage image)
    {
    	try
    	{
            ImageIO.write(image, "jpg", f);
    		//FileOutputStream fout = new FileOutputStream(f);
////	       	JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(fout);
	///		en.encode(image);
	    //    fout.flush();
	    //    fout.close();
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
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
    
    void writePitToFile(avscience.ppc.PitObs pit)
    {
                avscience.ppc.User u = pit.getUser();
    	
		StringBuffer buffer = new StringBuffer();
		avscience.ppc.Location loc = pit.getLocation();
		buffer.append(pit.getDateString()+ "\n");
		buffer.append("Observer ,"+u.getFirst()+ " "+ u.getLast()+ "\n");
		buffer.append("Location ,"+loc.getName()+ "\n");
		buffer.append("Mtn Range ,"+loc.getRange()+"\n");
		buffer.append("State/Prov ,"+loc.getState()+"\n");
		buffer.append("Elevation "+u.getElvUnits()+" ,"+loc.getElv()+"\n");
		buffer.append("Lat. ,"+loc.getLat()+"\n");
		buffer.append("Long. ,"+loc.getLongitude()+"\n");
		
		java.util.Hashtable labels = getPitLabels();
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
			avscience.ppc.Layer layer = (avscience.ppc.Layer) l.nextElement();
			buffer.append(layer.getStartDepth()+", "+layer.getEndDepth()+", "+layer.getHardness1()+", "+layer.getHardness2()+", "+layer.getGrainType1()+", "+layer.getGrainType2()+", "+layer.getGrainSize1()+", "+layer.getGrainSize2()+", "+layer.getGrainSizeUnits1()+", "+layer.getGrainSizeUnits2()+", "+layer.getDensity1()+", "+layer.getDensity2()+", "+layer.getWaterContent()+"\n");
                        
		}
                avscience.ppc.Layer cl = pit.getCriticalLayer();
                if (cl!=null)
                {
                    buffer.append("Critical Layer:  ");
                    buffer.append("\n");
                    buffer.append(cl.getStartDepth()+", "+cl.getEndDepth()+", "+cl.getHardness1()+", "+cl.getHardness2()+", "+cl.getGrainType1()+", "+cl.getGrainType2()+", "+cl.getGrainSize1()+", "+cl.getGrainSize2()+", "+cl.getGrainSizeUnits1()+", "+cl.getGrainSizeUnits2()+", "+cl.getDensity1()+", "+cl.getDensity2()+", "+cl.getWaterContent()+"\n");
                    //buffer.append("Critical interface depth: "+ pit.iDepth);
                    //buffer.append("\n");
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
			ShearTestResult result = (ShearTestResult) tests.nextElement();
			buffer.append(result.getCode()+", "+result.getScore()+", "+result.getQuality()+", "+result.getDepth()+"\n");
		}
		
		buffer.append("\n");
		buffer.append("Temperature Data:, Temp Units:, "+u.getTempUnits()+"\n");
		buffer.append("Depth, Temperature \n");
		Enumeration dpths=null;
		if ( pit.hasTempProfile())
		{
			TempProfile tp = pit.getTempProfile();
			
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
		/////
		buffer.append("\n");
		buffer.append("Density Data:, Density Units:, "+u.getRhoUnits()+"\n");
		buffer.append("Depth, Density \n");
		DensityProfile dp = pit.getDensityProfile();
		
		if ( dp!=null )
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
		////////
	   try 
       {
            FileDialog dialog = new FileDialog(this, "Save Pit File", FileDialog.SAVE);
          ///  dialog.setFilenameFilter(new SPTextFileFilter());
          //	dialog.setDirectory("/Desktop");
            dialog.setMode(FileDialog.SAVE);
           // dialog.setFile(pit.getLocation().getName().trim()+"-"+pit.getDate()+"-"+pit.getTime()+".txt");
           	dialog.setFile(pit.getLocation().getName().trim()+".txt");
            //dialog.setFile("test.txt.txt");
            dialog.setVisible(true);
            
            
            if( ( dialog.getFile()!=null) && (dialog.getFile().trim().length()>0))
            {
                File file = new File(dialog.getDirectory()+"\\"+dialog.getFile());
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
        catch (Exception ex) {ex.printStackTrace();}
	}
    
    public void removePit(avscience.ppc.PitObs pit)
    {
    	if ( webEdit ) removeWebPit(pit);
    	else
    	{
    		if (mf!=null) mf.store.removePit(pit.getSerial());
    	}
    }
	
	public void updatePit(avscience.ppc.PitObs pit)
    {
    	this.pit=pit;
    	updatePit();
    }
    
    public void removeWebPit(avscience.ppc.PitObs pit)
    {
    	System.out.println("removeWebPit()");
    	System.out.println("serial: "+dbserial);
    	try
        {
            URL url = new URL(mf.pitserver);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            props.put("TYPE", "DELETEDBPIT");
            props.put("DBSERIAL", dbserial);
            
            msg.sendGetMessage(props);
        }
        catch(Exception e){System.out.println(e.toString());}
    }
    
    public void updateWebPit(avscience.ppc.PitObs pit)
    {
    	try
        {
            URL url = new URL(mf.server);
            HttpMessage msg = new HttpMessage(url);
            Properties props = new Properties();
            
            System.out.println("Pit name: "+pit.getName());
           // System.out.println("Pit dat: "+pit.dataString());
            String s = pit.toXML();
            
            int dsize=s.length();
            System.out.println("Data size: "+dsize);
            if ( dsize > 2*maxDataLength )
            {
            	System.out.println("Dsize:: "+dsize);
            	String s1=s.substring(0, maxDataLength);
            	String s2=s.substring(maxDataLength, 2*maxDataLength);
            	String s3=s.substring(2*maxDataLength, s.length());
            	s1 = URLEncoder.encode(s1);
            	s2 = URLEncoder.encode(s2);
            	s3 = URLEncoder.encode(s3);
            	HttpMessage msg1 = new HttpMessage(url);
            	Properties props1 = new Properties();
            	props1.put("format", "bigpit1");
            	HttpMessage msg2 = new HttpMessage(url);
            	Properties props2 = new Properties();
            	props2.put("format", "bigpit2");
            	props1.put("PITDATA1", s1);
            	props2.put("PITDATA2", s2);
            	HttpMessage msg3 = new HttpMessage(url);
            	Properties props3 = new Properties();
            	props3.put("format", "bigpit3");
            	props3.put("PITDATA3", s3);
            	msg1.sendGetMessage(props1);
            	Thread.sleep(1200);
            	msg2.sendGetMessage(props2);
            	Thread.sleep(1200);
            	msg3.sendGetMessage(props3);
            //	if ((msg1!=null ) && (msg2!=null) && (msg3!=null)) sent = true;
            }
          
            else if ( dsize > maxDataLength )
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
            	props.put("format", "pitsend");
                props.put("PITDATA", s);
                msg.sendGetMessage(props);
        	}
            
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
        if (applet!=null) applet.reinit();	
    }
    
    public void updatePit()
    {
    	if ( webEdit ) updateWebPit(pit);
    	else
    	{
    		if (mf!=null) mf.store.addPit(pit);
    	}
    	if ( oframe!=null ) oframe.pit = pit;
    	if ( ooframe!=null ) ooframe.pit = pit;
    	if (iCanvas.tframe!=null) iCanvas.tframe.dispose();
        iCanvas.update(pit);
        iCanvas.repaint();
        canvas.reinit(pit);
        canvas.repaint();
        repaint();
    }
    
    public void print()
    {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("SnowPilot"+pit.getName());
        Paper paper = new Paper();
        paper.setSize(612, 792);
        paper.setImageableArea(42, 6, 612, 780);
        PageFormat format = new PageFormat();
        format.setPaper(paper);
        
        try
        {
            if(job.printDialog())
            {
                format = job.pageDialog(format);
                job.setPrintable(this, format);
                job.print(); 
                if (iCanvas.longNotes)
                {
                	job.setPrintable(iCanvas.tframe, format);
                	job.print(); 
                }
             }
                
        }
        catch(Exception e){System.out.println(e.toString());}
     	reinit();
   }
    
    void reinit()
    {
    	p.remove(canvas);
    	if (iCanvas.tframe!=null) iCanvas.tframe.dispose();
    	///iCanvas.dispose();
    	p.remove(iCanvas);
    	remove(p);
    	remove(sp);
    	sp.remove(p);
    	canvas=null;
    	iCanvas=null;
    	sp=null;
    	initFrame();
    }
    
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException 
    {
        
        if (pi >= 1) return Printable.NO_SUCH_PAGE;
        
        Graphics2D g2 = (Graphics2D) g;
        double xscale = .55;
        g2.translate(pf.getImageableX(), pf.getImageableY());
        if (pf.getOrientation() == PageFormat.LANDSCAPE)
        {
           // g2.scale(0.74, 0.78);
           g2.scale(0.74, 0.68);
           g2.translate(0, -42);
        }
        
        else
        {
            canvas.scaleForPrint();
            g2.scale(xscale, 0.84);
        }
        
       // if ( mf.macos ) doPaint(g2);
     //   else paintAll(g2);
      	doPaint(g2);
        
        return Printable.PAGE_EXISTS;
    }
    
	public avscience.ppc.PitObs getPit()
	{
	   return pit;
    }
    
    public void setPit(avscience.ppc.PitObs pit)
    {
        this.pit=pit;
    }
        
	private void buildMenu()
    {
    	if (!forDisplay || webEdit )
    	{
        	deleteMenuItem.setLabel("delete pit");
        	editMenuItem.setLabel("edit pit header");
        	fullEditMenuItem.setLabel("edit pit");
        }
        printMenuItem.setLabel("print pit");
        saveMenuItem.setLabel("Save pit image");
        saveFileMenuItem.setLabel("Save pit as text file");
        saveAsXMLMenuItem.setLabel("Save pit as SnowPilot XML");
        saveAsCAAMLMenuItem.setLabel("Save pit as CAAML");
    	menu.setLabel("Pit..");
    	if (!forDisplay || webEdit )
    	{
    		if (!pit.getCrownObs()) menu.add(deleteMenuItem);
        	menu.add(editMenuItem);
        	menu.add(fullEditMenuItem);
        }
        menu.add(printMenuItem);
        menu.add(saveMenuItem);
        menu.add(saveFileMenuItem);
        menu.add(saveAsXMLMenuItem);
        menu.add(saveAsCAAMLMenuItem);
    	mainMenuBar.add(menu);
    	setMenuBar(mainMenuBar);
    	if (!forDisplay || webEdit )
    	{
    		deleteMenuItem.addActionListener(new MenuAction());
        	editMenuItem.addActionListener(new MenuAction());
        	fullEditMenuItem.addActionListener(new MenuAction());
        }
        printMenuItem.addActionListener(new MenuAction());
        saveMenuItem.addActionListener(new MenuAction());
        saveFileMenuItem.addActionListener(new MenuAction());
        saveAsXMLMenuItem.addActionListener(new MenuAction());
        saveAsCAAMLMenuItem.addActionListener(new MenuAction());
    }
    
    public void dispose()
    {
    	super.dispose();
    	if (iCanvas.tframe!=null) iCanvas.tframe.dispose();
    }
	
	class SymWindow extends java.awt.event.WindowAdapter
	{	
		public void windowClosing(java.awt.event.WindowEvent event)
		{
			Object object = event.getSource();
			if (object == PitFrame.this) PitFrame.this.dispose();
		}
	}
	
	class MenuAction implements java.awt.event.ActionListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
            if (object == editMenuItem)  editPit();
            if (object == fullEditMenuItem)  fullEditPit();
		    if (object == deleteMenuItem) deletePit();
            if (object == printMenuItem)
            {
            	print();
            //	if (pit.getPitNotes().length() > 2*iCanvas.linebrk) iCanvas.tframe.print();
            ///	reinit();
            }
            if (object == saveMenuItem) saveImage();
            if (object == saveFileMenuItem)
            {
            	 writePitToFile(pit);
            }
            if (object == saveAsXMLMenuItem)
            {
            	saveAsXML();
            }
            if (object == saveAsCAAMLMenuItem)
            {
            	saveAsCAAML();
            }
            
		}
	}
	
	void editPit()
	{
		if (mf!=null) mf.showPitSumFrame(this);
		System.out.println("editPit");
    }
    
    void fullEditPit()
    {
    	if (mf!=null) mf.showPitHeaderFrame(this);
    }
	
	void deletePit()
	{
		new avscience.pc.DPDialog(this).setVisible(true);
	}
	
	class SPDoc implements Pageable
	{
		public PageFormat getPageFormat(int idx)
		{
			Paper paper = new Paper();
        	paper.setSize(612, 792);
        	paper.setImageableArea(42, 6, 612, 780);
        	PageFormat format = new PageFormat();
        	format.setPaper(paper);
        	return format;
		}
		
		public Printable getPrintable(int idx)
		{
			if (idx==1) return iCanvas.tframe;
			else return PitFrame.this;
		}
		
		public int getNumberOfPages()
		{
			return 2;
		}
	}
	
}	
	