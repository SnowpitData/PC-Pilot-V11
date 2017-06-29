package avscience.desktop;

import java.util.*;
import java.awt.*;
import avscience.wba.*;
import avscience.ppc.*;
import avscience.pc.Sorter;

public class PitCanvas extends Canvas
{
    private int base = 72;
    private int base_inc = 20;
    private double base_incd = 20;
    private int height = 180;
    
    private final int inset = 14;
    private final int vspace = 18;
    private final int ticLength = 6;
    private final int smallTicLength = 3;
    private int formWidth = 64;
    private int sizeWidth = 92;
    private int testColWidth = 220;
    private int rhoColWidth = 40;
    private int crystalWidth;
    private int crystalColStart;
    private int yaxiLength;
    private int xaxiLength;
    private int depthScaleCen;
    private Graphics g;
    
    private int depthIncr = 50;

    private int[] ytics;
    private int[] xtics;
    private double maxDepth = 1;
    private int minDepth = 0;
 
    private int pCount = 0;
    private TempProfile tp;
 
    private Vector depths;
    private Vector temps;
   
    private avscience.ppc.PitObs pit;
    private boolean invert = false;
   
    private int tempScaleCen = 1;
    
    private int minTemp;
    private int maxTemp;
    private int tempRange;
    private int tempIncr = 10;
    boolean started = false;
    private GrainTypeSymbols symbols;
    private int size;
    private int lastTestDepth=0;
    private int[] lstarts;
    private int[] lends;
    private int[] olstarts;
    private int[] olends;
    java.util.Vector lays;
    private final static int baseY=31;
    private int minThick=24;
    boolean[] thin;
    boolean printing=false;
    Hashtable rhoProfile;
    private int miny;
    private int maxy;
   	boolean forPrint = true;
    public int linebrk=154;
    boolean escale;
    int bld;
    public PitCanvas(avscience.ppc.PitObs ppit, int base, int height, boolean escale, int bld)
    {
    	this.pit=ppit;
    	this.escale=escale;
        this.base = base;
        this.bld=bld;
  		this.height =(int)( height - 3.5*inset);	
  		init();
  		
    }
    
    private int getWhitespaceLineBreak(String notes)
    {
    ///	String notes=pit.getPitNotes();
    	try
    	{
    		if ( notes.length()<linebrk) return linebrk;
	    	int brk=linebrk;
	    	for ( brk=linebrk+3; brk>linebrk-12; brk--)
	    	{
	    		if ( notes.length()>=brk)
	    		{
	    			char c=notes.charAt(brk);
	    			if (Character.isWhitespace(c)) return brk;
	    		}
	    	}
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.toString());
    	}
    	return linebrk;
    }
    
    public void reinit(avscience.ppc.PitObs pit)
    {
    	System.out.println("reinit()");
    	this.pit=pit;
    	init();
    	repaint();
    }
    
    void init()
    {
    	System.out.println("PitCanvas:init()");
    	maxDepth = getMaxDepth();
        size =  ( (int)(maxDepth + 1)/depthIncr)+1;
        System.out.println("Max Depth: "+maxDepth);
        if ( maxDepth >= 1000 ) depthIncr=100;
        if ( maxDepth >= 2000 ) depthIncr=200;
  		yaxiLength = this.height - 2*inset;
  		base_inc = base/40;
        xaxiLength = 20*base_inc - 33;
       
       	System.out.println("computing size.");
  		if ( maxDepth > 0 ) depthScaleCen = (int) (100*yaxiLength)/((int)maxDepth);
  		this.setSize(xaxiLength + (base/2) + inset, height+68);
  		if ( pit.getMeasureFrom()==null ) invert = true;
  		else if (!( pit.getMeasureFrom().equals("top") )) invert = true;
  		crystalWidth = formWidth + sizeWidth;
  		crystalColStart = xaxiLength + 5*inset-6;
        ytics = new int[size];
        int depth=0;
        for (int i=0; i<size; i++ )
        {
            ytics[i] = (int)((depthScaleCen*depth)/100) + inset + vspace;
            depth+=depthIncr;
        } 
        if ( invert ) ytics = invertYpoints(ytics);
        miny=inset + vspace;
        maxy=height - inset + vspace;
        System.out.println("call compLayers()");
        compLayers();
        System.out.println("get CombRhoProfile()");
        rhoProfile = getDensityPlots();
        System.out.println("init complete");
    }
    
    boolean checkBounds(int ypoint)
    {
    	if (ypoint==miny) return false;
    	if (ypoint==baseY) return false;
    	if (ypoint==maxy) return false;
    	return true;
    }
    
    public Dimension getPreferredSize()
    {
    	return new Dimension(getWidth(), getHeight());
    }
    public void invertLayerPoints()
    {
    	int size = lstarts.length;
    	int end=size-1;
    	int[] sTemp=new int[size];
    	int[] eTemp=new int[size];
    	for ( int i =0; i<size; i++)
    	{
    		eTemp[end]=lends[i];
    		sTemp[end]=lstarts[i];
    		end--;
    	}
    	for (int i=0; i<size; i++)
    	{
    		lstarts[i]=sTemp[i];
    		lends[i]=eTemp[i];
    	}
    }
    public void scaleForPrint()
    {
        printing=true;
    }
    
    public void unscale()
    {
         printing=false;
    }
    
    public void doPaint(Graphics2D g)
    {
    	onPaint(g);
    	//g.translate(0, height);
    	///g.drawString("This is a test test test test gtest test test..", 50, height-20);
    }
    
    public void onPaint(Graphics g)
    {
    	System.out.println("onPaint()");
        this.g = g;
        paint(g);
    }
   
   void drawInfo()
   {
        avscience.ppc.Location loc = pit.getLocation();
        String s = "Location: " + loc.getName() + " State-Prov: " + loc.getState() + ", Range: " + loc.getRange() + " Lat: " + loc.getLatType() + " " + loc.getLat() +  " Long: " + loc.getLongType() + " " + loc.getLongitude() + ", User: " + pit.getUser().getName()+" Date: "+pit.getDateString();
        g.drawString(s, 30, height - inset + vspace + 16);
        s = "Aspect: " + pit.getAspect() + ", Slope Angle: " + pit.getIncline() + ", Precip. " + pit.getPrecip() + ", Sky Cover: " + pit.getSky() + ", WindSpeed: " + pit.getWindspeed() + ", Wind Loading: " + pit.getWindLoading() ;
        g.drawString(s, 30, height - inset + vspace + 34);
        s = "Penetration: " + pit.getSkiBoot() + " (" + pit.getUser().getDepthUnits() + ") " + pit.getSurfacePen() + ", Stability on simular slopes: " + pit.getStability() + ", Air Temp: (" + pit.getUser().getTempUnits() + ") " + pit.getAirTemp();
        g.drawString(s, 30, height - inset + vspace + 52);
        g.drawString("Notes: "+pit.getPitNotes(), 30, height - inset + vspace + 70);
        if ( !pit.getCrownObs())
        {
            StringBuffer buffer = new StringBuffer();
            java.util.Enumeration e = pit.getActivities().elements();
            while ( e.hasMoreElements() )
            {
                buffer.append( (String) e.nextElement());
                buffer.append("  ");
            }
            s = "Activities: " + buffer.toString();
            g.drawString(s, 30, height - inset + vspace + 88);
            
        }
       
   }
    
    private int[] invertYpoints(int[] ypoints)
    {
    	int length = ypoints.length;
    	int[] yi = new int[length];
    	int i = 0;
    	for ( i = 0; i < length; i++ )
    	{
    		yi[i] = invertYpoint(ypoints[i]);
    	}
    	return yi;
    }
    
    private boolean TestsAreSame(avscience.ppc.ShearTestResult t1, avscience.ppc.ShearTestResult t2)
    {
    	boolean same = true;
    	if (!t1.getScore().equals(t2.getScore())) same=false;
    	if (!t1.getQuality().equals(t2.getQuality())) same=false;
    	if (!t1.getDepth().equals(t2.getDepth())) same=false;
    	if ( t1.getCode().equals("CT") && t2.getCode().equals("CT"))
    	{
            if (!t1.getCTScore().equals(t2.getCTScore())) same=false;
    	}
        if ( t1.getCode().equals("DT") && t2.getCode().equals("DT"))
    	{
            if (!t1.getDTScore().equals(t2.getDTScore())) same=false;
    	}
    	if ( t1.getCode().equals("RB") && t2.getCode().equals("RB"))
    	{
            if (!t1.getReleaseType().equals(t2.getReleaseType())) same=false;
    	}
    	if ( t1.getCode().equals("PST") && t2.getCode().equals("PST"))
    	{
            if (!t1.lengthOfCut.equals(t2.lengthOfCut)) return false;
            if (!t1.lengthOfColumn.equals(t2.lengthOfColumn)) return false;
    	}
    	
    	if ( t1.getCode().equals("EC") && t2.getCode().equals("EC"))
    	{
            if (!t1.getECScore().equals(t2.getECScore())) same=false;
            if (!t1.numberOfTaps.equals(t2.numberOfTaps)) same=false;
    	}
    	return same;
    }
    
    private int invertYpoint(int point)
    {
    	return 2*vspace + 2*inset + yaxiLength - point;
    }
    
     private void buildTics()
    {
        int i = 0;
        int intY = 0;
        int intX = 0;
        int largeTic = 20;
        String s="";
        for ( i = 0; i < size; i++ )
        {
            if ( i*depthIncr < (maxDepth) )
            {
                g.drawLine(xaxiLength - ticLength, ytics[i], xaxiLength,  ytics[i]);
                g.drawLine(inset, ytics[i], inset + ticLength,  ytics[i]);
                g.drawLine(crystalColStart, ytics[i], xaxiLength + 5*inset - 6 + ticLength,  ytics[i]);
                g.drawLine(crystalColStart + crystalWidth, ytics[i], xaxiLength + 5*inset - 6 + crystalWidth - ticLength,  ytics[i]);
                if ( i < (size - 1) ) 
                {
                	intY = (ytics[i] + ytics[i + 1])/2;
                	if (intY > (inset+vspace))
                	{
                		g.drawLine(xaxiLength - smallTicLength, intY, xaxiLength, intY);
                		g.drawLine(inset, intY, inset + smallTicLength,  intY);
                		g.drawLine(crystalColStart, intY, xaxiLength + 5*inset - 6 + smallTicLength,  intY);
                		g.drawLine(crystalColStart + crystalWidth, intY, xaxiLength + 5*inset - 6 + crystalWidth - smallTicLength,  intY);
               
                	}
                } 
                s = (new Integer(i*depthIncr/10)).toString();
                if (!(i==(size-1)))g.drawString(s, xaxiLength + 14,  ytics[i] + 6);
                else 
                {
                	if ( ((maxDepth/10)-(size-1)*depthIncr)>2) g.drawString(s, xaxiLength + 14,  ytics[i] + 6);
                }
            }
        }
        int ii = (int)(maxDepth/10);
        int ld = new Integer(s).intValue();
    	String md = ii+"";
    	
    	int lastDepth = (int)((depthScaleCen*maxDepth)/100) + inset + vspace;
    	if (invert) lastDepth = invertYpoint(lastDepth);
	    //if (invert) g.drawString(md,  xaxiLength + 14, inset+vspace+6);
	    //int df = lastDepth-(ytics[i] + 6);
	   // System.out.println("DIFF: "+df);
	    int dff=ii-ld;
	    System.out.println("ii "+ii);
	    System.out.println("ld "+ld);
	    System.out.println("diff: "+dff);
	    if (!md.equals("6"))
	    {
	    	if (invert) 
	    	{
	    	//int dff=ii-ld;
	    	//System.out.println("diff: "+dff);
	    //	if ( ((ii-ld)>1) || (ii-ld)==0 ) g.drawString(md,  xaxiLength + 14, lastDepth+2);
	    		g.drawString(md,  xaxiLength + 14, lastDepth+2);
	    	
	    	//if ( (lastDepth-(ytics[i-1] + 6)) > 6) g.drawString(md,  xaxiLength + 14, lastDepth);
	    	}
	    	else g.drawString(md,  xaxiLength + 14, lastDepth+12);
	    }
	   
        int  hsize = 6;
        xtics = new int[hsize];
        xtics[0] = 2*base_inc;
        xtics[1] = 5*base_inc;
        xtics[2] = 8*base_inc;
        xtics[3] = 11*base_inc;
        xtics[4] = 14*base_inc;
        xtics[5] = 17*base_inc;
        
        if ( escale )
        {
        	xtics[0] = (int)(0.5*base_inc);
	        xtics[1] = 2*base_inc;
	        xtics[2] = 4*base_inc;
	        xtics[3] = 8*base_inc;
	        xtics[4] = 14*base_inc;
	        xtics[5] = 16*base_inc;
        }
        
        Hardness h = Hardness.getInstance();
        String[] codes = h.getCodes();
       
        for ( i = 0; i < hsize; i++ )
        {
        	g.setColor(java.awt.Color.gray);
            g.drawLine(xaxiLength - xtics[i], inset + vspace , xaxiLength - xtics[i], largeTic + inset + vspace);
            g.drawLine(xaxiLength - xtics[i], inset + vspace+yaxiLength , xaxiLength - xtics[i], yaxiLength-largeTic + inset + vspace);
            g.setColor(java.awt.Color.black);
           	if ( i < (hsize - 1) ) 
            {
            	intX = (xtics[i] + xtics[i+1])/2;
                g.drawLine(xaxiLength - intX, height - inset + vspace , xaxiLength - intX,  height - inset - ticLength + vspace );
                g.drawLine(xaxiLength - intX, inset + vspace , xaxiLength - intX,  inset + ticLength + vspace );
                
            }
           	
            s = codes[i+1];
            g.drawString(s, xaxiLength - xtics[i], inset + vspace+yaxiLength+12 );
        }
    }
    
    private void drawHardness(avscience.ppc.Layer layer)
    {
    	System.out.println("drawHardness()");
    	String multiple = layer.getMultipleHardness();
    	if ( multiple==null ) multiple="";
        boolean mult = false;
        if ( multiple.equals("true") ) mult = true;
       // System.out.println("mult: "+mult);
        double length1 = 0;
        double length2 = 0;
        int start = layer.getStartDepthInt();
     //   System.out.println("start: "+start);
        int end = layer.getEndDepthInt();
     //   System.out.println("end: "+end);
        start = (depthScaleCen*start/100) + inset + vspace;
        end = (depthScaleCen*end/100) + inset + vspace;
        if ( invert )
        {
        	start = invertYpoint(start);
        	end = invertYpoint(end);
        }
        
        int mid = (start + end)/2;
      ///	System.out.println("mid: "+mid);
      	if ( layer==null )System.out.println("Layer is null.");
      	///else System.out.println(layer.dataString());
        if ( mult )
        {
            length1 = base_inc*(getLength(layer.getHardnessTop(), layer.getHSuffixTop()));
            length2 = base_inc*(getLength(layer.getHardnessBottom(), layer.getHSuffixBottom()));
        }
        else
        {
            length1 = base_inc*(getLength(layer.getHardness1(), layer.getHSuffix1()));
            length2 = length1;
        }
        int l1 = (int) length1;
        int l2 = (int) length2;
        g.setColor(java.awt.Color.blue);
        g.drawLine(xaxiLength, start, xaxiLength - l1, start);
        g.drawLine(xaxiLength, end, xaxiLength - l2, end);
        g.setColor(java.awt.Color.blue);
        g.drawLine(xaxiLength - l1, start, xaxiLength - l2, end);
        g.setColor(java.awt.Color.black);
        System.out.println("end DrawHardness");
    
    }
    
    
    private void drawLayerPolygon(avscience.ppc.Layer l)
    {
        int[] xpts = new int[4];
        int[] ypts = new int[4];
        double length1=0;
        double length2=0;
        String multiple = l.getMultipleHardness();
        boolean mult = false;
        if ( multiple.equals("true") ) mult = true;
        int start = l.getStartDepthInt();
        int end = l.getEndDepthInt();
        
        start = (depthScaleCen*start/100) + inset + vspace;
        end = (depthScaleCen*end/100) + inset + vspace;
        
        if ( invert )
        {
            end = invertYpoint(end);
            start = invertYpoint(start);
        }
        if ( mult )
        {
            length1 = base_inc*(getLength(l.getHardnessTop(), l.getHSuffixTop()));
            length2 = base_inc*(getLength(l.getHardnessBottom(), l.getHSuffixBottom()));
        }
        else
        {
            length1 = base_inc*(getLength(l.getHardness1(), l.getHSuffix1()));
            length2 = length1;
        }
        
        xpts[0]= xaxiLength;
        xpts[1]= (int)(xaxiLength-length1);
        xpts[2]= (int)(xaxiLength-length2);
        xpts[3]= xaxiLength;
        
        ypts[0]=start;
        ypts[1]=start;
        ypts[2]=end;
        ypts[3]=end;
        java.awt.Color myBlue = new java.awt.Color(0f, 0f, 0.6f, 0.4f);
        g.setColor(myBlue);
        g.fillPolygon(xpts, ypts, 4);
        g.setColor(java.awt.Color.black);
    }
    
    void drawCriticalInterface()
    {
    	avscience.ppc.Layer l = pit.getCriticalLayer();
    	if ( l!=null )
    	{
    		String multiple = l.getMultipleHardness();
	        boolean mult = false;
	        if ( multiple.equals("true") ) mult = true;
	        
	        int start = l.getStartDepthInt();
	        int end = l.getEndDepthInt();
	        start = (depthScaleCen*start/100) + inset + vspace;
	        end = (depthScaleCen*end/100) + inset + vspace;
	        if ( invert )
	        {
	        	start = invertYpoint(start);
	        	end = invertYpoint(end);
	        }
	        
			boolean iStart = false;
	       	if ( l.getStartDepthString().trim().equals(pit.iDepth.trim())) iStart = true;
	       	int iDepth=0;
	       	if (iStart) iDepth=start;
	       	else iDepth=end;
	       	
	       	int length1=0;
	     	if ( mult)
	     	{
	     		if (invert)
	     		{
	     			if (iStart) length1 = (int)(base_inc*(getLength(l.getHardness1(), l.getHSuffix1())));
	     			else length1 = (int)(base_inc*(getLength(l.getHardness2(), l.getHSuffix2())));
	     		}
	     		else
	     		{
	     			if (iStart) length1 = (int)(base_inc*(getLength(l.getHardness2(), l.getHSuffix2())));
	     			else length1 = (int)(base_inc*(getLength(l.getHardness1(), l.getHSuffix1())));
	     		}
	     	}
	        else length1 = (int)(base_inc*(getLength(l.getHardness1(), l.getHSuffix1())));
	        g.setColor(java.awt.Color.red);
	     	g.drawLine(xaxiLength, iDepth, xaxiLength - length1, iDepth);
	     	g.drawLine(xaxiLength, iDepth+1, xaxiLength - length1, iDepth+1);
	     	g.setColor(java.awt.Color.black);
    	}
    }
    
    private double getExponentiaLength(String hardness, String suffix)
    {
    	if ( hardness==null) hardness="";
    	if ( suffix == null) suffix ="";
        hardness = hardness.trim();
        suffix = suffix.trim();
        double l = 0;
        
        if (suffix.length()<1)
        {
        	if ( hardness.equals("F") ) l = .5;
	        if ( hardness.equals("4F") ) l = 2;
	        if ( hardness.equals("1F") ) l = 4;
	        if ( hardness.equals("P") ) l = 8;
	        if ( hardness.equals("K") ) l = 14;
	        if ( hardness.equals("I") ) l = 16;
        }
        
        else if ( suffix.equals("+")) 
        {
        	if ( hardness.equals("F") ) l = 1;
	        if ( hardness.equals("4F") ) l = 3;
	        if ( hardness.equals("1F") ) l = 6;
	        if ( hardness.equals("P") ) l = 11;
	        if ( hardness.equals("K") ) l = 15;
	        if ( hardness.equals("I") ) l = 18;
        }
        else if ( suffix.equals("-"))
        {
        	if ( hardness.equals("F") ) l = 0.25;
	        if ( hardness.equals("4F") ) l = 1.2;
	        if ( hardness.equals("1F") ) l = 3.4;
	        if ( hardness.equals("P") ) l = 7;
	        if ( hardness.equals("K") ) l = 12;
	        if ( hardness.equals("I") ) l = 15;
        }
        return l;
    }
    
    private double getLength(String hardness, String suffix)
    {
    	if ( escale )
        {
        	return getExponentiaLength(hardness, suffix);
        }
    	if ( hardness==null) hardness="";
    	if ( suffix == null) suffix ="";
        hardness = hardness.trim();
        suffix = suffix.trim();
        double l = 0;
        
        if ( hardness.equals("F") ) l = 2;
        if ( hardness.equals("4F") ) l = 5;
        if ( hardness.equals("1F") ) l = 8;
        if ( hardness.equals("P") ) l = 11;
        if ( hardness.equals("K") ) l = 14;
        if ( hardness.equals("I") ) l = 17;
        
        if ( suffix.equals("+")) l+=1;
        if ( suffix.equals("-")) l-=1;
        return l;
    }
    
    public void repaint()
    {
    	System.out.println("repaint()");
        super.repaint();
       
    }
    
    public void compLayers()
    {
    	System.out.println("compLayers()");
    	if (!pit.hasLayers()) return;
    	pit.orderLayers();
    	lays = new java.util.Vector();
    	java.util.Enumeration e = pit.getLayers();
        while ( e.hasMoreElements() )
        {
        	avscience.ppc.Layer L = (avscience.ppc.Layer) e.nextElement();
        	lays.add(L);
        }
        
        lays = sortAscendingLayers(lays);
        e = lays.elements();
        while ( e.hasMoreElements())
        {
        	avscience.ppc.Layer L = (avscience.ppc.Layer) e.nextElement();
        }
        int size = lays.size();
        lstarts = new int[size];
        lends = new int[size];
        olstarts = new int[size];
        olends = new int[size];
        
        e = lays.elements();
        int i=0;
        while ( e.hasMoreElements() )
        {
            int start=0;
            int end=0;
           	
            avscience.ppc.Layer l = (avscience.ppc.Layer) e.nextElement();
            int ss = l.getStartDepthInt();
            int ee = l.getEndDepthInt();
            
            if ( ss < ee )
            {
                start=ss;
                end=ee;
            }
            else if ( ss==ee )
            {
            	start=ss;
            	end=ee;
            }
           
            else
            {
                start=ee;
                end=ss;
            }
            start = (depthScaleCen*start/100) + inset + vspace;
            end = (depthScaleCen*end/100) + inset + vspace;
        
            lstarts[i] = start;
            lends[i]=end;
            olstarts[i] = start;
            olends[i]=end;
            i++;
        }
    	
    	 for ( i=0; i<size; i++ )
	     {
	     	int strt = lstarts[i];
	     	int end = lends[i];
	     	int thk = lends[i]-lstarts[i];
	     	if ( thk < 0 ) 
	     	{
	     		lstarts[i]=end;
	     		lends[i]=strt;
	     	}
	     	     	
	     }
	     
	     int[][] layerArray = new int[2][lstarts.length];
	     layerArray[0]=lstarts;
	     layerArray[1]=lends;
	     LayerPusher pusher = new LayerPusher();
	     
	     pusher.getPushedLayers(layerArray);
	     lstarts=layerArray[0];
	     lends=layerArray[1];
	     
	  //   }
	     
	   //  pushLayers();
	  //   pushUp();
 		 // if ( pit.getMeasureFrom().equals("top") ) 
 		//  {
 		  	
     		
 		 //  }
	     	
    	/*	boolean nothins=true;
    		int quit=0;
		    boolean done = false; 
		   
	        while (!done)
	        {
	        /*	for ( i = 0; i<lends.length; i++ )
	     		{
	     			int xtra=lends[i]-lstarts[i];
	     			if (xtra<0) xtra=-xtra;
	     			if (xtra<minThick) pushThinLayersUp(xtra, i);
	     		 }*/
	        /*	while ( pushLayers());
	       		for ( i=0; i<size; i++ )
			     {
			     	int strt = lstarts[i];
			     	int end = lends[i];
			     	int thk = lends[i]-lstarts[i];
			     	if ( thk < 0 ) 
			     	{
			     		lstarts[i]=end;
			     		lends[i]=strt;
			     	}
			    			     	
			     }
	        	nothins = true;
	        	for  ( i = 1; i<lstarts.length-1; i++ )
	      		{
	      			lends[i]=lstarts[i+1];
	      			int thk = lends[i]-lstarts[i];
	      			if ( thk < 0 ) thk=-thk;
	      			if (thk < minThick ) nothins=false;
	      		}
	      		
	      		if (nothins) done = true;
	      		quit++;
	      		if ( quit > 200 ) break;
	      	}
	      	
	      
	      	nothins=true;
	      	for  ( i = 0; i<lstarts.length; i++ )
	      	{
	      		int thk = lends[i]-lstarts[i];
	      		if ( thk < 0 ) thk=-thk;
	      		if (thk < minThick ) nothins=false;
	      		
	      	}
	      	
	      	if (!nothins) 
	      	{
		      	pushUp();
		      	for  ( i = 1; i<lstarts.length; i++ )
		      	{
	      			lstarts[i]=lends[i-1];
		      	}
		      	pushUp();
		      	for  ( i = 1; i<lstarts.length; i++ )
		      	{
	      			lstarts[i]=lends[i-1];
		      	}	
		      	
		     }
		     
		     pushUp();
		     for  ( i = 1; i<lstarts.length; i++ )
		     {
	      		lstarts[i]=lends[i-1];
		     }
			
	      	for ( int j =0; j<lstarts.length; j++ )
	      	{
	      		if (lends[j] < baseY) lends[j]=baseY;
			    if (lstarts[j] < baseY) lstarts[j]=baseY;
			}
			compressLayers();*/
    }
    
    int pushThinLayersUp(int xtra, int layer)
    {
        int over=xtra;
        int thck=0;
        int size = lends.length;
        if ( ( layer >= 0 )  && ( layer!= (lends.length-1)))
        {
        	int lt = lends[layer]-lstarts[layer];
        	int dn = minThick-lt;
        	lends[layer]=lstarts[layer]+minThick;
            for ( int i=layer+1; i<=size-1; i++ )
            {
               thck = lends[i]-lstarts[i];
               if ( thck < 0 ) thck=-thck;
               if ( thck <= minThick )
               {
               	   if (( (lends[i]+dn)>maxy) || ( (lstarts[i]+dn)>maxy)) return over;
               	  // System.out.println("case1");
	               lstarts[i]+=dn;
	               lends[i]+=dn;
	           }
	           else if ((thck-dn)>=minThick)
	           {
	           	//	System.out.println("case2");
	           		if ((lstarts[i]+dn)>maxy ) return over;
	           		lstarts[i]+=dn;
	           		return 0;
	           }
	           else
	           {
	           	//	System.out.println("case3");
	           		int df = thck-minThick;
	           		if (( (lends[i]+df)>maxy) || ( (lstarts[i]+dn)>maxy)) return over;
	           		lstarts[i]+=dn;
	           		lends[i]+=df;
	           		dn-=df;
	           }
	        }
        }
        return over;
    }

    public void paint(Graphics g)
    {
       	System.out.println("PitCanvas paint()");
        this.g = g;
        symbols = GrainTypeSymbols.getInstance(this.g); 
        ///if (printing) symbols.scaleForPrint();
       	try
       	{
	        pCount = 0;
	      
        	pCount++;
	        drawAxi();
	        buildTics();
	    
	        drawLayers();
	        drawCriticalInterface();
	        drawTests();
	        System.out.println("draw temp profile:: ");
	        if ( pit.getTempProfile()!=null)
	        {
		        if (( pit.hasTempProfile() && (pit.getTempProfile().getDepths().size()>0)))
		        {
		            drawTempAxi();
		            drawProfile();
		        }
		    }
		    else System.out.println("temp profile null.");
	        drawLabels();
	        
            connectLayers();
            drawDensity();
	        started = true;
	     }
	     catch(Exception e)
	     {
	       System.out.println(e.toString());
	     }
	     /////////////
	    Font f = g.getFont();
        Font bold=null;
      
   		bold = new Font(f.getName(), Font.BOLD, f.getSize());
	    int x=14;
	    int y=height+28;
        g.drawString("Notes:", x, y);
        g.setFont(bold);
        String notes = pit.getPitNotes();
        System.out.println("NOTES::: "+notes);
        if ( notes==null ) notes="";
        notes = notes.trim();
      //  notes = notes+"   bld: "+bld;
       /* if (pit.getPitNotes().length()>2*linebrk) 
        {
        	g.drawString("See attached notes.", x+42, y);
        	longNotes=true;
        	return;
        }*/
        if (notes.length()>linebrk)
        {
        	while (notes.length()>linebrk)
	        {
	        	int brk = getWhitespaceLineBreak(notes);
	        	String nt = "";
		        nt = notes.substring(0, brk);
		        g.drawString(nt, x+42, y);
		        y+=vspace;
		        if (notes.length()<=brk ) break;
		        notes = notes.substring(brk, notes.length());
	        }
	        g.drawString(notes, x+42, y);
        }
       else g.drawString(notes, x+42, y);     
    }
    
    String purgeTrailZero(String s)
    {
    	if ( ( s.indexOf(".")<0) || ( s.indexOf("0")<0)) return s;
    	s = s.trim();
    	int l = s.length();
    	String end="";
    	String beg="";
    	if (l>2)
    	{
    		end = s.substring(l-2, l);
    		beg = s.substring(0, l-2);
    		if ( end.equals(".0")) end = ".";
    	}
    	s=beg+end;
    	return s;
    }
    
    void drawDensity()
    {
    	Enumeration e = rhoProfile.keys();
    	while ( e.hasMoreElements())
    	{
    		Integer D = (Integer) e.nextElement();
    		String rho = (String) rhoProfile.get(D);
    		rho = purgeTrailZero(rho);
    		int d = D.intValue();
    	///	d=d*10;
    		g.drawString(rho, crystalColStart+crystalWidth+2, d+10);
    	}
    }
    
    void connectLayers()
    {
    	System.out.println("connectLayers()");
        int thk=0;
        if ((olstarts!=null) && (olends!=null))
        {
        	int size = olstarts.length;
	        for ( int i = 0; i < size; i++ )
	        {
	            int ostart = olstarts[i];
	            int oend = olends[i];
	            int start = lstarts[i];
	            int end = lends[i];
	            if ( i< (size-1 )) thk=lstarts[i+1]-lstarts[i];
	            else thk=lends[i]-lstarts[i];
	            if (thk<0) thk=-thk;
	            if ( invert )
	            {
	                ostart=invertYpoint(ostart);
	                oend = invertYpoint(oend);
	                start=invertYpoint(start);
	                end = invertYpoint(end);
	               
	            }
	            drawSymbol(start, thk, i);
	            g.drawLine(crystalColStart-2*inset,ostart , crystalColStart-2*inset+ticLength+smallTicLength,  ostart);
	          	g.drawLine(crystalColStart-2*inset, oend, crystalColStart-2*inset+ticLength+smallTicLength,  oend);
	           
	            int delt = start-ostart;
	            if (delt<0)delt=-delt;
	           // if ((i<1) && (delt>0));
	           	System.out.println("i: "+i +" delt:"+delt);
	            if (delt<400)g.drawLine(crystalColStart-2*inset+ticLength+smallTicLength,  ostart, crystalColStart, start);
	            
	            
	            //g.drawLine(crystalColStart-2*inset+ticLength+smallTicLength,  oend, crystalColStart, end);
	         }
	      }
    }
    
    boolean pushLayers()
    {
    	boolean pushed=false;
        int done =1;
        int size = lends.length;
        int thck=0;
        
        for ( int i=1; i<size; i++ )
        {
            thck = lends[i]-lstarts[i];
            if ( thck < 0 ) thck=-thck;
           
            if ( thck < minThick )
            {
            	pushed=true;
                int xtra=minThick-thck;
                done=pushLayersDown(xtra, i);
                
                 for ( i=0; i<size; i++ )
			     {
			     	int strt = lstarts[i];
			     	int end = lends[i];
			     	int thk = lends[i]-lstarts[i];}
            	 }   
        }
        return pushed;
    }
    
    boolean pushUp()
    {
    	boolean pushed=false;
        int done =1;
        int size = lends.length;
        int thck=0;
       	int quit = 0;
        for ( int i=0; i<size; i++ )
        {
            thck = lends[i]-lstarts[i];
            if ( thck < 0 ) thck=-thck;
            if ( thck < minThick )
            {
            	pushed=true;
                int xtra=minThick-thck;
                done=pushLayersUp(xtra, i);
            }   
        }
        return pushed;
    }
    
    int pushLayersDown(int xtra, int layer)
    {
      //  System.out.println("pushLayersDown: "+xtra+" "+layer);
        int over=xtra;
        int thck=0;
        int size = lends.length;
        lstarts[layer]=lends[layer]-minThick;
        int quit=0;
        if ( layer > 0 )
        {
        	while ( over > 0 )
        	{
	            for ( int i=layer-1; i>=0; i-- )
	            {
	               if ( i < lends.length )
	               {
	               		  // System.out.println("start i"+i+" lend "+lends[i]+" start "+lstarts[i]);
			               thck = lends[i]-lstarts[i];
			               if ( thck < 0 ) thck=-thck;
			               if ( (thck-over) >= minThick )
			               {
			               	  
			                  lends[i]=lstarts[layer];
			               //   System.out.println("Case: 1 end "+lends[i]+" = "+lstarts[layer]);
			                  return 0;
			               }
			               else if ( thck > minThick )
			               {
			               		if ( (lends[i]-(thck-minThick))<=baseY) return 0;
			                    lends[i]-=(thck-minThick);
			                    over-=(thck-minThick);
			                    if ( over<= 0 ) return 0;
			               }
			               else
			               {
			               		if ((lstarts[i]-over ) <=baseY) return 0;
			               		else
			               		{
			                    	lstarts[i]-=over;
			                    	lends[i]-=over;
			                    }
			               }
			               if (lends[i] < baseY) lends[i]=baseY;
			               if (lstarts[i] < baseY) lstarts[i]=baseY;
			              
			               for  ( int j = 0; j<lstarts.length-1; j++ )
				      		{
				      			lends[j]=lstarts[j+1];
				      			
				      		}
			              
			               quit++;
	         			   if (quit > 50 ) break;
		           }
	            }
	         }
	         
        }
        return over;
    }
    
    void compressLayers()
    {
    	for (int i=0; i<lends.length; i++ )
    	{
    		int thk = olends[i]-olstarts[i];
    		if (thk<0) thk=-thk;
    		if ( thk < minThick )
    		{
    			int tk = lends[i]-lstarts[i];
    			if ( tk<0 ) tk=-tk;
    			if ( tk > minThick ) 
    			{
    				lends[i]=lstarts[i]+minThick;
    			}
    		}
    	}
    }
    
    //////
    int pushLayersUp(int xtra, int layer)
    {
       // System.out.println("pushLayersUp: "+xtra+" "+layer);
        int over=xtra;
        int thck=0;
        int size = lends.length;
        
        if ( layer >= 0 )
        {
        	if (checkBounds(lends[layer])) lends[layer]=lstarts[layer]+minThick;
        	
            for ( int i=layer; i<size-1; i++ )
            {
               thck = lends[i]-lstarts[i];
               if ( thck < 0 ) thck=-thck;
               if ( (thck-over) > minThick )
               {
                  if (checkBounds(lstarts[i])) lstarts[i]=lends[layer];
                  return 0;
               }
               else if ( thck > minThick )
               {
               		if (checkBounds(lends[i]))
               		{
	                    lends[i]+=(thck-minThick);
	                    over-=(thck-minThick);
	                    if ( over<= 0 ) return 0;
	                }
                    
               }
               else
               {
                    if (checkBounds(lstarts[i]))lstarts[i]+=over;
                    if (checkBounds(lends[i]))lends[i]+=over;
               }
	        }
        }
        return over;
    }
    
    private void drawSymbol(int start, int thck, int layer)
    {
    	System.out.println("drawSymbol()");
		int mid=0;
        if (invert) mid = (2*start-thck)/2;
       	else mid=(2*start+thck)/2;
        avscience.ppc.Layer l = (avscience.ppc.Layer) lays.get(layer);
        if (l==null) 
        {
        	System.out.println("Layer null.");
        	return;
        }
        System.out.println("Layer: "+l.toString());
        System.out.println("User From Top:: "+ pit.getUser().getMeasureFrom());
        System.out.println("Layer From Top:: "+l.getFromTop());
    	g.drawLine(crystalColStart, start, crystalColStart + crystalWidth, start);
    	String gt1="";
    	String gt2="";
    	gt1=l.getGrainTypeTop();
    	gt2=l.getGrainTypeBottom();
    	if ( gt1==null ) gt1="";
    	if ( gt2==null ) gt2="";
    	System.out.println("Grain types: "+gt1+" "+gt2);
        symbols.drawSymbol(crystalColStart + ticLength+4, mid - (symbols.getHeight()/3)-2 , gt1);
        if (gt2.length()>1)
        {
        	Font f = g.getFont();
   			Font ff = new Font(f.getName(), Font.PLAIN, f.getSize()+6);
   			g.setFont(ff);
        	g.drawString("(", crystalColStart + 2*ticLength+symbols.getHeight()+2, mid+5);
        	symbols.drawSymbol(crystalColStart + 3*ticLength+symbols.getHeight(), mid - (symbols.getHeight()/3)-2 , gt2);
        	g.drawString(")", crystalColStart + 2*ticLength+symbols.getHeight()+28, mid+5);
        	g.setFont(f);
        }
       // String u = l.getGrainUnitsTop();
        String s = l.getGrainSizeTop();
        if (s.equals("50+")) s="50";
        
    //    if (u==null) u ="";
        if (s==null) s ="";
        String s1="";
        
       /* if ( u.equals("cm"))
        {
            Float F = new Float(s);
            float f = F.floatValue();
            f = f*10;
            F = new Float(f);
            s = F.toString();
        }*/
        String gs = l.getGrainSuffixTop();
        if ( gs==null) gs="";
        s =  gs + " " + s;
        String mgs = l.getMultipleGrainSize();
        if (mgs==null) mgs = "";
        if ( mgs.equals("true")) 
        {
         //   u = l.getGrainUnitsBottom();
            s1 = l.getGrainSizeBottom();
            if (s1.equals("50+")) s1="50";
          //  if (u==null) u ="";
        	if (s1==null) s1="";
         /*   if ( u.equals("cm"))
            {
                Float F = new Float(s1);
                float f = F.floatValue();
                f = f*10;
                F = new Float(f);
                s1 = F.toString();
            }*/
            String gs1 = l.getGrainSuffixBottom();
            if (gs1==null) gs1 ="";
            s1 = gs1 + " " + s1;
            s1=s1.trim();
            if ( s1.trim().length()>0 ) s = s + " - " + s1;
        }
        String wcd = l.getWaterContent();
        if ( wcd==null ) wcd="";
        System.out.println("wcd:: "+wcd);
        String wc="";
        wc=wc.trim();
        if ( wcd.length() > 0 )
        {
        	if (!wcd.equals("Dry"))
        	{
        		if ( wcd.trim().length()>0)wc = WaterContent.getInstance().getCode(wcd);
        		
        	}
        }
        s=s+" "+wc;
        System.out.println("wc:: "+s);
        g.drawString(s, crystalColStart + formWidth + 2, mid + 4);
        System.out.println("end drawSymbol.");
    }
   
    private void drawLabels()
    {
    //	System.out.println("drawLabels");
        String s = "";
        if ( pit.getTempProfile().getDepths().size()>0)
        {
    	    s = "Temp " + tp.getTempUnits();
            g.drawString(s, xaxiLength + 3, 28);
        }
     
        s = "Depth"; 
      
        s = "Form";
        g.drawString(s, crystalColStart + 4, inset + vspace - 2);
        s = "Size (mm)";
        g.drawString(s, crystalColStart + formWidth + 4, inset + vspace - 2);
        s = "Crystal";
        g.drawString(s, crystalColStart + 26, 14);
        s = "Stability Tests";
        g.drawString(s, crystalColStart + crystalWidth + rhoColWidth + 36,14);
        s="\u03C1";
        g.drawString(s, crystalColStart + crystalWidth+16,14);
        if (pit.getUser().getRhoUnits().trim().equals("lbs/cubic_ft")) s="lbs/ft"+"\u00B3";
        else s="kg/m"+"\u00B3";
        g.drawString(s, crystalColStart + crystalWidth+4, inset + vspace - 2);
    }
    
    Hashtable getDensityPlots()
    {
    	Hashtable plots = new Hashtable();
    	if (( pit.getDensityProfile() == null ) || (pit.getDensityProfile().getDepths()==null)) return plots;
    	Vector depths = pit.getDensityProfile().getDepths();
    	depths=sortAscending(depths);
    	Enumeration e = depths.elements();
    	while ( e.hasMoreElements())
    	{
    		Integer I = (Integer) e.nextElement();
    		String rho = pit.getDensityProfile().getDensity(I);
    		if ( rho.trim().length()>0)
    		{
                        Float rh = new Float(0.0);
                        try
                        {
                            rh = new Float(rho);
                        }
                        catch(Exception ex)
                        {
                            System.out.println(ex.toString());
                        }
	    		if ( rh.floatValue()>0.0f )
	    		{
		    		int d = I.intValue();
		            if ( d < 0 )  d = 0;
		            d = (depthScaleCen*d/10) + inset + vspace;
		            if ( invert ) d = invertYpoint(d);
		            
		            if ( checkRhoPoint(d, plots))
		            {
		            	Integer D = new Integer(d);
		            	plots.put(D, rho);
		            }
		         }
		     }
    	}
    	////////////////////
    	pit.orderLayers();
    	lays = new java.util.Vector();
    	java.util.Enumeration ee = pit.getLayers();
    	
        while ( ee.hasMoreElements() )
        {
        	avscience.ppc.Layer L = (avscience.ppc.Layer) ee.nextElement();
        	lays.add(L);
        }
        
        lays = sortAscendingLayers(lays);
        int i = 0;
    	java.util.Enumeration layers = lays.elements();
    	while ( layers.hasMoreElements() )
    	{
    		
    		avscience.ppc.Layer l = (avscience.ppc.Layer) layers.nextElement();
    	//	System.out.println("Mult Density? "+l.getMultipleDensity());
    		if ( l.getMultipleDensity().trim().equals("false"))
    		{
    			int d = (lstarts[i]+lends[i])/2;
    			if ( d < 0 )  d = 0;
            	if ( invert ) d = invertYpoint(d);
    			String rho = l.getDensity1();
    			if ( rho!=null)
    			{
	    			if ( rho.trim().length()>0)
	    			{
		    			Float rh = new Float(rho);
		    			if ( rh.floatValue()>0.0f )
		    			{
			    			if ( checkRhoPoint(d, plots))
			            	{
			            		Integer D = new Integer(d);
			            		plots.put(D, rho);
			            	}
			            }
			         }
			     }
    		}
    		else
        	{
        		int mid = (lstarts[i]+lends[i])/2;
        		int ds = (lends[i]+mid)/2;
        		if ( ds < 0 )  ds = 0;
            	if ( invert ) ds = invertYpoint(ds);
        		int de = (lstarts[i]+mid)/2;
        		if ( de < 0 )  de = 0;
            	if ( invert ) de = invertYpoint(de);
            	String rho=l.getDensity1();
            	if ( rho.trim().length()>0)
    			{
	            	Float rh = new Float(rho);
	    			if ( rh.floatValue()>0.0f )
	    			{
		        		if ( checkRhoPoint(ds, plots))
		            	{
		            		Integer D = new Integer(ds);
		            		plots.put(D, rho);
		            	}
		            }
		        }
	            rho=l.getDensity2();
	            if ( rho.trim().length()>0)
    			{
	            	Float rh = new Float(rho);
	    			if ( rh.floatValue()>0.0f )
	    			{
		            	if ( checkRhoPoint(de, plots))
		            	{
		            		Integer D = new Integer(de);
		            		plots.put(D, l.getDensity2());
		            	}
		            }
		        }
        		
        	}
        	i++;
    	}
    	
    	return plots;
    }
    
    boolean checkRhoPoint(int d, Hashtable profile)
    {
    	Enumeration dpths = profile.keys();
    	while ( dpths.hasMoreElements() )
    	{
    		Integer D = (Integer) dpths.nextElement();
    		int dpth=D.intValue();
    		int delt=d-dpth;
    		if ( delt<0 ) delt=-delt;
    		if ( delt < 14 ) return false;
    	}
    	
    	return true;
    }
    
    private void drawProfile()
    {
    	System.out.println("drawProfile(()");
        Vector depths = tp.getDepths();
        if ( depths.size()>0)
        {
            depths = sortAscending(depths);
            TempList tl = TempList.getInstance();
            int size = depths.size();
            int[][] profile = new int[2][size];
            int i = 0;
            for ( i = 0; i < size; i++ )
            {
                Integer I = (Integer) depths.elementAt(i);
                int d = I.intValue();
                if ( d < 0 )  d = 0;
                int t = tp.getTemp(I);
                d = (depthScaleCen*d/10) + inset + vspace;
                if ( invert ) d = invertYpoint(d);
                System.out.println("DEPTH SCALE CEN: "+depthScaleCen);
                System.out.println("TEMP SCALE CEN: "+tempScaleCen);
             	t = xaxiLength + (tempScaleCen*(t - maxTemp)/1000);
              
                profile[0][i] = t;
                profile[1][i] = d;
            }
            
            g.setColor(java.awt.Color.red);
            for ( i = 0; i < (size - 1); i++ )
            {
                g.drawLine(profile[0][i], profile[1][i], profile[0][i+1], profile[1][i+1]);
                g.fillOval(profile[0][i]-2, profile[1][i]-2, 4, 4);
                g.fillOval(profile[0][i+1]-2, profile[1][i+1]-2, 4, 4);
            }
            g.setColor(java.awt.Color.black);
            System.out.println("draw profile complete.");
        }
    }
    
    private void drawLayers()
    {
    	System.out.println("drawLayers()");
    	g.setColor(java.awt.Color.black);
      	if ( lays==null )
      	{
      		System.out.println("lays null.");
      		return;
      	}
        java.util.Enumeration e = lays.elements();
      
      	
        while ( e.hasMoreElements() )
        {
        	
    		avscience.ppc.Layer l = (avscience.ppc.Layer) e.nextElement();
        	drawHardness(l);
        	drawLayerPolygon(l);
        }
	     
    }
    
    private java.util.Vector combineTestResults(java.util.Enumeration tests)
    {
    	System.out.println("Combine Tests");
    	java.util.Vector results = new java.util.Vector();
    	java.util.Vector temp1 = new java.util.Vector();
    	java.util.Vector temp2 = new java.util.Vector();
    	avscience.ppc.ShearTestResult test=null;
    	
    	if (tests!=null)
    	{
            while (tests.hasMoreElements())
	    {
                test = (avscience.ppc.ShearTestResult) tests.nextElement();
	    	temp1.add(test);
	    	temp2.add(test);
	    }
	}
    	int count=0;
    	java.util.Enumeration e = temp1.elements();
    	int i=0;
    	
    	while ( e.hasMoreElements())
    	{
            avscience.ppc.ShearTestResult first = (avscience.ppc.ShearTestResult) e.nextElement();
            java.util.Enumeration ee = temp2.elements();
            count=0;
            while (ee.hasMoreElements())
            {
                test = (avscience.ppc.ShearTestResult) ee.nextElement();
    		if ( first != test ) if (TestsAreSame(first, test)) count++;
            }
            if ( count>0 )
            {
                first.setMult(count+1);
    		if (!containsTest(first, results)) results.add(first);
            }
            else results.add(first);
    	}
    	return results;
    }
    
    private boolean containsTest(avscience.ppc.ShearTestResult test, java.util.Vector v)
    {
    	System.out.println("contains test:");
    	String res = test.toString();
    	java.util.Enumeration e = v.elements();
    	while ( e.hasMoreElements())
    	{
    		avscience.ppc.ShearTestResult t = (avscience.ppc.ShearTestResult) e.nextElement();
    		if ( TestsAreSame(t, test) && ( t.getMult()==test.getMult())) return true;
    	}
    	return false;
    }
    
    private void drawTests()
    {
    	System.out.println("drawTests");
        Vector testDepths = new Vector();
        String s = null;
        java.util.Enumeration e = pit.getShearTests();
        java.util.Vector v = combineTestResults(e);
        v=sortAscendingTests(v);
        java.util.Enumeration ee = v.elements();
        int rendDepth;
        while ( ee.hasMoreElements() )
        {
            avscience.ppc.ShearTestResult test = (avscience.ppc.ShearTestResult) ee.nextElement();
            int depth = test.getDepthValueInt();
            String dpth = test.getDepth();
            if ( depth < 0 )  depth = 0;
            String score = test.getScore();
            if (score.equals("ECTP")) score = score+test.getECScore();
            if ( score.contains("EC") )if ( test.numberOfTaps.length()>0) score = score + " "+test.numberOfTaps;
            if ( score.contains("RB") )
            {
            	String rls = test.getReleaseType();
            	rls.trim();
            	if ( rls.length()>0 ) 
            	{
                    String rcode = RBReleaseTypes.getInstance().getCode(rls);
                    rcode.trim();
                    if (rcode.length()>1) rcode = "("+rcode+")";
                    score=score+" "+rcode;
            	}
            }
            String qual=test.getQuality();
            if ( test.fractureCat.equals("Fracture Character")) qual=test.character;
            if (test.isNoFail()) s = score;
            else s = score + " " + qual + " Depth: (" + test.getDepthUnits() + ") " + dpth;
            if (test.getCTScore().trim().length()>0) s = s+" CT Score: "+test.getCTScore();
            if (test.getDTScore().trim().length()>0) s = s+" DT Score: "+test.getDTScore();
           
            if ( test.code.equals("PST"))
            {
            	s = "PST"+test.lengthOfCut+"/"+test.lengthOfColumn+"("+test.getScore()+"),("+test.getDepthUnits()+")"+dpth;
            }
           
            if (test.getMult()>1) s=test.getMult()+"x "+s;
            rendDepth = (depthScaleCen*depth/100) + inset + vspace;
            if ( !invert ) rendDepth+=15;
            if ( invert ) rendDepth = invertYpoint(rendDepth);
            rendDepth = checkRendDepth(testDepths, rendDepth);
            int oRendDepth=rendDepth;
            java.lang.Integer I = new java.lang.Integer(rendDepth);
            int mid = (miny+maxy)/2;
            boolean top = false;
            int min = inset + vspace+12;
          
            while ( testDepths.contains(I) | (overWritesTest(testDepths, I)) )
            {
            	if (top)rendDepth-=15;
                else rendDepth+=15;
                if ( rendDepth>this.height) rendDepth = oRendDepth-=15;
                I = new java.lang.Integer(rendDepth);
            }
               
            testDepths.add(I);
            g.drawLine(crystalColStart + crystalWidth+rhoColWidth, rendDepth, crystalColStart + crystalWidth + testColWidth+rhoColWidth+inset, rendDepth);
            g.drawString(s, crystalColStart + crystalWidth+rhoColWidth + 2, rendDepth - 2);
        }
    }
    
    public boolean overWritesTest(Vector tests, Integer I)
    {
    	int i = I.intValue();
    	Enumeration e = tests.elements();
    	while ( e.hasMoreElements() )
    	{
    		Integer d = (Integer) e.nextElement();
    		int dpth = d.intValue();
    		int delt = i-d;
    		if (delt<0) delt=-delt;
    		if (delt<15) return true;
    	}
    	
    	return false;
    }
    
    private int checkRendDepth(Vector dpths, int dpth)
    {
    	int d = dpth;
    	boolean done=false;
    	Enumeration e = dpths.elements();
    	while ( e.hasMoreElements() )
    	{
    		Integer I = (Integer) e.nextElement();
    		int i = I.intValue();
    		int delt = i-d;
    		if ( delt < 0 ) delt=-delt;
    		if ( delt < 15 ) 
    		{
    			return i;
    		}
    	}
    	return d;
    }
    
    public int getMaxDepth()
    {
    	System.out.println("getMaxDepth");
        int max = 0;
        if ( pit==null )
        {
        	System.out.println("PIT IS NULL.");
        	return 60;
        }
      
      	System.out.println("max depth layers.");
        java.util.Enumeration e=null;
        if ( pit.hasLayers())
        {
	        e = pit.getLayers();
	        if ( e!=null )
	        {
		        while ( e.hasMoreElements() )
		        {
		            avscience.ppc.Layer l = (avscience.ppc.Layer) e.nextElement();
		            int end = l.getEndDepthInt();
		            if ( end > max ) max=end;
		            int start = l.getStartDepthInt();
		            if ( start > max ) max=start;
		        }
		     }
		 }
		 System.out.println("max depth tests.");
       if ( max==0 )
       {
	   		e = pit.getShearTests();
	   		if ( e!=null )
        	{
		   		while ( e.hasMoreElements() )
		    	{
		    		avscience.ppc.ShearTestResult result = (avscience.ppc.ShearTestResult) e.nextElement();
		    		int depth = result.getDepthValueInt();
		    		if ( depth > max ) max=depth;
		    	}
		    }
	    //	max+=6;
	    }
	    System.out.println("max depth tempprofile.");
	    if ( (pit.getTempProfile()!=null) && (pit.getTempProfile().getDepths()!=null))
	    {
	    	Enumeration ee = pit.getTempProfile().getDepths().elements();
		   
		    while ( ee.hasMoreElements() )
		    {
		    	Integer I = (Integer)ee.nextElement();
		    	int depth = I.intValue();
		    	// need to scale for temp depth??
		    	depth=depth*10;
		    	if ( depth > max ) max=depth;
		    }
		}
	    boolean mor=false;
	    System.out.println("max depth rho profile.");
	    if (( pit.getDensityProfile())!=null)
            {
             if (pit.getDensityProfile().getDepths()!=null)
	    {
	    	Enumeration ee = pit.getDensityProfile().getDepths().elements();
		    
		    while ( ee.hasMoreElements() )
		    {
                        Object o = ee.nextElement();
                        int depth=0;
                        if (o instanceof java.lang.Integer)
                        {
                            java.lang.Integer I = (java.lang.Integer) o;
                            depth = I.intValue();
                        }
                       
		    	// need to scale for rho depth??
		    	depth=depth*10;
		    	if ( depth > max )
		    	{
		    		max=depth;
		    		mor=true;
		    	}
		    }
		}
            }
	    //if (mor) max+=4;
       		
       if ( max == 0 ) max = 60;
       return max;
    }
    
    public int getMinDepth()
    {
        int min = 0;
        java.util.Vector v = new java.util.Vector();
        java.util.Enumeration e = pit.getLayers();
        while ( e.hasMoreElements() )
        {
            avscience.ppc.Layer l = (avscience.ppc.Layer) e.nextElement();
            int end = l.getEndDepthInt();
            Integer End = new Integer(end);
            v.addElement(End);
        }
        v = sortAscending(v);
        if ( v.size() > 0 ) min = ((Integer) v.firstElement()).intValue();
        return min;
    }
    
    private java.util.Vector sortAscending(java.util.Vector  list)
    {
        boolean sorted = false;
        int length = list.size();
        int i = 0;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    int n = ( (Integer) list.elementAt(i) ).intValue();
                    int ninc = ( (Integer) list.elementAt(i+1) ).intValue();
                   
                    if ( ninc < n )
                    {
                            list.setElementAt(new Integer(ninc), i);
                            list.setElementAt(new Integer(n), i+1);
                            sorted = false;
                    }
                }
            }
        }
        return list;
    }
    
    /*private Vector sortAscending(Vector  list)
    {
        boolean sorted = false;
        int length = list.size();
        int i = 0;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    int n = ( (Integer) list.elementAt(i) ).intValue();
                    int ninc = ( (Integer) list.elementAt(i+1) ).intValue();
                   
                    if ( ninc < n )
                    {
                            list.setElementAt(new Integer(ninc), i);
                            list.setElementAt(new Integer(n), i+1);
                            sorted = false;
                    }
                }
            }
        }
        return list;
    }*/
    
    
    private java.util.Vector sortAscendingLayers(java.util.Vector layers)
    {
    	//System.out.println("sortAsc");
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
                	int strt = layer.getStartDepthInt();
                	int end = layer.getEndDepthInt();
                    int n = strt+end;
                    layerInc = (avscience.ppc.Layer) layers.elementAt(i+1);
                    int istrt = layerInc.getStartDepthInt();
                    int iend = layerInc.getEndDepthInt();
                    int ninc = istrt+iend;
                  
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
    
    private java.util.Vector sortAscendingTests(java.util.Vector tests)
    {
    	//System.out.println("sortAsc");
        boolean sorted = false;
        int length = tests.size();
        int i = 0;
        avscience.ppc.ShearTestResult test;
        avscience.ppc.ShearTestResult testInc;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                	test = (avscience.ppc.ShearTestResult) tests.elementAt(i);
                    double depth = test.getDepthValue();
                    testInc = (avscience.ppc.ShearTestResult) tests.elementAt(i+1);
                    double depthInc = testInc.getDepthValue();
                  
                  	if (  depthInc != depth  ) 
                  	{
                  		if ( depthInc < depth )
                    	{
                            tests.setElementAt(testInc, i);
                            tests.setElementAt(test, i+1);
                            sorted = false;
                    	}
                  	}
                    ///else sorted=false;
                    
                   /// if ( depthInc == depth )sorted = false;
                }
            }
        }
        return tests;
    }
    
    private java.util.Vector sortDescendingLayers(java.util.Vector layers)
    {
    	//System.out.println("sortDesc");
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
                	int strt = layer.getStartDepthInt();
                	int end = layer.getEndDepthInt();
                    int n = strt+end;
                    layerInc = (avscience.ppc.Layer) layers.elementAt(i+1);
                    int istrt = layerInc.getStartDepthInt();
                    int iend = layerInc.getEndDepthInt();
                    int ninc = istrt+iend;
                  
                    if ( ninc > n )
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
    
    private java.util.Vector sortDescendingTests(java.util.Vector tests)
    {
    	//System.out.println("sortDesc");
        boolean sorted = false;
        int length = tests.size();
        int i = 0;
        avscience.ppc.ShearTestResult test;
        avscience.ppc.ShearTestResult testInc;

        if (length > 0)
        {
            while (!sorted)
            {
                sorted = true;
                for(i=0; i<length - 1; i++)
                {
                    test = (avscience.ppc.ShearTestResult) tests.elementAt(i);
                    double depth = test.getDepthValue();
                    testInc = (avscience.ppc.ShearTestResult) tests.elementAt(i+1);
                    double depthInc = testInc.getDepthValue();
                    
                    if (  depthInc != depth  ) 
                  	{
                    	if ( depthInc > depth )
                    	{
                            tests.setElementAt(testInc, i);
                            tests.setElementAt(test, i+1);
                            sorted = false;
                    	}
                    }
                  //  else sorted=false;
                   /// if ( depthInc == depth )sorted = false;
                }
            }
        }
        return tests;
    }
    
    private void drawAxi()
    {
      g.drawLine(xaxiLength, inset + vspace,  xaxiLength,  height - inset + vspace);
      g.drawLine(crystalColStart, inset + vspace,  crystalColStart,  height - inset + vspace);
      g.drawLine(crystalColStart + crystalWidth, inset + vspace,  crystalColStart + crystalWidth,  height - inset + vspace);
      g.drawLine(crystalColStart + formWidth, inset,  crystalColStart + formWidth,  height - inset + vspace);
      g.drawLine(crystalColStart + crystalWidth + testColWidth + rhoColWidth+inset, inset + vspace,  crystalColStart + testColWidth + crystalWidth + rhoColWidth+inset,  height - inset + vspace);
      g.drawLine(crystalColStart + crystalWidth + rhoColWidth, inset + vspace,  crystalColStart + rhoColWidth + crystalWidth,  height - inset + vspace);
      g.drawLine(inset, inset + vspace,  inset,  height - inset + vspace);
      g.drawLine(inset, inset + vspace , xaxiLength, inset + vspace );
      g.drawLine(inset, height - inset + vspace , xaxiLength, height - inset + vspace);
      g.drawLine(crystalColStart, inset + vspace, crystalColStart + crystalWidth + testColWidth+rhoColWidth+inset, inset + vspace);
      g.drawLine(crystalColStart, height - inset + vspace, crystalColStart + crystalWidth + testColWidth+rhoColWidth+inset, height - inset + vspace);
      g.drawLine(crystalColStart-2*inset, inset + vspace, crystalColStart-2*inset, height - inset + vspace);
      g.drawLine(crystalColStart-2*inset, height - inset + vspace, crystalColStart, height - inset + vspace);
      g.drawLine(crystalColStart-2*inset, inset + vspace, crystalColStart, inset + vspace);
    }
    
    public int getDepthScale()
    {
    	return depthScaleCen;
	}
	
	private void drawTempAxi()
	{
		System.out.println("draw temp axi.");
		g.drawLine(inset, (22), xaxiLength, (22));
		tp = pit.getTempProfile();
        if ( tp.getTempUnits().equals("F")) tempIncr = 20;
       
        if (tp.getTemps()!=null)
        {
            temps = tp.getTemps();
            if ( temps.size() > 0)
            {
                temps = sortAscending(temps);
                minTemp = ((Integer)temps.firstElement()).intValue();
                System.out.println("Min temp: "+minTemp);
                if ( ( minTemp < -100 ) && (tp.getTempUnits().equals("C"))) tempIncr = 20;
                maxTemp = ((Integer)temps.lastElement()).intValue();
                if ( tp.getTempUnits().equals("C")) 
                {
                	
                	maxTemp=0;
                	if (minTemp < -100 ) minTemp =-200;
                	else if (minTemp < -200 ) minTemp =-400;
                	else if (minTemp < -400 ) minTemp =-600;
                	else minTemp=-100;
                }
                tempRange = maxTemp - minTemp;
                System.out.println("temp range: ");
                if ( tempRange >  0  )tempScaleCen = (int) (1000*(xaxiLength - inset))/tempRange;
        	     
        		int intX = 0;
        		int size = (tempRange/tempIncr) + 3;
                xtics = new int[size];
                int temp = minTemp;
                int i = 0;
                TempList tl = TempList.getInstance();
                String tu = tp.getTempUnits();
                if ( tu==null ) 
                {
                	System.out.println("tu is null.");
                }
                else System.out.println("tu: "+tu);
                while ( temp <= maxTemp)
                {
                    xtics[i] = xaxiLength  + (tempScaleCen*(temp-maxTemp)/1000);
                    g.drawLine(xtics[i], 18, xtics[i],  18 + 2*ticLength);
                    	
                    String s = tl.getTempString(tu, temp);
                    if (s==null)s="";
                    g.drawString(s, xtics[i] - 4,  14);
                    temp+=tempIncr;
                    i++;
                }
                temp=maxTemp;
                xtics[i] = xaxiLength  + (tempScaleCen*(temp-maxTemp)/1000);
                g.drawLine(xtics[i], 18, xtics[i],  18 + 2*ticLength);
                    	
                String s = tl.getTempString(tu, temp);
                if (s==null)s="";
                g.drawString(s, xtics[i] - 4,  14);
                
                i = 0;
                temp = minTemp;
                System.out.println("draw temp axi complete.:");
            }
        }
	}
	
	private String[] invert(String[] a)
	{
	   int size = a.length;
	   String[] r = new String[size];
	   int i = 0;
	   int j = size - 1;
	   
	   for ( i = 0; i < size; i++ )
	   {
	       r[i] = a[j];
	       j--;
	   }
	   return r;
    }
}